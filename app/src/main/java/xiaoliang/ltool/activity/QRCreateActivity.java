package xiaoliang.ltool.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import java.util.ArrayList;

import xiaoliang.ltool.R;
import xiaoliang.ltool.util.QRUtil;

public class QRCreateActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText editText;
    private Button createBtn;
    private ImageView imageView;
    //定制按钮
    private LinearLayout typeLayout,rotateLayout,bgLayout,otherLayout,colorsLayout,bitmapLayout;
    private SwitchCompat openSwitch,isBgSwitch;
    private RadioGroup typeGroup;
    private RadioButton linearRadio,radialRadio,sweepRadio,bitmapRadio;
    private SeekBar rotateBar;
    private ImageView otherImg,bitmapImg;
    private LinearLayout colorGroup;
    //定制值
    private final int TYPE_LINEAR = 0;
    private final int TYPE_RADIAL = 1;
    private final int TYPE_SWEEP = 2;
    private final int TYPE_BITMAP = 3;
    private boolean open = false;
    private int type = TYPE_LINEAR;//定制类型
    private int rotate = 0;//旋转角度
    private boolean isBg = false;//渲染为背景
    private int otherColor = Color.WHITE;//补色
    private ArrayList<Integer> colorsList;//颜色集合
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_qr_create_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView(){
        editText = (TextInputEditText) findViewById(R.id.activity_qr_create_edit);
        createBtn = (Button) findViewById(R.id.activity_qr_create_create);
        imageView = (ImageView) findViewById(R.id.activity_qr_create_img);
        typeLayout = (LinearLayout) findViewById(R.id.activity_qr_create_type_layout);
        rotateLayout = (LinearLayout) findViewById(R.id.activity_qr_create_rotate_layout);
        bgLayout = (LinearLayout) findViewById(R.id.activity_qr_create_isbg_layout);
        otherLayout = (LinearLayout) findViewById(R.id.activity_qr_create_other_layout);
        colorsLayout = (LinearLayout) findViewById(R.id.activity_qr_create_colors_layout);
        bitmapLayout = (LinearLayout) findViewById(R.id.activity_qr_create_bitmap_layout);
        openSwitch = (SwitchCompat) findViewById(R.id.activity_qr_create_open);
        isBgSwitch = (SwitchCompat) findViewById(R.id.activity_qr_create_isbg);


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
//        imageView.setImageBitmap(QRUtil.getQuickQRImage(text,imageView.getWidth()));
//        imageView.setImageBitmap(QRUtil.getLinearGradientQRImg(text,imageView.getWidth(),76,new int[]{Color.BLUE,Color.RED,Color.BLACK,Color.YELLOW,Color.GREEN},true,Color.WHITE));
//        imageView.setImageBitmap(QRUtil.getRadialGradientQRImg(text,imageView.getWidth(),new int[]{Color.BLUE,Color.RED,Color.BLACK,Color.YELLOW,Color.GREEN},false,Color.WHITE));
        imageView.setImageBitmap(QRUtil.getSweepGradientQRImg(text,imageView.getWidth(),76,new int[]{Color.BLUE,Color.RED,Color.BLACK,Color.YELLOW,Color.GREEN,Color.BLUE},false,Color.WHITE));
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
        }
    }
}
