package xiaoliang.ltool.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by liuj on 2016/11/21.
 * 就是一个小圆点
 * 受够了Tint了。一个小原点都各种折腾。。。。。。
 */

public class DotDrawable extends Drawable {

    private Paint paint;
    private int color = Color.BLACK;
    private int size = 0;
    private Rect bundle;
    private int diameter = -1;

    @Override
    public int getIntrinsicHeight() {
        return diameter;
    }

    @Override
    public int getIntrinsicWidth() {
        return diameter;
    }

    public DotDrawable() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(color);
    }

    public DotDrawable(Context context) {
        this();
        diameter = dip2px(context,24);
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(bundle.centerX(),bundle.centerY(),size,paint);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        bundle = getBounds();
        size = Math.min(bundle.width(),bundle.height())/2;
    }

    @Override
    public void setAlpha(int i) {
        paint.setAlpha(i);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
