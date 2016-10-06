package xiaoliang.ltool.bean;

/**
 * Created by liuj on 2016/10/6.
 * 妹子图的bean
 */

public class MeizhiBean {
    public String url;
    public String from;
    public String title;

    public MeizhiBean() {
        url = "";
        from = "";
        title = "";
    }

    public MeizhiBean(String url) {
        this.url = url;
        from = "";
        title = "";
    }

    @Override
    public String toString() {
        return "MeizhiBean{" +
                "from='" + from + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
