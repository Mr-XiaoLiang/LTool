package xiaoliang.ltool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import xiaoliang.ltool.R;
import xiaoliang.ltool.view.LLoadView;
import xiaoliang.ltool.view.LLoadView2;
import xiaoliang.ltool.view.LLoadView2Option;

public class LoadDialog2 extends Dialog {

	private LLoadView2 lLoadView;
	private TextView textView;

	public LoadDialog2(Context context) {
		super(context);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除屏幕title
		setContentView(R.layout.dialog_load_progress);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		lLoadView = (LLoadView2) findViewById(R.id.dialog_load_progress_load);
		textView = (TextView) findViewById(R.id.dialog_load_progress_text);
		LLoadView2Option.Builder builder = new LLoadView2Option.Builder();
		builder.setProgress(0.1f).setBorderColor(Color.parseColor("#41ddaf")).setWaterColor(Color.parseColor("#FF4081"));
		lLoadView.setOption(new LLoadView2Option(builder));
	}
	public void setProgress(Float f){
		if(lLoadView!=null&&f!=null){
			lLoadView.setProgress(f);
		}
	}

	public void setText(String text) {
		if(TextUtils.isEmpty(text)){
			textView.setVisibility(View.GONE);
		}else{
			textView.setVisibility(View.VISIBLE);
			textView.setText(text);
		}
	}
}
