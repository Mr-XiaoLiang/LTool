package xiaoliang.ltool.bean;

import android.content.Intent;

/**
 * Created by liuj on 2016/11/19.
 * 快捷方式信息的Bean
 */
public class ShortcutBean {
    public int index;
    public String id;
    public String shortName;//短名称
    public String longName;//长名称
    public String failureName;//失效时名称
    public int imgId;
    public Intent intent;
    public void putIntent(String action){
        intent = new Intent(action);
    }
}
