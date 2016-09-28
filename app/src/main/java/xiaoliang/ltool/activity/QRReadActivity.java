package xiaoliang.ltool.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.Vector;

import xiaoliang.ltool.R;
import xiaoliang.ltool.qr.camera.CameraManager;
import xiaoliang.ltool.qr.decoding.InactivityTimer;
import xiaoliang.ltool.qr.decoding.QRReadActivityHandler;
import xiaoliang.ltool.util.DialogUtil;
import xiaoliang.ltool.view.QRFinderView;

public class QRReadActivity extends AppCompatActivity implements SurfaceHolder.Callback {

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
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
//            qrFinderView.setFrame(CameraManager.get().getFramingRect());
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
        qrFinderView.setSize(width,height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public void onScanEnd(final String result, Bitmap bmp){
        DialogUtil.getAlertDialog(this, "扫描结果", result, "复制", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myClip = ClipData.newPlainText("text", result);
                myClipboard.setPrimaryClip(myClip);
            }
        });
    }

    public void onScanEnd(int resultCode, Intent data){
        if(resultCode==RESULT_OK){
            Bundle bundle = data.getExtras();
            onScanEnd(bundle.getString("result"),(Bitmap) data.getParcelableExtra("bitmap"));
        }
    }

}
