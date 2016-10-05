package xiaoliang.ltool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;

import xiaoliang.ltool.R;
import xiaoliang.ltool.view.ZoomImageView;

public class MeizhiDetailedActivity extends AppCompatActivity implements View.OnClickListener {

    private String url = "";
    private ZoomImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meizhi_detailed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_meizhi_detailed_fab_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_meizhi_detailed_fab);
        fab.setOnClickListener(this);
        imageView = (ZoomImageView) findViewById(R.id.activity_meizhi_detailed_fab_img);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        if(null!=url&&!"".equals(url)){
            Glide.with(this).load(url).into(imageView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_meizhi_detailed_fab:

                break;
        }
    }
}
