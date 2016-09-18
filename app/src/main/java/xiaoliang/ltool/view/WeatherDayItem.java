package xiaoliang.ltool.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.WeatherDayBean;

/**
 * Created by liuj on 2016/9/18.
 * 天气中每天天气的item
 */
public class WeatherDayItem extends LinearLayout{

    private TextView date,temperature,dayType,dayWind,nightType,nightWind;
    private Context context;
    private WeatherDayBean dayBean;

    private void init(){
        LayoutInflater.from(context).inflate(R.layout.item_weather_exponent,
                this, true);
        date = (TextView) findViewById(R.id.item_weather_weather_day_date);
        temperature = (TextView) findViewById(R.id.item_weather_weather_day_temperature);
        dayType = (TextView) findViewById(R.id.item_weather_weather_day_daytype);
        dayWind = (TextView) findViewById(R.id.item_weather_weather_day_daywind);
        nightType = (TextView) findViewById(R.id.item_weather_weather_day_nighttype);
        nightWind = (TextView) findViewById(R.id.item_weather_weather_day_nightwind);
        if(dayBean!=null){
            date.setText(dayBean.getDate());
            temperature.setText(dayBean.getQuickTemperature());
            dayType.setText(dayBean.getDaytype());
            dayWind.setText(dayBean.getQuickDayWind());
            nightType.setText(dayBean.getNighttype());
            nightWind.setText(dayBean.getQuickNightWind());
        }
    }

    public WeatherDayBean getDayBean() {
        return dayBean;
    }

    public void setDayBean(WeatherDayBean dayBean) {
        this.dayBean = dayBean;
    }

    public WeatherDayItem(Context context) {
        this(context,null);
    }
    public WeatherDayItem(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public WeatherDayItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
}
