package xiaoliang.ltool.util;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/9/13.
 */
public class BlurBitmapRunnable implements Runnable{

    public BlurBitmapRunnable(Bitmap bitmap, Context context, BlurBitmapListener listener) {
        this.bitmap = bitmap;
        this.context = context;
        this.listener = listener;
    }

    Context context;
    Bitmap bitmap;
    BlurBitmapListener listener;

    @Override
    public void run() {
        Bitmap retBmp = BlurUtil.quickBlurBitmap(context,bitmap);
        listener.onBlur(retBmp);
    }
    public interface BlurBitmapListener{

        void onBlur(Bitmap bitmap);

    }
}

