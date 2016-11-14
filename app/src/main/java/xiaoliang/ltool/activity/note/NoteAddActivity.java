package xiaoliang.ltool.activity.note;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

import xiaoliang.ltool.R;
import xiaoliang.ltool.util.OtherUtil;
import xiaoliang.ltool.view.note.NoteAddItem;

/**
 * 添加一份笔记，并且也是回显笔记，修改笔记的页面
 * @author Liuj
 */
public class NoteAddActivity extends AppCompatActivity implements View.OnClickListener,NoteAddItem.OnNoteAddItemClickListener {

    public static final String ARG_NOTE_ID = "ARG_NOTE_ID";

    //时间，地址，金额等固定部分
    private View addressLayout,moneyLayout,timeLayout;
    private TextInputEditText addressEditText,moneyEditText,advanceEditText;
//    private View addressCancel,moneyCancel,timeCancel;
    private RadioGroup incomeGroup;
    private TextView startDate,startTime,endDate,endTime;
    private int startYear,startMonth,startDay,startHour,startMinute,endYear,endMonth,endDay,endHour,endMinute;
    //勾选项，内容项，列表项
    private ArrayList<NoteAddItem> noteItems;
    private int itemType = -1;
    private LinearLayout body;
    //6个按钮（此处获取仅仅是为了修改按钮颜色，点击事件的监听并不依靠对象）
    private ImageView checkListBtn,numberListBtn,textListBtn,moneyBtn,addressBtn,advanceBtn;
    //回显/修改
    private int noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_note_add_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init(){
        addressLayout = findViewById(R.id.content_note_add_address_layout);
        moneyLayout = findViewById(R.id.content_note_add_money_layout);
        timeLayout = findViewById(R.id.content_note_add_alert_layout);
        addressEditText = (TextInputEditText) findViewById(R.id.content_note_add_address);
        moneyEditText = (TextInputEditText) findViewById(R.id.content_note_add_money);
        advanceEditText = (TextInputEditText) findViewById(R.id.content_note_add_alert_advance_num);
        incomeGroup = (RadioGroup) findViewById(R.id.content_note_add_money_income);
        startDate = (TextView) findViewById(R.id.content_note_add_alert_start_date);
        startTime = (TextView) findViewById(R.id.content_note_add_alert_start_time);
        endDate = (TextView) findViewById(R.id.content_note_add_alert_end_date);
        endTime = (TextView) findViewById(R.id.content_note_add_alert_end_time);
        body = (LinearLayout) findViewById(R.id.content_note_add_body);
        checkListBtn = (ImageView) findViewById(R.id.content_note_add_checklist_btn);
        numberListBtn = (ImageView) findViewById(R.id.content_note_add_numlist_btn);
        textListBtn = (ImageView) findViewById(R.id.content_note_add_text_btn);
        moneyBtn = (ImageView) findViewById(R.id.content_note_add_money_btn);
        addressBtn = (ImageView) findViewById(R.id.content_note_add_address_btn);
        advanceBtn = (ImageView) findViewById(R.id.content_note_add_time_btn);
        noteItems = new ArrayList<>();
        addressBtn.setImageDrawable(getDrawable(R.drawable.ic_edit_location,false));
        moneyBtn.setImageDrawable(getDrawable(R.drawable.ic_attach_money,false));
        advanceBtn.setImageDrawable(getDrawable(R.drawable.ic_query_builder,false));
        textListBtn.setImageDrawable(getDrawable(R.drawable.ic_subject,false));
        checkListBtn.setImageDrawable(getDrawable(R.drawable.ic_format_list_bulleted,false));
        numberListBtn.setImageDrawable(getDrawable(R.drawable.ic_format_list_numbered,false));
        setItemType(NoteAddItem.TEXT);
        Intent intent = getIntent();
        noteId = intent.getIntExtra(ARG_NOTE_ID,-1);
        if(noteId<0){
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
            addItem();
        }else{
            //TODO 获取数据库数据
        }
    }

    @Override
    protected void onDestroy() {
        //TODO 提交数据
        super.onDestroy();
    }

    private void addItem(){
        NoteAddItem noteAddItem = new NoteAddItem(this,itemType);
        noteAddItem.setClickListener(this);
        noteItems.add(noteAddItem);
        body.addView(noteAddItem);
        updateNumberList();
    }

