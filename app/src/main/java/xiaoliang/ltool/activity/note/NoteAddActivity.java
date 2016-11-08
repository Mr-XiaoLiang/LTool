package xiaoliang.ltool.activity.note;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import xiaoliang.ltool.R;
import xiaoliang.ltool.activity.LToolApplication;

public class NoteAddActivity extends AppCompatActivity implements View.OnClickListener {

    //时间，地址，金额等固定部分
    private View addressLayout,moneyLayout,timeLayout;
    private TextInputEditText addressEditText,moneyEditText,advanceEditText;
    private View addressCancel,moneyCancel,timeCancel;
    private RadioGroup incomeGroup;
    private TextView startDate,startTime,endDate,endTime;
    //勾选项，内容项，列表项


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_note_add_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }
}
