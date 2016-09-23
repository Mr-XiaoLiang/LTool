package xiaoliang.ltool.util;

import android.graphics.Bitmap;

/**
 * Created by liuj on 2016/9/23.
 * 二维码任务列表
 */

public class QRTask {
    /**
     * 获取一个环形渐变的二维码
     * @param str 内容
     * @param width 宽度
     * @param colors 颜色
     * @param isBg 渲染类型
     * @param otherColor 补色
     * @param logo logo
     * @param callBack 回调
     */
    public static void getRadialGradientQRImg(String str, int width, int[] colors, boolean isBg, int otherColor, Bitmap logo, QRCreateRunnable.QRCallBack callBack){
        HttpUtil.getThread(new QRCreateRunnable(str,width,colors,isBg,otherColor,logo,callBack));
    }

    /**
     * 获取一个扇形渲染的二维码
     * @param str 内容
     * @param width 宽度
     * @param r 旋转角度
     * @param colors 颜色
     * @param isBg 渲染类型
     * @param otherColor 补色
     * @param logo logo
     * @param callBack 回调
     */
    public static void getSweepGradientQRImg(String str, int width, int r, int[] colors, boolean isBg, int otherColor, Bitmap logo, QRCreateRunnable.QRCallBack callBack){
        HttpUtil.getThread(new QRCreateRunnable(str,width,r,colors,isBg,otherColor,QRCreateRunnable.TYPE_SWEEP,logo,callBack));
    }

    /**
     * 获取一个线性渲染的二维码
     * @param str 内容
     * @param width 宽度
     * @param r 旋转角度
     * @param colors 颜色
     * @param isBg 渲染类型
     * @param otherColor 补色
     * @param logo logo
     * @param callBack 回调
     */
    public static void getLinearGradientQRImg(String str, int width, int r, int[] colors, boolean isBg, int otherColor, Bitmap logo, QRCreateRunnable.QRCallBack callBack){
        HttpUtil.getThread(new QRCreateRunnable(str,width,r,colors,isBg,otherColor,QRCreateRunnable.TYPE_LINEAR,logo,callBack));
    }

    /**
     * 获取一个简易的二维码
     * @param str 内容
     * @param width 宽度
     * @param logo logo
     * @param callBack 回调
     */
    public static void getQuickQRImage(String str, int width, Bitmap logo, QRCreateRunnable.QRCallBack callBack){
        HttpUtil.getThread(new QRCreateRunnable(str,width,logo,callBack));
    }

    /**
     *
     * @param str 内容
     * @param width 宽度
     * @param r 旋转角度
     * @param bitmap 渲染资料
     * @param isBg 渲染类型
     * @param otherColor 补色
     * @param logo logo
     * @param callBack 回调
     */
    public static void getBitmapShaderQRImg(String str,int width,int r,Bitmap bitmap,boolean isBg,int otherColor,Bitmap logo, QRCreateRunnable.QRCallBack callBack){
        HttpUtil.getThread(new QRCreateRunnable(str,width,r,bitmap,isBg,otherColor,logo,callBack));
    }

}
