package xiaoliang.ltool.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.FloatRange;

/**
 * Created by liuj on 2016/10/28.
 * 动态返回键的drawable实现
 * 可以加入到ToolBar中显示,或者其他View
 */

public class LBackDrawable extends Drawable {

    private Paint paint;
    private float progress = 0;
    private int size = 0;
    private Rect bundle;
    private Path path;
    private float strokeWidthFloat = 0.13F;

    public LBackDrawable(Context context) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.WHITE);
        size = dip2px(context,18);
        paint.setStrokeWidth(size*strokeWidthFloat);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setStrokeCap(Paint.Cap.BUTT);
        path = new Path();
    }



    public float getProgress() {
        return progress;
    }

    public void setProgress(@FloatRange(from = 0.0, to = 1.0) float progress) {
        if (this.progress != progress) {
            this.progress = progress;
            invalidateSelf();
        }
    }

    @Override
    public int getIntrinsicHeight() {
        return size;
    }

    @Override
    public int getIntrinsicWidth() {
        return size;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        bundle = getBounds();
        size = Math.min(bundle.width(),bundle.height());
        paint.setStrokeWidth(size*strokeWidthFloat);
//        Log.d("onBoundsChange",""+paint.getStrokeWidth());
    }

    @Override
    public void draw(Canvas canvas) {
        path.rewind();
        PointF start = new PointF(bundle.centerX()-size*0.4f,bundle.centerY());
        float arrow = size*0.4f*progress;
        path.moveTo(start.x,start.y);
        path.lineTo(start.x+size*0.8f,start.y);
        path.moveTo(bundle.centerX(),bundle.centerY()-size*0.4f);
        path.lineTo(start.x+arrow,start.y);
        path.lineTo(bundle.centerX(),bundle.centerY()+size*0.4f);
        canvas.save();
        canvas.rotate(135*(progress-1)+135,bundle.centerX(),bundle.centerY());
        canvas.drawPath(path,paint);
        canvas.restore();
    }
    public void setAntiAlias(boolean antiAlias){
        paint.setAntiAlias(antiAlias);
        invalidateSelf();
    }
    public void setDrawDither(boolean Dither){
        paint.setDither(Dither);
        invalidateSelf();
    }
    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
        invalidateSelf();
    }
    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
        invalidateSelf();
    }
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setColor(int color){
        paint.setColor(color);
        invalidateSelf();
    }

    public void setSize(int size) {
        if (this.size != size) {
            this.size = size;
            invalidateSelf();
        }
    }
}
