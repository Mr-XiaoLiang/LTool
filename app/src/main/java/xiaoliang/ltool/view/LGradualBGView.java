package xiaoliang.ltool.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by liuj on 2016/9/18.
 * 这是一个渐变的View,专门为了天气的详细页面定制
 * 作为背景色,
 * 用途:根据当前时间动态调整白天黑夜的色调,
 * 根据温度动态调整色调,根据天气动态调整色温
 * 冰蓝0(#cceeff) 水蓝15（#00eedd） 水橙25(#ff88bb) 火红35(#ff2233)
 *
 */
public class LGradualBGView extends View {

    public LGradualBGView(Context context) {
        super(context);
    }

    public LGradualBGView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LGradualBGView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
