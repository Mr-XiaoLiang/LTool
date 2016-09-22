package xiaoliang.ltool.util;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import xiaoliang.ltool.bean.CityBean;
import xiaoliang.ltool.bean.CityBean4City;
import xiaoliang.ltool.bean.CityBean4Province;
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
        xml = xml.replaceAll("<!-*.*-*>", "");
        WeatherBean weatherBean = new WeatherBean();
        XMLUtil util = new XMLUtil(xml);
        Element error = util.getElement("error");
        if(error!=null){
            weatherBean.setSeccess(false);
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
        if(envi!=null){
            weatherBean.setAqi(util.getText(envi,"aqi"));
            weatherBean.setPm25(util.getText(envi,"pm25"));
            weatherBean.setSuggest(util.getText(envi,"suggest"));
            weatherBean.setQuality(util.getText(envi,"quality"));
        }else{
            weatherBean.setAqi("暂无数据");
            weatherBean.setPm25("暂无数据");
            weatherBean.setSuggest("暂无数据");
            weatherBean.setQuality("暂无数据");
        }
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

    /**
     * 从json里解析城市列表
     * @param json
     * @return
     * @throws JSONException
     */
    public static CityBean getCitys(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        CityBean cityBean;
        if(jsonObject.getInt("retCode")==200){
            cityBean = new CityBean(true);
            JSONArray jsonArray4Pro = jsonObject.getJSONArray("result");
            for(int i = 0;i<jsonArray4Pro.length();i++){
                JSONObject province = jsonArray4Pro.getJSONObject(i);
                CityBean4Province cityBean4Province = new CityBean4Province(province.getString("province"));
                JSONArray jsonArray4City = province.getJSONArray("city");
                for(int j = 0;j<jsonArray4City.length();j++){
                    JSONObject city = jsonArray4City.getJSONObject(j);
                    CityBean4City cityBean4City = new CityBean4City(city.getString("city"));
                    if(city.isNull("district")){
                        cityBean4City.addArea(cityBean4City.getName());
                    }else{
                        JSONArray jsonArray4Area = city.getJSONArray("district");
                        for(int k = 0;k<jsonArray4Area.length();k++){
                            cityBean4City.addArea(jsonArray4Area.getJSONObject(k).getString("district"));
                        }
                    }
                    cityBean4Province.addCity(cityBean4City);
                }
                cityBean.add(cityBean4Province);
            }
        }else{
            cityBean = new CityBean(false);
            cityBean.setMsg(jsonObject.getString("msg"));
        }
        return cityBean;
    }

}