    private void updateNumberList(){
        //刷新序号
        if(itemType==NoteAddItem.LIST){
            int num = 0;
            int index = 1;
            for(NoteAddItem item:noteItems){//循环一遍拿到总数量
                if(item.getType()==NoteAddItem.LIST)
                    num++;
            }
            for(NoteAddItem item:noteItems){//第二遍再来全部修改序号
                if(item.getType()==NoteAddItem.LIST)
                    item.setIndex(index++,num);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_add, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_note_add_share:
                //TODO 分享
                break;
            case R.id.menu_note_add_done:
                //TODO 提交
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.content_note_add_alert_start_date:
                new DatePickerDialog(this,
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
            case R.id.content_note_add_alert_start_time:
                new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                startHour = hourOfDay;
                                startMinute = minute;
                                setDate();
                            }
                        },startHour,startMinute,true).show();
                break;
            case R.id.content_note_add_alert_end_date:
                new DatePickerDialog(this,
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
            case R.id.content_note_add_alert_end_time:
                new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                endHour = hourOfDay;
                                endMinute = minute;
                                setDate();
                            }
                        },endHour,endMinute,true).show();
                break;
            case R.id.content_note_add_address_cancel:
                if(addressLayout.getVisibility()==View.VISIBLE){
                    addressLayout.setVisibility(View.GONE);
                    addressBtn.setImageDrawable(getDrawable(R.drawable.ic_edit_location,false));
                }
                break;
            case R.id.content_note_add_additem:
                addItem();
                break;
            case R.id.content_note_add_money_cancel:
                if(moneyLayout.getVisibility()==View.VISIBLE){
                    moneyLayout.setVisibility(View.GONE);
                    moneyBtn.setImageDrawable(getDrawable(R.drawable.ic_attach_money,false));
                }
                break;
            case R.id.content_note_add_alert_cancel:
                if(timeLayout.getVisibility()==View.VISIBLE){
                    timeLayout.setVisibility(View.GONE);
                    advanceBtn.setImageDrawable(getDrawable(R.drawable.ic_query_builder,false));
                }
                break;
            case R.id.content_note_add_checklist_btn:
                setItemType(NoteAddItem.TODO);
                break;
            case R.id.content_note_add_numlist_btn:
                setItemType(NoteAddItem.LIST);
                break;
            case R.id.content_note_add_text_btn:
                setItemType(NoteAddItem.TEXT);
                break;
            case R.id.content_note_add_money_btn:
                if(moneyLayout.getVisibility()==View.GONE)
                    moneyLayout.setVisibility(View.VISIBLE);
                moneyEditText.requestFocus();
                setItemType(NoteAddItem.MONEY);
                break;
            case R.id.content_note_add_time_btn:
                if(timeLayout.getVisibility()==View.GONE)
                    timeLayout.setVisibility(View.VISIBLE);
                advanceEditText.requestFocus();
                setItemType(NoteAddItem.TIME);
                break;
            case R.id.content_note_add_address_btn:
                if(addressLayout.getVisibility()==View.GONE)
                    addressLayout.setVisibility(View.VISIBLE);
                addressEditText.requestFocus();
                setItemType(NoteAddItem.ADDRESS);
                break;
        }
    }

    private void setItemType(int type){
        if(itemType!=type){
            switch (type){
                case NoteAddItem.ADDRESS:
                    addressBtn.setImageDrawable(getDrawable(R.drawable.ic_edit_location,true));
                    break;
                case NoteAddItem.TEXT:
                    closeBtnColor();
                    textListBtn.setImageDrawable(getDrawable(R.drawable.ic_subject,true));
                    itemType=type;
                    break;
                case NoteAddItem.TODO:
                    closeBtnColor();
                    checkListBtn.setImageDrawable(getDrawable(R.drawable.ic_format_list_bulleted,true));
                    itemType=type;
                    break;
                case NoteAddItem.LIST:
                    closeBtnColor();
                    numberListBtn.setImageDrawable(getDrawable(R.drawable.ic_format_list_numbered,true));
                    itemType=type;
                    break;
                case NoteAddItem.TIME:
                    advanceBtn.setImageDrawable(getDrawable(R.drawable.ic_query_builder,true));
                    break;
                case NoteAddItem.MONEY:
                    moneyBtn.setImageDrawable(getDrawable(R.drawable.ic_attach_money,true));
                    break;
            }
        }
    }

    private void closeBtnColor(){
        switch (itemType){
            case NoteAddItem.TEXT:
                textListBtn.setImageDrawable(getDrawable(R.drawable.ic_subject,false));
                break;
            case NoteAddItem.TODO:
                checkListBtn.setImageDrawable(getDrawable(R.drawable.ic_format_list_bulleted,false));
                break;
            case NoteAddItem.LIST:
                numberListBtn.setImageDrawable(getDrawable(R.drawable.ic_format_list_numbered,false));
                break;
        }
    }

    private Drawable getDrawable(int draw, boolean show){
        int color = Color.GRAY;
        if(show)
            color = getResources().getColor(R.color.colorAccent);
        return OtherUtil.tintDrawable(
                getResources().getDrawable(draw),
                ColorStateList.valueOf(color));
    }

    private void setDate(){
        endDate.setText(endYear+"年"+endMonth+"月"+endDay+"日");
        startDate.setText(startYear+"年"+startMonth+"月"+startDay+"日");
        endTime.setText(endHour+":"+endMinute);
        startTime.setText(startHour+":"+startMinute);
    }

    @Override
    public void onClickCancelBtn(NoteAddItem v) {
        int index = body.indexOfChild(v);
        body.removeView(v);
        noteItems.remove(index);
        updateNumberList();
    }
}
