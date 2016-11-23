package xiaoliang.ltool.listener;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import xiaoliang.ltool.adapter.note.NoteHolder;

/**
 * Created by liuj on 2016/11/19.
 * 用来做RecyclerView滑动删除和拖拽排序
 */

public class LItemTouchHelper extends ItemTouchHelper {

    private LItemTouchCallback callback;

    /**
     * Creates an ItemTouchHelper that will work with the given Callback.
     * <p>
     * You can attach ItemTouchHelper to a RecyclerView via
     * {@link #attachToRecyclerView(RecyclerView)}. Upon attaching, it will add an item decoration,
     * an onItemTouchListener and a Child attach / detach listener to the RecyclerView.
     *
     * @param callback The Callback which controls the behavior of this touch helper.
     */
    public LItemTouchHelper(Callback callback) {
        super(callback);
    }

    public static LItemTouchHelper newInstance(RecyclerView recyclerView,LItemTouchCallback.OnItemTouchCallbackListener onItemTouchCallbackListener) {
        return newInstance(recyclerView,true,true,onItemTouchCallbackListener);
    }

    public static LItemTouchHelper newInstance(RecyclerView recyclerView, boolean canDrag, boolean canSwipe, LItemTouchCallback.OnItemTouchCallbackListener onItemTouchCallbackListener) {
        LItemTouchCallback callback = new LItemTouchCallback(onItemTouchCallbackListener);
        LItemTouchHelper helper = new LItemTouchHelper(callback);
        helper.setCanDrag(canDrag);
        helper.setCanSwipe(canSwipe);
        helper.setCallback(callback);
        helper.attachToRecyclerView(recyclerView);
        return helper;
    }

    public void setCanDrag(boolean canDrag) {
        if(callback!=null)
            callback.setCanDrag(canDrag);
    }

    public void setCanSwipe(boolean canSwipe) {
        if(callback!=null)
            callback.setCanSwipe(canSwipe);
    }
    public void onSwiped(NoteHolder holder){
        callback.onSwiped(holder,0);
    }
    public void onItemViewClick(RecyclerView.ViewHolder holder, View v){
        callback.onItemViewClick(holder,v);
    }
    public void setCallback(LItemTouchCallback callback) {
        this.callback = callback;
    }
}
