package xiaoliang.ltool.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import java.util.ArrayList;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.HealthSearchBean;
import xiaoliang.ltool.view.LBackDrawable;

public class HealthActivity extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener,SwipeRefreshLayout.OnRefreshListener,TextWatcher {

    private LBackDrawable backDrawable;
    private LToolApplication app;
    private ValueAnimator openAnimator,offAnimator;
    private boolean isOpen = false;
    private TextInputEditText editText;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ImageView searchBtn;
    private ArrayList<HealthSearchBean> successBeans;
    private ArrayList<HealthSearchBean> errorBeans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        backDrawable = new LBackDrawable(this);
        backDrawable.setColor(Color.GRAY);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_health_toolbar);
        toolbar.setNavigationIcon(backDrawable);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen){
                    editText.clearFocus();
                    refreshLayout.requestFocus();
                }else{
                    finish();
                }
            }
        });
        app = (LToolApplication) getApplicationContext();
        init();
    }

    private void init(){
        if(app.getBlurBackground()!=null)
            ((ImageView)findViewById(R.id.activity_health_bg)).setImageBitmap(app.getBlurBackground());
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
        editText = (TextInputEditText) findViewById(R.id.activity_health_edittext);
        editText.setOnFocusChangeListener(this);
        editText.addTextChangedListener(this);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_health_swiperefreshlayout);
        refreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.activity_health_recyclerview);
        searchBtn = (ImageView) findViewById(R.id.activity_health_search);
        searchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_health_search:
                onSearch();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v.getId()==R.id.activity_health_edittext){
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
    public void onRefresh() {
        //TODO
    }

    private void onSearch(){
        //TODO
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.length()>0){
            searchBtn.setVisibility(View.VISIBLE);
        }else{
            searchBtn.setVisibility(View.GONE);
        }
    }
    @Override
    public void afterTextChanged(Editable s) {}
}
