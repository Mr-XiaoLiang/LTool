package xiaoliang.ltool.listener;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by liuj on 2016/11/25.
 * 重写FloatingActionButton来做到跟随列表滑动而变化
 * 在原本的基础上，做到跟随界面滑动效果
 */

public class ScrollViewFloatBtnBehavior extends FloatingActionButton.Behavior{

    int scrollSize = 0;
    private RecyclerViewOnScrollListener onScrollListener;

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        if(target instanceof RecyclerView){
            ((RecyclerView)target).addOnScrollListener(onScrollListener = new RecyclerViewOnScrollListener(child));
        }

        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        if(onScrollListener==null){
//            scrollSize = 0;
//            child.show();
//            ViewCompat.animate(child)
//                    .scaleX(1f)
//                    .scaleY(1f)
//                    .setDuration(500)
//                    .start();
        }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        //父类中什么也没做，真棒（反正夸一下），但是我也意思一下，调用父类的方法
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        scrollSize+=Math.abs(dyConsumed+dyUnconsumed);
        if(scrollSize>child.getWidth()){
            child.hide();
        }else{
            float scale = 1-scrollSize*1.0f/child.getWidth();
            child.setScaleX(scale);
            child.setScaleY(scale);
        }
    }

    private class RecyclerViewOnScrollListener extends RecyclerView.OnScrollListener{

        private FloatingActionButton child;

        public RecyclerViewOnScrollListener(FloatingActionButton child) {
            this.child = child;
        }
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            scrollSize+=Math.abs(dy);
            if(scrollSize>child.getWidth()){
                child.hide();
            }else{
                float scale = 1-scrollSize*1.0f/child.getWidth();
                child.setScaleX(scale);
                child.setScaleY(scale);
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if(newState==RecyclerView.SCROLL_STATE_IDLE){
                scrollSize = 0;
                child.show();
                ViewCompat.animate(child)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(500)
                        .start();
            }
        }
    }

}
