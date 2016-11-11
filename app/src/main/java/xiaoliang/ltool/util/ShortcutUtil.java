package xiaoliang.ltool.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.Log;

/**
 * Created by LiuJ on 2016/3/4.
 * 创建快捷方式的工具类
 */
public class ShortcutUtil {
    public static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    public static final String ACTION_REMOVE_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";
    private Context context;
    private int redPointHeight = 0;
    private int redPointTextSize = 0;
    private Paint paint;
    private int width = 0;
    private Matrix matrix;
    private boolean repeat = false;
    private boolean circular = true;
    private boolean redPoint = false;

    public ShortcutUtil(Context context) {
        this.context = context;
        // 创建画笔
        paint = new Paint();
        resetPaint();
        width = DensityUtil.dip2px(this.context,56);
    }

    /**
     * 添加一个带有消息数量的快捷方式
     * @param name 快捷方式名
     * @param size 数量
     * @param bitmap 图标
     * @param cls 点击跳转目标
     */
    public void addShortcut(String name, int size, int bitmap,Class cls){
        addShortcut(name,size,bitmap,cls);
    }



    /**
     * 创建一个带有文字提示的快捷方式
     * @param name 图标名
     * @param size 小红点内容
     * @param bitmap 图片地址
     * @param cls 跳转目标
     */
    public void addShortcut(String name, String size, int bitmap,Class cls){
        // 创建一张空白图片
        Bitmap bit = BitmapFactory.decodeResource(context.getResources(), bitmap);//直接拿出来的是不可更改的位图
        addShortcut(name,getShortcutBmp(size,bit),cls);
    }

    /**
     * 创建一个快捷方式
     * @param name
     * @param size
     * @param bitmap
     * @param cls
     */
    public void addShortcut(String name, int size, Bitmap bitmap,Class cls){
        // 创建一张空白图片
        addShortcut(name,getShortcutBmp(size,bitmap),cls);
    }

    /**
     * 创建一个快捷方式
     * @param name
     * @param size
     * @param bitmap
     * @param intent
     */
    public void addShortcut(String name, int size, Bitmap bitmap,Intent intent){
        // 创建一张空白图片
        addShortcut(name,getShortcutBmp(size,bitmap),intent);
    }

    /**
     * 获取一个桌面快捷方式的图片
     * @param size
     * @param bitmap
     * @return
     */
    public Bitmap getShortcutBmp(int size,Bitmap bitmap){
        String s;
        if(size<0)
            s = "";
        else if(size>99)
            s="99+";
        else
            s=size+"";
        return getShortcutBmp(s,bitmap);
    }

    public Bitmap getShortcutBmp(String size, Bitmap bitmap){
        // 创建一张空白图片
        Bitmap b = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        redPointHeight = (int) (b.getHeight()*0.35);
        redPointTextSize = (int) (redPointHeight*0.7);
        paint.setTextSize(redPointTextSize);
        // 创建一张画布
        Canvas canvas = new Canvas(b);
        getCroppedRoundBitmap(bitmap, width / 2);
        if(circular){
            canvas.drawCircle(width/2,width/2,width/2,paint);
        }else{
            canvas.drawRect(0, 0, width, width, paint);
        }
        if(redPoint&&size.length()>0){
            //画红色小点
            resetPaint();
            paint.setColor(Color.RED);
            int left;
            if(size.length()>1)
                left = width - redPointHeight - (size.length()-1) * redPointTextSize/2;
            else
                left = width - redPointHeight;
            if(left<0){
                left = 0;
            }
            //本函数要求版本教高，所以换一个绘制方式
//            canvas.drawRoundRect(left ,
//                    0, width, redPointHeight, redPointHeight / 2, redPointHeight / 2, paint);
            canvas.drawCircle(left+redPointHeight/2,redPointHeight/2,redPointHeight/2,paint);
            canvas.drawCircle(width-redPointHeight/2,redPointHeight/2,redPointHeight/2,paint);
            canvas.drawRect(left+redPointHeight/2,0,width-redPointHeight/2,redPointHeight,paint);
            //画字
            paint.setColor(Color.WHITE);
            Paint.FontMetrics fm = paint.getFontMetrics();
            float textY = redPointHeight/2-fm.descent + (fm.descent - fm.ascent) / 2;
            int textX = width-(width - left)/2;
            if(size.length()*redPointTextSize/2>width-redPointHeight)
                size = size.substring(0,(width-redPointHeight)/redPointTextSize);
            canvas.drawText(size,textX,textY,paint);
        }
        return b;
    }

    /**
     * 生成一个指定图片的快捷方式
     * @param name 名称
     * @param bitmap 图片
     * @param cls 跳转目标
     */
    public void addShortcut(String name, Bitmap bitmap,Class cls) {
        Intent startIntent = new Intent(context, cls);
        addShortcut(name,bitmap,startIntent);
    }

    public void addShortcut(String name, Bitmap bitmap,Intent startIntent) {
        Intent addShortCutIntent = new Intent(ACTION_ADD_SHORTCUT);

        // 不允许重复创建
        addShortCutIntent.putExtra("duplicate", repeat);// 经测试不是根据快捷方式的名字判断重复的
        // 应该是根据快链的Intent来判断是否重复的,即Intent.EXTRA_SHORTCUT_INTENT字段的value
        // 但是名称不同时，虽然有的手机系统会显示Toast提示重复，仍然会建立快链
        // 屏幕上没有空间时会提示
        // 注意：重复创建的行为MIUI和三星手机上不太一样，小米上似乎不能重复创建快捷方式
        //用于点击快捷方式要启动的程序，这里就启动本程序了
        //快捷方式的名称
        addShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        //快捷方式的图标
        addShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);
//        addShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        //将快捷方式与要启动的程序关联起来
        addShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, startIntent);
        // 发送广播
        context.sendBroadcast(addShortCutIntent);
        Log.d("addShortcut","添加桌面快捷方式");
    }

    public void deleteShortcut(String name,Class cls) {
        // remove shortcut的方法在小米系统上不管用，在三星上可以移除
        Intent intent = new Intent(ACTION_REMOVE_SHORTCUT);
        // 名字
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        //用于点击快捷方式要启动的程序，这里就启动本程序了
        Intent startIntent = new Intent(context, cls);
        //将快捷方式与要启动的程序关联起来
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, startIntent);
        // 发送广播
        context.sendBroadcast(intent);
    }
    /**
     * 使用渲染来绘制圆形图片
     *
     * @param bmp  图片
     * @param radius 半径
     * @return
     */
    public void getCroppedRoundBitmap(Bitmap bmp, int radius) {
        // 将bmp作为着色器，就是在指定区域内绘制bmp
        Shader mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale;
        // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
        scale = Math.max(radius * 2.0f / bmp.getWidth(), radius * 2.0f / bmp.getHeight());
        if(matrix==null){
            matrix = new Matrix();
        }
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        matrix.setScale(scale, scale);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(matrix);
        // 设置shader
        paint.setShader(mBitmapShader);
    }

    private void resetPaint(){
        paint.reset();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setTextSize(redPointTextSize);
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public boolean isCircular() {
        return circular;
    }

    public void setCircular(boolean circular) {
        this.circular = circular;
    }

    public boolean isRedPoint() {
        return redPoint;
    }

    public void setRedPoint(boolean redPoint) {
        this.redPoint = redPoint;
    }
}
