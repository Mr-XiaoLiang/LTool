package xiaoliang.ltool.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import xiaoliang.ltool.R;
import xiaoliang.ltool.dialog.ColorsDialog;
import xiaoliang.ltool.dialog.LoadDialog;
import xiaoliang.ltool.dialog.QRWidthDialog;
import xiaoliang.ltool.util.DensityUtil;
import xiaoliang.ltool.util.DialogUtil;
import xiaoliang.ltool.util.OtherUtil;
import xiaoliang.ltool.util.QRCreateRunnable;
import xiaoliang.ltool.util.QRTask;
import xiaoliang.ltool.util.SaveBitmapRunnable;

public class QRCreateActivity extends AppCompatActivity implements
        View.OnClickListener,
        RadioGroup.OnCheckedChangeListener,
        CompoundButton.OnCheckedChangeListener,
        SeekBar.OnSeekBarChangeListener,
        ColorsDialog.OnColorConfirmListener{

    private TextInputEditText editText;
    private ImageView imageView;
    private LToolApplication app;
    private NestedScrollView scrollView;
    private LoadDialog loadDialog;
    private QRHandler handler;
    //定制按钮
    private LinearLayout typeLayout,rotateLayout,bgLayout,otherLayout,colorsLayout,bitmapLayout,logoLayout;
    private SwitchCompat openSwitch,isBgSwitch;
    private RadioGroup typeGroup;
//    private RadioButton linearRadio,radialRadio,sweepRadio,bitmapRadio;
    private SeekBar rotateBar;
    private ImageView otherImg,bitmapImg,logoImg;
    private LinearLayout colorGroup;
    private TextView rotateText;
    //定制值
    private static final int TYPE_LINEAR = 0;
    private static final int TYPE_RADIAL = 1;
    private static final int TYPE_SWEEP = 2;
    private static final int TYPE_BITMAP = 3;
    private static final int TYPE_QUICK = 4;
    private boolean open = false;
    private int type = TYPE_LINEAR;//定制类型
    private int rotate = 0;//旋转角度
    private boolean isBg = false;//渲染为背景
    private int otherColor = Color.WHITE;//补色
    private ArrayList<Integer> colorsList;//颜色集合
    private Bitmap bitmap;//渲染资源
    private Bitmap logo;//logo资源
    private Bitmap qRBitmap;//二维码图片
//    private static final int GET_BITMAP_CAMERA = 987;
    private static final int GET_BITMAP_PHOTO = 986;
    private static final int GET_BITMAP_CLIPPING = 985;
//    private static final int GET_LOGO_CAMERA = 984;
    private static final int GET_LOGO_PHOTO = 983;
    private static final int GET_LOGO_CLIPPING = 982;
//    private static final int PERMISSON_REQUESTCODE = 981;
//    private Uri thisPhotoUri;
    private int imageWidth = 0;
    private QRCreateRunnable.QRCallBack showQRCB;//展示用的二维码回调
    private QRCreateRunnable.QRCallBack saveQRCB;//保存用的二维码回调

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_qr_create_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        app = (LToolApplication) getApplicationContext();
        handler = new QRHandler();
        createQRCallBack();
        initView();
        openMade();
    }

    private void initView(){
        editText = (TextInputEditText) findViewById(R.id.activity_qr_create_edit);
        imageView = (ImageView) findViewById(R.id.activity_qr_create_img);
        scrollView = (NestedScrollView) findViewById(R.id.content_qrcreate);
        typeLayout = (LinearLayout) findViewById(R.id.activity_qr_create_type_layout);
        rotateLayout = (LinearLayout) findViewById(R.id.activity_qr_create_rotate_layout);
        bgLayout = (LinearLayout) findViewById(R.id.activity_qr_create_isbg_layout);
        otherLayout = (LinearLayout) findViewById(R.id.activity_qr_create_other_layout);
        colorsLayout = (LinearLayout) findViewById(R.id.activity_qr_create_colors_layout);
        bitmapLayout = (LinearLayout) findViewById(R.id.activity_qr_create_bitmap_layout);
        logoLayout = (LinearLayout) findViewById(R.id.activity_qr_create_logo_layout);
        openSwitch = (SwitchCompat) findViewById(R.id.activity_qr_create_open);
        isBgSwitch = (SwitchCompat) findViewById(R.id.activity_qr_create_isbg);
        typeGroup = (RadioGroup) findViewById(R.id.activity_qr_create_type_group);
//        linearRadio = (RadioButton) findViewById(R.id.activity_qr_create_type_linear);
//        radialRadio = (RadioButton) findViewById(R.id.activity_qr_create_type_radial);
//        sweepRadio = (RadioButton) findViewById(R.id.activity_qr_create_type_sweep);
//        bitmapRadio = (RadioButton) findViewById(R.id.activity_qr_create_type_bitmap);
        rotateBar = (SeekBar) findViewById(R.id.activity_qr_create_rotate);
        otherImg = (ImageView) findViewById(R.id.activity_qr_create_other);
        bitmapImg = (ImageView) findViewById(R.id.activity_qr_create_bitmap);
        logoImg = (ImageView) findViewById(R.id.activity_qr_create_logo);
        colorGroup = (LinearLayout) findViewById(R.id.activity_qr_create_colors);
        rotateText = (TextView) findViewById(R.id.activity_qr_create_rotate_text);
        otherLayout.setOnClickListener(this);
        colorsLayout.setOnClickListener(this);
        colorGroup.setOnClickListener(this);
        bitmapLayout.setOnClickListener(this);
        logoLayout.setOnClickListener(this);
        openSwitch.setOnCheckedChangeListener(this);
        isBgSwitch.setOnCheckedChangeListener(this);
        typeGroup.setOnCheckedChangeListener(this);
        rotateBar.setOnSeekBarChangeListener(this);
    }

    private void openMade(){
        if(open){
            onCheckedChanged(typeGroup,typeGroup.getCheckedRadioButtonId());
            madeType();
        }else{
            type = TYPE_QUICK;
            typeLayout.setVisibility(View.GONE);
            rotateLayout.setVisibility(View.GONE);
            bgLayout.setVisibility(View.GONE);
            otherLayout.setVisibility(View.GONE);
            colorsLayout.setVisibility(View.GONE);
            bitmapLayout.setVisibility(View.GONE);
            logoLayout.setVisibility(View.GONE);
        }
    }

    private void madeType(){
        typeLayout.setVisibility(View.VISIBLE);
        bgLayout.setVisibility(View.VISIBLE);
        otherLayout.setVisibility(View.VISIBLE);
        logoLayout.setVisibility(View.VISIBLE);
        switch (type){
            case TYPE_LINEAR:
                rotateLayout.setVisibility(View.VISIBLE);
                colorsLayout.setVisibility(View.VISIBLE);
                bitmapLayout.setVisibility(View.GONE);
                break;
            case TYPE_RADIAL:
                colorsLayout.setVisibility(View.VISIBLE);
                rotateLayout.setVisibility(View.GONE);
                bitmapLayout.setVisibility(View.GONE);
                break;
            case TYPE_SWEEP:
                rotateLayout.setVisibility(View.VISIBLE);
                colorsLayout.setVisibility(View.VISIBLE);
                bitmapLayout.setVisibility(View.GONE);
                break;
            case TYPE_BITMAP:
                rotateLayout.setVisibility(View.VISIBLE);
                bitmapLayout.setVisibility(View.VISIBLE);
                colorsLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void initColorsList(){
        if(colorsList!=null&&colorsList.size()>0){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(this,20), ViewGroup.LayoutParams.MATCH_PARENT);
            while(colorsList.size()<colorGroup.getChildCount()){
                colorGroup.removeViewAt(0);
            }
            while(colorsList.size()>colorGroup.getChildCount()){
                View view = new View(this);
                view.setLayoutParams(layoutParams);
                colorGroup.addView(view);
            }
            for(int i = 0;i<colorsList.size();i++){
                colorGroup.getChildAt(i).setBackgroundColor(colorsList.get(i));
            }
        }else{
            colorGroup.removeAllViews();
        }
    }

    private void createQR(boolean isShow){
        InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),0);
        }
            String text = editText.getText().toString();
        if(text.length()<1){
            editText.setError("请输入内容");
            app.T("请输入内容");
            return;
        }
        int[] ca = null;
        if(type == TYPE_BITMAP){
            if(bitmap==null){
                app.T("请选择渲染图片");
                return;
            }
        }
        if(type == TYPE_SWEEP||type == TYPE_RADIAL||type == TYPE_LINEAR){
            if(colorsList==null||colorsList.size()<2){
                app.T("请选择2种以上渲染颜色");
                return;
            }
            ca = new int[colorsList.size()+1];
            for(int i = 0;i<colorsList.size();i++){
                ca[i] = colorsList.get(i);
            }
            if(type == TYPE_SWEEP)
                ca[ca.length-1] = colorsList.get(0);
        }
        loadDialog = DialogUtil.getLoadDialog(this);
        QRCreateRunnable.QRCallBack thisCb;
        if(isShow)
            thisCb = showQRCB;
        else
            thisCb = saveQRCB;
        switch (type){
            case TYPE_BITMAP:
                QRTask.getBitmapShaderQRImg(text,imageWidth,rotate,bitmap,isBg,otherColor,logo,thisCb);
                break;
            case TYPE_SWEEP:
                QRTask.getSweepGradientQRImg(text,imageWidth,rotate,ca,isBg,otherColor,logo,thisCb);
                break;
            case TYPE_RADIAL:
                QRTask.getRadialGradientQRImg(text,imageWidth,ca,isBg,otherColor,logo,thisCb);
                break;
            case TYPE_LINEAR:
                QRTask.getLinearGradientQRImg(text,imageWidth,rotate,ca,isBg,otherColor,logo,thisCb);
                break;
            case TYPE_QUICK:
                QRTask.getQuickQRImage(text,imageWidth,logo,thisCb);
                break;
        }
    }

    private void getImage(final boolean isLogo){

//        DialogUtil.getAlertDialog(this, "图片来源", "请选择你需要的图片来源",
//                "相机",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        checkCameraPermission(isLogo);
//                    }
//                },
//                "相册",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(Intent.ACTION_PICK, null);
//                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
//                        if(isLogo)
//                            startActivityForResult(intent, GET_LOGO_PHOTO);
//                        else
//                            startActivityForResult(intent, GET_BITMAP_PHOTO);
//                    }
//                }
//        );
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
            if(isLogo)
                startActivityForResult(intent, GET_LOGO_PHOTO);
            else
                startActivityForResult(intent, GET_BITMAP_PHOTO);
    }

