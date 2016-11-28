package xiaoliang.ltool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import xiaoliang.ltool.R;
import xiaoliang.ltool.adapter.note.ListHolder;
import xiaoliang.ltool.bean.NoteListBean;
import xiaoliang.ltool.listener.LItemTouchHelper;

/**
 * Created by liuj on 2016/11/28.
 * 笔记列表的适配器
 */

public class NoteListAdapter extends RecyclerView.Adapter<ListHolder> {

    private ArrayList<NoteListBean> beans;
    private LItemTouchHelper helper;
    private Context context;
    private LayoutInflater inflater;

    public NoteListAdapter(Context context, ArrayList<NoteListBean> beans, LItemTouchHelper helper) {
        this.context = context;
        this.beans = beans;
        this.helper = helper;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListHolder listHolder = new ListHolder(inflater.inflate(R.layout.item_note,parent,false));
        listHolder.setTypeface(context);
        return listHolder;
    }

    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
        if(position>0)
            beans.get(position).lastTime = beans.get(position-1).time;
        holder.onBind(beans.get(position),helper);
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }
}
