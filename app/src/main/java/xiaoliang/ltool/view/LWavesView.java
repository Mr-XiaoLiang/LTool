package xiaoliang.ltool.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by liuj on 2016/9/30.
 * 用在扫描页面的一个波浪View
 */

public class LWavesView extends View {

    private int color = Color.parseColor("#41ddaf");
    private final static int alpha = 150;
    private int width;
    private int offX = 0;
    private int baseLine;
    private float proportionY = 1;
    private ArrayList<Point> points;
    private ArrayList<Point> lastPoints;
    private final static float damping = 0.9f;
    private Path path;
    private Paint paint;
    private Paint linePaint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0,baseLine-1,width,baseLine+1,linePaint);
        for(int i = 0;i<points.size();i++){
            Point point = points.get(i);
            drawPath(point,canvas);
        }
        for(int i = 0;i<lastPoints.size();i++){
            Point point = lastPoints.get(i);
            drawPath(point,canvas);
        }
        lastPoints.addAll(points);
        points.clear();
        Iterator<Point> pointIterable = lastPoints.iterator();
        while(pointIterable.hasNext()){
            Point point = pointIterable.next();
            if(Math.abs(point.y)>5){
                point.y *= damping;
            }else{
                pointIterable.remove();
            }
        }
        invalidate();
    }

    private void drawPath(Point p,Canvas canvas){
        int lw = Math.abs(p.y)*4;
        int rw = lw;
        rw = (p.x+rw)>width?width:(p.x+rw);
        lw = (p.x-lw)<0?0:(p.x-lw);
        path.reset();
        path.moveTo(0,baseLine);
        path.lineTo(lw,baseLine);
        path.cubicTo((lw+p.x)/2,baseLine,(lw+p.x)/2,p.y+baseLine,p.x,p.y+baseLine);
        path.cubicTo((rw+p.x)/2,p.y+baseLine,(rw+p.x)/2,baseLine,rw,baseLine);
        path.lineTo(width,baseLine);
        path.close();
        canvas.drawPath(path,paint);
    }

    public void addPoint(Point point){
        point.y *= proportionY;
        point.y -= baseLine;
        point.x += offX;
        points.add(point);
    }

    public void setOffX(int offX) {
        this.offX = offX;
    }

    public void setProportionY(float proportionY) {
        this.proportionY = proportionY;
    }

    public void setWavesColor(int color) {
        this.color = color;
        if(paint!=null)
            paint.setColor(color);
    }

    private void init(){
        points = new ArrayList<>();
        lastPoints = new ArrayList<>();
//        paths = new ArrayList<>();
        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setAlpha(alpha);
        linePaint = new Paint();
        linePaint.setColor(color);
        linePaint.setAntiAlias(true);
        linePaint.setDither(true);
        path = new Path();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        baseLine = getHeight()/2;
    }

    public LWavesView(Context context) {
        this(context,null);
    }
    public LWavesView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public LWavesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
}
