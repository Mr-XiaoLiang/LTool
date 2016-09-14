package xiaoliang.ltool.bean;

/**
 * Created by Liuj on 2016/9/13.
 * 键值对的bean
 */
public class NameValueBean<N,V> {
    private N name;
    private V value;

    public N getName() {
        return name;
    }

    public void setName(N name) {
        this.name = name;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public NameValueBean() {
    }

    public NameValueBean(N name, V value) {
        this.name = name;
        this.value = value;
    }
}
