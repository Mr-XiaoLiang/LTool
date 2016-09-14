package xiaoliang.ltool.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
/**
 * 模糊工具类
 * @author LiuJ
 *
 */
public class BlurUtil {
	/**
	 * 高斯模糊
	 * @param context
	 * @param bitmap
	 * @return
	 */
	public static Bitmap blurBitmap(Context context,Bitmap bitmap){
		//Let's create an empty bitmap with the same size of the bitmap we want to blur
		Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		//Instantiate a new Renderscript
		RenderScript rs = RenderScript.create(context);
		//Create an Intrinsic Blur Script using the Renderscript
		ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
		//Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
		Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
		Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
		//Set the radius of the blur
		blurScript.setRadius(25.f);
		//Perform the Renderscript
		blurScript.setInput(allIn);
		blurScript.forEach(allOut);
		//Copy the final bitmap created by the out Allocation to the outBitmap
		allOut.copyTo(outBitmap);
		//recycle the original bitmap
		bitmap.recycle();
		//After finishing everything, we destroy the Renderscript.
		rs.destroy();
		return outBitmap;
	}
	/**
	 * 获取一个模糊处理之后的图片
	 * @param context
	 * @param rec
	 * @return
	 */
	public static Bitmap blurBitmap(Context context,int rec){
		Resources res=context.getResources();
		return blurBitmap(context,BitmapFactory.decodeResource(res, rec));
	}

	/**
	 * 快速高斯模糊一个图片
	 * 做模糊处理前先压缩图片为原本的1/4
	 * @param context
	 * @param bitmap
     * @return
     */
	public static Bitmap quickBlurBitmap(Context context,Bitmap bitmap){
		Matrix matrix = new Matrix();
		matrix.postScale(0.3f,0.3f); //长和宽放大缩小的比例
		bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		bitmap = blurBitmap(context,bitmap);
//		matrix.postScale(2,2);
//		Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true)
		return bitmap;
	}

}
