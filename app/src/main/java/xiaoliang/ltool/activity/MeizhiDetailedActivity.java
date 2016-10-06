package xiaoliang.ltool.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;

import xiaoliang.ltool.R;
import xiaoliang.ltool.constant.Constant;
import xiaoliang.ltool.dialog.LoadDialog;
import xiaoliang.ltool.dialog.LoadDialog2;
import xiaoliang.ltool.util.DialogUtil;
import xiaoliang.ltool.util.NetTasks;
import xiaoliang.ltool.util.RequestParameters;
import xiaoliang.ltool.util.ToastUtil;
import xiaoliang.ltool.view.LLoadView2;
import xiaoliang.ltool.view.ZoomImageView;
import xiaoliang.ltool.view.zoom_img.GestureImageView;

public class MeizhiDetailedActivity extends AppCompatActivity implements View.OnClickListener {

    private String url = "";
    private String title = "";
    private String from = "";
    private GestureImageView imageView;
    private LoadDialog loadDialog;
    private ClipData myClip;
    private ClipboardManager myClipboard;
    private static final int PROGRESS_UPDATE = 200;
    private static final int DOWNLOAD_SECCESS = 201;
    private static final int DOWNLOAD_ERROR = 202;
    private LoadDialog2 progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meizhi_detailed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_meizhi_detailed_fab_toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_meizhi_detailed_fab);
        imageView = (GestureImageView) findViewById(R.id.activity_meizhi_detailed_fab_img);
        setSupportActionBar(toolbar);
        fab.setOnClickListener(this);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        from = intent.getStringExtra("from");
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if(null!=title&&!"".equals(title))
                getSupportActionBar().setTitle(title);
        }
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        loadDialog = DialogUtil.getLoadDialog(this);
        if(null!=url&&!"".equals(url)){
            Glide.with(this)
                    .load(url)
                    .into(new GlideDrawableImageViewTarget(imageView){
                        @Override
                        public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                            super.onResourceReady(drawable, anim);
                            if(loadDialog!=null)
                                loadDialog.dismiss();
                        }
                    });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_meizhi_detailed_link:
                myClip = ClipData.newPlainText("text", from);
                myClipboard.setPrimaryClip(myClip);
                ToastUtil.T(this,"图片来源地址已复制");
                Uri uri = Uri.parse(from);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_meizhi_detailed_fab:
                progressDialog = DialogUtil.getLoadDialog2(this);
                NetTasks.downloadImage(url, new RequestParameters.Progress() {
                    @Override
                    public void onProgress(float pro) {
                        Message message = handler.obtainMessage(PROGRESS_UPDATE);
                        if(message==null){
                            message = new Message();
                            message.what = PROGRESS_UPDATE;
                            message.setTarget(handler);
                        }
                        message.obj = pro;
                        message.sendToTarget();
                    }
                    @Override
                    public void onLoadSeccess(String path) {
                        handler.sendEmptyMessage(DOWNLOAD_SECCESS);
                    }
                    @Override
                    public void onLoadError(Exception e, int type) {
                        Log.d("downloadImage",e.getMessage());
                        Message message = handler.obtainMessage(DOWNLOAD_ERROR);
                        if(message==null){
                            message = new Message();
                            message.what = DOWNLOAD_ERROR;
                            message.setTarget(handler);
                        }
                        message.arg1 = type;
                        message.sendToTarget();
                    }
                });
                break;
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PROGRESS_UPDATE:
                    if(progressDialog!=null){
                        progressDialog.setProgress((Float) msg.obj);
                    }
                    break;
                case DOWNLOAD_SECCESS:
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
                    DialogUtil.getAlertDialog(MeizhiDetailedActivity.this,"图片下载完成，位于手机/LTool/img目录下");
                    break;
                case DOWNLOAD_ERROR:
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
                    switch (msg.arg1){
                        case Constant.DownLoadError:
                            DialogUtil.getAlertDialog(MeizhiDetailedActivity.this,"对不起，下载失败");
                            break;
                        case Constant.DownLoadSizeNotFind:
                            DialogUtil.getAlertDialog(MeizhiDetailedActivity.this,"对不起，下载失败:\n无法获取文件大小");
                            break;
                        case Constant.DownLoadStreamIsNull:
                            DialogUtil.getAlertDialog(MeizhiDetailedActivity.this,"对不起，下载失败:\n下载链接为空");
                            break;
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(null!=from&&!"".equals(from)){
            getMenuInflater().inflate(R.menu.menu_meizhi_detailed, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
}
