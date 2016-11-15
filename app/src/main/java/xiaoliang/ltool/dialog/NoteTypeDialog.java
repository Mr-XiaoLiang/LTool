package xiaoliang.ltool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SeekBar;

import xiaoliang.ltool.R;

/**
 * Created by liuj on 2016/11/14.
 * 笔记类型选择
 */

public class NoteTypeDialog extends Dialog implements
        View.OnClickListener,TextWatcher,
        AdapterView.OnItemClickListener{
    public NoteTypeDialog(Context context) {
        super(context);
    }

    private ListView listView;
    private View colorView;
    private SeekBar rebBar,greenBar,blueBar,alphaBar;
    private View doneBtn;
    private TextInputEditText colorEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除屏幕title
        setContentView(R.layout.dialog_note_type);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        listView = (ListView) findViewById(R.id.dialog_note_type_listview);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public interface OnNoteTypeSelectedListener{
        void onNoteTypeSelected(int typeId,int color,String typeName);
    }
}
