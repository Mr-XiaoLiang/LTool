package xiaoliang.ltool.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import xiaoliang.ltool.R;

public class QRCreateActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText editText;
    private Button createBtn;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_qr_create_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView(){
        editText = (TextInputEditText) findViewById(R.id.activity_qr_create_edit);
        createBtn = (Button) findViewById(R.id.activity_qr_create_create);
        imageView = (ImageView) findViewById(R.id.activity_qr_create_img);
    }

    /**
     * 创建二维码
     */
    private void createQR(){
        String text = editText.getText().toString();
        if(text==null||text.length()<1){
            editText.setError("请输入内容");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_qr_create_create:
                createQR();
                break;
        }
    }
}
