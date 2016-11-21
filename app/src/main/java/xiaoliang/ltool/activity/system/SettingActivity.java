package xiaoliang.ltool.activity.system;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import xiaoliang.ltool.R;
import xiaoliang.ltool.util.SharedPreferencesUtils;
import xiaoliang.ltool.util.ToastUtil;

public class SettingActivity extends AppCompatActivity implements SwitchCompat.OnCheckedChangeListener,View.OnClickListener {

    private SwitchCompat  onlyWifiSwitch, dayBgSwitch, meizhiSwitch, meizhiOnceSwitch,weatherSwitch;
    private View updateBtn, aboutBtn;
    private TextView version;
//    private static final int CHECK_UPDATE = 200;
//    private static final int DOWNLOAD_SECCESS = 201;
//    private static final int DOWNLOAD_ERROR = 202;
//    private static final int CHECK_ERROR = 203;
//    private static final int DOWNLOAD_GO = 204;
//    private static final int DOWNLOAD_PROGRESS = 205;
//    private LoadDialog loadDialog;
//    private LoadDialog2 loadDialog2;
//    private String ver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_setting_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView() {
        onlyWifiSwitch = (SwitchCompat) findViewById(R.id.activity_setting_onlywifi);
        dayBgSwitch = (SwitchCompat) findViewById(R.id.activity_setting_daybg);
        meizhiSwitch = (SwitchCompat) findViewById(R.id.activity_setting_meizhi);
        meizhiOnceSwitch = (SwitchCompat) findViewById(R.id.activity_setting_meizhi_onlyonce);
        updateBtn = findViewById(R.id.activity_setting_update);
        aboutBtn = findViewById(R.id.activity_setting_about);
        version = (TextView) findViewById(R.id.activity_setting_version);
        weatherSwitch = (SwitchCompat) findViewById(R.id.activity_setting_meizhi_weather);

        onlyWifiSwitch.setOnCheckedChangeListener(this);
        dayBgSwitch.setOnCheckedChangeListener(this);
        meizhiOnceSwitch.setOnCheckedChangeListener(this);
        meizhiSwitch.setOnCheckedChangeListener(this);
        weatherSwitch.setOnCheckedChangeListener(this);
        updateBtn.setOnClickListener(this);
        aboutBtn.setOnClickListener(this);

        onlyWifiSwitch.setChecked(SharedPreferencesUtils.isOnlyWifi(this));
        dayBgSwitch.setChecked(SharedPreferencesUtils.isLoadWebImg(this));
        meizhiSwitch.setChecked(SharedPreferencesUtils.getShowMeizhi(this));
        meizhiOnceSwitch.setChecked(SharedPreferencesUtils.getShowMeizhiOnce(this));
        weatherSwitch.setChecked(SharedPreferencesUtils.getWeatherShow(this));
//        ver = OtherUtil.getVersion(this);
//        version.setText("V"+ ver);
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.N_MR1){
            findViewById(R.id.activity_setting_shortcut).setVisibility(View.GONE);
            findViewById(R.id.activity_setting_shortcut_title).setVisibility(View.GONE);
            ToastUtil.T(this,"当前版本号为：API"+Build.VERSION.SDK_INT);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.activity_setting_onlywifi:
                SharedPreferencesUtils.setIsOnlyWifi(this, b);
                if (b) {
                    dayBgSwitch.setChecked(true);
                }
                break;
            case R.id.activity_setting_daybg:
                SharedPreferencesUtils.setIsLoadWebImg(this, b);
                if (!b) {
                    onlyWifiSwitch.setChecked(false);
                }
                break;
            case R.id.activity_setting_meizhi:
                SharedPreferencesUtils.setShowMeizhi(this, b);
                if (!b) {
                    meizhiOnceSwitch.setChecked(false);
                }
                break;
            case R.id.activity_setting_meizhi_onlyonce:
                SharedPreferencesUtils.setShowMeizhiOnce(this, b);
                if (b) {
                    meizhiSwitch.setChecked(true);
                }
            case R.id.activity_setting_meizhi_weather:
                SharedPreferencesUtils.setWeatherShow(this,b);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_setting_about:
                startActivity(new Intent(this,AboutActivity.class));
                break;
            case R.id.activity_setting_update:
//                checkUpdate();
                break;
            case R.id.activity_setting_shortcut:
                startActivity(new Intent(this,ShortcutManagerActivity.class));
                break;
        }
    }

