package xiaoliang.ltool.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.Vector;

import xiaoliang.ltool.R;
import xiaoliang.ltool.qr.camera.CameraManager;
import xiaoliang.ltool.qr.decoding.InactivityTimer;
import xiaoliang.ltool.qr.decoding.QRReadActivityHandler;
import xiaoliang.ltool.util.DensityUtil;
import xiaoliang.ltool.util.DialogUtil;
import xiaoliang.ltool.util.OtherUtil;
import xiaoliang.ltool.view.LWavesView;
import xiaoliang.ltool.view.QRFinderView;

public class QRReadActivity extends AppCompatActivity implements SurfaceHolder.Callback,View.OnClickListener {

    private QRReadActivityHandler handler;
    private QRFinderView qrFinderView;
    private SurfaceView surfaceView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private static final long VIBRATE_DURATION = 200L;
    private LToolApplication app;
    private ClipData myClip;
    private ClipboardManager myClipboard;
    private LWavesView wavesView;
    private View flashBtn,photoBtn;
    private static final int GET_PHOTO = 986;
    private Dialog loadDialog;
    private View btnLayout;
    private View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrread);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_qrread_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init(){
        app = (LToolApplication) getApplicationContext();
        CameraManager.init(app);
        qrFinderView = (QRFinderView) findViewById(R.id.activity_qrread_finder);
        surfaceView = (SurfaceView) findViewById(R.id.activity_qrread_surface);
        wavesView = (LWavesView) findViewById(R.id.activity_qrread_waves);
        flashBtn = findViewById(R.id.activity_qrread_flash);
        photoBtn = findViewById(R.id.activity_qrread_photo);
        btnLayout = findViewById(R.id.activity_qrread_btn_layout);
        root = findViewById(R.id.content_qrread);
        flashBtn.setOnClickListener(this);
        photoBtn.setOnClickListener(this);
        qrFinderView.setWavesView(wavesView);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
//        checkCameraPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeReader();
    }

    private void initializeReader(){
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setKeepScreenOn(true);// 屏幕常亮
        }
        decodeFormats = null;
        characterSet = null;
        if(handler!=null)
            handler.preview();
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);
            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(handler!=null){
            handler.stop();
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
            int[] size = new int[2];
            CameraManager.get().getCameraSize(size);
//            Log.d("openDriver","x="+size[0]+",y"+size[1]);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int x = metrics.widthPixels;//获取了屏幕的宽度
            int y = (int) (1.0*x*size[0]/size[1]);
            surfaceHolder.setFixedSize(x,y);
        } catch (Exception e) {
            return;
        }
        if (handler == null) {
            handler = new QRReadActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    public QRReadActivityHandler getHandler() {
        return handler;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public QRFinderView getQrFinderView() {
        return qrFinderView;
    }
    /**
     * 处理扫描结果
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (resultString.equals("")) {
            app.T("扫描失败!");
        }else {
            onScanEnd(resultString,barcode);
        }
    }
    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }
    public void drawViewfinder() {
        qrFinderView.drawViewfinder();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //统一调整布局尺寸，预览窗口跟随摄像头尺寸，扫描框，按钮，波浪图跟随预览窗口
        qrFinderView.setLayoutParams(new FrameLayout.LayoutParams(width,height));
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int y = metrics.heightPixels;//获取了屏幕的高度
        int y = root.getHeight();
        if(y-height< DensityUtil.dip2px(this,60)){
            y = DensityUtil.dip2px(this,60);
        }else{
            y -= height;
        }
        btnLayout.setLayoutParams(new LinearLayout.LayoutParams(width,y));
        wavesView.setTranslationY(root.getHeight()-y-wavesView.getHeight()/2);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public void onScanEnd(final String result, Bitmap bmp){
        if(loadDialog!=null)
            loadDialog.dismiss();
        AlertDialog dialog = DialogUtil.getAlertDialog(this, "扫描结果", result, "打开", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse(result);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        }, "复制", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myClip = ClipData.newPlainText("text", result);
                myClipboard.setPrimaryClip(myClip);
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.start();
            }
        });
    }

    public void onScanEnd(int resultCode, Intent data){
        if(resultCode==RESULT_OK){
            Bundle bundle = data.getExtras();
            onScanEnd(bundle.getString("result"),(Bitmap) data.getParcelableExtra("bitmap"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_qrread_flash:
                CameraManager.get().enableFlashlight();
                break;
            case R.id.activity_qrread_photo:
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent, GET_PHOTO);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GET_PHOTO:
                try{
                    if (data != null){
                        Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());//显得到bitmap图片
                        if(bmp==null){
                            app.T("图片获取失败");
                            return;
                        }
                        if(handler!=null){
                            loadDialog = DialogUtil.getLoadDialog(this);
                            handler.decodeImage(bmp);
                        }else{
                            app.T("解码器获取失败");
                            return;
                        }
                    }else{
                        app.T("图片获取失败");
                        return;
                    }
                }catch (Exception e){
                    Log.d("QRCreate-->saveImage",e.getMessage());
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void decodeImgFailed(){
        if(loadDialog!=null)
            loadDialog.dismiss();
        app.T("解码失败");
    }

    /****************摄像头权限检查开始*****************/
    private static final int CAMERA_PERMISSON_REQUESTCODE = 1;
    /**
     *  启动应用的设置
     *
     * @since 2.5.0
     *
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     *
     */
    private void showCameraMissingPermissionDialog() {
        DialogUtil.getAlertDialog(this, "权限获取", "您选择了二维码识别，为此，需要您对我们授权使用摄像头，否则操作无法进行。",
                "拒绝授权",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        QRReadActivity.this.finish();
                    }
                },
                "同意授权",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                }
        );
    }

    private void checkCameraPermission(){
        String perm = Manifest.permission.CAMERA;
        if (ContextCompat.checkSelfPermission(this,perm)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.shouldShowRequestPermissionRationale(
                this, perm)) {
            ActivityCompat.requestPermissions(this,new String[]{perm},CAMERA_PERMISSON_REQUESTCODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == CAMERA_PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showCameraMissingPermissionDialog();
            }
        }
    }

    /**
     * 检测是否说有的权限都已经授权
     * @param grantResults
     * @return
     * @since 2.5.0
     *
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /****************摄像头权限检查结束*****************/

}
