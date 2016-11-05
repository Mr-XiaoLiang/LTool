package xiaoliang.ltool.activity.weather;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.dom4j.DocumentException;

import java.util.ArrayList;

import xiaoliang.ltool.R;
import xiaoliang.ltool.activity.LToolApplication;
import xiaoliang.ltool.bean.WeatherBean;
import xiaoliang.ltool.bean.WeatherDayBean;
import xiaoliang.ltool.bean.WeatherExponentBean;
import xiaoliang.ltool.dialog.CityDialog;
import xiaoliang.ltool.dialog.LoadDialog;
import xiaoliang.ltool.util.DialogUtil;
import xiaoliang.ltool.util.HttpTaskRunnable;
import xiaoliang.ltool.util.NetTasks;
import xiaoliang.ltool.util.SharedPreferencesUtils;
import xiaoliang.ltool.util.WeatherUtil;
import xiaoliang.ltool.view.LGradualBGView;
import xiaoliang.ltool.view.WeatherDayItem;
import xiaoliang.ltool.view.WeatherExponentItem;

/**
 * 天气的详细页面
 */
public class WeatherActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private WeatherBean weatherBean;
    private MyHandler handler;
    private LToolApplication app;
    private Toolbar toolbar;
    private SwipeRefreshLayout refreshLayout;
    private TextView tempView,highAndLowView,timeView,windView,windDirecView,pm25View,humidityView,aqiView,majorView,msgView;
    private LinearLayout dayLayout,exponentLayout;
    private LoadDialog loadDialog;
    private LGradualBGView bgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        initView();
        app = (LToolApplication) getApplicationContext();
        handler = new MyHandler();
    }

    private void initView(){
        toolbar = (Toolbar) findViewById(R.id.activity_weather_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bgView = (LGradualBGView) findViewById(R.id.activity_weather_bg);
        tempView = (TextView) findViewById(R.id.activity_weather_temp);
        highAndLowView = (TextView) findViewById(R.id.activity_weather_highlow);
        timeView = (TextView) findViewById(R.id.activity_weather_time);
        windView = (TextView) findViewById(R.id.activity_weather_wind);
        windDirecView = (TextView) findViewById(R.id.activity_weather_wind_direction);
        pm25View = (TextView) findViewById(R.id.activity_weather_pm25);
        humidityView = (TextView) findViewById(R.id.activity_weather_humidity);
        aqiView = (TextView) findViewById(R.id.activity_weather_aqi);
        majorView = (TextView) findViewById(R.id.activity_weather_major);
        msgView = (TextView) findViewById(R.id.activity_weather_msg);
        dayLayout = (LinearLayout) findViewById(R.id.activity_weather_day_layout);
        exponentLayout = (LinearLayout) findViewById(R.id.activity_weather_exponent_layout);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_weather_refresh);
        refreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(this);
    }

    private void setWeatherView(){
        bgView.setTemperature(Integer.parseInt(weatherBean.getNowTemperature()));
        bgView.setHours(Integer.parseInt(weatherBean.getUpdateTime().substring(0,2)));
        tempView.setText(weatherBean.getNowTemperature());
        highAndLowView.setText(weatherBean.getDayBeen(0).getQuickTemperature());
        timeView.setText(weatherBean.getUpdateTime());
        windView.setText(weatherBean.getWind());
        windDirecView.setText(weatherBean.getWindDirection());
        pm25View.setText(weatherBean.getPm25());
        humidityView.setText(weatherBean.getHumidity());
        aqiView.setText(weatherBean.getQuickAqi());
        majorView.setText(weatherBean.getMajorPollutants());
        msgView.setText(weatherBean.getMsg());
        toolbar.setTitle(weatherBean.getCity());
        ArrayList<WeatherDayBean> dayBeanArrayList = weatherBean.getDayBeen();
        ArrayList<WeatherExponentBean> exponentBeanArrayList = weatherBean.getExponentBeen();
        dayLayout.removeAllViews();
        exponentLayout.removeAllViews();
        for(WeatherDayBean dayBean : dayBeanArrayList){
            WeatherDayItem dayItem = new WeatherDayItem(this);
            dayItem.setDayBean(dayBean);
            dayLayout.addView(dayItem);
        }
        int index = 0;
        while(index<exponentBeanArrayList.size()){
            WeatherExponentItem exponentItem = new WeatherExponentItem(this);
            if(exponentBeanArrayList.size()-index>1)
                exponentItem.setBeans(exponentBeanArrayList.get(index++),exponentBeanArrayList.get(index++));
            else
                exponentItem.setBeans(exponentBeanArrayList.get(index++));
            exponentLayout.addView(exponentItem);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoad();
        getWeather();
    }

    private void showLoad(){
        loadDialog = DialogUtil.getLoadDialog(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_weather_city:
                DialogUtil.getCityDialog(this, new CityDialog.CitySelectedListener() {
                    @Override
                    public void citySelected(String name) {
                        SharedPreferencesUtils.setCity(WeatherActivity.this,name);
                        showLoad();
                        getWeather();
                    }
                });
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void getWeather(){
        Log.d("城市",SharedPreferencesUtils.getCity(this));
        NetTasks.getEtouchWeather(new HttpTaskRunnable.CallBack<WeatherBean>() {
            Message message = new Message();
            @Override
            public void success(WeatherBean object) {
                message.what = 201;
                if(object!=null)
                    weatherBean = object;
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
            public WeatherBean str2Obj(String str) {
                try {
                    return WeatherUtil.getEtouchWeather(str);
                } catch (DocumentException e) {
                    message.what = 202;
                    message.obj = "抱歉！天气信息解析出错";
                    e.printStackTrace();
                    handler.sendMessage(message);
                    return null;
                }
            }
        },SharedPreferencesUtils.getCity(this));
    }

    @Override
    public void onRefresh() {
        getWeather();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            refreshLayout.setRefreshing(false);
            if(loadDialog!=null)
                loadDialog.dismiss();
            switch (msg.what){
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
}
