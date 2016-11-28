package xiaoliang.ltool.adapter.note;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.text.DecimalFormat;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.NoteAddBean;
import xiaoliang.ltool.listener.LItemTouchHelper;

/**
 * Created by liuj on 2016/11/23.
 * 金额的Holder
 */

public class MoneyHolder extends NoteHolder implements TextWatcher,RadioGroup.OnCheckedChangeListener,View.OnClickListener {

    private TextInputEditText editText;
    private TextInputLayout inputLayout;
    private View cancel;
//    private RadioGroup income;
    private NoteAddBean bean;
    private LItemTouchHelper helper;
    private RadioButton in,out;
    private DecimalFormat decimalFormat;
    private DecimalFormat decimalFormat2;

    public MoneyHolder(View itemView) {
        super(itemView);
        editText = (TextInputEditText) itemView.findViewById(R.id.item_note_add_text);
        cancel = itemView.findViewById(R.id.item_note_add_cancel);
        RadioGroup income = (RadioGroup) itemView.findViewById(R.id.item_note_add_income);
        in = (RadioButton) itemView.findViewById(R.id.item_note_add_income_in);
        out = (RadioButton) itemView.findViewById(R.id.item_note_add_income_out);
        inputLayout = (TextInputLayout) itemView.findViewById(R.id.item_note_add_text_hint);
        decimalFormat = new DecimalFormat("#,###0.00");
        decimalFormat2 = new DecimalFormat("#0.00");
        editText.addTextChangedListener(this);
        cancel.setOnClickListener(this);
        income.setOnCheckedChangeListener(this);
    }

    @Override
    public void onBind(NoteAddBean bean, LItemTouchHelper helper) {
        this.bean = bean;
        this.helper = helper;
        editText.setText(bean.note);
        if(bean.note.length()>0)
            inputLayout.setHint("￥"+decimalFormat.format(Double.parseDouble(bean.note)));
        else
            inputLayout.setHint("金额...");
        if(bean.income){
            in.setChecked(true);
            out.setChecked(false);
        }else{
            in.setChecked(false);
            out.setChecked(true);
        }
    }

    @Override
    public int type() {
        return NoteAddBean.MONEY;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(charSequence.toString().length()>0){
            inputLayout.setHint("￥"+decimalFormat.format(Double.parseDouble(charSequence.toString())));
            if(bean!=null)
                bean.note = decimalFormat2.format(Double.parseDouble(charSequence.toString()));
        }else{
            inputLayout.setHint("金额...");
            if(bean!=null)
                bean.note = "0.00";
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==cancel.getId()){
            helper.onSwiped(this);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if(bean!=null){
            bean.income = i==in.getId();
        }
//        switch (i){
//            case R.id.item_note_add_income_in:
//                break;
//            case R.id.item_note_add_income_out:
//                break;
//        }
    }

}
