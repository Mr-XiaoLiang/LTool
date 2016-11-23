package xiaoliang.ltool.adapter.note;

import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.NoteAddBean;
import xiaoliang.ltool.listener.LItemTouchHelper;

/**
 * Created by liuj on 2016/11/22.
 * 地址Item
 */

public class AddressHolder extends NoteHolder implements TextWatcher,View.OnClickListener {

    private TextInputEditText editText;
    private View cancel;
    private NoteAddBean bean;
    private LItemTouchHelper helper;

    public AddressHolder(View itemView) {
        super(itemView);
        editText = (TextInputEditText) itemView.findViewById(R.id.item_note_add_text);
        cancel = itemView.findViewById(R.id.item_note_add_cancel);
        editText.addTextChangedListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(bean!=null)
            bean.note = charSequence.toString();
    }

    @Override
    public void afterTextChanged(Editable editable) {}

    @Override
    public void onClick(View view) {
        if(view.getId()==cancel.getId())
            helper.onSwiped(this);
    }

    @Override
    public void onBind(NoteAddBean bean, LItemTouchHelper helper) {
        this.bean = bean;
        this.helper = helper;
        editText.setText(bean.note);
    }

    @Override
    public int type() {
        return NoteAddBean.ADDRESS;
    }
}
