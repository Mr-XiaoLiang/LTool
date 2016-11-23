package xiaoliang.ltool.adapter;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import xiaoliang.ltool.R;
import xiaoliang.ltool.adapter.note.AddHolder;
import xiaoliang.ltool.adapter.note.AddressHolder;
import xiaoliang.ltool.adapter.note.CheckHolder;
import xiaoliang.ltool.adapter.note.MoneyHolder;
import xiaoliang.ltool.adapter.note.NoteHolder;
import xiaoliang.ltool.adapter.note.NumberHolder;
import xiaoliang.ltool.adapter.note.TextHolder;
import xiaoliang.ltool.adapter.note.TimeHolder;
import xiaoliang.ltool.bean.NoteAddBean;
import xiaoliang.ltool.listener.LItemTouchHelper;

/**
 * Created by liuj on 2016/11/22.
 * 添加笔记的适配器
 */

public class NoteAddAdapter extends RecyclerView.Adapter<NoteHolder> {

    private ArrayList<NoteAddBean> data;
    private LItemTouchHelper helper;
    private Context context;
    private LayoutInflater inflater;

    public NoteAddAdapter(Context context, ArrayList<NoteAddBean> data, LItemTouchHelper helper) {
        this(context);
        this.data = data;
        this.helper = helper;
    }

    public NoteAddAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<NoteAddBean> data) {
        this.data = data;
    }

    public void setHelper(LItemTouchHelper helper) {
        this.helper = helper;
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NoteHolder holder = null;
        switch (viewType){
            case NoteAddBean.ADDITEM:
                holder = new AddHolder(inflater.inflate(R.layout.item_note_add_add,parent,false));
                break;
            case NoteAddBean.ADDRESS:
                holder = new AddressHolder(inflater.inflate(R.layout.item_note_add_address,parent,false));
                break;
            case NoteAddBean.LIST:
                holder = new NumberHolder(inflater.inflate(R.layout.item_note_add_list,parent,false));
                ((NumberHolder)holder).setTypeface(context);
                break;
            case NoteAddBean.MONEY:
                holder = new MoneyHolder(inflater.inflate(R.layout.item_note_add_money,parent,false));
                break;
            case NoteAddBean.TEXT:
                holder = new TextHolder(inflater.inflate(R.layout.item_note_add_check,parent,false));
                break;
            case NoteAddBean.TODO:
                holder = new CheckHolder(inflater.inflate(R.layout.item_note_add_check,parent,false));
                break;
            case NoteAddBean.TIME:
                holder = new TimeHolder(inflater.inflate(R.layout.item_note_add_time,parent,false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(NoteHolder holder, int position) {
        holder.onBind(data.get(position),helper);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

}
