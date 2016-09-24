package xiaoliang.ltool.util;

import android.graphics.Bitmap;

/**
 * Created by liuj on 2016/9/25.
 * 保存图片的任务
 */

public class SaveBitmapRunnable implements Runnable {

    private SaveBitmapCallBack callBack;
    private String path;
    private String name;
    private Bitmap bitmap;

    @Override
    public void run() {
        try{
            OtherUtil.saveBitmap(bitmap,name,path);
            if(callBack!=null)
                callBack.onSaveEnd(true);
        }catch (Exception e){
            if(callBack!=null)
                callBack.onSaveEnd(false);
        }
    }

    public SaveBitmapRunnable(String path, String name, Bitmap bitmap,SaveBitmapCallBack callBack) {
        this.callBack = callBack;
        this.path = path;
        this.name = name;
        this.bitmap = bitmap;
    }

    public interface SaveBitmapCallBack{
        void onSaveEnd(boolean seccess);
    }

}