//    private void checkUpdate(){
//        loadDialog = DialogUtil.getLoadDialog(this);
//        NetTasks.getSimpleData(Constant.UPDATE_URL, new HttpTaskRunnable.CallBack<String[]>() {
//            @Override
//            public void success(String[] object) {
//                Message message = handler.obtainMessage(CHECK_UPDATE);
//                message.obj = object;
//                handler.sendMessage(message);
//            }
//
//            @Override
//            public void error(int code, String msg) {
//                handler.sendEmptyMessage(CHECK_ERROR);
//            }
//            @Override
//            public String[] str2Obj(String str) {
//                return OtherUtil.getUpdate(str);
//            }
//        });
//    }
//
//    private void downloadAPK(String url){
//        loadDialog2 = DialogUtil.getLoadDialog2(this);
//        NetTasks.downloadApp(url, new RequestParameters.Progress() {
//            @Override
//            public void onProgress(float pro) {
//                Message message = handler.obtainMessage(DOWNLOAD_PROGRESS);
//                message.obj = pro;
//                handler.sendMessage(message);
//            }
//
//            @Override
//            public void onLoadSeccess(String path) {
//                Message message = handler.obtainMessage(DOWNLOAD_SECCESS);
//                message.obj = path;
//                handler.sendMessage(message);
//            }
//
//            @Override
//            public void onLoadError(Exception e, int type) {
//                Log.d("downloadAPK",type+e.getMessage());
//                handler.sendEmptyMessage(DOWNLOAD_ERROR);
//            }
//        });
//    }
//
//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            if(loadDialog!=null){
//                loadDialog.dismiss();
//            }
//            switch (msg.what){
//                case CHECK_UPDATE:
//                    final String[] check = (String[]) msg.obj;
//                    if(check!=null&&check.length>1){
//                        showChecked(check);
//                        break;
//                    }
//                case CHECK_ERROR:
//                    ToastUtil.T(SettingActivity.this,"检查更新出错");
//                    break;
//                case DOWNLOAD_ERROR:
//                    if(loadDialog2!=null)
//                        loadDialog2.dismiss();
//                    DialogUtil.getAlertDialog(SettingActivity.this,"很抱歉，下载失败");
//                    break;
//                case DOWNLOAD_SECCESS:
//                    if(loadDialog2!=null)
//                        loadDialog2.dismiss();
//                    String apk = (String) msg.obj;
//                    if(apk!=null&&!apk.equals("")){
//                        OtherUtil.installApk(SettingActivity.this,apk);
//                    }
//                    break;
//                case DOWNLOAD_PROGRESS:
//                    if(loadDialog2!=null)
//                        loadDialog2.setProgress((Float) msg.obj);
//                    break;
//                case DOWNLOAD_GO:
//                    downloadAPK("https://dl.coolapk.com/down?pn=xiaoliang.ltool&v=MTAyNDc2&h=ab6437d0oeto36&extra=0");
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };
//
//    private void showChecked(final String[] check){
//        if(!ver.equals(check[0])){
//            if(!"".equals(check[1])){
//                DialogUtil.getAlertDialog(SettingActivity.this,
//                        "更新", "检查到新的版本，版本号为：\nV" + check[0] + "\n是否更新？",
//                        "更新",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Message message = handler.obtainMessage(DOWNLOAD_GO);
//                                message.obj = check[1];
//                                handler.sendMessage(message);
//                                dialog.dismiss();
//                            }
//                        }
//                );
//            }else{
//                DialogUtil.getAlertDialog(SettingActivity.this,
//                        "更新", "检查到新的版本，版本号为：V" + ver + "但是无法获取到下载链接，请前往酷安市场手动更新");
//            }
//        }else{
//            ToastUtil.T(SettingActivity.this,"已经是最新版");
//        }
//    }

}
