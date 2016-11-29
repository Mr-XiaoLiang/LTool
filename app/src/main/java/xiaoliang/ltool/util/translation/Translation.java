package xiaoliang.ltool.util.translation;

import android.os.Handler;
import android.os.Message;

import java.util.List;

import xiaoliang.ltool.bean.StringNameValueBean;
import xiaoliang.ltool.util.HttpTaskRunnable;
import xiaoliang.ltool.util.HttpUtil;
import xiaoliang.ltool.util.RequestParameters;

/**
 * Created by liuj on 2016/11/29.
 * 翻译
 */

public class Translation {

    private static final int SECCESS = 200;
    private static final int ERROR = 400;

    private TranslationRequest request;
    private TranslationCallback callback;
    private TranslationNetWorkCallBack netWork;
    private String fromStr;

    public Translation() {
        netWork = new TranslationNetWorkCallBack();
    }

    public static Translation newInstance(TranslationRequest request,TranslationCallback callback){
        Translation translation = new Translation();
        translation.setRequest(request);
        translation.setCallback(callback);
        return translation;
    }

    public void execute(String from){
        fromStr = from;
        RequestParameters parameters = new RequestParameters();
        parameters.setAccessType(RequestParameters.ACCESS_TYPE_OBJECT);
        request.setFrom(from);
        parameters.setParameters(request.getParameters());
        parameters.setUrl(request.getURL());
        HttpUtil.createTask(netWork, parameters);
    }

    public void setCallback(TranslationCallback callback) {
        this.callback = callback;
    }

    public void setRequest(TranslationRequest request) {
        this.request = request;
    }

    private class TranslationNetWorkCallBack implements HttpTaskRunnable.CallBack<String>{
        @Override
        public void success(String object) {
            Message message = handler.obtainMessage(SECCESS);
            message.obj = object;
            handler.sendMessage(message);
        }

        @Override
        public void error(int code, String msg) {
            Message message = handler.obtainMessage(ERROR);
            message.obj = msg;
            handler.sendMessage(message);
        }

        @Override
        public String str2Obj(String str) {
            return request.decode(str);
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SECCESS:
                case ERROR:
                    callback.onTranslation(fromStr, (String) msg.obj);
                    return;
            }
            super.handleMessage(msg);
        }
    };

    public interface TranslationRequest{
        List<StringNameValueBean> getParameters();
        void setFrom(String from);
        String getURL();
        String decode(String str);
    }

    public interface TranslationCallback{
        void onTranslation(String from,String to);
    }
}
