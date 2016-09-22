package xiaoliang.ltool.bean;

import java.util.ArrayList;

/**
 * Created by liuj on 2016/9/13.
 * 天气信息的Bean
 */
public class WeatherBean {
    private boolean seccess;
    private String msg;
    private String city;//城市
    private String updateTime;//更新时间
    private String nowTemperature;//当前温度
    private String wind;//风力
    private String humidity;//湿度
    private String windDirection;//风向
    private String aqi;//空气质量
    private String pm25;//pm2.5
    private String suggest;//建议
    private String quality;//空气质量
    private String majorPollutants;//主要污染物
    private ArrayList<WeatherDayBean> dayBeen;//每天天气
    private ArrayList<WeatherExponentBean> exponentBeen;//天气指数

    public WeatherBean() {
        seccess = true;
    }

    public String getAqi() {
        return aqi;
    }

    public String getQuickAqi() {
        return aqi+"["+quality+"]";
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getCity() {
        return city;
    }

    public String getTitle(){
        String title = "";
        if(getCity()!=null&&!getCity().equals("null")){
            title+=(getCity()+" ");
        }
        if(getNowTemperature()!=null&&!getNowTemperature().equals("null")){
            title+=(getNowTemperature()+"℃");
        }
        return title;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public ArrayList<WeatherDayBean> getDayBeen() {
        return dayBeen;
    }
    public WeatherDayBean getDayBeen(int index) {
        return dayBeen.get(index);
    }

    public void setDayBeen(ArrayList<WeatherDayBean> dayBeen) {
        this.dayBeen = dayBeen;
    }

    public ArrayList<WeatherExponentBean> getExponentBeen() {
        return exponentBeen;
    }

    public WeatherExponentBean getExponentBeen(int index) {
        return exponentBeen.get(index);
    }

    public void setExponentBeen(ArrayList<WeatherExponentBean> exponentBeen) {
        this.exponentBeen = exponentBeen;
    }

    public String getMajorPollutants() {
        return majorPollutants;
    }

    public void setMajorPollutants(String majorPollutants) {
        this.majorPollutants = majorPollutants;
    }

    public String getNowTemperature() {
        return nowTemperature;
    }

    public void setNowTemperature(String nowTemperature) {
        this.nowTemperature = nowTemperature;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSeccess() {
        return seccess;
    }

    public void setSeccess(boolean seccess) {
        this.seccess = seccess;
    }
}
