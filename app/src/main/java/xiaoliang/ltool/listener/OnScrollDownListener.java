package xiaoliang.ltool.listener;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

/**
 * Created by liuj on 2016/10/7.
 * RecyclerView的加载更多监听
 */

public abstract class OnScrollDownListener extends RecyclerView.OnScrollListener{
    //用来标记是否正在向最后一个滑动，既是否向下滑动
    private boolean isSlidingToLast = false;
    private StaggeredGridLayoutManager manager;
    private int loadSize = 5;

    public OnScrollDownListener(StaggeredGridLayoutManager manager) {
        this.manager = manager;
    }

    public OnScrollDownListener(int loadSize, StaggeredGridLayoutManager manager) {
        this.loadSize = loadSize;
        this.manager = manager;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//        manager.invalidateSpanAssignments();
        // 当不滚动时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                onScroll(true,newState);
            }else{
                onScroll(!isSlidingToLast,newState);
            }
    }
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        isSlidingToLast = dy>0;
        //获取最后一个完全显示的ItemPosition
        int[] lastVisiblePositions = manager.findLastVisibleItemPositions(new int[manager.getSpanCount()]);
        int lastVisiblePos = getMaxElem(lastVisiblePositions);
        int totalItemCount = manager.getItemCount();
        // 判断是否滚动到底部
        if (lastVisiblePos > (totalItemCount - loadSize) && isSlidingToLast) {
            //加载更多功能的代码
            onMore();
        }
        Log.d("onScrollStateChanged","ItemCount-----------------"+totalItemCount);
    }
    private int getMaxElem(int[] arr) {
        int maxVal = Integer.MIN_VALUE;
        for(int a:arr){
            if(a>maxVal)
                maxVal = a;
        }
        return maxVal;
    }

    public abstract void onScroll(boolean down,int newState);

    public abstract void onMore();

}