package xiaoliang.ltool.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import xiaoliang.ltool.constant.Constant;

public class OtherUtil {
	/**
	 * 跳转到qq
	 * @param context 上下文
	 * @param qqNum qq号码
	 * @return 是否成功
	 */
	public static boolean toTheQQ(Context context,String qqNum){
		if(null==qqNum||qqNum.length()<5){
			return false;
		}
		String url11 = "mqqwpa://im/chat?chat_type=wpa&uin="+qqNum+"&version=1";
		context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url11)));
		return true;
	}

	/**
	 * 格式化时间
	 * @param millisecond 毫秒数
	 * @return 格式：HH:mm:ss:ms
	 */
	public static String formatTimeByMilliSecond(long millisecond){
		int ms2hour = 3600000;
		int ms2minutes = 60000;
		int ms2second = 1000;
		long hour = millisecond/ms2hour;
		int minutes = (int) (millisecond%ms2hour/ms2minutes);
		int second = (int) (millisecond%ms2hour%ms2minutes/ms2second);
		int millisecond2 = (int) (millisecond%ms2hour%ms2minutes%ms2second);
		String hh = hour>9?""+hour:"0"+hour;
		String mm = minutes>9?""+minutes:"0"+minutes;
		String ss = second>9?""+second:"0"+second;
		String ms = millisecond2>9?""+millisecond2:"0"+millisecond2;
		ms = ms.length()>2?""+millisecond2:"0"+ms;
		return hh+":"+mm+":"+ss+":"+ms;
	}

	/**
	 * 格式化事件
	 * @param second 秒数
	 * @return 格式：HH:mm:ss
	 */
	public static String formatTimeBySecond(long second){
		int s2hour = 3600;
		int s2minutes = 60;
		long hour = second/s2hour;
		int minutes = (int) (second%s2hour/s2minutes);
		int second2 = (int) (second%s2hour%s2minutes);
		String hh = hour>9?""+hour:"0"+hour;
		String mm = minutes>9?""+minutes:"0"+minutes;
		String ss = second2>9?""+second2:"0"+second2;
		return hh+":"+mm+":"+ss;
	}

	/** 保存方法 */
	public static void saveBitmap(Bitmap bm,String picName,String path) {
		File pathFile = new File(path);
		File f = new File(path, picName);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getAppImgPath(Context context){
		return context.getFilesDir().getAbsolutePath()+"/img";
	}

	public static String getSDImgPath(){
		return Environment.getExternalStorageDirectory()+"/LTool/img";
	}
	public static String getSDAppPath(){
		return Environment.getExternalStorageDirectory()+"/LTool/app";
	}

	/**
	 * 保存到SD卡
	 * @param bm
	 * @param picName
     */
	public static void saveBitmapToSD(Bitmap bm,String picName){
			saveBitmap(bm,picName,getSDImgPath());
	}

	/**
	 * 保存到APP
	 * @param context
	 * @param bm
	 * @param picName
     */
	public static void saveBitmapToApp(Context context,Bitmap bm,String picName){
		saveBitmap(bm,picName,getAppImgPath(context));
	}

	/**
	 * 保存背景图
	 * @param context
	 * @param bm
     */
	public static void saveBabkground(Context context,Bitmap bm){
		saveBitmap(bm,"background.png",context.getFilesDir().getAbsolutePath()+"/bg");
//		saveBitmapToSD(bm,"background.png");
	}

	/**
	 * 获取网络的类型
	 * @param context
	 * @return
     */
	public static int getNetworkType(Context context){
		ConnectivityManager connectMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
		if(networkInfo == null||!networkInfo.isConnected()){
			return Constant.NetWord_NULL;
		}else if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
			return Constant.NetWord_WIFI;
		}else if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
			return Constant.NetWord_MOBILE;
		}else{
			return Constant.NetWord_Other;
		}
	}

	/**
	 * 获取网络类型
	 * @param context
	 * @return
     */
	public static String getNetworkTypeStr(Context context){
		String strNetworkType = "";
		ConnectivityManager connectMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()){
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
				strNetworkType = "WIFI";
			}else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
				String _strSubTypeName = networkInfo.getSubtypeName();
				Log.e("LTool", "Network getSubtypeName : " + _strSubTypeName);
				// TD-SCDMA   networkType is 17
				int networkType = networkInfo.getSubtype();
				switch (networkType) {
					case TelephonyManager.NETWORK_TYPE_GPRS:
					case TelephonyManager.NETWORK_TYPE_EDGE:
					case TelephonyManager.NETWORK_TYPE_CDMA:
					case TelephonyManager.NETWORK_TYPE_1xRTT:
					case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
						strNetworkType = "2G";
						break;
					case TelephonyManager.NETWORK_TYPE_UMTS:
					case TelephonyManager.NETWORK_TYPE_EVDO_0:
					case TelephonyManager.NETWORK_TYPE_EVDO_A:
					case TelephonyManager.NETWORK_TYPE_HSDPA:
					case TelephonyManager.NETWORK_TYPE_HSUPA:
					case TelephonyManager.NETWORK_TYPE_HSPA:
					case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
					case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
					case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
						strNetworkType = "3G";
						break;
					case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
						strNetworkType = "4G";
						break;
					default:
						// http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
						if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")){
							strNetworkType = "3G";
						}else{
							strNetworkType = _strSubTypeName;
						}
						break;
				}
				Log.e("LTool", "Network getSubtype : " + Integer.valueOf(networkType).toString());
			}
		}
		Log.e("LTool", "Network Type : " + strNetworkType);
		return strNetworkType;
	}

	public static void seveBitmapOnBackground(String path, String name, Bitmap bitmap,SaveBitmapRunnable.SaveBitmapCallBack callBack){
		HttpUtil.getThread(new SaveBitmapRunnable(path,name,bitmap,callBack));
	}

	public static void seveBitmapToSDOnBackground(String name, Bitmap bitmap,SaveBitmapRunnable.SaveBitmapCallBack callBack){
		seveBitmapOnBackground(getSDImgPath(),name,bitmap,callBack);
	}

	/**
	 * 输出文本文件
	 * @param value
	 * @param name
     */
	public static void outputTextFile(String value,String name){
		try{
			FileOutputStream outStream = new FileOutputStream(OtherUtil.getSDImgPath()+"/"+name+".txt",true);
			OutputStreamWriter writer = new OutputStreamWriter(outStream,"gbk");
			writer.write(value);
			writer.write("/n");
			writer.flush();
			writer.close();//记得关闭
		}catch (Exception e){}
	}

	/**
	 * 从酷安的返回数据解析更新
	 * @param json
	 * @return
     */
	public static String[] getUpdate(String json){
		try{
			outputTextFile(json,"getUpdate");
			json = json.substring(json.indexOf("ex-apk-view-title"),json.indexOf("ex-container"));
			json = json.substring(json.indexOf("<small>")+7);
			String ver = json.substring(0,json.indexOf("<"));
			json = json.substring(json.indexOf("apkDownloadUrl")+18);
			String url = json.substring(0,json.indexOf("\""));
			return new String[]{ver,url};
		}catch (Exception e){
			return null;
		}
	}

	/**
	 * 获取版本号
	 *
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 安装apk方法
	 */
	public static void installApk(Context context,String filename) {
		File file = new File(filename);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		String type = "application/vnd.android.package-archive";
		intent.setDataAndType(Uri.fromFile(file), type);
		context.startActivity(intent);
	}

	/**
	 * 资源转图片
	 * @param drawable
	 * @return
     */
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		} else if (drawable instanceof NinePatchDrawable) {
			Bitmap bitmap = Bitmap
					.createBitmap(
							drawable.getIntrinsicWidth(),
							drawable.getIntrinsicHeight(),
							drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
									: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			return bitmap;
		} else {
			return null;
		}
	}

	/**
	 * 对资源进行渲染
	 * @param drawable
	 * @param colors
     * @return
     */
	public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
		final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
		 DrawableCompat.setTintList(wrappedDrawable, colors);
		return wrappedDrawable;
	}
}
