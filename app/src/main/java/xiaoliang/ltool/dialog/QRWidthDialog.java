package xiaoliang.ltool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import xiaoliang.ltool.R;

/**
 * Created by liuj on 2016/9/25.
 * 二维码的宽度选择弹出框
 */

public class QRWidthDialog extends Dialog implements View.OnClickListener{

    private TextView msg;
    private TextInputEditText editText;
    private View leftBtn,rightBtn;
    private int width = 0;
    private QRWidthDialogListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除屏幕title
        setContentView(R.layout.dialog_qr_width);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        msg = (TextView) findViewById(R.id.dialog_qr_width_msg);
        editText = (TextInputEditText) findViewById(R.id.dialog_qr_width_edit);
        leftBtn = findViewById(R.id.dialog_qr_width_left);
        rightBtn = findViewById(R.id.dialog_qr_width_right);
        msg.setText("您正在设置保存二维码的宽度，当前您预览的二维码宽度为："+width+",为了保证生成效果一致，请勿填写差距过大的值。");
        editText.setText(""+width);
        leftBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);
    }

    public QRWidthDialog(Context context,int width,QRWidthDialogListener listener) {
        super(context);
        this.width = width;
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        String s = editText.getText().toString().trim();
        switch (v.getId()){
            case R.id.dialog_qr_width_right:
                if(s.length()<1){
                    editText.setError("请输入有效的宽度值");
                    return;
                }else{
                    if(listener!=null)
                        listener.getWidth(Integer.parseInt(s));
                }
            case R.id.dialog_qr_width_left:
                dismiss();
                break;
        }
    }

    public  interface QRWidthDialogListener{
        void getWidth(int width);
    }

}
