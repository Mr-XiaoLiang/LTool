package xiaoliang.ltool.activity;

import android.app.Application;
import android.widget.Toast;

import xiaoliang.ltool.util.ToastUtil;

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
        ToastUtil.T(this,s);
    }

}