//    private void checkCameraPermission(final boolean isLogo){
//        String perm = Manifest.permission.CAMERA;
//        if(ContextCompat.checkSelfPermission(this,perm)
//                == PackageManager.PERMISSION_GRANTED){
//            thisPhotoUri = getPhotoName();
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            // 指定调用相机拍照后照片的储存路径
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, thisPhotoUri);
//            if(isLogo)
//                startActivityForResult(intent, GET_LOGO_CAMERA);
//            else
//                startActivityForResult(intent, GET_BITMAP_CAMERA);
//        }else if (ContextCompat.checkSelfPermission(this,perm)
//                != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.shouldShowRequestPermissionRationale(
//                this, perm)) {
//            ActivityCompat.requestPermissions(this,new String[]{perm},PERMISSON_REQUESTCODE);
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
//            case GET_BITMAP_CAMERA:
//                startPhotoZoom(thisPhotoUri, imageWidth,GET_BITMAP_CLIPPING);
//                break;
//            case GET_LOGO_CAMERA:
//                startPhotoZoom(thisPhotoUri, (int) (imageWidth*0.2),GET_LOGO_CLIPPING);
//                break;
            case GET_BITMAP_PHOTO:
                saveImage(data,false);
//                startPhotoZoom(data.getData(),GET_BITMAP_CLIPPING);
                break;
            case GET_LOGO_PHOTO:
                saveImage(data,true);
