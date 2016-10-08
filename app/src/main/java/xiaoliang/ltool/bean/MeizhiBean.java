package xiaoliang.ltool.bean;

import java.io.Serializable;

/**
 * Created by liuj on 2016/10/6.
 * 妹子图的bean
 */

public class MeizhiBean implements Serializable {
    public String url;
    public String from;
    public String title;
    public String page;

    public MeizhiBean() {
        url = "";
        from = "";
        title = "";
        page = "";
    }

    public MeizhiBean(String url) {
        this.url = url;
        from = "";
        title = "";
        page = "";
    }

    @Override
    public String toString() {
        return "MeizhiBean{" +
                "from='" + from + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", page='" + page + '\'' +
                '}';
    }
}
