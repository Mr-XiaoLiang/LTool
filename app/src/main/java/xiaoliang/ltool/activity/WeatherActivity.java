package xiaoliang.ltool.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import org.dom4j.DocumentException;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.WeatherBean;
import xiaoliang.ltool.bean.WeatherDayBean;
import xiaoliang.ltool.dialog.CityDialog;
import xiaoliang.ltool.util.DialogUtil;
import xiaoliang.ltool.util.HttpTaskRunnable;
import xiaoliang.ltool.util.NetTasks;
import xiaoliang.ltool.util.SharedPreferencesUtils;
import xiaoliang.ltool.util.WeatherUtil;

public class WeatherActivity extends AppCompatActivity {

    private WeatherBean weatherBean;
    private MyHandler handler;
    private LToolApplication app;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        app = (LToolApplication) getApplicationContext();
        handler = new MyHandler();

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
                        SharedPreferencesUtils.setCity(WeatherActivity.this,name);
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
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
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
    private void setWeatherView(){
        //TODO
    }
}
