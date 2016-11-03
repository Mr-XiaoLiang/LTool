package xiaoliang.ltool.bean;

/**
 * Created by liuj on 2016/11/2.
 * 搜索结果的bean
 */

public class HealthSearchBean {
    public String typeStr;
    public int type;
    public String title;
    public String msg;
    public String imgUrl;
    public int status;
    public boolean success;
    public String href;

    public HealthSearchBean() {
        typeStr = "";
        title = "";
        msg = "";
        imgUrl = "";
        href = "";
        type = -1;
        status = -1;
        success = false;
    }

    public HealthSearchBean(int type, String typeStr) {
        this.type = type;
        this.typeStr = typeStr;
        title = "";
        msg = "";
        imgUrl = "";
        href = "";
        status = -1;
        success = false;
    }
}
