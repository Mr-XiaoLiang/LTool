package xiaoliang.ltool.adapter.note;


import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.NoteAddBean;
import xiaoliang.ltool.listener.LItemTouchHelper;

/**
 * Created by liuj on 2016/11/22.
 * 带有复选框的item
 */

public class CheckHolder extends NoteHolder implements CompoundButton.OnCheckedChangeListener,
        View.OnClickListener,TextWatcher,View.OnTouchListener,View.OnFocusChangeListener {

    private TextInputEditText editText;
    private View cancel;
    private CheckBox checkBox;
    private View move;
    private NoteAddBean bean;
    private LItemTouchHelper helper;

    public CheckHolder(View itemView) {
        super(itemView);
        editText = (TextInputEditText) itemView.findViewById(R.id.item_note_add_text);
        cancel = itemView.findViewById(R.id.item_note_add_cancel);
        checkBox = (CheckBox) itemView.findViewById(R.id.item_note_add_checkbox);
        move = itemView.findViewById(R.id.item_note_add_move);
        editText.addTextChangedListener(this);
        editText.setOnFocusChangeListener(this);
        checkBox.setOnCheckedChangeListener(this);
        move.setOnTouchListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onBind(NoteAddBean bean, LItemTouchHelper helper) {
        this.bean = bean;
        this.helper = helper;
        checkBox.setChecked(bean.isChecked);
        editText.setText(bean.note);
    }

    @Override
    public int type() {
        return NoteAddBean.TODO;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(compoundButton.getId()==checkBox.getId()){
            if(bean!=null)
                bean.isChecked = b;
            if(b){
//                editText.getPaint().setStrokeWidth(2);
                editText.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
                editText.setTextColor(Color.GRAY);
            }else{
                editText.getPaint().setFlags(0);  // 取消设置的的划线
                editText.setTextColor(Color.BLACK);

            }
        }
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
    public void onFocusChange(View view, boolean b) {
        if(view.getId() == R.id.item_note_add_text){
            if(b)
                cancel.setVisibility(View.VISIBLE);
            else
                cancel.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(view.getId()==move.getId())
            helper.startDrag(this);
        return false;
    }
}
