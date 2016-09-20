package xiaoliang.ltool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import xiaoliang.ltool.view.LLoadView;

public class LoadDialog extends Dialog {

	public LoadDialog(Context context) {
		super(context);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除屏幕title
		LLoadView lLoadView = new LLoadView(getContext());
		lLoadView.setShowBg(true);
		setContentView(lLoadView);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
	}
}
