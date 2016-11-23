package xiaoliang.ltool.adapter.note;

import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.NoteAddBean;
import xiaoliang.ltool.listener.LItemTouchHelper;

/**
 * Created by liuj on 2016/11/22.
 * 普通文本输入框
 */

public class TextHolder  extends NoteHolder implements View.OnClickListener,TextWatcher,View.OnTouchListener,View.OnFocusChangeListener {

    private TextInputEditText editText;
    private View cancel;
    private View move;
    private NoteAddBean bean;
    private LItemTouchHelper helper;

    public TextHolder(View itemView) {
        super(itemView);
        editText = (TextInputEditText) itemView.findViewById(R.id.item_note_add_text);
        cancel = itemView.findViewById(R.id.item_note_add_cancel);
        itemView.findViewById(R.id.item_note_add_checkbox).setVisibility(View.GONE);
        move = itemView.findViewById(R.id.item_note_add_move);
        editText.addTextChangedListener(this);
        editText.setOnFocusChangeListener(this);
        move.setOnTouchListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onBind(NoteAddBean bean,LItemTouchHelper helper) {
        this.bean = bean;
        this.helper = helper;
        editText.setText(bean.note);
    }

    @Override
    public int type() {
        return NoteAddBean.TEXT;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(bean!=null)
            bean.note = charSequence.toString();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==cancel.getId())
            helper.onSwiped(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(view.getId()==move.getId())
            helper.startDrag(this);
        return false;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(view.getId() == R.id.item_note_add_text){
            if(b)
                cancel.setVisibility(View.VISIBLE);
            else
                cancel.setVisibility(View.GONE);
        }
    }
}