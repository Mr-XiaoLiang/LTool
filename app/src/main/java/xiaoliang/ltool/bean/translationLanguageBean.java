package xiaoliang.ltool.bean;

/**
 * Created by liuj on 2016/11/30.
 * 语言类型的bean
 */

public class TranslationLanguageBean {
    public String name;
    public String id;

    public TranslationLanguageBean(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public TranslationLanguageBean() {
        this("","");
    }
}