//                startPhotoZoom(data.getData(),GET_LOGO_CLIPPING);
                break;
            case GET_LOGO_CLIPPING:
//                saveImage(data,true);
                try{
                    if(data!=null){
                        Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());//显得到bitmap图片
                        if(bmp!=null){
                            logo = bmp;
                            logoImg.setImageBitmap(logo);
                        }
                    }
                }catch (IOException e){
                    Log.d("QRCreate-->Result",e.getMessage());
                }
                break;
            case GET_BITMAP_CLIPPING:
                try{
                    if(data!=null){
//                    Bitmap bmp = data.getExtras().getParcelable("data");
                        Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());//显得到bitmap图片
                        if(bmp!=null){
                            bitmap = bmp;
                            bitmapImg.setImageBitmap(bitmap);
                        }
                    }
                }catch (IOException e){
                    Log.d("QRCreate-->Result",e.getMessage());
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveImage(Intent data,boolean isLogo){
//        if (data != null){
//            Bundle bundle = data.getExtras();
//            Bitmap bmp;
//            if (bundle != null) {
//                bmp = bundle.getParcelable("data");
//                if(bmp==null)
//                    return;
//                if(isLogo){
//                    if(bmp.getWidth()==bmp.getHeight()) {
//                        logo = bmp;
//                        logoImg.setImageBitmap(logo);
//                    }else{
//                        startPhotoZoom(data.getData(),GET_LOGO_CLIPPING);
//                    }
//                }else{
//                    if(bmp.getWidth()==bmp.getHeight()) {
//                        bitmap = bmp;
//                        bitmapImg.setImageBitmap(bitmap);
//                    }else{
//                        startPhotoZoom(data.getData(),GET_BITMAP_CLIPPING);
//                    }
//                }
//            }
//        }
        try{
            if (data != null){
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());//显得到bitmap图片
                if(bmp==null)
                    return;
                if(isLogo){
                    if(bmp.getWidth()==bmp.getHeight()) {
                        logo = bmp;
                        logoImg.setImageBitmap(logo);
                    }else{
                        startPhotoZoom(data.getData(),GET_LOGO_CLIPPING);
                    }
                }else{
                    if(bmp.getWidth()==bmp.getHeight()) {
                        bitmap = bmp;
                        bitmapImg.setImageBitmap(bitmap);
                    }else{
                        startPhotoZoom(data.getData(),GET_BITMAP_CLIPPING);
                    }
                }
            }
        }catch (Exception e){
            Log.d("QRCreate-->saveImage",e.getMessage());
        }
    }

    private void startPhotoZoom(Uri uri,int type) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高，本APP不做限制
