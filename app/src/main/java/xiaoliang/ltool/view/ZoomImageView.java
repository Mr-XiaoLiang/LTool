package xiaoliang.ltool.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
/**
 * 缩放ImageView
 * 网上实在找不到用起来舒服的，就自己写了，心好累
 * Created by liuj on 2016/10/5.
 */
public class ZoomImageView extends ImageView {
    private PointF startPoint1 = new PointF();
    private PointF startPoint2 = new PointF();
    private Matrix matrix = new Matrix();
    private PointF midPoint;//中心点
    private int touchSize = 0;
    private float startDis = 0;
    private Matrix currentMaritx = new Matrix();

    /**
     * 默认构造函数
     *
     * @param context
     */
    public ZoomImageView(Context context) {
        this(context,null);
    }

    /**
     * 该构造方法在静态引入XML文件中是必须的
     * @param context
     * @param paramAttributeSet
     */
    public ZoomImageView(Context context, AttributeSet paramAttributeSet) {
        super(context, paramAttributeSet);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                touchSize++;
                currentMaritx.set(this.getImageMatrix());//记录ImageView当期的移动位置
                startPoint1.set(event.getX(), event.getY());//开始点
                break;
            case MotionEvent.ACTION_MOVE://移动事件
                if (touchSize==1) {//图片拖动事件
                    float x = event.getX(0) - startPoint1.x;
                    float y = event.getY(0) - startPoint1.y;
                    matrix.set(currentMaritx);//在当前的位置基础上移动
                    matrix.postTranslate(x, y);
                } else if (touchSize>1) {//图片放大事件
                    float endDis = distance(event);//结束距离
                    if (endDis > 10f) {
                        float scale = endDis / startDis;//放大倍数
                        matrix.set(currentMaritx);
                        matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                touchSize = 0;
                break;
            //有手指离开屏幕，但屏幕还有触点(手指)
            case MotionEvent.ACTION_POINTER_UP:
                touchSize--;
                break;
            //当屏幕上已经有触点（手指）,再有一个手指压下屏幕
            case MotionEvent.ACTION_POINTER_DOWN:
                touchSize++;
                startDis = distance(event);
                if (startDis > 10f) {//避免手指上有两个茧
                    midPoint = mid(event);
                    currentMaritx.set(this.getImageMatrix());//记录当前的缩放倍数
                }
                break;
        }
        this.setImageMatrix(matrix);
        return true;
    }

    private PointF move(MotionEvent event){
        float x = event.getX(0) - startPoint1.x;
        float y = event.getY(0) - startPoint1.y;
        return new PointF(x,y);
    }

    /**
     * 两点之间的距离
     *
     * @param event
     * @return
     */
    private static float distance(MotionEvent event) {
        //两根线的距离
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * 计算两点之间中心点的距离
     *
     * @param event
     * @return
     */
    private static PointF mid(MotionEvent event) {
        float midx = event.getX(1) + event.getX(0);
        float midy = event.getY(1) - event.getY(0);

        return new PointF(midx / 2, midy / 2);
    }

    /**
     * 计算两条线之间的交点
     * @param s1 第一条线起点
     * @param s2 第二条线起点
     * @param e1 第一条线终点
     * @param e2 第二条线终点
     * @return
     */
    private static PointF intersection(PointF s1,PointF s2,PointF e1,PointF e2){
        return intersection(s1.x,s1.y,s2.x,s2.y,e1.x,e1.y,e2.x,e2.y);
    }
    private static PointF intersection(float sx1,float sy1,float sx2,float sy2,float ex1,float ey1,float ex2,float ey2){
        //得到函数值y = a1 * x + b1
        float a1 = (sy1-ey1)/(sx1-ex1);
        float b1 = sy1-a1*sx1;
        //得到函数值y = a2 * x + b2
        float a2 = (sy2-ey2)/(sx2-ex2);
        float b2 = sy2-a2*sx2;
        //求交点
        float x = (b2-b1)/(a1-a2);
        float y = a1*x+b1;
        return new PointF(x,y);
    }
}
