package xiaoliang.ltool.util;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuj on 2016/9/22.
 * 二维码工具
 */

public class QRUtil {

//    private static final int TRANSPARENT = Color.TRANSPARENT;
    private static final int WHITE = Color.WHITE;
    private static final int BLACK = Color.BLACK;

    /**
     * 快速获取一个基本的二维码
     * @param str
     * @param width
     * @return
     */
    public static Bitmap getQuickQRImage(String str,int width){
        return createQRImage(getQRPixels(str,width),width);
    }

    public static Bitmap getQuickQRImage(String str,int width,Bitmap logo){
        return addLogo(getQuickQRImage(str,width),logo);
    }

    /**
     * 获取一个线性渲染的bitmap
     * @param str 文字
     * @param width 宽度
     * @param r 旋转角
     * @param colors 颜色
     * @param isBg 是否渲染背景
     * @param otherColor 其他色，当isBg=true的时候，渲染背景，本颜色作为前景色。
     *                   当isBg=false时，渲染前景，本颜色作为背景色。
     * @return 返回二维码图片
     */
    public static Bitmap getLinearGradientQRImg(String str,int width,float r,int[] colors,boolean isBg,int otherColor){
        LinearGradient linearGradient = new LinearGradient(0,0,width,width,colors,null, Shader.TileMode.CLAMP);
        return getShaderQRImg(str,width,linearGradient,r+45,isBg,otherColor);
    }

    public static Bitmap getLinearGradientQRImg(String str,int width,float r,int[] colors,boolean isBg,int otherColor,Bitmap logo){
        return addLogo(getLinearGradientQRImg(str,width,r,colors,isBg,otherColor),logo);
    }

    /**
     * 获取一个环形渲染的二维码
     * @param str 内容
     * @param width 宽度
     * @param colors 颜色
     * @param isBg 是否背景渲染
     * @param otherColor 其他补色
     * @return 图片
     */
    public static Bitmap getRadialGradientQRImg(String str,int width,int[] colors,boolean isBg,int otherColor){
        RadialGradient radialGradient = new RadialGradient(width/2,width/2,(float) Math.sqrt((width/2)*(width/2)*2),colors,null, Shader.TileMode.CLAMP);
        return getShaderQRImg(str,width,radialGradient,0,isBg,otherColor);
    }

    public static Bitmap getRadialGradientQRImg(String str,int width,int[] colors,boolean isBg,int otherColor,Bitmap logo){
        return addLogo(getRadialGradientQRImg(str,width,colors,isBg,otherColor),logo);
    }

    /**
     * 获取一个梯度渲染的二维码
     * @param str 内容
     * @param width 宽度
     * @param r 旋转角
     * @param colors 颜色
     * @param isBg 是否背景渲染
     * @param otherColor 其他补色
     * @return 图片
     */
    public static Bitmap getSweepGradientQRImg(String str,int width,int r,int[] colors,boolean isBg,int otherColor){
        SweepGradient sweepGradient = new SweepGradient(width/2,width/2,colors,null);
        return getShaderQRImg(str,width,sweepGradient,r,isBg,otherColor);
    }

    public static Bitmap getSweepGradientQRImg(String str,int width,int r,int[] colors,boolean isBg,int otherColor,Bitmap logo){
        return addLogo(getSweepGradientQRImg(str,width,r,colors,isBg,otherColor),logo);
    }

    /**
     * 获取一个图片渲染的二维码图
     * @param str 内容
     * @param width 宽度
     * @param r 旋转角度
     * @param bitmap 图片
     * @param isBg 是否渲染为背景
     * @param otherColor 补色
     * @return 图片
     */
    public static Bitmap getBitmapShaderQRImg(String str,int width,int r,Bitmap bitmap,boolean isBg,int otherColor){
        // 将bmp作为着色器，就是在指定区域内绘制bmp
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//        float radius = (float) Math.sqrt(width*width*2);
        float radius = width;
        // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
//        float scale = Math.max(radius / bitmap.getWidth(), radius / bitmap.getHeight());
        float scale = radius / bitmap.getWidth();
        Matrix matrix = new Matrix();
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        matrix.setScale(scale, scale);
        // 设置变换矩阵
        bitmapShader.setLocalMatrix(matrix);
        return getShaderQRImg(str,width,bitmapShader,r,isBg,otherColor);
    }

    public static Bitmap getBitmapShaderQRImg(String str,int width,int r,Bitmap bitmap,boolean isBg,int otherColor,Bitmap logo){
        return addLogo(getBitmapShaderQRImg(str,width,r,bitmap,isBg,otherColor),logo);
    }

    /**
     * 生成一个渲染的二维码图片
     * @param str 内容
     * @param width 宽度
     * @param shader 渲染
     * @param r 旋转角度
     * @param isBg 是否背景渲染
     * @param otherColor 其他补色
     * @return 图片
     */
    public static Bitmap getShaderQRImg(String str,int width,Shader shader,float r,boolean isBg,int otherColor){
        int[] pixels = getQRPixels(str,width);
        Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setShader(shader);
        canvas.save();
        canvas.rotate(r,width/2,width/2);
        canvas.drawCircle(width/2,width/2, (float) Math.sqrt((width/2)*(width/2)*2),paint);
        canvas.restore();
        int changeColor = BLACK;
        if(isBg)
            changeColor = WHITE;
        changeQRColor(pixels,width,bitmap,changeColor,otherColor);
        return createQRImage(pixels,width);

    }

    /**
     * 修改二维码颜色
     * @param pixels 二维码颜色数组
     * @param width 图片宽度
     * @param bitmap 渲染的模板图片
     * @param changeColor 修改的颜色
     * @param otherColor 替补颜色
     */
    public static void changeQRColor(int[] pixels,int width,Bitmap bitmap,int changeColor,int otherColor){
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < width; x++) {
                if(pixels[y * width + x] == changeColor){
                    pixels[y * width + x] = bitmap.getPixel(x,y);
                }else{
                    pixels[y * width + x] = otherColor;
                }
            }
        }
    }


    /**
     * 生成二维码Bitmap
     *
     * @param pixels   内容
     * @param widthPix  图片宽度
     * @return 生成二维码及保存文件是否成功
     */
    public static Bitmap createQRImage(int[] pixels, int widthPix) {
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(widthPix, widthPix, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, widthPix);
            return bitmap;
    }

    /**
     * 获取二维码数据
     * @param content 内容
     * @param widthPix 图片尺寸
     * @return
     */
    public static int[] getQRPixels(String content, int widthPix){
        try {
            if (content == null || "".equals(content)) {
                return null;
            }
            // 用于设置QR二维码参数
            Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 设置QR二维码的纠错级别（H为最高级别）具体级别信息
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度
//            hints.put(EncodeHintType.MARGIN, 2); //default is 4
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, widthPix, hints);
            int[] pixels = new int[widthPix * widthPix];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < widthPix; y++) {
                for (int x = 0; x < widthPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = BLACK;
                    } else {
                        pixels[y * widthPix + x] = WHITE;
                    }
                }
            }
            return pixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 在二维码中间添加Logo图案
     */
    public static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }
        if (logo == null) {
            return src;
        }
        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }
        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }
        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
//        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(src);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            src = null;
            e.getStackTrace();
        }
        return src;
    }
}
