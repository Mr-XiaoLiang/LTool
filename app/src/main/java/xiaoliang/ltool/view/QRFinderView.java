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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

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
     * 扫描框头部
     */
    private int scanRight;
    /**
     * 扫描框左侧
     */
    private int scanBottom;
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
     * 绘制返回的Bitmap
     */
    private Bitmap resultBitmap;
    /**
     * 黑色画笔
     */
    private Paint blackPaint;
    /**
     * 黑色画笔
     */
    private Paint boxPaint;
    /**
     * 透明色
     */
    private int TRANSPARENT = Color.argb(0,255,255,255);

    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;

    private void init(){
        if(CameraManager.get()==null)
            return;
        frame = CameraManager.get().getFramingRectInPreview();
        createPaint();
        if(frame==null){
            initFrame();
        }
        scanWidth = frame.width();
        scanHeight = frame.height();
        scanTop = frame.top;
        scanLeft = frame.left;
        scanRight = frame.right;
        scanBottom = frame.bottom;
        angleLength = (int) (scanWidth*0.1);
        angleWidth = (int) (angleLength*0.1);
        stepLength = scanHeight*0.01f;
        paint.setStrokeWidth(angleWidth);
        LinearGradient linearGradient = new LinearGradient(
                scanLeft,scanTop,scanRight,scanTop,
                new int[]{TRANSPARENT,color,TRANSPARENT},
                null, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
    }

    private void createPaint(){
        width = getWidth();
        height = getHeight();
        color = getResources().getColor(R.color.colorPrimary);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setShadowLayer(10,0,0,color);
        blackPaint = new Paint();
        blackPaint.setAntiAlias(true);
        blackPaint.setDither(true);
        blackPaint.setColor(Color.BLACK);
        blackPaint.setAlpha(128);
        boxPaint = new Paint();
        boxPaint.setAntiAlias(true);
        boxPaint.setDither(true);
        boxPaint.setColor(color);
        possibleResultPoints = new HashSet<>(5);
    }

    private void initFrame(){
        int w = width * 3 / 5;
        if (w < CameraManager.MIN_FRAME_WIDTH) {
            w = CameraManager.MIN_FRAME_WIDTH;
        } else if (w > CameraManager.MAX_FRAME_WIDTH) {
            w = CameraManager.MAX_FRAME_WIDTH;
        }
        int h = height * 3 / 5;
        if (h < CameraManager.MIN_FRAME_HEIGHT) {
            h = CameraManager.MIN_FRAME_HEIGHT;
        } else if (h > CameraManager.MAX_FRAME_HEIGHT) {
            h = CameraManager.MAX_FRAME_HEIGHT;
        }
        int leftOffset = (width - w) / 2;
        int topOffset = (height - h) / 2;
        frame = new Rect(leftOffset, topOffset, leftOffset + w, topOffset + h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(frame==null){
            init();
        }
        canvas.drawRect(0,0,width,scanTop,blackPaint);//上
        canvas.drawRect(0,scanTop,scanLeft,scanBottom,blackPaint);//左
        canvas.drawRect(scanRight,scanTop,width,scanBottom,blackPaint);//右
        canvas.drawRect(0,scanBottom,width,height,blackPaint);//下
        if(resultBitmap!=null){
            canvas.drawBitmap(resultBitmap,scanLeft,scanTop,paint);
        }else{
            canvas.drawRect(scanLeft,scanTop+stepLength*step,scanRight,scanTop+stepLength*step+angleWidth,paint);//画扫描线

            canvas.drawRect(scanLeft,scanTop,scanLeft+angleLength,scanTop+angleWidth,boxPaint);
            canvas.drawRect(scanLeft,scanTop,scanLeft+angleWidth,scanTop+angleLength,boxPaint);
            canvas.drawRect(scanRight-angleLength,scanTop,scanRight,scanTop+angleWidth,boxPaint);
            canvas.drawRect(scanRight-angleWidth,scanTop,scanRight,scanTop+angleLength,boxPaint);
            canvas.drawRect(scanLeft,scanBottom-angleWidth,scanLeft+angleLength,scanBottom,boxPaint);
            canvas.drawRect(scanLeft,scanBottom-angleLength,scanLeft+angleWidth,scanBottom,boxPaint);
            canvas.drawRect(scanRight-angleWidth,scanBottom-angleLength,scanRight,scanBottom,boxPaint);
            canvas.drawRect(scanRight-angleLength,scanBottom-angleWidth,scanRight,scanBottom,boxPaint);

            Collection<ResultPoint> currentPossible = possibleResultPoints;
            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new HashSet<>(5);
                lastPossibleResultPoints = currentPossible;
                for (ResultPoint point : currentPossible) {
                        canvas.drawCircle(scanLeft + point.getX(), scanTop
                                + point.getY(), 6.0f, boxPaint);
                }
            }
            if (currentLast != null) {
                for (ResultPoint point : currentLast) {
                        canvas.drawCircle(scanLeft + point.getX(), scanTop
                                + point.getY(), 3.0f, boxPaint);
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
            //只刷新扫描框的内容，其他地方不刷新
            postInvalidateDelayed(ANIMATION_DELAY, scanLeft, scanTop,
                    scanRight, scanBottom);
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

    public void drawViewfinder() {
        resultBitmap = null;
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live
     * scanning display.
     *
     * @param barcode
     *            An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void setFrame(Rect frame) {
        this.frame = frame;
        init();
    }

    public void setSize(int w,int h) {
        width = w;
        height = h;
        initFrame();
        init();
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
    }

}
