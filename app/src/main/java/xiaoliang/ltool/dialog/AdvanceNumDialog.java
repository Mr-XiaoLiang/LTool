package xiaoliang.ltool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.NoteAddBean;

/**
 * Created by liuj on 2016/11/25.
 * 提醒提前时间的选择
 */
public class AdvanceNumDialog extends Dialog implements View.OnClickListener,RadioGroup.OnCheckedChangeListener,TextWatcher {

    private TextInputEditText numEdit;
    private int unit = NoteAddBean.ADVANCE_UNIT_MINUTE;
    private long num = 15;
    private OnAdvanceNumChangeListener listener;
    private TextView doneBtn;

    public AdvanceNumDialog(Context context) {
        super(context);
    }

    public static AdvanceNumDialog newInstance(Context context,long num,int unit,OnAdvanceNumChangeListener listener){
        AdvanceNumDialog dialog = new AdvanceNumDialog(context);
        dialog.setNum(num);
        dialog.setUnit(unit);
        dialog.setOnAdvanceNumChangeListener(listener);
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除屏幕title
        setContentView(R.layout.dialog_advance_num);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        numEdit = (TextInputEditText) findViewById(R.id.dialog_advance_num_num);
        doneBtn = (TextView) findViewById(R.id.dialog_advance_num_done);
        doneBtn.setOnClickListener(this);
        findViewById(R.id.dialog_advance_num_cancel).setOnClickListener(this);
        ((RadioGroup)findViewById(R.id.dialog_advance_num_unit)).setOnCheckedChangeListener(this);
        numEdit.setText(String.valueOf(num));
        numEdit.addTextChangedListener(this);
        switch (unit){
            case NoteAddBean.ADVANCE_UNIT_MINUTE:
                ((RadioButton)findViewById(R.id.dialog_advance_num_minutes)).setChecked(true);
                break;
            case NoteAddBean.ADVANCE_UNIT_HOUR:
                ((RadioButton)findViewById(R.id.dialog_advance_num_hours)).setChecked(true);
                break;
            case NoteAddBean.ADVANCE_UNIT_DAY:
                ((RadioButton)findViewById(R.id.dialog_advance_num_day)).setChecked(true);
                break;
            case NoteAddBean.ADVANCE_UNIT_WEEK:
                ((RadioButton)findViewById(R.id.dialog_advance_num_week)).setChecked(true);
                break;
        }
    }

    public void setNum(long num) {
        this.num = num;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public void setOnAdvanceNumChangeListener(OnAdvanceNumChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_advance_num_done:
                String str = numEdit.getText().toString();
                if(str.length()<1){
                    numEdit.setError("请填写时间");
                    return;
                }
                if(listener!=null){
                    listener.onAdvanceNumChange(Integer.parseInt(str),unit);
                }
            case R.id.dialog_advance_num_cancel:
                dismiss();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if(radioGroup.getId()==R.id.dialog_advance_num_unit){
            switch (i){
                case R.id.dialog_advance_num_minutes:
                    unit = NoteAddBean.ADVANCE_UNIT_MINUTE;
                    break;
                case R.id.dialog_advance_num_hours:
                    unit = NoteAddBean.ADVANCE_UNIT_HOUR;
                    break;
                case R.id.dialog_advance_num_day:
                    unit = NoteAddBean.ADVANCE_UNIT_DAY;
                    break;
                case R.id.dialog_advance_num_week:
                    unit = NoteAddBean.ADVANCE_UNIT_WEEK;
                    break;
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(charSequence.length()<1){
            doneBtn.setTextColor(Color.GRAY);
            doneBtn.setEnabled(false);
        }else{
            doneBtn.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
            doneBtn.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public interface OnAdvanceNumChangeListener{
        void onAdvanceNumChange(long num,int unit);
    }

}
