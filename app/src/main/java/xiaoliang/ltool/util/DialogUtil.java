package xiaoliang.ltool.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.CityBean;
import xiaoliang.ltool.dialog.CityDialog;
import xiaoliang.ltool.dialog.ColorsDialog;
import xiaoliang.ltool.dialog.LoadDialog;
import xiaoliang.ltool.dialog.LoadDialog2;
import xiaoliang.ltool.dialog.QRWidthDialog;

/**
 * dialog调用类
 *
 * @author LiuJ
 */
public class DialogUtil {
    private Context context;

    public DialogUtil(Context context) {
        super();
        this.context = context;
    }

    public static Dialog getCityDialog(Activity context, CityDialog.CitySelectedListener listener) {
        CityDialog dialog = new CityDialog(context, listener);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.dialogstyle_vertical); // 添加动画
        dialog.setCancelable(true);
        dialog.show();
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.5);
        p.width = d.getWidth();
        dialog.onWindowAttributesChanged(p);
        window.setAttributes(p);
        return dialog;
    }

    public static LoadDialog getLoadDialog(Activity context) {
        LoadDialog dialog = new LoadDialog(context);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.dialogstyle_vertical); // 添加动画
        dialog.setCancelable(true);
        dialog.show();
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.width = p.height = (int) (d.getWidth() * 0.3);
        dialog.onWindowAttributesChanged(p);
        window.setAttributes(p);
        return dialog;
    }

    public static LoadDialog2 getLoadDialog2(Activity context) {
        LoadDialog2 dialog = new LoadDialog2(context);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
//		window.setWindowAnimations(R.style.dialogstyle_vertical); // 添加动画
        dialog.setCancelable(true);
        dialog.show();
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.width = p.height = (int) (d.getWidth() * 0.3);
        dialog.onWindowAttributesChanged(p);
        window.setAttributes(p);
        return dialog;
    }

    public static ColorsDialog getColorDialog(Activity context, ArrayList<Integer> color, int what, ColorsDialog.OnColorConfirmListener listener) {
        ColorsDialog dialog = new ColorsDialog(context, color, what, listener);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.dialogstyle_vertical); // 添加动画
        dialog.setCancelable(true);
        dialog.show();
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.5);
        p.width = (int) (d.getWidth() * 0.9);
        dialog.onWindowAttributesChanged(p);
        window.setAttributes(p);
        return dialog;
    }

    public static ColorsDialog getColorDialog(Activity context, int color, int what, ColorsDialog.OnColorConfirmListener listener) {
        ColorsDialog dialog = new ColorsDialog(context, color, what, listener);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.dialogstyle_vertical); // 添加动画
        dialog.setCancelable(true);
        dialog.show();
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.5);
        p.width = (int) (d.getWidth() * 0.8);
        dialog.onWindowAttributesChanged(p);
        window.setAttributes(p);
        return dialog;
    }

    public static QRWidthDialog getQRWidthDialog(Activity context, int width, QRWidthDialog.QRWidthDialogListener listener){
        QRWidthDialog dialog = new QRWidthDialog(context,width,listener);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
        dialog.setCancelable(true);
        dialog.show();
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
//        p.height = (int) (d.getHeight() * 0.5);
        p.width = (int) (d.getWidth() * 0.8);
        dialog.onWindowAttributesChanged(p);
        window.setAttributes(p);
        return dialog;
    }

    public static AlertDialog getAlertDialog(
            Activity context, String msg) {
        AlertDialog dialog = new AlertDialog.Builder(context).setIcon(R.mipmap.ic_launcher).setTitle("提示").setMessage(msg).create();
        dialog.show();
        return dialog;
    }

    public static AlertDialog getAlertDialog(
            Activity context, String msg,boolean cancelable) {
        AlertDialog dialog;
        if(!cancelable){
            dialog = getAlertDialog(context, "提示", msg, "知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(false);
        }else{
            dialog = getAlertDialog(context,msg);
        }
        return dialog;
    }

    public static AlertDialog getAlertDialog(
            Activity context, String title, String msg) {
        AlertDialog dialog = new AlertDialog.Builder(context).setIcon(R.mipmap.ic_launcher).setTitle(title).setMessage(msg).create();
        dialog.show();
        return dialog;
    }

    public static AlertDialog getAlertDialog(
            Activity context, String title, String msg,
            String rightName, DialogInterface.OnClickListener rightBtn) {
        AlertDialog dialog = new AlertDialog.Builder(context).setIcon(R.mipmap.ic_launcher).setTitle(title).setMessage(msg).setPositiveButton(rightName, rightBtn).create();
        dialog.show();
        return dialog;
    }

    public static AlertDialog getAlertDialog(
            Activity context, String title, String msg,
            String leftName, DialogInterface.OnClickListener leftBtn,
            String rightName, DialogInterface.OnClickListener rightBtn) {
        AlertDialog dialog = new AlertDialog.Builder(context).setIcon(R.mipmap.ic_launcher).setTitle(title).setMessage(msg).setNegativeButton(leftName, leftBtn).setPositiveButton(rightName, rightBtn).create();
        dialog.show();
        return dialog;
    }

    public static AlertDialog getAlertDialog(
            Activity context, String title, String msg,View view,
            String leftName, DialogInterface.OnClickListener leftBtn,
            String rightName, DialogInterface.OnClickListener rightBtn) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(title)
                .setMessage(msg)
                .setView(view)
                .setNegativeButton(leftName, leftBtn)
                .setPositiveButton(rightName, rightBtn)
                .create();
        dialog.show();
        return dialog;
    }

    public static AlertDialog getAlertDialog(
            Activity context, String title, String msg,int view,
            String leftName, DialogInterface.OnClickListener leftBtn,
            String rightName, DialogInterface.OnClickListener rightBtn) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(title)
                .setMessage(msg)
                .setView(view)
                .setNegativeButton(leftName, leftBtn)
                .setPositiveButton(rightName, rightBtn)
                .create();
        dialog.show();
        return dialog;
    }

}
