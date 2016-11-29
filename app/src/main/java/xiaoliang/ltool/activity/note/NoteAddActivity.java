package xiaoliang.ltool.activity.note;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import xiaoliang.ltool.R;
import xiaoliang.ltool.adapter.NoteAddAdapter;
import xiaoliang.ltool.bean.NoteAddBean;
import xiaoliang.ltool.dialog.AdvanceNumDialog;
import xiaoliang.ltool.dialog.LoadDialog;
import xiaoliang.ltool.dialog.NoteTypeDialog;
import xiaoliang.ltool.listener.LItemTouchCallback;
import xiaoliang.ltool.listener.LItemTouchHelper;
import xiaoliang.ltool.util.DatabaseHelper;
import xiaoliang.ltool.util.DialogUtil;
import xiaoliang.ltool.util.NoteUtil;
import xiaoliang.ltool.util.OtherUtil;
import xiaoliang.ltool.view.DotDrawable;

/**
 * 添加一份笔记，并且也是回显笔记，修改笔记的页面
 * @author Liuj
 */
public class NoteAddActivity extends AppCompatActivity implements
        View.OnClickListener,NoteTypeDialog.OnNoteTypeSelectedListener,
        LItemTouchCallback.OnItemTouchCallbackListener,
        NoteUtil.GetNoteDetailCallback,NoteUtil.SaveNoteDetailCallback{

    public static final String ARG_NOTE_ID = "ARG_NOTE_ID";

    private int itemType = -1;
    private TextInputEditText titleEdit;
    //6个按钮（此处获取仅仅是为了修改按钮颜色，点击事件的监听并不依靠对象）
    private ImageView checkListBtn,numberListBtn,textListBtn,moneyBtn,addressBtn,advanceBtn;
    //笔记类型
    private int noteTypeId = -1;
//    private ImageView noteTypeColor;
    private DotDrawable dotDrawable;
    //回显/修改
    private int noteId = -1;
    private RecyclerView recyclerView;
    private ArrayList<NoteAddBean> noteAddBeans;
    private NoteAddAdapter adapter;
    private int maxPosition = 0;
    private Calendar calendar;
    private LoadDialog dialog;
    private boolean isDestroy = false;

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
        checkListBtn = (ImageView) findViewById(R.id.content_note_add_checklist_btn);
        numberListBtn = (ImageView) findViewById(R.id.content_note_add_numlist_btn);
        textListBtn = (ImageView) findViewById(R.id.content_note_add_text_btn);
        moneyBtn = (ImageView) findViewById(R.id.content_note_add_money_btn);
        addressBtn = (ImageView) findViewById(R.id.content_note_add_address_btn);
        advanceBtn = (ImageView) findViewById(R.id.content_note_add_time_btn);
        advanceBtn.setVisibility(View.GONE);
        titleEdit = (TextInputEditText) findViewById(R.id.content_note_add_title);
        ImageView noteTypeColor = (ImageView) findViewById(R.id.content_note_add_color);
        recyclerView = (RecyclerView) findViewById(R.id.content_note_add_recyclerview);
        noteTypeColor.setImageDrawable(dotDrawable = new DotDrawable(this));
        textListBtn.setImageDrawable(getDrawable(R.drawable.ic_subject,false));
        checkListBtn.setImageDrawable(getDrawable(R.drawable.ic_format_list_bulleted,false));
        numberListBtn.setImageDrawable(getDrawable(R.drawable.ic_format_list_numbered,false));
        addressBtn.setImageDrawable(getDrawable(R.drawable.ic_edit_location,false));
        advanceBtn.setImageDrawable(getDrawable(R.drawable.ic_query_builder,false));
        moneyBtn.setImageDrawable(getDrawable(R.drawable.ic_attach_money,false));
        setItemType(NoteAddBean.TEXT);
        Intent intent = getIntent();
        noteId = intent.getIntExtra(ARG_NOTE_ID,-1);
        //初始化列表
        noteAddBeans = new ArrayList<>();//初始化数据集
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//初始化列表layout管理器
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设定为纵向
        recyclerView.setLayoutManager(linearLayoutManager);//将管理器设置进列表
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置列表item动画
        LItemTouchHelper helper = LItemTouchHelper.newInstance(recyclerView,this);//设置控制帮助类
        adapter = new NoteAddAdapter(this,noteAddBeans,helper);//初始化列表适配器
        recyclerView.setAdapter(adapter);//为列表设置适配器
        if(noteId<0){
            noteAddBeans.add(new NoteAddBean(NoteAddBean.TEXT));
            noteAddBeans.add(new NoteAddBean(NoteAddBean.ADDITEM));
            adapter.notifyDataSetChanged();//通知适配器数据变化
        }else{
            getNote();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_add, menu);
        menu.findItem(R.id.menu_note_add_share).setVisible(false);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_note_add_delete:
                DatabaseHelper.deleteNote(this,noteId);
                finish();
                break;
            case R.id.menu_note_add_share:
                //TODO 分享
                break;
            case R.id.menu_note_add_done:
                saveNote();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.content_note_add_checklist_btn:
                setItemType(NoteAddBean.TODO);
                break;
            case R.id.content_note_add_numlist_btn:
                setItemType(NoteAddBean.LIST);
                break;
            case R.id.content_note_add_text_btn:
                setItemType(NoteAddBean.TEXT);
                break;
            case R.id.content_note_add_money_btn:
                setItemType(NoteAddBean.MONEY);
                break;
            case R.id.content_note_add_time_btn:
                setItemType(NoteAddBean.TIME);
                break;
            case R.id.content_note_add_address_btn:
                setItemType(NoteAddBean.ADDRESS);
                break;
            case R.id.content_note_add_color:
                DialogUtil.getNoteTypeDialog(this,this);
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        isDestroy = true;
//        saveNote();
        super.onBackPressed();
    }

    private void setItemType(int type){
        if(itemType!=type){
            switch (type){
                case NoteAddBean.ADDRESS:
                    addressBtn.setImageDrawable(getDrawable(R.drawable.ic_edit_location,true));
                    addItem(type,0);
                    break;
                case NoteAddBean.TEXT:
                    closeBtnColor();
                    textListBtn.setImageDrawable(getDrawable(R.drawable.ic_subject,true));
                    itemType=type;
                    break;
                case NoteAddBean.TODO:
                    closeBtnColor();
                    checkListBtn.setImageDrawable(getDrawable(R.drawable.ic_format_list_bulleted,true));
                    itemType=type;
                    break;
                case NoteAddBean.LIST:
                    closeBtnColor();
                    numberListBtn.setImageDrawable(getDrawable(R.drawable.ic_format_list_numbered,true));
                    itemType=type;
                    break;
                case NoteAddBean.TIME:
                    advanceBtn.setImageDrawable(getDrawable(R.drawable.ic_query_builder,true));
                    addItem(type,0);
                    break;
                case NoteAddBean.MONEY:
                    moneyBtn.setImageDrawable(getDrawable(R.drawable.ic_attach_money,true));
                    addItem(type,0);
                    break;
            }
        }
    }

    private void closeBtnColor(){
        switch (itemType){
            case NoteAddBean.TEXT:
                textListBtn.setImageDrawable(getDrawable(R.drawable.ic_subject,false));
                break;
            case NoteAddBean.TODO:
                checkListBtn.setImageDrawable(getDrawable(R.drawable.ic_format_list_bulleted,false));
                break;
            case NoteAddBean.LIST:
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


    @Override
    public void onNoteTypeSelected(int typeId, int color, String typeName) {
        noteTypeId = typeId;
        dotDrawable.setColor(color);
    }

    @Override
    public void onSwiped(int adapterPosition) {
        if(noteAddBeans!=null){
            boolean numChanged = false;
            switch (noteAddBeans.get(adapterPosition).type){
                case NoteAddBean.ADDRESS:
                    addressBtn.setImageDrawable(getDrawable(R.drawable.ic_edit_location,false));
                    break;
                case NoteAddBean.LIST:
                    numChanged = true;
                    break;
                case NoteAddBean.TIME:
                    advanceBtn.setImageDrawable(getDrawable(R.drawable.ic_query_builder,false));
                    break;
                case NoteAddBean.MONEY:
                    moneyBtn.setImageDrawable(getDrawable(R.drawable.ic_attach_money,false));
                    break;
            }
            noteAddBeans.remove(adapterPosition);
            if(adapter!=null){
                adapter.notifyItemRemoved(adapterPosition);
                if(numChanged)
                    onDataChange();
            }
        }
    }

    @Override
    public boolean onMove(int srcPosition, int targetPosition) {
        if(noteAddBeans!=null&&maxPosition>targetPosition){
            // 更换数据源中的数据Item的位置
            Collections.swap(noteAddBeans, srcPosition, targetPosition);
            if(adapter!=null){
                // 更新UI中的Item的位置，主要是给用户看到交互效果
                adapter.notifyItemMoved(srcPosition, targetPosition);
                //判断是否有序号的变化，如果有，那就变化
                if(noteAddBeans.get(srcPosition).type==NoteAddBean.LIST&&noteAddBeans.get(srcPosition).type==noteAddBeans.get(targetPosition).type){
                    //交换他们之间的序号
                    int index = noteAddBeans.get(srcPosition).index;
                    noteAddBeans.get(srcPosition).index = noteAddBeans.get(targetPosition).index;
                    noteAddBeans.get(targetPosition).index = index;
                    //通知适配器，这两个item变化了
                    adapter.notifyItemChanged(srcPosition);
                    adapter.notifyItemChanged(targetPosition);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void onItemViewClick(RecyclerView.ViewHolder holder, View v) {
        int year,month,day,hour,minute;
        final int index = holder.getAdapterPosition();
        switch (v.getId()){
            case R.id.item_note_add_additem:
                addItem(itemType,holder.getAdapterPosition());
                break;
            case R.id.item_note_add_start_date:
                if(calendar==null)
                    calendar = Calendar.getInstance();
                calendar.setTimeInMillis(noteAddBeans.get(index).startTime);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                calendar.set(Calendar.YEAR,year);
                                calendar.set(Calendar.MONTH,monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                noteAddBeans.get(index).startTime = calendar.getTimeInMillis();
                                if(noteAddBeans.get(index).startTime>noteAddBeans.get(index).endTime)
                                    noteAddBeans.get(index).endTime = noteAddBeans.get(index).startTime;
                                adapter.notifyItemChanged(index);
                            }
                        },year,month,day).show();
                break;
            case R.id.item_note_add_start_time:
                if(calendar==null)
                    calendar = Calendar.getInstance();
                calendar.setTimeInMillis(noteAddBeans.get(index).startTime);
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);
                new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                calendar.set(Calendar.MINUTE,minute);
                                noteAddBeans.get(index).startTime = calendar.getTimeInMillis();
                                if(noteAddBeans.get(index).startTime>noteAddBeans.get(index).endTime)
                                    noteAddBeans.get(index).endTime = noteAddBeans.get(index).startTime;
                                adapter.notifyItemChanged(index);
                            }
                        },hour,minute,true).show();
                break;
            case R.id.item_note_add_end_date:
                if(calendar==null)
                    calendar = Calendar.getInstance();
                calendar.setTimeInMillis(noteAddBeans.get(index).endTime);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                calendar.set(Calendar.YEAR,year);
                                calendar.set(Calendar.MONTH,monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                noteAddBeans.get(index).endTime = calendar.getTimeInMillis();
                                if(noteAddBeans.get(index).startTime>noteAddBeans.get(index).endTime)
                                    noteAddBeans.get(index).startTime = noteAddBeans.get(index).endTime;
                                adapter.notifyItemChanged(index);
                            }
                        },year,month,day).show();
                break;
            case R.id.item_note_add_end_time:
                if(calendar==null)
                    calendar = Calendar.getInstance();
                calendar.setTimeInMillis(noteAddBeans.get(index).endTime);
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);
                new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                calendar.set(Calendar.MINUTE,minute);
                                noteAddBeans.get(index).endTime = calendar.getTimeInMillis();
                                if(noteAddBeans.get(index).startTime>noteAddBeans.get(index).endTime)
                                    noteAddBeans.get(index).startTime = noteAddBeans.get(index).endTime;
                                adapter.notifyItemChanged(index);
                            }
                        },hour,minute,true).show();
                break;
            case R.id.item_note_add_advance_num:
                DialogUtil.getAdvanceNumDialog(
                        this, noteAddBeans.get(index).advance,
                        noteAddBeans.get(index).advanceUnit,
                        new AdvanceNumDialog.OnAdvanceNumChangeListener() {
                    @Override
                    public void onAdvanceNumChange(long num, int unit) {
                        noteAddBeans.get(index).advance = num;
                        noteAddBeans.get(index).advanceUnit = unit;
                        adapter.notifyItemChanged(index);
                    }
                });
                break;
        }
    }

    private void addItem(int type,int addIndex){
        NoteAddBean bean = new NoteAddBean(type);
        switch (type){
            case NoteAddBean.ADDRESS:
            case NoteAddBean.TIME:
                bean.endTime = bean.startTime = System.currentTimeMillis();
            case NoteAddBean.MONEY:
                if(!noteAddBeans.contains(bean)){//存在就不再添加了
                    noteAddBeans.add(bean);
                    adapter.notifyItemInserted(noteAddBeans.indexOf(bean));
                }
                break;
            case NoteAddBean.TEXT:
            case NoteAddBean.LIST:
            case NoteAddBean.TODO:
                noteAddBeans.add(addIndex,bean);
                adapter.notifyItemInserted(addIndex);
                maxPosition = ++addIndex;
                onDataChange();
                break;

        }
    }

    /**
     * 当数据发生变化
     * 主要是为了编号item
     */
    private void onDataChange(){
        int max = 0;
        int index = 1;
        for(NoteAddBean bean:noteAddBeans){
            if(bean!=null&&bean.type==NoteAddBean.LIST){
                max++;
            }
        }
        for(int i = 0;i<noteAddBeans.size();i++){
            NoteAddBean bean = noteAddBeans.get(i);
            if(bean!=null&&bean.type==NoteAddBean.LIST){
                bean.index = index++;
                bean.maxIndex = max;
                adapter.notifyItemChanged(i);
            }
        }
    }

    private void getNote(){
        dialog = DialogUtil.getLoadDialog(this);
        NoteUtil.getNoteDetail(this,noteId,this);
    }

    private void saveNote(){
        String title = titleEdit.getText().toString().trim();
        if(TextUtils.isEmpty(title)){
            titleEdit.setError("请输入标题");
            titleEdit.requestFocus();
            return;
        }else{
            titleEdit.setError(null);
        }
        if(noteTypeId<0){
            Snackbar.make(recyclerView,"请选择标签类型",Snackbar.LENGTH_SHORT)
                    .setAction("选择", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DialogUtil.getNoteTypeDialog(NoteAddActivity.this,NoteAddActivity.this);
                        }
                    }).show();
            return;
        }
        dialog = DialogUtil.getLoadDialog(this);
        NoteUtil.saveNoteDetail(this,noteId,title,noteTypeId,noteAddBeans,this);
    }

    @Override
    public void onGetNoteCallback(int id, String title, int typeId, int typeColor, ArrayList<NoteAddBean> beans) {
        if(dialog!=null)
            dialog.dismiss();
        this.noteId = id;
        this.noteTypeId = typeId;
        dotDrawable.setColor(typeColor);
        titleEdit.setText(title);
        noteAddBeans.clear();
        noteAddBeans.addAll(beans);
        if(noteAddBeans.contains(new NoteAddBean(NoteAddBean.MONEY))){
            moneyBtn.callOnClick();
        }
        if(noteAddBeans.contains(new NoteAddBean(NoteAddBean.ADDRESS))){
            addressBtn.callOnClick();
        }
        if(noteAddBeans.contains(new NoteAddBean(NoteAddBean.TIME))){
            advanceBtn.callOnClick();
        }
        adapter.notifyDataSetChanged();
        onDataChange();
    }

    @Override
    public void onSaveNoteCallback(int i) {
        if(dialog!=null)
            dialog.dismiss();
        String toast;
        if(noteId>0){
            if(i>0)
                toast = "数据已更新";
            else
                toast = "数据更新失败";
        }else{
            noteId = i;
            if(i>0)
                toast = "笔记已保存";
            else
                toast = "笔记保存失败";
        }
        Snackbar.make(recyclerView,toast,Snackbar.LENGTH_SHORT).show();
        if(isDestroy){
            finish();
        }
    }
}
