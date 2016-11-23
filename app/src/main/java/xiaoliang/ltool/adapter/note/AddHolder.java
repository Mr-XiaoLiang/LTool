package xiaoliang.ltool.adapter.note;

import android.view.View;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.NoteAddBean;
import xiaoliang.ltool.listener.LItemTouchHelper;

/**
 * Created by liuj on 2016/11/23.
 * 添加的按钮
 */

public class AddHolder extends NoteHolder implements View.OnClickListener {

    private View root;
    private LItemTouchHelper helper;

    public AddHolder(View itemView) {
        super(itemView);
        root = itemView.findViewById(R.id.item_note_add_additem);
        root.setOnClickListener(this);
    }

    @Override
    public void onBind(NoteAddBean bean, LItemTouchHelper helper) {
        this.helper = helper;
    }

    @Override
    public int type() {
        return NoteAddBean.ADDITEM;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==root.getId()){
            helper.onItemViewClick(this,view);
        }
    }
}
