package xiaoliang.ltool.fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;

import xiaoliang.ltool.bean.MeizhiBean;

/**
 * Created by liuj on 2016/10/6.
 * 妹子图分页的handler
 */

public class MeizhiFragmentHandler extends Handler {

    private MeizhiFragment fragment;
    public static final int getdata = 200;
    public static final int error = 202;

    public MeizhiFragmentHandler(MeizhiFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case getdata:
                if(fragment!=null){
                    fragment.setData((ArrayList<MeizhiBean>) msg.obj);
                }
                break;
            case error:
                if(fragment!=null){
                    fragment.onError((String) msg.obj);
                }
                break;
        }
        super.handleMessage(msg);
    }
}
