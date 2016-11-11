package xiaoliang.ltool.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.dom4j.DocumentException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import xiaoliang.ltool.R;
import xiaoliang.ltool.activity.lock.CreateLockActivity;
import xiaoliang.ltool.activity.meizhi.MeizhiActivity;
import xiaoliang.ltool.activity.note.NoteActivity;
import xiaoliang.ltool.activity.qr.QRCreateActivity;
import xiaoliang.ltool.activity.qr.QRReadActivity;
import xiaoliang.ltool.activity.system.SettingActivity;
import xiaoliang.ltool.activity.weather.WeatherActivity;
import xiaoliang.ltool.activity.webcode.WebCodeActivity;
import xiaoliang.ltool.bean.WeatherBean;
import xiaoliang.ltool.bean.WeatherDayBean;
import xiaoliang.ltool.constant.Constant;
import xiaoliang.ltool.dialog.CityDialog;
import xiaoliang.ltool.dialog.LoadDialog2;
import xiaoliang.ltool.util.BlurBitmapRunnable;
import xiaoliang.ltool.util.DialogUtil;
import xiaoliang.ltool.util.HttpTaskRunnable;
import xiaoliang.ltool.util.HttpUtil;
import xiaoliang.ltool.util.NetTasks;
import xiaoliang.ltool.util.OtherUtil;
import xiaoliang.ltool.util.RequestParameters;
import xiaoliang.ltool.util.SharedPreferencesUtils;
import xiaoliang.ltool.util.ToastUtil;
import xiaoliang.ltool.util.WeatherUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {
    /*主要控件*/
    private CollapsingToolbarLayout toolbarLayout;
    private FloatingActionButton fab;
    private ImageView headImg;
    private TextView headText;
    private boolean loadWebImg = false;
    private ImageView backgroundImg;
    private MyHandler handler;
    private LToolApplication app;
    private Toolbar toolbar;
    /*天气控件*/
    private TextView weatherDate,weatherTime,weatherTemperature,weatherDayType,weatherDayWind,weatherNightType,weatherNightWind;
    private CardView weather;
    private WeatherBean weatherBean;
    private boolean showWeather = true;
    //二维码部分
    private CardView qrRead;
    private CardView qrCreate;
    //图片部分
    private CardView meizi;
    private static Boolean isExit = false;
    //一键锁屏
    private CardView lock;
    //下载壁纸
    private LoadDialog2 loadDialog2;
    private static final int ON_DOWNLOAD_IMG = 655;
    private static final int ON_DOWNLOAD_IMG_SECCESS = 656;
    private static final int ON_DOWNLOAD_IMG_ERROR = 657;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化整体布局
        initView();
        //初始化天气部分
        initWeatherView();
        //加载壁纸
        loadImg();
    }

    private void initView(){
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        app = (LToolApplication) getApplicationContext();
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.activity_main_toolbar_layout);
        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        headImg = (ImageView) findViewById(R.id.activity_main_headimg);
        headText = (TextView) findViewById(R.id.activity_main_headtext);
        backgroundImg = (ImageView) findViewById(R.id.activity_main_bg);
        qrRead = (CardView) findViewById(R.id.content_main_qrread);
        qrCreate = (CardView) findViewById(R.id.content_main_qrcreate);
        meizi = (CardView) findViewById(R.id.content_main_meizhi);
        lock = (CardView) findViewById(R.id.content_main_lock);
        findViewById(R.id.content_main_note).setOnClickListener(this);
        handler = new MyHandler();
        qrCreate.setOnClickListener(this);
        qrRead.setOnClickListener(this);
        meizi.setOnClickListener(this);
        lock.setOnClickListener(this);
        headImg.setOnLongClickListener(this);
        showWeather = SharedPreferencesUtils.getWeatherShow(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(showWeather){
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }else{
            getMenuInflater().inflate(R.menu.menu_main2, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_main_city:
                DialogUtil.getCityDialog(this, new CityDialog.CitySelectedListener() {
                    @Override
                    public void citySelected(String name) {
                        SharedPreferencesUtils.setCity(MainActivity.this,name);
                        getWeather();
                    }
                });
                return true;
            case R.id.menu_main_setting:
                startActivity(new Intent(this,SettingActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMeizhi(){
        if(SharedPreferencesUtils.getShowMeizhi(this)){
            meizi.setVisibility(View.VISIBLE);
        }else{
            meizi.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWebImg = SharedPreferencesUtils.isLoadWebImg(this);
        showWeather = SharedPreferencesUtils.getWeatherShow(this);
        if(showWeather){
            getWeather();
        }
        //检查是否显示妹子卡片
        showMeizhi();
        //检查下载权限
        if(isNeedCheck){
            checkPermissions(needPermissions);
        }
    }

    private void setWeatherView(){
        if(weatherBean==null||!weatherBean.isSeccess()){
            app.T("天气数据获取出错\n可能是无此地区\n请手动选择地区");
            return;
        }
        if(weatherBean.getDayBeen()!=null&&weatherBean.getDayBeen().size()>0){
            weather.setVisibility(View.VISIBLE);
            WeatherDayBean today = weatherBean.getDayBeen(0);
            weatherDate.setText(today.getDate());
            weatherTemperature.setText(today.getQuickTemperature());
            weatherDayType.setText(today.getDaytype());
            weatherDayWind.setText(today.getQuickDayWind());
            weatherNightType.setText(today.getNighttype());
            weatherNightWind.setText(today.getQuickNightWind());
        }else{
            app.T("天气数据获取出错\n可能是无此地区\n请手动选择地区");
        }
        weatherTime.setText(weatherBean.getUpdateTime());
        if(weatherBean.getTitle()!=null&&!"".equals(weatherBean.getTitle()))
            toolbarLayout.setTitle(weatherBean.getTitle());
    }

    private void initWeatherView(){
        weatherDate = (TextView) findViewById(R.id.content_main_weather_date);
        weatherTime = (TextView) findViewById(R.id.content_main_weather_time);
        weatherTemperature = (TextView) findViewById(R.id.content_main_weather_temperature);
        weatherDayType = (TextView) findViewById(R.id.content_main_weather_daytype);
        weatherDayWind = (TextView) findViewById(R.id.content_main_weather_daywind);
        weatherNightType = (TextView) findViewById(R.id.content_main_weather_nighttype);
        weatherNightWind = (TextView) findViewById(R.id.content_main_weather_nightwind);
        weather = (CardView) findViewById(R.id.content_main_weather);
        weather.setVisibility(View.GONE);
        weather.setOnClickListener(this);
    }

    private void loadImg(){
        int networdType = OtherUtil.getNetworkType(this);
        boolean onlyWifi = SharedPreferencesUtils.isOnlyWifi(this);
        loadWebImg = SharedPreferencesUtils.isLoadWebImg(this);
        Glide.with(this)
                .load(Constant.getBabkgroundPath(this))
                .asBitmap()
                .diskCacheStrategy( DiskCacheStrategy.NONE )
                .skipMemoryCache(true)
                .into(new onHeadImgLoaded(this,false));
        if(networdType==Constant.NetWord_WIFI||(networdType==Constant.NetWord_MOBILE&&!onlyWifi)){
            if(loadWebImg&&!SharedPreferencesUtils.isGetBgEnd(this)){
//                Log.d("loadImg","isLoadWebImg...........");
                Glide.with(this)
                        .load(Constant.head_img_url_720)
                        .asBitmap()
                        .diskCacheStrategy( DiskCacheStrategy.NONE )
                        .skipMemoryCache(true)
                        .into(new onHeadImgLoaded(this,true));
            }
        }
        toolbarLayout.setStatusBarScrimColor(Color.TRANSPARENT);
        toolbarLayout.setContentScrimColor(Color.TRANSPARENT);
        toolbarLayout.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.content_main_weather:
                startActivity(new Intent(this,WeatherActivity.class));
                break;
            case R.id.content_main_qrcreate:
                startActivity(new Intent(this,QRCreateActivity.class));
                break;
            case R.id.content_main_lock:
                startActivity(new Intent(this,CreateLockActivity.class));
                break;
            case R.id.content_main_qrread:
                checkCameraPermission();
                break;
            case R.id.content_main_meizhi:
                int networdType = OtherUtil.getNetworkType(this);
                if(networdType!=Constant.NetWord_WIFI){
                    DialogUtil.getAlertDialog(this, "警告 请认真阅读",
                            "您当前不是WIFI状态，前方将消耗大量流量。\n因为是解析民间图片网站，所以将有部分流量被无用广告占用，虽然将会剔除，但是流量消耗不会恢复。" +
                                    "每次访问将消耗上百Kib用于获取图片链接。并且图片获取也需要大量流量，尽管压缩了，但是消耗仍然很大！\n是否继续？", "继续", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(MainActivity.this,MeizhiActivity.class));
                                    dialog.dismiss();
                                }
                            });
                }else{
                    startActivity(new Intent(MainActivity.this,MeizhiActivity.class));
                }
                break;
            case R.id.content_main_note:
                startActivity(new Intent(this,NoteActivity.class));
                break;
            case R.id.content_main_webcode:
                startActivity(new Intent(this,WebCodeActivity.class));
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.activity_main_headimg:
                DialogUtil.getAlertDialog(this, "提示", "是否下载本张壁纸？", "1080P", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getHeadBGUrl();
                        dialog.dismiss();
                    }
                }, "不用了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                return true;
        }
        return false;
    }

    private void getHeadBGUrl(){
        loadDialog2 = new LoadDialog2(this);
        NetTasks.getSimpleData(Constant.bing_image_url, new HttpTaskRunnable.CallBack<String>() {
            @Override
            public void success(String object) {
                downloadHeadBG(object);
            }
            @Override
            public void error(int code, String msg) {
                handler.sendEmptyMessage(ON_DOWNLOAD_IMG_ERROR);
//                Log.d("downloadHeadBG",msg);
            }

            @Override
            public String str2Obj(String json) {
                String url;
                json = json.substring(json.indexOf("url\":\"")+6);
                url = json.substring(0,json.indexOf("\""));
                return url;
            }
        });
    }

    private void downloadHeadBG(String url){
        NetTasks.downloadImage(url, new RequestParameters.Progress() {
            @Override
            public void onProgress(float pro) {
                Message message = handler.obtainMessage(ON_DOWNLOAD_IMG);
                message.obj = pro;
                handler.sendMessage(message);
            }
            @Override
            public void onLoadSeccess(String path) {
                handler.sendEmptyMessage(ON_DOWNLOAD_IMG_SECCESS);
            }
            @Override
            public void onLoadError(Exception e, int type) {
                handler.sendEmptyMessage(ON_DOWNLOAD_IMG_ERROR);
//                Log.d("downloadHeadBG",e.getMessage());
            }
        });
    }

    private class onHeadImgLoaded extends SimpleTarget<Bitmap>{

        final boolean isSave;
        final Context context;

        public onHeadImgLoaded(Context context, boolean isSave) {
            this.context = context;
            this.isSave = isSave;
//            Log.d("onHeadImgLoaded","onHeadImgLoaded.............");
        }

        @Override
        public void onResourceReady(final Bitmap resource, GlideAnimation glideAnimation) {
            headImg.setImageBitmap(resource);
            if(isSave){
//                Log.d("onHeadImgLoaded","onResourceReady.............");
                OtherUtil.saveBabkground(context,resource);
                SharedPreferencesUtils.setGetBgTime(context);
            }
            //异步对图片进行高斯模糊
            HttpUtil.getThread(new BlurBitmapRunnable(resource,context,new BlurBitmapRunnable.BlurBitmapListener(){
                @Override
                public void onBlur(Bitmap bitmap) {
                    Message message = new Message();
                    message.what = 200;
                    message.obj = bitmap;
                    handler.sendMessage(message);
                }
            }));
        }
    }

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 200://背景图高斯模糊完毕
                    Bitmap bg = (Bitmap)msg.obj;
                    backgroundImg.setImageBitmap(bg);
                    app.setBlurBackground(bg);
                    break;
                case 201://天气加载成功
                    setWeatherView();
                    break;
                case 202://天气加载失败
                    app.T((String) msg.obj);
                    break;
                case ON_DOWNLOAD_IMG:
                    if(loadDialog2!=null)
                        loadDialog2.setProgress((Float) msg.obj);
                    break;
                case ON_DOWNLOAD_IMG_SECCESS:
                    if(loadDialog2!=null)
                        loadDialog2.dismiss();
                    ToastUtil.T(MainActivity.this,"下载完成，位于LTool/img中");
                    break;
                case ON_DOWNLOAD_IMG_ERROR:
                    ToastUtil.T(MainActivity.this,"下载失败，请稍后重试。");
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private void getWeather(){
        String city = SharedPreferencesUtils.getCity(this);
//        Log.d("城市",city);
//        app.T("当前城市："+city);
        if(city==null||"".equals(city.trim()))
            return;
        NetTasks.getEtouchWeather(new HttpTaskRunnable.CallBack<WeatherBean>() {
            Message message = new Message();
            @Override
            public void success(WeatherBean object) {
                if(object!=null){
                    weatherBean = object;
                    message.what = 201;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void error(int code, String msg) {
                message.what = 202;
                message.obj = "抱歉！天气信息获取出错！\n请更换天气源";
                handler.sendMessage(message);
//                Log.d("天气获取",msg);
            }

            @Override
            public WeatherBean str2Obj(String str) {
                try {
                    return WeatherUtil.getEtouchWeather(str);
                } catch (DocumentException e) {
                    message.what = 202;
                    message.obj = "抱歉！天气信息解析出错";
                    handler.sendMessage(message);
                    e.printStackTrace();
                    return null;
                }
            }
        },city);
    }

    @Override
    public void onBackPressed() {
        exitBy2Click();
    }

    private void exitBy2Click() {
        Timer tExit = null;
        if (!isExit) {
            isExit = true; // 准备退出
            ToastUtil.T(this, "再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
        }
    }

    /****************权限检查代码块开始*******************/
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    private static final int SD_PERMISSON_REQUESTCODE = 0;
    private static final int CAMERA_PERMISSON_REQUESTCODE = 1;

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    /**
     *
     * @param permissions
     * @since 2.5.0
     *
     */
    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    SD_PERMISSON_REQUESTCODE);
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     *
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == CAMERA_PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showCameraMissingPermissionDialog();
            }
        }
        if (requestCode == SD_PERMISSON_REQUESTCODE) {
            isNeedCheck = false;
            if (!verifyPermissions(paramArrayOfInt)) {
                showSDMissingPermissionDialog();
            }
        }
    }

    /****************摄像头权限检查开始*****************/

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

    private void showSDMissingPermissionDialog() {
        DialogUtil.getAlertDialog(this, "权限获取", "您需要同意我们对储存设备的读写功能，这样我们才能下载和缓存图片，否则将造成某些功能不可用。",
                "拒绝授权",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
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
        if(ContextCompat.checkSelfPermission(this,perm)
                == PackageManager.PERMISSION_GRANTED){
            startActivity(new Intent(this,QRReadActivity.class));
        }else
        if (ContextCompat.checkSelfPermission(this,perm)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.shouldShowRequestPermissionRationale(
                this, perm)) {
            ActivityCompat.requestPermissions(this,new String[]{perm},CAMERA_PERMISSON_REQUESTCODE);
        }
    }

    /****************摄像头权限检查结束*****************/

    /****************权限检查代码块结束*******************/

    @Override
    protected void onDestroy() {
        if(SharedPreferencesUtils.getShowMeizhiOnce(this)){
            SharedPreferencesUtils.setShowMeizhi(this,false);
            SharedPreferencesUtils.setShowMeizhiOnce(this,false);
        }
        super.onDestroy();
    }
}
