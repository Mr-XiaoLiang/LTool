package xiaoliang.ltool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import xiaoliang.ltool.R;
import xiaoliang.ltool.util.OtherUtil;

/**
 * Created by liuj on 2016/11/11.
 * 分享的dialog
 */

public class ShareDialog extends Dialog implements View.OnClickListener {

    private File imgPath;
    private String title;
    private String msg;
    private String pMsg;
    private TextInputEditText titleEditText,msgEditText;
    private ImageView imageView;

    public ShareDialog(Context context) {
        this(context,true,null);
    }

    public static ShareDialog newInstance(Context context,File imgPath,String title,String msg,String pMsg) {
        ShareDialog dialog = new ShareDialog(context);
        dialog.setImgPath(imgPath);
        dialog.setTitle(title);
        dialog.setMsg(msg);
        dialog.setpMsg(pMsg);
        return dialog;
    }

    public ShareDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除屏幕title
        setContentView(R.layout.dialog_share);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        titleEditText = (TextInputEditText) findViewById(R.id.dialog_sharre_title);
        msgEditText = (TextInputEditText) findViewById(R.id.dialog_sharre_msg);
        findViewById(R.id.dialog_sharre_cancel).setOnClickListener(this);
        findViewById(R.id.dialog_sharre_send).setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.dialog_sharre_img);
        if(imgPath!=null&&imgPath.exists()){
            Glide.with(getContext()).load(imgPath).into(imageView);
        }
        titleEditText.setText(title);
        msgEditText.setText(msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_sharre_cancel:
                dismiss();
                break;
            case R.id.dialog_sharre_send:
                OtherUtil.shareMsg(getContext(),"分享",title,pMsg+msg,imgPath.getPath());
                dismiss();
                break;
        }
    }

    public File getImgPath() {
        return imgPath;
    }

    public void setImgPath(File imgPath) {
        this.imgPath = imgPath;
    }

    public void setImgPath(String imgPath) {
        setImgPath(new File(imgPath));
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getpMsg() {
        return pMsg;
    }

    public void setpMsg(String pMsg) {
        this.pMsg = pMsg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
