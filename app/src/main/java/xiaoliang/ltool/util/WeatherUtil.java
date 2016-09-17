package xiaoliang.ltool.util;

import com.google.gson.JsonObject;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

import xiaoliang.ltool.bean.WeatherBean;
import xiaoliang.ltool.bean.WeatherDayBean;
import xiaoliang.ltool.bean.WeatherExponentBean;

/**
 * Created by liuj on 2016/9/13.
 * 解析天气接口返回的数据的工具类
 */
public class WeatherUtil {
    /**
     * 中华万年历的数据接口
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static WeatherBean getEtouchWeather(String xml) throws DocumentException {
        WeatherBean weatherBean = new WeatherBean();
        XMLUtil util = new XMLUtil(xml);
        Element error = util.getElement("error");
        if(error!=null){
            weatherBean.setType(false);
            weatherBean.setMsg(error.getText());
            return weatherBean;
        }
        weatherBean.setCity(util.getText("city"));
        weatherBean.setUpdateTime(util.getText("updatetime"));
        weatherBean.setNowTemperature(util.getText("wendu"));
        weatherBean.setWind(util.getText("fengli"));
        weatherBean.setHumidity(util.getText("shidu"));
        weatherBean.setWindDirection(util.getText("fengxiang"));
        Element envi = util.getElement("environment");
        weatherBean.setAqi(util.getText(envi,"aqi"));
        weatherBean.setPm25(util.getText(envi,"pm25"));
        weatherBean.setSuggest(util.getText(envi,"suggest"));
        weatherBean.setQuality(util.getText(envi,"quality"));
        List<Element> weathers = util.getList(util.getElement("forecast"),"weather");
        ArrayList<WeatherDayBean> dayBeanList = new ArrayList<>();
        for(Element weather : weathers){
            WeatherDayBean dayBean = new WeatherDayBean();
            dayBean.setDate(util.getText(weather,"date"));
            dayBean.setHigh(util.getText(weather,"high"));
            dayBean.setLow(util.getText(weather,"low"));
            Element day = util.getElement(weather,"day");
            Element night = util.getElement(weather,"night");
            dayBean.setDaytype(util.getText(day,"type"));
            dayBean.setDayWind(util.getText(day,"fengli"));
            dayBean.setDayWindDirection(util.getText(day,"fengxiang"));
            dayBean.setNighttype(util.getText(night,"type"));
            dayBean.setNightWind(util.getText(night,"fengli"));
            dayBean.setNightWindDirection(util.getText(night,"fengxiang"));
            dayBeanList.add(dayBean);
        }
        List<Element> exponents = util.getList(util.getElement("zhishus"),"zhishu");
        ArrayList<WeatherExponentBean> exponentBeenList = new ArrayList<>();
        for(Element weather : exponents){
            WeatherExponentBean bean = new WeatherExponentBean();
            bean.setName(util.getText(weather,"name"));
            bean.setValue(util.getText(weather,"value"));
            bean.setDetail(util.getText(weather,"detail"));
            exponentBeenList.add(bean);
        }
        weatherBean.setDayBeen(dayBeanList);
        weatherBean.setExponentBeen(exponentBeenList);
        return weatherBean;
    }

    public static void get(String json){
//        JsonObject jsonObject =
    }

}
