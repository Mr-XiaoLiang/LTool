package xiaoliang.ltool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import xiaoliang.ltool.R;
import xiaoliang.ltool.adapter.MeizhiAdapter;
import xiaoliang.ltool.bean.MeizhiBean;
import xiaoliang.ltool.constant.MeizhiType;
import xiaoliang.ltool.listener.OnScrollDownListener;
import xiaoliang.ltool.util.HttpTaskRunnable;
import xiaoliang.ltool.util.MeizhiUrlUtil;
import xiaoliang.ltool.util.MeizhiUtil;
import xiaoliang.ltool.util.NetTasks;

/**
 * 妹子图的二级列表页面
 */
public class MeizhiListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,MeizhiAdapter.OnCardClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MeizhiType type;
    private MeizhiBean bean;
    private ArrayList<MeizhiBean> urlList;
    private boolean isLoading = false;
    private int page = 0;
    private LToolApplication app;
    private MeizhiAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    private static final int startPage= 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meizhi_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_meizhi_list_toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        type = (MeizhiType) intent.getSerializableExtra("type");
        bean = (MeizhiBean) intent.getSerializableExtra("bean");
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(bean.title);
        }
        app = (LToolApplication) getApplicationContext();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_meizhi_list_swiperefreshlayout);
        recyclerView = (RecyclerView) findViewById(R.id.activity_meizhi_list_recyclerview);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);
        urlList = new ArrayList<>();
        //设置layoutManager
        layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);
        //设置adapter
        recyclerView.setAdapter(adapter = new MeizhiAdapter(urlList, this, this));
        //设置滑动监听器
        recyclerView.addOnScrollListener(new OnMeizhiScrollDownListener(layoutManager));


    }

    private class OnMeizhiScrollDownListener extends OnScrollDownListener {
        public OnMeizhiScrollDownListener(StaggeredGridLayoutManager manager) {
            super(manager);
        }

        @Override
        public void onMore() {
            onLoadMore();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meizhi, menu);
        return true;
    }
    public void selectedToTop(){
        if(recyclerView!=null)
            recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_meizhi_top:
                selectedToTop();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 200:
                    isLoading = false;
                    if(swipeRefreshLayout!=null)
                        swipeRefreshLayout.setRefreshing(false);
                    if(msg.obj instanceof ArrayList){
                        ArrayList<MeizhiBean> data = (ArrayList<MeizhiBean>) msg.obj;
                        if(data.size()<1)
                            return;
                        if(page==startPage){
                            urlList.clear();
                        }
                        urlList.addAll(data);
                        if(adapter!=null)
                            adapter.notifyDataSetChanged();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onResume","Type:"+type.getName());
        if(urlList==null)
            urlList = new ArrayList<>();
        if(urlList.size()<1)
            onRefresh();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

    private void onLoadMore(){
        if(type.getValue()< MeizhiType.MM_All.getValue()){
            return;
        }
        String url = getUrl();
        if(!isLoading&&!url.equals("")){
            isLoading = true;
            page++;
            getData(getUrl());
        }
    }

    private String getUrl(){
        if(type.getValue()<MeizhiType.MM_All.getValue())
            return bean.page;
        return MeizhiUrlUtil.getUrl(type,page);
    }
    @Override
    public void onRefresh() {
        String url = getUrl();
        if(!isLoading&&!url.equals("")){
            isLoading = true;
            page = startPage;
            getData(getUrl());
        }
    }

    @Override
    public void OnCardClick(MeizhiBean bean) {
        Intent intent = null;
        if(type.getValue()<MeizhiType.MM_All.getValue()){
            intent = new Intent(this,MeizhiDetailedActivity.class);
            intent.putExtra("bean",bean);
            intent.putExtra("type",type);
        }
//        switch (type){
//            case MEIZHI51_ALL:
//            case MEIZHI51_COMIC:
//            case MEIZHI51_JAPAN:
//            case MEIZHI51_KITTY:
//            case MEIZHI51_LIU:
//            case MEIZHI51_PURE:
//            case MEIZHI51_SEX:
//            case MEIZHI51_TAIWAN:
//            case MEIZHI51_WEIBO:
//            case MEIZHI51_WOMAN:
//            case MEIZHI51_ZHAO:
//
//        }
        startActivity(intent);
    }

    /****数据加载开始***/

    private void getData(String url){
        Log.d("数据加载","Type:"+type.getName());
        if(swipeRefreshLayout!=null)
            swipeRefreshLayout.setRefreshing(true);
        NetTasks.getSimpleData(url, new HttpTaskRunnable.CallBack<ArrayList<MeizhiBean>>(){
            @Override
            public void success(ArrayList<MeizhiBean> object) {
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
            public ArrayList<MeizhiBean> str2Obj(String str) {
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
                        return MeizhiUtil.getMeizhi51PageImgUrl(str);
                }
                return null;
            }
        });
    }
    /****s数据加载结束***/
}
