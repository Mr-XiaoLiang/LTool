package xiaoliang.ltool.view;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Liuj on 2016/10/28.
 * 缩放的一个动画
 */

public class ZoomPageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {
        float absPosition = Math.abs(position);
        if(position>1||position<-1){
            page.setAlpha(0);
        }else if(absPosition<0.1){
            page.setScaleX(1-absPosition);
            page.setScaleY(1-absPosition);
        }else{
            page.setScaleX(0.9f);
            page.setScaleY(0.9f);
        }
    }
}
