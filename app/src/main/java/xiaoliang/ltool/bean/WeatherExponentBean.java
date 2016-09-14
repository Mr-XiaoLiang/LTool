package xiaoliang.ltool.bean;

/**
 * Created by Liuj on 2016/9/13.
 * 天气指数
 */
public class WeatherExponentBean {
    private String name;//名字
    private String value;//等级
    private String detail;//建议

    public WeatherExponentBean() {
    }

    public WeatherExponentBean(String detail, String name, String value) {
        this.detail = detail;
        this.name = name;
        this.value = value;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
