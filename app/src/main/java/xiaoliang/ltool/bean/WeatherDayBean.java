package xiaoliang.ltool.bean;

/**
 * Created by liuj on 2016/9/13.
 * 每天天气的Bean
 */
public class WeatherDayBean {
    private String date;//日期
    private String high;//最高温度
    private String low;//最低温度
    private String daytype;//白天天气
    private String dayWind;//风力
    private String dayWindDirection;//风向
    private String nighttype;//白天天气
    private String nightWind;//风力
    private String nightWindDirection;//风向


    public WeatherDayBean() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDaytype() {
        return daytype;
    }

    public void setDaytype(String daytype) {
        this.daytype = daytype;
    }

    public String getDayWind() {
        return dayWind;
    }

    public void setDayWind(String dayWind) {
        this.dayWind = dayWind;
    }

    public String getDayWindDirection() {
        return dayWindDirection;
    }

    public void setDayWindDirection(String dayWindDirection) {
        this.dayWindDirection = dayWindDirection;
    }

    public String getHigh() {
        return high;
    }

    public String getQuickTemperature(){
        return getHigh()+" "+getLow();
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getNighttype() {
        return nighttype;
    }

    public void setNighttype(String nighttype) {
        this.nighttype = nighttype;
    }

    public String getNightWind() {
        return nightWind;
    }

    public void setNightWind(String nightWind) {
        this.nightWind = nightWind;
    }

    public String getNightWindDirection() {
        return nightWindDirection;
    }

    public void setNightWindDirection(String nightWindDirection) {
        this.nightWindDirection = nightWindDirection;
    }

    public String getQuickDayWind(){
        return getDayWindDirection()+" "+getDayWind();
    }

    public String getQuickNightWind(){
        return getNightWindDirection()+" "+getNightWind();
    }

}
