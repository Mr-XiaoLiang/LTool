package xiaoliang.ltool.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by liuj on 2016/9/18.
 * 这是一个渐变的View,专门为了天气的详细页面定制
 * 作为背景色,
 * 用途:根据当前时间动态调整白天黑夜的色调,
 * 根据温度动态调整色调,根据天气动态调整色温
 * 冰蓝0(#cceeff) 水蓝15（#00eedd） 水橙25(#ff88bb) 火红35(#ff2233)
 *
 */
public class LGradualBGView extends View {

    private int oldCentreColor = Color.WHITE;
    private int oldEdgeColor = Color.WHITE;
    private int newCentreColor = Color.WHITE;
    private int newEdgeColor = Color.WHITE;
    private float width,height,radius,centreX,centreY;
    private int step = 0;
    private Paint paint;
    private boolean isAnimation = true;
    private int hours = 12;
    private Paint shadowPaint;
    private static final int color_0 = Color.parseColor("#cceeff");
    private static final int color_15 = Color.parseColor("#00eedd");
    private static final int color_25 = Color.parseColor("#ff88bb");
    private static final int color_35 = Color.parseColor("#ff2233");

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setShader(getShader());
        shadowPaint.setAlpha(Math.abs((hours-12)*5));
        canvas.drawRect(0,0,width,height,paint);
        canvas.drawRect(0,0,width,height,shadowPaint);
        if(step<100){
            step++;
            invalidate();
        }
    }

    public void setHours(int hours) {
        this.hours = hours;
        invalidate();
    }

    public void setTemperature(int t){
        if(isAnimation){
            step=0;
        }else{
            step = 100;
        }
        oldCentreColor = newCentreColor;
        oldEdgeColor = newEdgeColor;
        if(t<0){
            newCentreColor = Color.WHITE;
            newEdgeColor = color_0;
        }else if(t<15){
            if(t/15.0f>0.5){
                newEdgeColor = color_0;
            }else{
                newEdgeColor = color_15;
            }
            newCentreColor = getCentreColor(color_0,color_15,t/15.0f);
        }else if(t<25){
            if((t-15)/10F>0.5){
                newEdgeColor = color_25;
            }else{
                newEdgeColor = color_15;
            }
            newCentreColor = getCentreColor(color_15,color_25,(t-15)/10F);
        }else if(t<35){
            if((t-25)/10F>0.5){
                newEdgeColor = color_35;
            }else{
                newEdgeColor = color_25;
            }
            newCentreColor = getCentreColor(color_25,color_35,(t-15)/10F);
        }else{
            newCentreColor = Color.RED;
            newEdgeColor = color_35;
        }
        invalidate();
    }

    private int getCentreColor(int low,int high,float t){
        int r = Color.red(high)-Color.red(low);
        int g = Color.green(high)-Color.green(low);
        int b = Color.blue(high)-Color.blue(low);
        r *= t;
        g *= t;
        b *= t;
        return Color.rgb(Color.red(low)+r,Color.green(low)+g,Color.blue(low)+b);
    }

    public void setAnimation(boolean animation) {
        isAnimation = animation;
    }

    private RadialGradient getShader(){
        return new RadialGradient(centreX,centreY,radius,
                new int[]{newCentreColor,newEdgeColor,oldCentreColor,oldEdgeColor},
                new float[]{step/2*0.01f,step*0.01f,(50+step)*0.01f,(100+step)*0.01f},
                Shader.TileMode.CLAMP);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        centreX = width*0.5f;
        centreY = height*0.5f;
        radius = (float) Math.sqrt(centreY*centreY+centreX*centreX);
    }

    public LGradualBGView(Context context) {
        this(context,null);
    }
    public LGradualBGView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public LGradualBGView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setDither(true);
        shadowPaint.setColor(Color.BLACK);
    }
}
