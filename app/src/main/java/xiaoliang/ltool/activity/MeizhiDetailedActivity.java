package xiaoliang.ltool.activity;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.MeizhiBean;
import xiaoliang.ltool.constant.Constant;
import xiaoliang.ltool.constant.MeizhiType;
import xiaoliang.ltool.dialog.LoadDialog;
import xiaoliang.ltool.dialog.LoadDialog2;
import xiaoliang.ltool.util.DialogUtil;
import xiaoliang.ltool.util.HttpTaskRunnable;
import xiaoliang.ltool.util.MeizhiUtil;
import xiaoliang.ltool.util.NetTasks;
import xiaoliang.ltool.util.OtherUtil;
import xiaoliang.ltool.util.RequestParameters;
import xiaoliang.ltool.util.ToastUtil;
import xiaoliang.ltool.view.LLoadView2;
import xiaoliang.ltool.view.ZoomImageView;
import xiaoliang.ltool.view.zoom_img.GestureImageView;

public class MeizhiDetailedActivity extends AppCompatActivity implements View.OnClickListener {

    private MeizhiBean bean;
    private MeizhiType type;
    private GestureImageView imageView;
    private LoadDialog loadDialog;
    private ClipData myClip;
    private ClipboardManager myClipboard;
    private static final int PROGRESS_UPDATE = 200;
    private static final int DOWNLOAD_SECCESS = 201;
    private static final int DOWNLOAD_ERROR = 202;
    private static final int GET_URL = 203;
    private static final int GET_URL_ERROR = 204;
    private LoadDialog2 progressDialog;
    private String url;
    private RequestManager requestManager;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meizhi_detailed);
        toolbar = (Toolbar) findViewById(R.id.activity_meizhi_detailed_fab_toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_meizhi_detailed_fab);
        imageView = (GestureImageView) findViewById(R.id.activity_meizhi_detailed_fab_img);
        appBarLayout = (AppBarLayout) findViewById(R.id.activity_meizhi_detailed_fab_appbarlayout);
        setSupportActionBar(toolbar);
        fab.setOnClickListener(this);
        Intent intent = getIntent();
        bean = (MeizhiBean) intent.getSerializableExtra("bean");
        type = (MeizhiType) intent.getSerializableExtra("type");
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if(null!=bean&&null!=bean.title&&!"".equals(bean.title))
                getSupportActionBar().setTitle(bean.title);
        }
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        loadDialog = DialogUtil.getLoadDialog(this);
        requestManager = Glide.with(MeizhiDetailedActivity.this);
        getUrl();
    }

    private void getUrl(){
        switch (type){
            case GANK:
            case DOUBAN_ALL:
            case DOUBAN_LIAN:
            case DOUBAN_OTHER:
            case DOUBAN_SIWA:
            case DOUBAN_TUI:
            case DOUBAN_TUN:
            case DOUBAN_XIONG:
            case MM_All:
            case MM_Recommended:
            case MM_Ranking:
            case MM_Label:
                loadImg(bean.url);
                break;
            case MEIZHI51_ALL:
            case MEIZHI51_COMIC:
            case MEIZHI51_JAPAN:
            case MEIZHI51_KITTY:
            case MEIZHI51_LIU:
            case MEIZHI51_PURE:
            case MEIZHI51_SEX:
            case MEIZHI51_TAIWAN:
            case MEIZHI51_WEIBO:
            case MEIZHI51_WOMAN:
            case MEIZHI51_ZHAO:
                getData(bean.from);
                break;
            default:
                loadImg(bean.url);
                break;
        }
    }

    private void getData(String url){
        Log.d("数据加载","Type:"+type.getName());
        NetTasks.getSimpleData(url, new HttpTaskRunnable.CallBack<String>(){
            @Override
            public void success(String object) {
                Message message = handler.obtainMessage(GET_URL);
                message.obj = object;
                handler.sendMessage(message);
            }
            @Override
            public void error(int code, String msg) {
                Message message = handler.obtainMessage(GET_URL_ERROR);
                message.obj = msg;
                handler.sendMessage(message);
            }
            @Override
            public String str2Obj(String str) {
                switch (type){
                    case MEIZHI51_ALL:
                    case MEIZHI51_COMIC:
                    case MEIZHI51_JAPAN:
                    case MEIZHI51_KITTY:
                    case MEIZHI51_LIU:
                    case MEIZHI51_PURE:
                    case MEIZHI51_SEX:
                    case MEIZHI51_TAIWAN:
                    case MEIZHI51_WEIBO:
                    case MEIZHI51_WOMAN:
                    case MEIZHI51_ZHAO:
                        return MeizhiUtil.getMeizhi51DetailImgUrl(str);
                }
                return null;
            }
        });
    }

    private void loadImg(String url){
        if(null!=url&&!"".equals(url)){
            Log.d("loadImg",url);
            this.url = url;
            requestManager.load(url)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>(){
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            imageView.setImageBitmap(resource);
                                Palette.from(resource).generate(new Palette.PaletteAsyncListener(){
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        toolbar.setBackgroundColor(palette.getLightMutedColor(Color.parseColor("#41ddaf")));
                                        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
                                           setStatusBarColor(palette.getLightMutedColor(Color.parseColor("#41ddaf")));
                                        }
                                    }
                                });
                            if(loadDialog!=null)
                                loadDialog.dismiss();
                        }
                    });

        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColor(int colot){
        getWindow().setStatusBarColor(colot);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_meizhi_detailed_link:
                if(bean!=null&&bean.from!=null&&!bean.from.equals("")){
                    myClip = ClipData.newPlainText("text", bean.from);
                    myClipboard.setPrimaryClip(myClip);
                    ToastUtil.T(this,"图片来源地址已复制");
                    Uri uri = Uri.parse(bean.from);
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(it);
                }else{
                    ToastUtil.T(this,"找不到图片来源");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_meizhi_detailed_fab:
                if(url!=null&&!url.equals("")){
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
                }
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
                case GET_URL:
                    String url = (String) msg.obj;
                    if(!TextUtils.isEmpty(url)){
                        loadImg(url);
                        break;
                    }
                case GET_URL_ERROR:
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
                    DialogUtil.getAlertDialog(MeizhiDetailedActivity.this,"对不起，获取图片地址失败");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(null!=bean&&null!=bean.from&&!"".equals(bean.from)){
            getMenuInflater().inflate(R.menu.menu_meizhi_detailed, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
}
