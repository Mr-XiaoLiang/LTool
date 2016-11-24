package xiaoliang.ltool.adapter.note;

import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.Calendar;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.NoteAddBean;
import xiaoliang.ltool.listener.LItemTouchHelper;

/**
 * Created by liuj on 2016/11/22.
 * 时间选择的item
 */

public class TimeHolder extends NoteHolder implements View.OnClickListener,
        SwitchCompat.OnCheckedChangeListener,AppCompatSpinner.OnItemSelectedListener {

    private SwitchCompat oneDay,alertBtn;
    private TextView startData,startTime,endData,endTime,aheadTime;
    private Calendar calendar;
    private NoteAddBean bean;
    private LItemTouchHelper helper;
    private String[] aheadUnitName = {"分钟","小时","天","星期"};

    public TimeHolder(View itemView) {
        super(itemView);
        oneDay = (SwitchCompat) itemView.findViewById(R.id.item_note_add_day);
        alertBtn = (SwitchCompat) itemView.findViewById(R.id.item_note_add_alert);
        startData = (TextView) itemView.findViewById(R.id.item_note_add_start_date);
        startTime = (TextView) itemView.findViewById(R.id.item_note_add_start_time);
        endData = (TextView) itemView.findViewById(R.id.item_note_add_end_date);
        endTime = (TextView) itemView.findViewById(R.id.item_note_add_end_time);
        aheadTime = (TextView) itemView.findViewById(R.id.item_note_add_advance_num);
        itemView.findViewById(R.id.item_note_add_cancel).setOnClickListener(this);
        oneDay.setOnCheckedChangeListener(this);
        alertBtn.setOnCheckedChangeListener(this);
        startData.setOnClickListener(this);
        startTime.setOnClickListener(this);
        endData.setOnClickListener(this);
        endTime.setOnClickListener(this);
        aheadTime.setOnClickListener(this);
        calendar = Calendar.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //此处将事件抛出，不做处理，因为涉及content和其他相关复杂处理
            case R.id.item_note_add_start_date:
            case R.id.item_note_add_start_time:
            case R.id.item_note_add_end_date:
            case R.id.item_note_add_end_time:
            case R.id.item_note_add_advance_num:
                helper.onItemViewClick(this,view);
                break;
            case R.id.item_note_add_cancel://调用删除方法
                helper.onSwiped(this);
                break;
        }
    }

    @Override
    public void onBind(NoteAddBean bean, LItemTouchHelper helper) {
        this.bean = bean;
        this.helper = helper;
        oneDay.setChecked(bean.oneDay);
        alertBtn.setChecked(bean.alert);
        calendar.setTimeInMillis(bean.startTime);
        startData.setText(getDateFormat(Calendar.YEAR)+"年"+getDateFormat(Calendar.MONTH)+"月"+getDateFormat(Calendar.DAY_OF_MONTH)+"日");
        startTime.setText(getDateFormat(Calendar.HOUR_OF_DAY)+":"+getDateFormat(Calendar.MINUTE));
        calendar.setTimeInMillis(bean.endTime);
        endData.setText(getDateFormat(Calendar.YEAR)+"年"+getDateFormat(Calendar.MONTH)+"月"+getDateFormat(Calendar.DAY_OF_MONTH)+"日");
        endTime.setText(getDateFormat(Calendar.HOUR_OF_DAY)+":"+getDateFormat(Calendar.MINUTE));
        aheadTime.setText(String.valueOf(bean.advance)+aheadUnitName[bean.advanceUnit]);
    }

    private String getDateFormat(int type){
        String out;
        switch (type){
            case Calendar.MONTH:
                out = calendar.get(type)+1+"";
                break;
            default:
                out = calendar.get(type)+"";
                break;
        }
        if(out.length()<2){
            out = "0"+out;
        }
        return out;
    }

    @Override
    public int type() {
        return NoteAddBean.TIME;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.item_note_add_day:
                if(bean!=null)
                    bean.oneDay = b;
                break;
            case R.id.item_note_add_alert:
                if(bean!=null)
                    bean.alert = b;
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
