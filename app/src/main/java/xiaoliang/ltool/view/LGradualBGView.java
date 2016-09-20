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
    private static final int color_1 = Color.parseColor("#cceeff");
    private static final int color_2 = Color.parseColor("#00eedd");
    private static final int color_3 = Color.parseColor("#ff88bb");
    private static final int color_4 = Color.parseColor("#ff2233");
    private static final float limit_1 = 0;
    private static final float limit_2 = 15;
    private static final float limit_3 = 28;
    private static final float limit_4 = 35;

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
        if(t<limit_1){
            newCentreColor = Color.WHITE;
            newEdgeColor = color_1;
        }else if(t<limit_2){
            if((t-limit_1)/(limit_2-limit_1)>0.5){
                newEdgeColor = color_1;
            }else{
                newEdgeColor = color_2;
            }
            newCentreColor = getCentreColor(color_1,color_2,(t-limit_1)/(limit_2-limit_1));
        }else if(t<limit_3){
            if((t-limit_2)/(limit_3-limit_2)>0.5){
                newEdgeColor = color_3;
            }else{
                newEdgeColor = color_2;
            }
            newCentreColor = getCentreColor(color_2,color_3,(t-limit_2)/(limit_3-limit_2));
        }else if(t<limit_4){
            if((t-limit_3)/(limit_4-limit_3)>0.5){
                newEdgeColor = color_4;
            }else{
                newEdgeColor = color_3;
            }
            newCentreColor = getCentreColor(color_3,color_4,(t-limit_3)/(limit_4-limit_3));
        }else{
            newCentreColor = Color.RED;
            newEdgeColor = color_4;
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