//        intent.putExtra("outputX", size);
//        intent.putExtra("outputY", size);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, type);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String[] permissions, int[] paramArrayOfInt) {
//        if (requestCode == PERMISSON_REQUESTCODE) {
//            if (!verifyPermissions(paramArrayOfInt)) {
//                showMissingPermissionDialog();
//            }
//        }
//    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     *
     */
//    private void showMissingPermissionDialog() {
//        DialogUtil.getAlertDialog(this, "权限获取", "您选择了从相机获取图片，为此，我们需要您对我们授权使用摄像头，否则操作无法进行。",
//                "拒绝授权",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                },
//                "同意授权",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        startAppSettings();
//                    }
//                }
//        );
//    }

    /**
     *  启动应用的设置
     *
     * @since 2.5.0
     *
     */
//    private void startAppSettings() {
//        Intent intent = new Intent(
//                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        intent.setData(Uri.parse("package:" + getPackageName()));
//        startActivity(intent);
//    }

    /**
     * 检测是否说有的权限都已经授权
     * @since 2.5.0
     *
     */
//    private boolean verifyPermissions(int[] grantResults) {
//        for (int result : grantResults) {
//            if (result != PackageManager.PERMISSION_GRANTED) {
//                return false;
//            }
//        }
//        return true;
//    }

