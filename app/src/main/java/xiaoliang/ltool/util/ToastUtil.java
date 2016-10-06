package xiaoliang.ltool.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by liuj on 2016/10/6.
 * 土司工具
 */

public class ToastUtil {
    public static void T(Context context,String t){
        Toast.makeText(context,t,Toast.LENGTH_SHORT).show();
    }
}
