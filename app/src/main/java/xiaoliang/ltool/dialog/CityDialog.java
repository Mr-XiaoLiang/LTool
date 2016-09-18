package xiaoliang.ltool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.CityBean;
import xiaoliang.ltool.bean.CityBean4City;
import xiaoliang.ltool.bean.CityBean4Province;
import xiaoliang.ltool.util.HttpTaskRunnable;
import xiaoliang.ltool.util.NetTasks;
import xiaoliang.ltool.util.WeatherUtil;
import xiaoliang.ltool.view.LLoadView;
import xiaoliang.ltool.view.LWheelView;

/**
 * Created by liuj on 2016/9/18.
 * 用来选择城市的dialog
 */
public class CityDialog extends Dialog implements View.OnClickListener,LWheelView.LWheelViewListener {

    private View cancel,enter;
    private LWheelView proWheel,cityWheel,areaWheel;
    private LinearLayout loadLayout;
    private LLoadView loadView;
    private CitySelectedListener citySelectedListener;
    private TextView msgView;
    private CityBean cityBean;
    private CityBean4Province province;
    private CityBean4City city;
    private String areaName = "";

    public CityDialog(Context context,CitySelectedListener listener) {
        super(context);
        this.citySelectedListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除屏幕title
        setContentView(R.layout.dialog_city);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        cancel = findViewById(R.id.dialog_city_cancel);
        enter = findViewById(R.id.dialog_city_enter);
        proWheel = (LWheelView) findViewById(R.id.dialog_city_pro);
        cityWheel = (LWheelView) findViewById(R.id.dialog_city_city);
        areaWheel = (LWheelView) findViewById(R.id.dialog_city_area);
        loadLayout = (LinearLayout) findViewById(R.id.dialog_city_load_layout);
        loadView = (LLoadView) findViewById(R.id.dialog_city_load);
        msgView = (TextView) findViewById(R.id.dialog_city_msg);
        cancel.setOnClickListener(this);
        enter.setOnClickListener(this);
        proWheel.setListener(this);
        cityWheel.setListener(this);
        areaWheel.setListener(this);
        enter.setVisibility(View.GONE);
        getData();
    }

    private void getData(){
        NetTasks.getCitys(new CityDataCallBack());
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            loadView.stop();
            loadLayout.setVisibility(View.GONE);
            switch (msg.what){
                case 200:
                    cityBean = (CityBean) msg.obj;
                    if(cityBean!=null&&cityBean.isSeccess()){
                        proWheel.setData(cityBean.getProvinceNames());
                    }
                    break;
                case 201:
                    msgView.setVisibility(View.VISIBLE);
                    msgView.setText("数据解析出错\n请截图联系开发者或重试\n");
                    msgView.append((String)msg.obj);
                    break;
                case 202:
                    msgView.setVisibility(View.VISIBLE);
                    msgView.setText("数据获取出错\n请截图联系开发者或重试\n");
//                    msgView.append((String)msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private class CityDataCallBack implements HttpTaskRunnable.CallBack<CityBean>{
        @Override
        public void success(CityBean object) {
            Message message = new Message();
            message.what = 200;
            message.obj = object;
            handler.sendMessage(message);
        }

        @Override
        public void error(int code, String msg) {
            Message message = new Message();
            message.what = 202;
            message.obj = msg;
            handler.sendMessage(message);
        }

        @Override
        public CityBean str2Obj(String str) {
            try {
                return WeatherUtil.getCitys(str);
            } catch (JSONException e) {
                e.printStackTrace();
                Message message = new Message();
                message.what = 201;
                message.obj = str;
                handler.sendMessage(message);
            }
            return null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_city_enter:
                citySelectedListener.citySelected(areaName);
            case R.id.dialog_city_cancel:
                dismiss();
                break;
        }
    }

    @Override
    public void onLWheelViewChange(LWheelView view, String value, int position) {
        switch (view.getId()){
            case R.id.dialog_city_pro:
                cityWheel.setData(cityBean.getCitys(position));
                province = cityBean.getProvinces(position);
                break;
            case R.id.dialog_city_city:
                city = province.getCity(position);
                areaWheel.setData(city.getAreaNames());
                break;
            case R.id.dialog_city_area:
                areaName = value;
                enter.setVisibility(View.VISIBLE);
                break;
        }
    }

    public interface CitySelectedListener{
        void citySelected(String name);
    }
}
