package xiaoliang.ltool.activity.system;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import xiaoliang.ltool.R;
import xiaoliang.ltool.util.DialogUtil;
import xiaoliang.ltool.util.OtherUtil;
import xiaoliang.ltool.util.SharedPreferencesUtils;
import xiaoliang.ltool.util.ToastUtil;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView version;
    private View img;
    private int imgInt = 0, copyrightInt = 0;
    private String ver;
    private int allInt = 0;
    private boolean firstOpen = false;
    private View copyright;
    private ClipboardManager myClipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_about_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        version = (TextView) findViewById(R.id.activity_about_version);
        img = findViewById(R.id.activity_about_img);
        copyright = findViewById(R.id.activity_about_copyright);
        ver = "V"+ OtherUtil.getVersion(this);
        version.setText(ver);
        copyright.setOnClickListener(this);
        version.setVisibility(View.GONE);
        img.setOnClickListener(this);
        firstOpen = SharedPreferencesUtils.get(this,"FIRSTOPEN",true);
        allInt = SharedPreferencesUtils.get(this,"ALLINT",0);
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_about_img:
                onImgClick();
                break;
            case R.id.activity_about_copyright:
                onVerClick();
                break;
            case R.id.activity_about_copyqq:
                ClipData myClip = ClipData.newPlainText("text", "390685779");
                myClipboard.setPrimaryClip(myClip);
                ToastUtil.T(this,"群号已复制到剪切板");
                break;
        }
    }
    private void onVerClick(){
        copyrightInt++;
        if(copyrightInt>10){
            DialogUtil.getAlertDialog(this,"恭喜你，解锁了神秘的版本号彩蛋！\n您当前的版本号是 "+ver,false);
            version.setVisibility(View.VISIBLE);
            copyrightInt = 0;
        }
    }
    private void onImgClick() {
        imgInt++;
        allInt++;
        if(imgInt==1&&allInt>imgInt&&!firstOpen){
            DialogUtil.getAlertDialog(this,"您已连续点击LOGO "+allInt+"下\n真棒！请再接再厉！\n截屏分享给朋友，PK一下吧！",false);
        }else
        if(imgInt==10&&firstOpen){
            DialogUtil.getAlertDialog(this,"恭喜你，解锁了神奇的彩蛋，点击更多次获取更多的彩蛋吧！",false);
            SharedPreferencesUtils.put(this,"FIRSTOPEN",false);
        }else
        if ((imgInt<100&&imgInt % 10 == 0)||(imgInt<1000&&imgInt % 100 == 0)||(imgInt % 1000 == 0)) {
            DialogUtil.getAlertDialog(this,"恭喜你，达成成就:\n"+imgInt+"次LOGO连续点击！\n你总计已经点击"+allInt+"次了。希望再接再厉",false);
        }
    }
    @Override
    protected void onDestroy() {
        SharedPreferencesUtils.put(this,"ALLINT",allInt);
        super.onDestroy();
    }
}
