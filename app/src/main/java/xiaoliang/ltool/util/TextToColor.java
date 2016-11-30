package xiaoliang.ltool.util;

import android.graphics.Color;

import java.security.MessageDigest;

/**
 * Created by LiuJ on 2016/6/15.
 * 自定义的文字转颜色算法
 */

public class TextToColor {

    public static int format(String text,boolean alphaLimit){
        try{
            String md5 = MD5(text);
            int red = (int) (Long.parseLong(md5.substring(0,8),16)%256);
            int green = (int) (Long.parseLong(md5.substring(8,16),16)%256);
            int blue = (int) (Long.parseLong(md5.substring(16,24),16)%256);
            int alpha = (int) (Long.parseLong(md5.substring(24,31),16)%256);
            if(alphaLimit){
                alpha = alpha%128+128;
            }
            return Color.argb(alpha,red,green,blue);
        }catch (Exception e){
            return Color.WHITE;
        }
    }
    //简易的，默认启动透明度限制
    public static int format(String text){
        return format(text,true);
    }

    public static String MD5(String text){
        return bytesToMD5(text.getBytes());
    }
    //把字节数组转成16进位制数
    private static String bytesToHex(byte[] bytes) {
        StringBuffer md5str = new StringBuffer();
        //把数组每一字节换成16进制连成md5字符串
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];
            if(digital < 0) {
                digital += 256;
            }
            if(digital < 16){
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString();
    }

    //把字节数组转换成md5
    private static String bytesToMD5(byte[] input) {
        String md5str = null;
        try {
            //创建一个提供信息摘要算法的对象，初始化为md5算法对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            //计算后获得字节数组
            byte[] buff = md.digest(input);
            //把数组每一字节换成16进制连成md5字符串
            md5str = bytesToHex(buff);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5str;
    }

}
