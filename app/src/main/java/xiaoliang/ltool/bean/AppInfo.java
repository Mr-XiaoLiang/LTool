package xiaoliang.ltool.bean;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by liuj on 2016/11/10.
 * app信息Bean
 */

public class AppInfo {
    public String name;
    public Drawable appIcon;
    public Intent intent;
    public String pkgName;

    public AppInfo() {
    }

    public AppInfo(Drawable appIcon, Intent intent, String name, String pkgName) {
        this.appIcon = appIcon;
        this.intent = intent;
        this.name = name;
        this.pkgName = pkgName;
    }
}
