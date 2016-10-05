package xiaoliang.ltool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;

import xiaoliang.ltool.view.LLoadView;
import xiaoliang.ltool.view.LLoadView2;
import xiaoliang.ltool.view.LLoadView2Option;

public class LoadDialog2 extends Dialog {

	private LLoadView2 lLoadView;

	public LoadDialog2(Context context) {
		super(context);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除屏幕title
		lLoadView = new LLoadView2(getContext());
		LLoadView2Option.Builder builder = new LLoadView2Option.Builder();
		builder.setProgress(0.1f).setBorderColor(Color.parseColor("#41ddaf")).setWaterColor(Color.parseColor("#FF4081"));
		lLoadView.setOption(new LLoadView2Option(builder));
		setContentView(lLoadView);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
	}
	public void setProgress(float f){
		if(lLoadView!=null){
			lLoadView.setProgress(f);
		}
	}
}
