package xiaoliang.ltool.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;

import java.util.Collection;
import java.util.HashSet;

import xiaoliang.ltool.R;
import xiaoliang.ltool.qr.camera.CameraManager;

/**
 * Created by liuj on 2016/9/27.
 * 二维码识别中的扫描框
 */

public class QRFinderView extends View {

    /**
     * 刷新界面的时间
     */
    private static final long ANIMATION_DELAY = 10L;
    /**
     * 扫描框大小
     */
    private Rect frame;
    /**
     * View宽度
     */
    private int width;
    /**
     * View高度
     */
    private int height;
    /**
     * 扫描角边长度
     */
    private int angleLength;
    /**
     * 扫描角边宽度
     */
    private int angleWidth;
    /**
     * 步长
     */
    private float stepLength;
    /**
     * 步数
     */
    private int step = 0;
    /**
     * 扫描框头部
     */
    private int scanTop;
    /**
     * 扫描框左侧
     */
    private int scanLeft;
    /**
     * 扫描框宽度
     */
    private int scanWidth;
    /**
     * 扫描框高度
     */
    private int scanHeight;
    /**
     * 扫描方向
     */
    private boolean scanDirection = true;
    /**
     * 绘制画笔
     */
    private Paint paint;
    /**
     * 绘制的颜色
     */
    private int color;
    /**
     * 返回时的颜色
     */
    private int resultColor;
    /**
     * 绘制返回的Bitmap
     */
    private Bitmap resultBitmap;
    /**
     * 黑色画笔
     */
    private Paint blackPaint;
    /**
     * 外框
     */
    private Path scanBox;

    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        frame = CameraManager.get().getFramingRect();
        scanWidth = frame.width();
        scanHeight = frame.height();
        scanTop = frame.top;
        scanLeft = frame.left;
        angleLength = (int) (scanWidth*0.1);
        angleWidth = (int) (angleLength*0.1);
        stepLength = scanHeight*0.01f;
        color = getResources().getColor(R.color.colorPrimary);
        resultColor = Color.WHITE;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setShadowLayer(10,0,0,color);
        paint.setStrokeWidth(angleWidth);
        blackPaint = new Paint();
        blackPaint.setAntiAlias(true);
        blackPaint.setDither(true);
        blackPaint.setColor(Color.BLACK);
        blackPaint.setAlpha(128);
        scanBox = new Path();
        scanBox.moveTo(scanLeft,scanTop);
        scanBox.lineTo(scanLeft+angleLength,scanTop);
        scanBox.moveTo(scanLeft+scanWidth-angleLength,scanTop);
        scanBox.lineTo(scanLeft+scanWidth,scanTop);
        scanBox.lineTo(scanLeft+scanWidth,scanTop+angleLength);
        scanBox.moveTo(scanLeft+scanWidth,scanTop+scanHeight-angleLength);
        scanBox.lineTo(scanLeft+scanWidth,scanTop+scanHeight);
        scanBox.lineTo(scanLeft+scanWidth-angleLength,scanTop+scanHeight);
        scanBox.moveTo(scanLeft+angleLength,scanTop+scanHeight);
        scanBox.lineTo(scanLeft,scanTop+scanHeight);
        scanBox.lineTo(scanLeft,scanTop+scanHeight-angleLength);
        scanBox.moveTo(scanLeft,scanTop-angleLength);
        scanBox.close();
        LinearGradient linearGradient = new LinearGradient(
                scanLeft,scanTop,scanLeft+scanWidth,scanTop,
                new int[]{Color.TRANSPARENT,color,Color.TRANSPARENT},
                null, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        possibleResultPoints = new HashSet<ResultPoint>(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(resultBitmap==null?color:resultColor);
        canvas.drawRect(0,0,width,scanTop,blackPaint);//上
        canvas.drawRect(0,scanTop,scanLeft,scanTop+scanHeight,blackPaint);//左
        canvas.drawRect(scanLeft+scanWidth,scanTop,width,scanTop+scanHeight,blackPaint);//右
        canvas.drawRect(0,scanTop+scanHeight,width,height,blackPaint);//下
        if(resultBitmap!=null){
            canvas.drawBitmap(resultBitmap,scanLeft,scanTop,paint);
        }else{
            canvas.drawPath(scanBox,paint);//画框
            canvas.drawRect(scanLeft+10,scanTop+stepLength*step,scanLeft+scanWidth-10,scanTop+stepLength*step+scanWidth,paint);//画扫描线

            Collection<ResultPoint> currentPossible = possibleResultPoints;
            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new HashSet<>(5);
                lastPossibleResultPoints = currentPossible;
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(scanLeft + point.getX(), scanTop
                            + point.getY(), 6.0f, paint);
                }
            }
            if (currentLast != null) {
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top
                            + point.getY(), 3.0f, paint);
                }
            }
            if(scanDirection){
                step++;
            }else{
                step--;
            }
            if(step==100||step==0){
                scanDirection = !scanDirection;
            }
        }
    }

    public QRFinderView(Context context) {
        this(context,null);
    }
    public QRFinderView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public QRFinderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
