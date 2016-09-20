package xiaoliang.ltool.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.dom4j.DocumentException;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        imageLoader = ImageLoader.getInstance();
        handler = new MyHandler();
        initWeatherView();
        loadImg();
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWebImg = SharedPreferencesUtils.isLoadWebImg(this);
        getWeather();
    }

    private void setWeatherView(){
        WeatherDayBean today = weatherBean.getDayBeen().get(0);
        weatherDate.setText(today.getDate());
        weatherTime.setText(weatherBean.getUpdateTime());
        weatherTemperature.setText(today.getQuickTemperature());
        weatherDayType.setText(today.getDaytype());
        weatherDayWind.setText(today.getQuickDayWind());
        weatherNightType.setText(today.getNighttype());
        weatherNightWind.setText(today.getQuickNightWind());
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
        weather.setOnClickListener(this);
    }

    private void loadImg(){
        imageLoader.displayImage(Constant.getBabkgroundPath(this),headImg,null,new MyImageLoadingListener(this,false));
        if(loadWebImg&&!SharedPreferencesUtils.isGetBgEnd(this)){
            imageLoader.displayImage(Constant.head_img_url_720,headImg,null,new MyImageLoadingListener(this,true));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.content_main_weather:
                startActivity(new Intent(this,WeatherActivity.class));
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
        Log.d("城市",SharedPreferencesUtils.getCity(this));
        NetTasks.getEtouchWeather(new HttpTaskRunnable.CallBack() {
            @Override
            public void success(Object object) {
                Message message = new Message();
                message.what = 201;
                try {
                    weatherBean = WeatherUtil.getEtouchWeather((String) object);
                } catch (DocumentException e) {
                    message.what = 202;
                    message.obj = "抱歉！天气信息解析出错";
                    e.printStackTrace();
                }
                handler.sendMessage(message);
            }

            @Override
            public void error(int code, String msg) {
                Message message = new Message();
                message.what = 202;
                message.obj = "抱歉！天气信息获取出错！\n请更换天气源";
                handler.sendMessage(message);
                Log.d("天气获取",msg);
            }

            @Override
            public Object str2Obj(String str) {
                return str;
            }
        },SharedPreferencesUtils.getCity(this));
    }



}
