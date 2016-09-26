package xiaoliang.ltool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import xiaoliang.ltool.R;

/**
 * Created by liuj on 2016/9/23.
 * 颜色选择的dialog
 */

public class ColorsDialog extends Dialog implements AdapterView.OnItemClickListener,SeekBar.OnSeekBarChangeListener,View.OnClickListener {

    private boolean onlyOne = true;

    //Test

    private TextView leftBtn,rightBtn,editBtn;
    private View show;
    private TextInputEditText editText;
    private SeekBar redBar,greenBar,blueBar;
    private ListView listView;
    private int thisColor = Color.WHITE;
    private ArrayList<Integer> colors;
    private LinearLayout addLayout;
    private OnColorConfirmListener listener;
    private int what = 0;
    private ColorSimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_colors);
        leftBtn = (TextView) findViewById(R.id.dialog_colors_leftbtn);
        rightBtn = (TextView) findViewById(R.id.dialog_colors_rightbtn);
        show = findViewById(R.id.dialog_colors_show);
        editText = (TextInputEditText) findViewById(R.id.dialog_colors_edit);
        redBar = (SeekBar) findViewById(R.id.dialog_colors_red);
        greenBar = (SeekBar) findViewById(R.id.dialog_colors_green);
        blueBar = (SeekBar) findViewById(R.id.dialog_colors_blue);
        listView = (ListView) findViewById(R.id.dialog_colors_list);
        editBtn = (TextView) findViewById(R.id.dialog_colors_btn);
        addLayout = (LinearLayout) findViewById(R.id.dialog_colors_addlayout);
        redBar.setOnSeekBarChangeListener(this);
        greenBar.setOnSeekBarChangeListener(this);
        blueBar.setOnSeekBarChangeListener(this);
        leftBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);
        editBtn.setOnClickListener(this);
        if(onlyOne){
            addLayout.setVisibility(View.VISIBLE);
            initView();
        }else{
            addLayout.setVisibility(View.GONE);
            initList();
        }
    }


    private void initView(){
        int r = Color.red(thisColor);
        int g = Color.green(thisColor);
        int b = Color.blue(thisColor);
        redBar.setProgress(r);
        greenBar.setProgress(g);
        blueBar.setProgress(b);
        show.setBackgroundColor(thisColor);
        editText.setText(Integer.toHexString(thisColor).substring(2));
    }

    private void initList(){
        if(colors==null)
            return;
        if(adapter==null){
            adapter = new ColorSimpleAdapter(getContext());
            listView.setAdapter(adapter);
            listView.invalidate();
        }
        adapter.notifyDataSetChanged();
    }

    private ColorsDialog(Context context,boolean one) {
        super(context);
        this.onlyOne = one;
    }

    public ColorsDialog(Context context,int color,int what,OnColorConfirmListener listener) {
        this(context,true);
        this.thisColor = color;
        this.listener = listener;
        this.what = what;
    }

    public ColorsDialog(Context context,ArrayList<Integer> color,int what,OnColorConfirmListener listener) {
        this(context,false);
        this.colors = color;
        this.listener = listener;
        this.what = what;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        thisColor = colors.get(position);
        addLayout.setVisibility(View.VISIBLE);
        initView();
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(!fromUser)
            return;
        switch (seekBar.getId()){
            case R.id.dialog_colors_blue:
                thisColor = Color.rgb(Color.red(thisColor),Color.green(thisColor),progress);
                break;
            case R.id.dialog_colors_red:
                thisColor = Color.rgb(progress,Color.green(thisColor),Color.blue(thisColor));
                break;
            case R.id.dialog_colors_green:
                thisColor = Color.rgb(Color.red(thisColor),progress,Color.blue(thisColor));
                break;
        }
        initView();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_colors_rightbtn:
                if(onlyOne){
                    if(listener!=null)
                        listener.onConfirmOne(what,thisColor);
                    dismiss();
                }else{
                    if(addLayout.isShown()){
                        colors.add(thisColor);
                        addLayout.setVisibility(View.GONE);
                        leftBtn.setText("添加");
                        initList();
                    }else{
                        if(listener!=null)
                            listener.onConfirmArray(what,colors);
                        dismiss();
                    }
                }
                break;
            case R.id.dialog_colors_leftbtn:
                if(onlyOne){
                    dismiss();
                }else{
                    if(addLayout.isShown()){
                        addLayout.setVisibility(View.GONE);
                        leftBtn.setText("添加");
                    }else{
                        leftBtn.setText("取消");
                        if(colors==null)
                            colors = new ArrayList<>();
                        addLayout.setVisibility(View.VISIBLE);
                        initView();
                    }
                }
                break;
            case R.id.dialog_colors_btn:
                String str = editText.getText().toString().replaceAll("[^(0-9 | a-f | A-F)]","");
                if(str.length()==6){
                    thisColor = Color.parseColor("#"+str);
                    initView();
                }else{
                    editText.setError("请输入正确的色值");
                }
                break;
        }
    }

    private class ColorSimpleAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return colors.size();
        }

        @Override
        public Integer getItem(int position) {
            return colors.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

        /**构造函数*/
        public ColorSimpleAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            //观察convertView随ListView滚动情况
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_color,null);
                holder = new ViewHolder();
                /**得到各个控件的对象*/
                holder.del = convertView.findViewById(R.id.item_color_del);
                holder.show = convertView.findViewById(R.id.item_color_col);
                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            holder.show.setBackgroundColor(getItem(position));
            /**为Button添加点击事件*/
            holder.del.setOnClickListener(new OnColorClickListener(position));
            return convertView;
        }
        /**存放控件*/
        public final class ViewHolder{
            public View del;
            public View show;
        }
    }

    private class OnColorClickListener implements View.OnClickListener{
        final int position;

        public OnColorClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            colors.remove(position);
            initList();
        }

    }

    public interface OnColorConfirmListener{
        void onConfirmOne(int what,int color);
        void onConfirmArray(int what,ArrayList<Integer> color);
    }

}
