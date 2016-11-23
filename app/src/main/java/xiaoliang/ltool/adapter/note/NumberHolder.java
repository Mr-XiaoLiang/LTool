package xiaoliang.ltool.adapter.note;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.NoteAddBean;
import xiaoliang.ltool.listener.LItemTouchHelper;

/**
 * Created by liuj on 2016/11/22.
 * 带有编号的holder
 */

public class NumberHolder extends NoteHolder implements View.OnClickListener,View.OnTouchListener,View.OnFocusChangeListener,TextWatcher {

    private TextInputEditText editText;
    private View cancel;
    private View move;
    private NoteAddBean bean;
    private LItemTouchHelper helper;
    private TextView numView;

    public NumberHolder(View itemView) {
        super(itemView);
        editText = (TextInputEditText) itemView.findViewById(R.id.item_note_add_text);
        cancel = itemView.findViewById(R.id.item_note_add_cancel);
        numView = (TextView) itemView.findViewById(R.id.item_note_add_number);
        move = itemView.findViewById(R.id.item_note_add_move);
        editText.addTextChangedListener(this);
        editText.setOnFocusChangeListener(this);
        move.setOnTouchListener(this);
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

    @Override
    public void onBind(NoteAddBean bean, LItemTouchHelper helper) {
        this.bean = bean;
        this.helper = helper;
        editText.setText(bean.note);
        String text = bean.index+"";
        String max = bean.maxIndex+"";
        while(text.length()<max.length()){
            text = "0"+text;
        }
        numView.setText(text);
    }

    @Override
    public int type() {
        return NoteAddBean.LIST;
    }

    public void setTypeface(Context context) {
        //将字体文件保存在assets/fonts/目录下，创建Typeface对象
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/century_gothic_std.otf");
        //使用字体
        setTypeface(typeFace);
    }
    public void setTypeface(Typeface typeFace) {
        //使用字体
        if(numView!=null)
            numView.setTypeface(typeFace);
    }
}
