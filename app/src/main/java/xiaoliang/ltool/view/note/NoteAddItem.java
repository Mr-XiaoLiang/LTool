package xiaoliang.ltool.view.note;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import xiaoliang.ltool.R;
import xiaoliang.ltool.util.DensityUtil;

/**
 * Created by liuj on 2016/11/5.
 * 记事本添加的item
 */

public class NoteAddItem extends LinearLayout implements TextInputEditText.OnFocusChangeListener {
    private Context context;
    private LinearLayout root;
    private LinearLayout title;
    private TextInputEditText editText;
    private ImageView cancel;
    private int type = 0;
    private int index = 0;
    private boolean isChecked = false;
    public static final int LIST = 0;
    public static final int TODO = 1;
    public static final int TEXT = 2;
    public static final int MONEY = 3;
    public static final int TIME = 4;
    public static final int ADDRESS = 5;

    public NoteAddItem(Context context,int type) {
        this(context);
        setType(type);
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
        init();
    }
    private void init() {
//        LayoutInflater.from(context).inflate(R.layout.item_note_add_check,
//                this, true);
//        root = (LinearLayout) findViewById(R.id.item_note_add_root);
//        title = (LinearLayout) findViewById(R.id.item_note_add_title);
//        editText = (TextInputEditText) findViewById(R.id.item_note_add_text);
//        cancel = (ImageView) findViewById(R.id.item_note_add_cancel);
//        editText.setOnFocusChangeListener(this);
//        setType(type);
    }

    public void setType(int type) {
        if(this.type!=type){
            this.type = type;
            switch (type){
                case LIST:
                    setListType();
                    break;
                case TODO:
                    setTodoType();
                    break;
                case TEXT:
                    break;
                case MONEY:
                    break;
                case TIME:
                    break;
                case ADDRESS:
                    break;
            }
        }
    }
    private void setTodoType(){
        CheckBox checkBox = new CheckBox(context);
        checkBox.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                NoteAddItem.this.isChecked = isChecked;
                if(isChecked){
                    editText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG| Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
                }else{
                    editText.getPaint().setFlags(0);  // 取消设置的的划线
                }
            }
        });
        title.removeAllViews();
        title.addView(checkBox);
    }

    private void setListType(){
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setTextSize(DensityUtil.dip2px(context,18));
        //将字体文件保存在assets/fonts/目录下，创建Typeface对象
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/century_gothic_std.TTF");
        //使用字体
        textView.setTypeface(typeFace);
        textView.setTextColor(Color.GRAY);
        title.removeAllViews();
        title.addView(textView);
    }

    public void setIndex(int index,int maxSize) {
        if(this.index != index&&type == LIST){
            this.index = index;
            View v = title.getChildAt(0);
            if(v instanceof TextView){
                String text = index+"";
                String max = maxSize+"";
                while(text.length()<max.length()){
                    text = "0"+text;
                }
                ((TextView)v).setText(index+"");
            }
        }
    }

    public void setChecked(boolean isChecked){
        if(this.isChecked != isChecked&&type == TODO){
            this.isChecked = isChecked;
            View v = title.getChildAt(0);
            if(v instanceof CheckBox){
                ((CheckBox)v).setChecked(isChecked);
            }
        }
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
}
