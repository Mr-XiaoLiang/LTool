package xiaoliang.ltool.util;

import android.graphics.Bitmap;

/**
 * Created by liuj on 2016/9/23.
 * 一个异步生成二维码的任务
 */

public class QRCreateRunnable implements Runnable {

    private String str;
    private int width;
    private int r;
    private int[] colors;
    private boolean isBg;
    private int otherColor;
    private Bitmap bitmap;
    private int type;
    private Bitmap logo;
    private QRCallBack callBack;
    public static final int TYPE_LINEAR = 0;
    public static final int TYPE_RADIAL = 1;
    public static final int TYPE_SWEEP = 2;
    public static final int TYPE_BITMAP = 3;
    public static final int TYPE_QUICK = 4;


    public QRCreateRunnable(String str,int width,QRCallBack callBack){
        this.str = str;
        this.width = width;
        this.callBack = callBack;
        this.type = TYPE_QUICK;
    }
    public QRCreateRunnable(String str,int width,int r,int[] colors,boolean isBg,int otherColor,int what,QRCallBack callBack){
        this.str = str;
        this.width = width;
        this.callBack = callBack;
        this.r = r;
        this.colors = colors;
        this.isBg = isBg;
        this.otherColor = otherColor;
        this.type = what;
    }
    public QRCreateRunnable(String str,int width,int[] colors,boolean isBg,int otherColor,QRCallBack callBack){
        this.str = str;
        this.width = width;
        this.callBack = callBack;
        this.colors = colors;
        this.isBg = isBg;
        this.otherColor = otherColor;
        this.type = TYPE_RADIAL;
    }
    public QRCreateRunnable(String str,int width,int r,Bitmap bitmap,boolean isBg,int otherColor,QRCallBack callBack){
        this.str = str;
        this.width = width;
        this.callBack = callBack;
        this.r = r;
        this.isBg = isBg;
        this.otherColor = otherColor;
        this.bitmap = bitmap;
        this.type = TYPE_BITMAP;
    }

    public QRCreateRunnable(String str,int width,Bitmap logo,QRCallBack callBack){
        this(str,width,callBack);
        this.logo = logo;
    }
    public QRCreateRunnable(String str,int width,int r,int[] colors,boolean isBg,int otherColor,int what,Bitmap logo,QRCallBack callBack){
        this(str,width,r,colors,isBg,otherColor,what,callBack);
        this.logo = logo;
    }
    public QRCreateRunnable(String str,int width,int[] colors,boolean isBg,int otherColor,Bitmap logo,QRCallBack callBack){
        this(str,width,colors,isBg,otherColor,callBack);
        this.logo = logo;
    }
    public QRCreateRunnable(String str,int width,int r,Bitmap bitmap,boolean isBg,int otherColor,Bitmap logo,QRCallBack callBack){
        this(str,width,r,bitmap,isBg,otherColor,callBack);
        this.logo = logo;
    }

    @Override
    public void run() {
        if(callBack==null)
            return;
        switch (type){
            case TYPE_BITMAP:
                callBack.obtainQR(QRUtil.getBitmapShaderQRImg(str,width,r,bitmap,isBg,otherColor,logo));
                break;
            case TYPE_SWEEP:
                callBack.obtainQR(QRUtil.getSweepGradientQRImg(str,width,r,colors,isBg,otherColor,logo));
                break;
            case TYPE_RADIAL:
                callBack.obtainQR(QRUtil.getRadialGradientQRImg(str,width,colors,isBg,otherColor,logo));
                break;
            case TYPE_QUICK:
                callBack.obtainQR(QRUtil.getQuickQRImage(str,width,logo));
                break;
            case TYPE_LINEAR:
                callBack.obtainQR(QRUtil.getLinearGradientQRImg(str,width,r,colors,isBg,otherColor,logo));
                break;
        }
    }

    public interface QRCallBack{
        void obtainQR(Bitmap bitmap);
    }

}
