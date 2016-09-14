package xiaoliang.ltool.bean;

/**
 * Created by liuj on 2016/9/13.
 * String的键值对
 */
public class StringNameValueBean extends NameValueBean<String,String> {
    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getValue() {
        return super.getValue();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
    }

    public StringNameValueBean() {
    }

    public StringNameValueBean(String name, String value) {
        super(name, value);
    }
}
