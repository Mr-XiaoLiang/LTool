package xiaoliang.ltool.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import xiaoliang.ltool.R;
import xiaoliang.ltool.dialog.ColorsDialog;
import xiaoliang.ltool.dialog.LoadDialog;
import xiaoliang.ltool.util.DensityUtil;
import xiaoliang.ltool.util.DialogUtil;
import xiaoliang.ltool.util.QRCreateRunnable;
import xiaoliang.ltool.util.QRTask;

public class QRCreateActivity extends AppCompatActivity implements
        View.OnClickListener,
        RadioGroup.OnCheckedChangeListener,
        CompoundButton.OnCheckedChangeListener,
        SeekBar.OnSeekBarChangeListener,
        ColorsDialog.OnColorConfirmListener,
        QRCreateRunnable.QRCallBack{

    private TextInputEditText editText;
    private ImageView imageView;
    private LToolApplication app;
    private NestedScrollView scrollView;
    private LoadDialog loadDialog;
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
    private final int TYPE_LINEAR = 0;
    private final int TYPE_RADIAL = 1;
    private final int TYPE_SWEEP = 2;
    private final int TYPE_BITMAP = 3;
    private final int TYPE_QUICK = 4;
    private boolean open = false;
    private int type = TYPE_LINEAR;//定制类型
    private int rotate = 0;//旋转角度
    private boolean isBg = false;//渲染为背景
    private int otherColor = Color.WHITE;//补色
    private ArrayList<Integer> colorsList;//颜色集合
    private Bitmap bitmap;//渲染资源
    private Bitmap logo;//logo资源
    private Bitmap qRBitmap;//二维码图片


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_qr_create_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        app = (LToolApplication) getApplicationContext();
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

    /**
     * 创建二维码
     */
    private void createQR(){
        InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),0);
        }
            String text = editText.getText().toString();
        if(text.length()<1){
            editText.setError("请输入内容");
            return;
        }
        int[] ca = null;
        if(type == TYPE_BITMAP){
            if(bitmap==null){
                editText.setError("请选择渲染图片");
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
        switch (type){
            case TYPE_BITMAP:
                QRTask.getBitmapShaderQRImg(text,imageView.getWidth(),rotate,bitmap,isBg,otherColor,logo,this);
                break;
            case TYPE_SWEEP:
                QRTask.getSweepGradientQRImg(text,imageView.getWidth(),rotate,ca,isBg,otherColor,logo,this);
                break;
            case TYPE_RADIAL:
                QRTask.getRadialGradientQRImg(text,imageView.getWidth(),ca,isBg,otherColor,logo,this);
                break;
            case TYPE_LINEAR:
                QRTask.getLinearGradientQRImg(text,imageView.getWidth(),rotate,ca,isBg,otherColor,logo,this);
                break;
            case TYPE_QUICK:
                QRTask.getQuickQRImage(text,imageView.getWidth(),logo,this);
                break;
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_qr_create_create:
                createQR();
                break;
            case R.id.activity_qr_create_other_layout:
                DialogUtil.getColorDialog(this,otherColor,223,this);
                break;
            case R.id.activity_qr_create_colors:
            case R.id.activity_qr_create_colors_layout:
                DialogUtil.getColorDialog(this,colorsList,233,this);
                break;
            case R.id.activity_qr_create_bitmap_layout:
                break;
        }
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

//    private class QRImgCallBack implements QRCreateRunnable.QRCallBack{
        @Override
        public void obtainQR(Bitmap bitmap) {
            qRBitmap = bitmap;
            handler.sendEmptyMessage(200);
        }
//    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 200:
                    if(loadDialog!=null)
                        loadDialog.dismiss();
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                    imageView.setImageBitmap(qRBitmap);
                    System.gc();//调用内存清理，防止溢出
                    break;
            }
            super.handleMessage(msg);
        }
    };

}
