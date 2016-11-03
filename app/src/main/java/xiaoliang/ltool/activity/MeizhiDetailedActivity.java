package xiaoliang.ltool.activity;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.MeizhiBeanArray;
import xiaoliang.ltool.constant.Constant;
import xiaoliang.ltool.constant.MeizhiType;
import xiaoliang.ltool.dialog.LoadDialog;
import xiaoliang.ltool.dialog.LoadDialog2;
import xiaoliang.ltool.fragment.MeizhiDetailedFragment;
import xiaoliang.ltool.util.DialogUtil;
import xiaoliang.ltool.util.NetTasks;
import xiaoliang.ltool.util.OtherUtil;
import xiaoliang.ltool.util.RequestParameters;
import xiaoliang.ltool.util.ToastUtil;
import xiaoliang.ltool.view.ZoomPageTransformer;

public class MeizhiDetailedActivity extends AppCompatActivity implements View.OnClickListener,MeizhiDetailedFragment.MeizhiDetailedFragmentListener,ViewPager.OnPageChangeListener {

    private MeizhiBeanArray beanArray;
    private MeizhiDetailedFragment[] fragments;
    private MeizhiType type;
    private int position;
//    private ClipData myClip;
    private ClipboardManager myClipboard;
    private static final int PROGRESS_UPDATE = 200;
    private static final int DOWNLOAD_SECCESS = 201;
    private static final int DOWNLOAD_ERROR = 202;
    private LoadDialog2 progressDialog;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private LoadDialog loadDialog;
    private int loadSize = 0;
    private int downSuccess = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meizhi_detailed);
        toolbar = (Toolbar) findViewById(R.id.activity_meizhi_detailed_fab_toolbar);
        findViewById(R.id.activity_meizhi_detailed_down).setOnClickListener(this);
        findViewById(R.id.activity_meizhi_detailed_downs).setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.activity_meizhi_detailed_viewpager);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        beanArray = (MeizhiBeanArray) intent.getSerializableExtra("bean");
        type = (MeizhiType) intent.getSerializableExtra("type");
        position = intent.getIntExtra("index",0);
        fragments = new MeizhiDetailedFragment[beanArray.size()];
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle();
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(this);
        viewPager.setPageTransformer(true,new ZoomPageTransformer());
        viewPager.setCurrentItem(position);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColor(int colot){
        getWindow().setStatusBarColor(colot);
    }

    public void setTitle(){
        if(getSupportActionBar()!=null){
            if(null!=beanArray&&null!=beanArray.bean(position).title&&!"".equals(beanArray.bean(position).title))
                getSupportActionBar().setTitle(beanArray.bean(position).title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_meizhi_detailed_link:
                if(beanArray!=null&&beanArray.bean(position).from!=null&&!beanArray.bean(position).from.equals("")){
                    ClipData myClip = ClipData.newPlainText("text", beanArray.bean(position).from);
                    myClipboard.setPrimaryClip(myClip);
                    DialogUtil.getAlertDialog(this, "可爱的乘客你好:", "图片来源地址已复制到剪切板\n是否用浏览器打开此网址?", "打开", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri uri = Uri.parse(beanArray.bean(position).from);
                            Intent it = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(it);
                        }
                    });
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
            case R.id.activity_meizhi_detailed_down:
                drawOnce();
                break;
            case R.id.activity_meizhi_detailed_downs:
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
                        DialogUtil.getAlertDialog(this,"抱歉,51系列因为网址解析方式不同,暂时无法批量下载");
                        break;
                    default:
                        drawAll();
                        break;
                }
                break;
        }
    }

    private void drawAll(){
        if(beanArray==null||beanArray.size()<1){
            ToastUtil.T(this,"下载链接库为空");
            return;
        }
        progressDialog = DialogUtil.getLoadDialog2(this);
        String urlFormat = beanArray.bean(0).url;
        urlFormat = urlFormat.substring(0,urlFormat.lastIndexOf("/"));
        urlFormat = urlFormat.replaceAll("/","_");
        String path = OtherUtil.getSDImgPath()+"/"+urlFormat;
        final int imgSize = beanArray.size();
        for(int posi = 0;posi<imgSize;posi++){
            if(beanArray.bean(posi)!=null
                    && !TextUtils.isEmpty(beanArray.bean(posi).url)){
//                final int thisPosi = posi;
                NetTasks.downloadImage(beanArray.bean(posi).url,"",path, new RequestParameters.Progress() {
                    @Override
                    public void onProgress(float pro) {
//                        Message message = handler.obtainMessage(PROGRESS_UPDATE);
//                        if(message.getTarget()==null){
//                            message.setTarget(handler);
//                        }
//                        message.obj = pro/imgSize;
//                        message.arg1 = thisPosi;
//                        message.sendToTarget();
                    }
                    @Override
                    public void onLoadSeccess(String path) {
                        Message message = handler.obtainMessage(DOWNLOAD_SECCESS);
                        if(message.getTarget()==null){
                            message.setTarget(handler);
                        }
                        message.arg1 = imgSize;
                        message.sendToTarget();
                    }
                    @Override
                    public void onLoadError(Exception e, int type) {
                        Log.d("downloadImage",e.getMessage());
                        Message message = handler.obtainMessage(DOWNLOAD_ERROR);
                        if(message.getTarget()==null){
                            message.setTarget(handler);
                        }
                        message.arg1 = type;
                        message.arg2 = imgSize;
                        message.sendToTarget();
                    }
                });
            }
        }
    }

    private void drawOnce(){
        if(!TextUtils.isEmpty(fragments[position].getImageUrl())){
            String urlFormat = beanArray.bean(0).url;
            urlFormat = urlFormat.substring(0,urlFormat.lastIndexOf("/"));
            urlFormat = urlFormat.replaceAll("/","_");
            String path = OtherUtil.getSDImgPath()+"/"+urlFormat;
            progressDialog = DialogUtil.getLoadDialog2(this);
            progressDialog.setProgress(0.5F);
            NetTasks.downloadImage(fragments[position].getImageUrl(),"",path, new RequestParameters.Progress() {
                @Override
                public void onProgress(float pro) {
//                    Message message = handler.obtainMessage(PROGRESS_UPDATE);
//                    if(message==null){
//                        message = new Message();
//                        message.what = PROGRESS_UPDATE;
//                        message.setTarget(handler);
//                    }
//                    message.obj = pro;
//                    message.arg1 = 0;
//                    message.sendToTarget();
                }
                @Override
                public void onLoadSeccess(String path) {
                    Message message = handler.obtainMessage(DOWNLOAD_SECCESS);
                    if(message.getTarget()==null){
                        message.setTarget(handler);
                    }
                    message.arg1 = 1;
                    message.sendToTarget();
                }
                @Override
                public void onLoadError(Exception e, int type) {
                    Log.d("downloadImage",e.getMessage());
                    Message message = handler.obtainMessage(DOWNLOAD_ERROR);
                    if(message.getTarget()==null){
                        message.setTarget(handler);
                    }
                    message.arg1 = type;
                    message.arg2 = 1;
                    message.sendToTarget();
                }
            });
        }else{
            ToastUtil.T(this,"下载链接库为空");
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PROGRESS_UPDATE:
//                    if(progressDialog!=null){
//                        progressDialog.setProgress((Float) msg.obj);
//                        progressDialog.setText("正在下载"+msg.arg1+"/"+beanArray.size()+"\n请不要退出此页面");
//                    }
                    break;
                case DOWNLOAD_SECCESS:
                    downSuccess++;
                    if(downSuccess>=msg.arg1){
                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }
                        DialogUtil.getAlertDialog(MeizhiDetailedActivity.this,"图片下载完成，位于手机/LTool/img目录下");
                    }else if(progressDialog!=null){
                        progressDialog.setProgress(1.0f*downSuccess/msg.arg1);
                        progressDialog.setText("正在下载"+(downSuccess+1)+"/"+msg.arg1+"\n请不要退出此页面");
                    }
                    break;
                case DOWNLOAD_ERROR:
                    downSuccess++;
                    if(msg.arg2<=downSuccess){
                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }
                    }
                    switch (msg.arg1){
                        case Constant.DownLoadError:
                            DialogUtil.getAlertDialog(MeizhiDetailedActivity.this,"节哀，第"+(downSuccess)+"张下载失败");
                            break;
                        case Constant.DownLoadSizeNotFind:
                            DialogUtil.getAlertDialog(MeizhiDetailedActivity.this,"节哀，第"+(downSuccess)+"张下载失败:\n无法获取文件大小");
                            break;
                        case Constant.DownLoadStreamIsNull:
                            DialogUtil.getAlertDialog(MeizhiDetailedActivity.this,"节哀，第"+(downSuccess)+"张下载失败:\n下载链接为空");
                            break;
                    }
                    if(progressDialog!=null){
                        progressDialog.setProgress(1.0f*downSuccess/msg.arg2);
                        progressDialog.setText("正在下载"+(downSuccess+1)+"/"+msg.arg2+"\n请不要退出此页面");
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
    @Override
    public void onPageSelected(int position) {
        this.position = position;
        setTitle();
    }
    @Override
    public void onPageScrollStateChanged(int state) {}

    private class PageAdapter extends FragmentStatePagerAdapter {

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(fragments[position]==null){
                fragments[position] = MeizhiDetailedFragment.newInstance(beanArray.bean(position),type);
            }
            return fragments[position];
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
        @Override
        public int getCount() {
            return beanArray.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meizhi_detailed, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onPaletteColorChange(int color) {
        toolbar.setBackgroundColor(color);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            setStatusBarColor(color);
        }
    }

    @Override
    public void onLoadImgae(boolean b) {
        if(b){
            loadSize++;
            if(loadDialog==null)
                loadDialog = DialogUtil.getLoadDialog(this);
        }else{
            loadSize--;
            if(loadSize<1&&loadDialog!=null)
                loadDialog.dismiss();
        }
    }
}
