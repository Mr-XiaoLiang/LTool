package xiaoliang.ltool.activity.webcode;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import xiaoliang.ltool.R;
import xiaoliang.ltool.dialog.LoadDialog;
import xiaoliang.ltool.util.DialogUtil;
import xiaoliang.ltool.util.HttpTaskRunnable;
import xiaoliang.ltool.util.HttpUtil;
import xiaoliang.ltool.util.NetTasks;
import xiaoliang.ltool.util.OtherUtil;
import xiaoliang.ltool.view.LBackDrawable;

public class WebCodeActivity extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener,TextWatcher,TextView.OnEditorActionListener {

    private LBackDrawable backDrawable;
    private ValueAnimator openAnimator,offAnimator;
    private boolean isOpen = false;
    private TextInputEditText editText;
    private TextView body;
    private LoadDialog loadDialog;
    private DateFormat simpleDateFormat;
    private boolean showBtn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_code);
        backDrawable = new LBackDrawable(this);
        backDrawable.setColor(Color.GRAY);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_web_code_toolbar);
        toolbar.setNavigationIcon(backDrawable);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen){
                    editText.clearFocus();
                    body.requestFocus();
                }else{
                    finish();
                }
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_web_code_fab);
        fab.setOnClickListener(this);
        init();
    }
    private void init(){
        openAnimator = ValueAnimator.ofFloat(0,1).setDuration(400);
        openAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                backDrawable.setProgress((Float) animation.getAnimatedValue());
            }
        });
        offAnimator = ValueAnimator.ofFloat(1,0).setDuration(400);
        offAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                backDrawable.setProgress((Float) animation.getAnimatedValue());
            }
        });
        editText = (TextInputEditText) findViewById(R.id.activity_web_code_edittext);
        editText.setOnFocusChangeListener(this);
        editText.addTextChangedListener(this);
        editText.setOnEditorActionListener(this);
        body = (TextView) findViewById(R.id.activity_web_code_text);
        simpleDateFormat = new SimpleDateFormat("yyyy|MM|dd-HH:mm:ss:SS");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        showBtn = s.length()>0;
//        getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_webcode, menu);
        menu.findItem(R.id.menu_webcode_done).setIcon(OtherUtil.tintDrawable(this,R.drawable.ic_search,Color.GRAY));
        menu.findItem(R.id.menu_webcode_done).setVisible(false);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(showBtn){
            menu.findItem(R.id.menu_webcode_done).setVisible(true);
        }else{
            menu.findItem(R.id.menu_webcode_done).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_webcode_done:
                getWebCode();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_web_code_fab:
                String code = body.getText().toString().trim();
                if(TextUtils.isEmpty(code)){
                    Snackbar.make(v,"当前内容为空，无法保存",Snackbar.LENGTH_LONG).show();
                }else{
                    loadDialog = DialogUtil.getLoadDialog(this);
                    Message message = handler.obtainMessage(202);
                    message.obj = v;
                    message.setTarget(handler);
                    HttpUtil.getThread(new SaveFileRunnable(message,code,simpleDateFormat.format(new Date())));
                }
                break;
        }
    }

    private void getWebCode(){
        editText.clearFocus();
        body.requestFocus();
        String url = editText.getText().toString().trim();
        if(TextUtils.isEmpty(url))
            return;
        loadDialog = DialogUtil.getLoadDialog(this);
        if(!url.toLowerCase().contains("http://")&&!url.toLowerCase().contains("https://")&&!url.toLowerCase().contains("ftp://")){
            url = "http://"+url;
        }
        NetTasks.getSimpleData(url, new HttpTaskRunnable.CallBack<String>(){
            @Override
            public void success(String object) {
                Message message = handler.obtainMessage(200);
                message.obj = object;
                handler.sendMessage(message);
            }
            @Override
            public void error(int code, String msg) {
                Message message = handler.obtainMessage(201);
                message.obj = msg;
                handler.sendMessage(message);
            }
            @Override
            public String str2Obj(String str) {
                return str;
            }
        });
    }

    private class SaveFileRunnable implements Runnable{

        private Message message;
        private String msg;
        private String name;

        SaveFileRunnable(Message message, String msg, String name) {
            this.message = message;
            this.msg = msg;
            this.name = name;
        }

        @Override
        public void run() {
            OtherUtil.outputTextFile(msg,name);
            message.sendToTarget();
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(loadDialog!=null)
                loadDialog.dismiss();
            switch (msg.what){
                case 200:
                    body.setText((String)msg.obj);
                    break;
                case 201:
                    body.setText("出现错误："+msg.obj);
                    break;
                case 202:
                    Snackbar.make((View) msg.obj,"保存完成，位于LTool/txt下",Snackbar.LENGTH_LONG).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v.getId()==R.id.activity_web_code_edittext){
            if(hasFocus){
                openAnimator.start();
                isOpen = true;
            }else{
                offAnimator.start();
                isOpen = false;
                InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),0);
                }
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(v.getId()==R.id.activity_web_code_edittext){
            getWebCode();
            return true;
        }
        return false;
    }
}
