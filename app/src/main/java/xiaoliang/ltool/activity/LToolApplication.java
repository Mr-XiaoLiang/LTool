package xiaoliang.ltool.activity;

import android.app.Application;
import android.widget.Toast;

/**
 * Created by liuj on 2016/9/13.
 * 应用上下文
 */
public class LToolApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void T(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

}
