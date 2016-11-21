package xiaoliang.ltool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.NoteTypeBean;
import xiaoliang.ltool.util.DatabaseHelper;
import xiaoliang.ltool.util.OtherUtil;
import xiaoliang.ltool.util.TextToColor;
import xiaoliang.ltool.util.ToastUtil;
import xiaoliang.ltool.view.DotDrawable;

/**
 * Created by liuj on 2016/11/14.
 * 笔记类型选择
 */

public class NoteTypeDialog extends Dialog implements
        View.OnClickListener,TextWatcher,
        AdapterView.OnItemClickListener,SeekBar.OnSeekBarChangeListener{
    public NoteTypeDialog(Context context) {
        super(context);
    }

    private ListView listView;
    private View colorView;
    private SeekBar redBar,greenBar,blueBar,alphaBar;
    private TextInputEditText colorEditText;
    private int r = 0;
    private int a = 255;
    private int g = 0;
    private int b = 0;
    private int color = Color.BLACK;
    private View addLayout;
    private ArrayList<NoteTypeBean> typeBeens;
    private TypeAdapter adapter;
    private OnNoteTypeSelectedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除屏幕title
        setContentView(R.layout.dialog_note_type);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        listView = (ListView) findViewById(R.id.dialog_note_type_listview);
        colorView = findViewById(R.id.dialog_note_type_show);
        redBar = (SeekBar) findViewById(R.id.dialog_note_type_red);
        greenBar = (SeekBar) findViewById(R.id.dialog_note_type_green);
        blueBar = (SeekBar) findViewById(R.id.dialog_note_type_blue);
        alphaBar = (SeekBar) findViewById(R.id.dialog_note_type_alpha);
        colorEditText = (TextInputEditText) findViewById(R.id.dialog_note_type_edit);
        addLayout = findViewById(R.id.dialog_note_type_addlayout);
        findViewById(R.id.dialog_note_type_done).setOnClickListener(this);
        findViewById(R.id.dialog_note_type_cancel).setOnClickListener(this);
        redBar.setOnSeekBarChangeListener(this);
        greenBar.setOnSeekBarChangeListener(this);
        blueBar.setOnSeekBarChangeListener(this);
        alphaBar.setOnSeekBarChangeListener(this);
        colorEditText.addTextChangedListener(this);
        adapter = new TypeAdapter(getContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        addLayout.setVisibility(View.GONE);
        getDate();
    }

    private void getDate(){
        if(typeBeens==null)
            typeBeens = new ArrayList<>();
        typeBeens.clear();
        typeBeens.add(new NoteTypeBean(-1,Color.GRAY,"添加类型"));
        typeBeens.addAll(DatabaseHelper.selectNoteType(getContext()));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_note_type_done:
                String name = colorEditText.getText().toString().trim();
                if(!TextUtils.isEmpty(name)){
                    long l = DatabaseHelper.addNoteType(getContext(),color,name);
                    if(l>0){
                        addLayout.setVisibility(View.GONE);
                        getDate();
                    }else{
                        ToastUtil.T(getContext(),"添加失败");
                    }
                }else{
                    colorEditText.setError("请输入名称");
                }
                break;
            case R.id.dialog_note_type_cancel:
                addLayout.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(addLayout.getVisibility()==View.VISIBLE){
            addLayout.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position>0){
            if(listener!=null){
                listener.onNoteTypeSelected(typeBeens.get(position).id,typeBeens.get(position).color,typeBeens.get(position).typeName);
            }
            dismiss();
        }else{
            addLayout.setVisibility(View.VISIBLE);
        }
    }

    public void setOnNoteTypeSelectedListener(OnNoteTypeSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String text = s.toString().trim();
        int color;
        if(TextUtils.isEmpty(text)){
            color = Color.BLACK;
        }else{
            colorEditText.setError(null);
            color = TextToColor.format(text);
        }
        if(Build.VERSION.SDK_INT>24){
            alphaBar.setProgress(Color.alpha(color),true);
            greenBar.setProgress(Color.green(color),true);
            redBar.setProgress(Color.red(color),true);
            blueBar.setProgress(Color.blue(color),true);
        }else{
            alphaBar.setProgress(Color.alpha(color));
            greenBar.setProgress(Color.green(color));
            redBar.setProgress(Color.red(color));
            blueBar.setProgress(Color.blue(color));
        }
    }
    @Override
    public void afterTextChanged(Editable s) {}

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.dialog_note_type_red:
                r = progress;
                break;
            case R.id.dialog_note_type_green:
                g = progress;
                break;
            case R.id.dialog_note_type_blue:
                b = progress;
                break;
            case R.id.dialog_note_type_alpha:
                a = progress;
                break;
        }
        color = Color.argb(a,r,g,b);
        colorView.setBackgroundColor(color);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    public interface OnNoteTypeSelectedListener{
        void onNoteTypeSelected(int typeId,int color,String typeName);
    }
    private class TypeAdapter extends BaseAdapter{

        private LayoutInflater inflater;

        public TypeAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if(typeBeens==null)
                return 0;
            return typeBeens.size();
        }

        @Override
        public Object getItem(int position) {
            if(typeBeens==null)
                return null;
            return typeBeens.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if(convertView==null){
                convertView = inflater.inflate(R.layout.item_note_type,parent,false);
                holder = new Holder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (Holder) convertView.getTag();
            }
            holder.onBind(typeBeens.get(position));
            return convertView;
        }
    }
    private class Holder{
        private TextView name;
        private ImageView color;
        private DotDrawable dotDrawable;

        public Holder(View item) {
            name = (TextView) item.findViewById(R.id.item_note_type_name);
            color = (ImageView) item.findViewById(R.id.item_note_type_img);
            dotDrawable = new DotDrawable();
        }

        private void onBind(NoteTypeBean bean){
            if(bean.id<0){
//                if(Build.VERSION.SDK_INT>21){
//                    color.setImageResource(R.drawable.ic_add);
//                    color.setImageTintList(ColorStateList.valueOf(bean.color));
//                }else{
//                }
                color.setImageDrawable(OtherUtil.tintDrawable(getContext(),R.drawable.ic_add,bean.color));
            }else{
                dotDrawable.setColor(bean.color);
                color.setImageDrawable(dotDrawable);
            }
            name.setText(bean.typeName);
        }
    }
}
