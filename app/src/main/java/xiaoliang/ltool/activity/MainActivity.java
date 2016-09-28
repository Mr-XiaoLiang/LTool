package xiaoliang.ltool.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.dom4j.DocumentException;

import java.util.ArrayList;
import java.util.List;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.WeatherBean;
import xiaoliang.ltool.bean.WeatherDayBean;
import xiaoliang.ltool.constant.Constant;
import xiaoliang.ltool.dialog.CityDialog;
import xiaoliang.ltool.util.BlurBitmapRunnable;
import xiaoliang.ltool.util.DialogUtil;
import xiaoliang.ltool.util.HttpTaskRunnable;
import xiaoliang.ltool.util.HttpUtil;
import xiaoliang.ltool.util.NetTasks;
import xiaoliang.ltool.util.OtherUtil;
import xiaoliang.ltool.util.SharedPreferencesUtils;
import xiaoliang.ltool.util.WeatherUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {
    /*主要控件*/
    private ImageLoader imageLoader;
    private CollapsingToolbarLayout toolbarLayout;
    private FloatingActionButton fab;
    private ImageView headImg;
    private TextView headText;
    private boolean loadWebImg = false;
    private ImageView backgroundImg;
    private MyHandler handler;
    private LToolApplication app;
    /*天气控件*/
    private TextView weatherDate,weatherTime,weatherTemperature,weatherDayType,weatherDayWind,weatherNightType,weatherNightWind;
    private CardView weather;
    private WeatherBean weatherBean;
    //定位部分
    private boolean autoLocation = true;
    private boolean isGetLocation = false;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();
    //二维码部分
    private CardView qrRead;
    private CardView qrCreate;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        headImg = (ImageView) findViewById(R.id.activity_main_headimg);
        headText = (TextView) findViewById(R.id.activity_main_headtext);
        backgroundImg = (ImageView) findViewById(R.id.activity_main_bg);
        qrRead = (CardView) findViewById(R.id.content_main_qrread);
        qrCreate = (CardView) findViewById(R.id.content_main_qrcreate);
        imageLoader = ImageLoader.getInstance();
        handler = new MyHandler();
        qrCreate.setOnClickListener(this);
        qrRead.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    protected void onResume() {
        super.onResume();
        loadWebImg = SharedPreferencesUtils.isLoadWebImg(this);
        autoLocation = SharedPreferencesUtils.getAutoLocation(this);
        getWeather();
        if(autoLocation){
            List<String> needRequestPermissonList = findDeniedPermissions(needPermissions);
            if(needRequestPermissonList==null||needRequestPermissonList.size()<1){
                if(!isGetLocation){
                    //初始化定位
                    initLocation();
                    isGetLocation = true;
                }
            }else if(isNeedCheck){
                checkPermissions(needPermissions);
            }
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
        imageLoader.displayImage(Constant.getBabkgroundPath(this),headImg,null,new MyImageLoadingListener(this,false));
        if(networdType==Constant.NetWord_WIFI||(networdType==Constant.NetWord_MOBILE&&!onlyWifi)){
            if(loadWebImg&&!SharedPreferencesUtils.isGetBgEnd(this)){
                imageLoader.displayImage(Constant.head_img_url_720,headImg,null,new MyImageLoadingListener(this,true));
            }
        }
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
            case R.id.content_main_qrread:
                checkCameraPermission();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    private class MyImageLoadingListener extends SimpleImageLoadingListener{
        private Context context;
        private boolean isSave;
        public MyImageLoadingListener(Context context,boolean isSave) {
            this.context = context;
            this.isSave = isSave;
        }
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if(isSave){
                OtherUtil.saveBabkground(context,loadedImage);
                SharedPreferencesUtils.setGetBgTime(context);
            }
            setBackground(loadedImage);
        }
    }

    /**
     * 异步对图片进行高斯模糊
     * @param bitmap
     */
    private void setBackground(Bitmap bitmap){
        HttpUtil.getThread(new BlurBitmapRunnable(bitmap,this,new BlurBitmapRunnable.BlurBitmapListener(){
            @Override
            public void onBlur(Bitmap bitmap) {
                Message message = new Message();
                message.what = 200;
                message.obj = bitmap;
                handler.sendMessage(message);
            }
        }));
    }

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 200://背景图高斯模糊完毕
                    backgroundImg.setImageBitmap((Bitmap)msg.obj);
                    break;
                case 201://天气加载成功
                    setWeatherView();
                    break;
                case 202://天气加载失败
                    app.T((String) msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private void getWeather(){
        String city = SharedPreferencesUtils.getCity(this);
        Log.d("城市",city);
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
                Log.d("天气获取",msg);
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

    /****************权限检查代码块开始*******************/
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private static final int LOCATION_PERMISSON_REQUESTCODE = 0;
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
                    LOCATION_PERMISSON_REQUESTCODE);
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
        if (requestCode == LOCATION_PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
                isNeedCheck = false;
            }
        }
        if (requestCode == CAMERA_PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showCameraMissingPermissionDialog();
            }
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     *
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("您开启了自动获取位置的功能\n但是我们需要必要的权限来定位，我们不会将此权限用于其他不法用途。请问是否去开启权限？");

        // 拒绝, 退出应用
        builder.setNegativeButton("拒绝",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setAuto();
                    }
                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 设置不再自动定位
     */
    private void setAuto(){
        SharedPreferencesUtils.setAutoLocation(this,false);
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
    /****************定位代码块开始*******************/

    /**
     * 初始化定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void initLocation(){
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        startLocation();
    }

    /**
     * 默认的定位参数
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
//        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是ture
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(true);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setLocationCacheEnable(true);// 设置是否开启缓存
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        return mOption;
    }

    /**
     * 定位监听
     */
    private AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc&&!"".equals(loc.getCity().trim())) {
                //解析定位结果
                setCity(loc);
            } else {
                app.T("定位失败，请手动设置城市");
            }
        }
    };

    /**
     * 保存位置信息
     * @param loc
     */
    private void setCity(AMapLocation loc){
        SharedPreferencesUtils.setAMapLocation(this,loc);
        app.T("自动定位结果："+loc.getCity());
        //定位后重新刷新天气
        getWeather();
    }

    /**
     * 开始定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void startLocation(){
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void stopLocation(){
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void destroyLocation(){
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }
    /****************定位代码块结束*******************/
}
