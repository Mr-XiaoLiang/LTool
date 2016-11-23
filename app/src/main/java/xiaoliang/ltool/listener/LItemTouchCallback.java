package xiaoliang.ltool.listener;

import android.provider.ContactsContract;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.Collections;
import java.util.List;

/**
 * Created by liuj on 2016/11/19.
 * 用来做RecyclerView滑动删除和拖拽排序
 */

public class LItemTouchCallback extends ItemTouchHelper.Callback {

    private OnItemTouchCallbackListener listener;
    /**
     * 是否可以拖拽
     */
    private boolean isCanDrag = false;
    /**
     * 是否可以被滑动
     */
    private boolean isCanSwipe = false;

    public LItemTouchCallback(OnItemTouchCallbackListener listener) {
        this.listener = listener;
    }

    public void setCanDrag(boolean canDrag) {
        isCanDrag = canDrag;
    }

    public void setCanSwipe(boolean canSwipe) {
        isCanSwipe = canSwipe;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return isCanSwipe;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return isCanSwipe;
    }


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {// GridLayoutManager
            // flag如果值是0，相当于这个功能被关闭
            int dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlag = 0;
            // create make
            return makeMovementFlags(dragFlag, swipeFlag);
        } else if (layoutManager instanceof LinearLayoutManager) {// linearLayoutManager
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int orientation = linearLayoutManager.getOrientation();

            int dragFlag = 0;
            int swipeFlag = 0;

            // 为了方便理解，相当于分为横着的ListView和竖着的ListView
            if (orientation == LinearLayoutManager.HORIZONTAL) {// 如果是横向的布局
                swipeFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            } else if (orientation == LinearLayoutManager.VERTICAL) {// 如果是竖向的布局，相当于ListView
                dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                swipeFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            }
            return makeMovementFlags(dragFlag, swipeFlag);
        }
        return 0;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if(listener!=null){
            return listener.onMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        }
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if(listener!=null){
            listener.onSwiped(viewHolder.getAdapterPosition());
        }
    }

    public void onItemViewClick(RecyclerView.ViewHolder holder, View v){
        if(listener!=null){
            listener.onItemViewClick(holder,v);
        }
    }

    public interface OnItemTouchCallbackListener {
        /**
         * 当某个Item被滑动删除的时候
         *
         * @param adapterPosition item的position
         */
        void onSwiped(int adapterPosition);

        /**
         * 当两个Item位置互换的时候被回调
         *
         * @param srcPosition    拖拽的item的position
         * @param targetPosition 目的地的Item的position
         * @return 开发者处理了操作应该返回true，开发者没有处理就返回false
         */
        boolean onMove(int srcPosition, int targetPosition);

        void onItemViewClick(RecyclerView.ViewHolder holder, View v);
    }

    public static class DefaultOnItemTouchCallbackListener implements OnItemTouchCallbackListener{

        private List data;
        private RecyclerView.Adapter adapter;

        public DefaultOnItemTouchCallbackListener(List data,RecyclerView.Adapter adapter) {
            this.adapter = adapter;
            this.data = data;
        }

        @Override
        public void onSwiped(int adapterPosition) {
            if(data!=null){
                data.remove(adapterPosition);
                if(adapter!=null)
                    adapter.notifyItemRemoved(adapterPosition);
            }
        }

        @Override
        public boolean onMove(int srcPosition, int targetPosition) {
            if(data!=null){
                // 更换数据源中的数据Item的位置
                Collections.swap(data, srcPosition, targetPosition);
                if(adapter!=null){
                    // 更新UI中的Item的位置，主要是给用户看到交互效果
                    adapter.notifyItemMoved(srcPosition, targetPosition);
                    return true;
                }
            }
            return false;
        }

        @Override
        public void onItemViewClick(RecyclerView.ViewHolder holder, View v) {

        }
    }

    public void setListener(OnItemTouchCallbackListener listener) {
        this.listener = listener;
    }
}