//    private Uri getPhotoName(){
//        File name = new File(
//                OtherUtil.getAppImgPath(this),
//                OtherUtil.formatTimeByMilliSecond(System.currentTimeMillis())+".png");
//        return Uri.fromFile(name);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_qr_create_create:
                imageWidth = imageView.getWidth();
                createQR(true);
                break;
            case R.id.activity_qr_create_save:
                showSaveMsgDialog();
//                imageWidth = imageView.getWidth();
//                createQR(false);
                break;
            case R.id.activity_qr_create_other_layout:
                DialogUtil.getColorDialog(this,otherColor,223,this);
                break;
            case R.id.activity_qr_create_colors:
            case R.id.activity_qr_create_colors_layout:
                DialogUtil.getColorDialog(this,colorsList,233,this);
                break;
            case R.id.activity_qr_create_bitmap_layout:
                getImage(false);
                break;
            case R.id.activity_qr_create_logo_layout:
                getImage(true);
                break;
        }
    }

    private void showSaveMsgDialog(){
        DialogUtil.getQRWidthDialog(this, imageView.getWidth(), new QRWidthDialog.QRWidthDialogListener() {
            @Override
            public void getWidth(int width) {
                imageWidth = width;
                createQR(false);
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.activity_qr_create_open:
                open = isChecked;
                openMade();
                break;
            case R.id.activity_qr_create_isbg:
                isBg = isChecked;
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.activity_qr_create_type_bitmap:
                type = TYPE_BITMAP;
                break;
            case R.id.activity_qr_create_type_radial:
                type = TYPE_RADIAL;
                break;
            case R.id.activity_qr_create_type_sweep:
                type = TYPE_SWEEP;
                break;
            case R.id.activity_qr_create_type_linear:
                type = TYPE_LINEAR;
                break;
        }
        madeType();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        rotate = progress;
        rotateText.setText("旋转角度 "+progress+"°");
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onConfirmOne(int what, int color) {
        if(what==223){
            otherColor = color;
            otherImg.setBackgroundColor(otherColor);
        }
    }

    @Override
    public void onConfirmArray(int what, ArrayList<Integer> color) {
        if(what==233){
            colorsList = color;
            initColorsList();
        }
    }

    private void createQRCallBack(){
        showQRCB = new QRCreateRunnable.QRCallBack() {
            @Override
            public void obtainQR(Bitmap bitmap) {
                qRBitmap = bitmap;
                handler.sendEmptyMessage(200);
            }
        };
        saveQRCB = new QRCreateRunnable.QRCallBack() {
            @Override
            public void obtainQR(Bitmap bitmap) {
                OtherUtil.seveBitmapToSDOnBackground(
                        System.currentTimeMillis()+".png",
                        bitmap,
                        new SaveBitmapRunnable.SaveBitmapCallBack(){
                            @Override
                            public void onSaveEnd(boolean seccess) {
                                if(seccess)
                                    handler.sendEmptyMessage(201);
                                else
                                    handler.sendEmptyMessage(202);
                            }
                        }
                );
            }
        };
    }

    private class QRHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            if(loadDialog!=null)
                loadDialog.dismiss();
            switch (msg.what){
                case 200://显示图片
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                    imageView.setImageBitmap(qRBitmap);
                    System.gc();//调用内存清理，防止溢出
                    break;
                case 201://保存成功
                    app.T("保存成功，图片位于："+OtherUtil.getSDImgPath());
                    break;
                case 202://保存失败
                    app.T("保存失败");
                    break;
            }
            super.handleMessage(msg);
        }
    }

}
