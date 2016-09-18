package xiaoliang.ltool.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.WeatherExponentBean;

/**
 * Created by liuj on 2016/9/18.
 * 用作天气指数的Item
 */
public class WeatherExponentItem extends LinearLayout {

    private WeatherExponentBean[] beans;

    private CardView cardView1,cardView2;
    private TextView name1,value1,msg1,name2,value2,msg2;
    private Context context;

    private void init(){
        LayoutInflater.from(context).inflate(R.layout.item_weather_exponent,
                this, true);
        cardView1 = (CardView) findViewById(R.id.item_weather_exponent_card1);
        cardView2 = (CardView) findViewById(R.id.item_weather_exponent_card2);
        name1 = (TextView) findViewById(R.id.item_weather_exponent_name1);
        value1 = (TextView) findViewById(R.id.item_weather_exponent_value1);
        msg1 = (TextView) findViewById(R.id.item_weather_exponent_msg1);
        name2 = (TextView) findViewById(R.id.item_weather_exponent_name2);
        value2 = (TextView) findViewById(R.id.item_weather_exponent_value2);
        msg2 = (TextView) findViewById(R.id.item_weather_exponent_msg2);
        if(beans!=null&&beans.length>0){
            name1.setText(beans[0].getName());
            value1.setText(beans[0].getValue());
            msg1.setText(beans[0].getDetail());
            if(beans.length>1){
                name2.setText(beans[1].getName());
                value2.setText(beans[1].getValue());
                msg2.setText(beans[1].getDetail());
            }else{
                cardView2.setVisibility(View.INVISIBLE);
            }
        }
    }

    public WeatherExponentBean[] getBeans() {
        return beans;
    }
    public void setBeans(WeatherExponentBean... beans) {
        this.beans = beans;
    }
    public WeatherExponentItem(Context context) {
        this(context,null);
    }
    public WeatherExponentItem(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public WeatherExponentItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }
}
