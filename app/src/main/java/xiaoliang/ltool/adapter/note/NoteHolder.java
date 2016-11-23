package xiaoliang.ltool.adapter.note;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import xiaoliang.ltool.bean.NoteAddBean;
import xiaoliang.ltool.listener.LItemTouchHelper;

/**
 * Created by liuj on 2016/11/22.
 * 添加笔记内容的Holder
 */

public abstract class NoteHolder extends RecyclerView.ViewHolder{

    public NoteHolder(View itemView) {
        super(itemView);
    }

    public abstract void onBind(NoteAddBean bean,LItemTouchHelper helper);

    public abstract int type();

}