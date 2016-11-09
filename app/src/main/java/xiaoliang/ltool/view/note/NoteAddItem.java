package xiaoliang.ltool.view.note;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import xiaoliang.ltool.R;
import xiaoliang.ltool.util.DensityUtil;

/**
 * Created by liuj on 2016/11/5.
 * 记事本添加的item
 */

public class NoteAddItem extends LinearLayout implements
        TextInputEditText.OnFocusChangeListener,
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener,
        RadioGroup.OnCheckedChangeListener{
    private Context context;
    private CheckBox checkBox;
    private TextView number;
    private TextInputEditText editText;
    private ImageView cancel;
    private TextView endTime,startTime,endDate,startDate;
    private TextInputEditText advanceNum;
    private AppCompatSpinner advanceUnit;
    private SwitchCompat allDay,alert;
    private RadioGroup radioGroup;
    private int startYear,startMonth,startDay,startHour,startMinute,endYear,endMonth,endDay,endHour,endMinute;
    private int type = -1;
    private int index = -1;
//    private boolean isChecked = false;
    private int income = 0;
    private OnNoteAddItemClickListener clickListener;
    public static final int LIST = 0;
    public static final int TODO = 1;
    public static final int TEXT = 2;
    public static final int MONEY = 3;
    public static final int TIME = 4;
    public static final int ADDRESS = 5;
    private String[] advanceUnitNames = {"分钟","小时"," 天 "," 周 "};

    public NoteAddItem(Context context,int type) {
        super(context);
        this.context = context;
        init(type);
    }

    public NoteAddItem(Context context) {
        this(context,null);
    }

    public NoteAddItem(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NoteAddItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(TEXT);
    }
    private void init(int t) {
//        LayoutInflater.from(context).inflate(R.layout.item_note_add_check,
//                this, true);
        if(type==t)
            return;
        type = t;
        switch (type){
            case LIST:
                setListType();
                break;
            case TODO:
                setTodoType();
                break;
            case TEXT:
                setTextType();
                break;
            case MONEY:
                setMoneyType();
                break;
            case TIME:
                setTimeType();
                break;
            case ADDRESS:
                setAddressType();
                break;
        }
    }

    private void setTodoType(){
        LayoutInflater.from(context).inflate(R.layout.item_note_add_check,
                this, true);
        editText = (TextInputEditText) findViewById(R.id.item_note_add_text);
        cancel = (ImageView) findViewById(R.id.item_note_add_cancel);
        checkBox = (CheckBox) findViewById(R.id.item_note_add_checkbox);
        checkBox.setOnCheckedChangeListener(this);
        editText.setOnFocusChangeListener(this);
        cancel.setOnClickListener(this);
    }

    private void setListType(){
        LayoutInflater.from(context).inflate(R.layout.item_note_add_list,
                this, true);
        editText = (TextInputEditText) findViewById(R.id.item_note_add_text);
        cancel = (ImageView) findViewById(R.id.item_note_add_cancel);
        number = (TextView) findViewById(R.id.item_note_add_number);
        //将字体文件保存在assets/fonts/目录下，创建Typeface对象
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/century_gothic_std.otf");
        //使用字体
        number.setTypeface(typeFace);
        editText.setOnFocusChangeListener(this);
        cancel.setOnClickListener(this);
    }

    private void setTextType(){
        LayoutInflater.from(context).inflate(R.layout.item_note_add_check,
                this, true);
        editText = (TextInputEditText) findViewById(R.id.item_note_add_text);
        findViewById(R.id.item_note_add_checkbox).setVisibility(View.INVISIBLE);
        cancel = (ImageView) findViewById(R.id.item_note_add_cancel);
        editText.setOnFocusChangeListener(this);
        cancel.setOnClickListener(this);
    }

    private void setMoneyType(){
        LayoutInflater.from(context).inflate(R.layout.item_note_add_money,
                this, true);
        editText = (TextInputEditText) findViewById(R.id.item_note_add_text);
        cancel = (ImageView) findViewById(R.id.item_note_add_cancel);
        radioGroup = (RadioGroup) findViewById(R.id.item_note_add_income);
//        editText.setOnFocusChangeListener(this);
        cancel.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
    }

    private void setTimeType(){
        LayoutInflater.from(context).inflate(R.layout.item_note_add_time,
                this, true);
        endTime = (TextView) findViewById(R.id.item_note_add_end_time);
        startTime = (TextView) findViewById(R.id.item_note_add_start_time);
        endDate = (TextView) findViewById(R.id.item_note_add_end_date);
        startDate = (TextView) findViewById(R.id.item_note_add_start_date);
        advanceNum = (TextInputEditText) findViewById(R.id.item_note_add_advance_num);
        advanceUnit = (AppCompatSpinner) findViewById(R.id.item_note_add_advance_unit);
        allDay = (SwitchCompat) findViewById(R.id.item_note_add_day);
        alert = (SwitchCompat) findViewById(R.id.item_note_add_alert);
        advanceUnit.setAdapter(new ArrayAdapter<>(context,R.layout.item_note_add_time_spinner,R.id.item_note_add_time_spinner_text,advanceUnitNames));
        endTime.setOnClickListener(this);
        startTime.setOnClickListener(this);
        endDate.setOnClickListener(this);
        startDate.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();
        startYear = calendar.get(Calendar.YEAR);
        startMonth = calendar.get(Calendar.MONTH);
        startDay = calendar.get(Calendar.DAY_OF_MONTH);
        startHour = calendar.get(Calendar.HOUR_OF_DAY);
        startMinute = calendar.get(Calendar.MINUTE);
        calendar.set(Calendar.MINUTE,startMinute+15);
        endYear = calendar.get(Calendar.YEAR);
        endMonth = calendar.get(Calendar.MONTH);
        endDay = calendar.get(Calendar.DAY_OF_MONTH);
        endHour = calendar.get(Calendar.HOUR_OF_DAY);
        endMinute = calendar.get(Calendar.MINUTE);
        setDate();
    }

    private void setAddressType(){
        LayoutInflater.from(context).inflate(R.layout.item_note_add_address,
                this, true);
        editText = (TextInputEditText) findViewById(R.id.item_note_add_text);
        cancel = (ImageView) findViewById(R.id.item_note_add_cancel);
        cancel.setOnClickListener(this);
    }

    private void setDate(){
        endDate.setText(endYear+"年"+endMonth+"月"+endDay+"日");
        startDate.setText(startYear+"年"+startMonth+"月"+startDay+"日");
        endTime.setText(endHour+":"+endMinute);
        startTime.setText(startHour+":"+startMinute);
    }

    public void setType(int type) {
        init(type);
    }

    public void setIndex(int index,int maxSize) {
        if(type == LIST){
            this.index = index;
            if(number==null)
                return;
            String text = index+"";
            String max = maxSize+"";
            while(text.length()<max.length()){
                text = "0"+text;
            }
            number.setText(text);
        }
    }

    public void setChecked(boolean isChecked){
        if(type == TODO&&checkBox!=null){
            checkBox.setChecked(isChecked);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_note_add_cancel:
                if(clickListener!=null)
                    clickListener.onClickCancelBtn(this);
                break;
            case R.id.item_note_add_end_date:
                new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                endYear = year;
                                endMonth = monthOfYear;
                                endDay = dayOfMonth;
                                setDate();
                            }
                        },endYear,endMonth,endDay).show();
                break;
            case R.id.item_note_add_end_time:
                new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                endHour = hourOfDay;
                                endMinute = minute;
                                setDate();
                            }
                        },endHour,endMinute,true).show();
                break;
            case R.id.item_note_add_start_date:
                new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                startYear = year;
                                startMonth = monthOfYear;
                                startDay = dayOfMonth;
                                setDate();
                            }
                        },startYear,startMonth,startDay).show();
                break;
            case R.id.item_note_add_start_time:
                new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                startHour = hourOfDay;
                                startMinute = minute;
                                setDate();
                            }
                        },startHour,startMinute,true).show();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.item_note_add_checkbox:
                if(isChecked){
                    editText.getPaint().setStrokeWidth(DensityUtil.dip2px(context,2));
                    editText.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
                    editText.setTextColor(Color.GRAY);
                }else{
                    editText.getPaint().setFlags(0);  // 取消设置的的划线
                    editText.setTextColor(Color.BLACK);

                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.item_note_add_income_in:
                income = 0;
                break;
            case R.id.item_note_add_income_out:
                income = 1;
                break;
        }
    }

    public interface OnNoteAddItemClickListener{
        void onClickCancelBtn(NoteAddItem v);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v.getId()==R.id.item_note_add_text){
            if(hasFocus)
                cancel.setVisibility(View.VISIBLE);
            else
                cancel.setVisibility(View.GONE);
        }
    }

    public void setClickListener(OnNoteAddItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public int getType() {
        return type;
    }
}
