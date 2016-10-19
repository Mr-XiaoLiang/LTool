package xiaoliang.ltool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import xiaoliang.ltool.R;
import xiaoliang.ltool.adapter.MeizhiAdapter;
import xiaoliang.ltool.bean.MeizhiBean;
import xiaoliang.ltool.constant.MeizhiType;
import xiaoliang.ltool.listener.OnScrollDownListener;
import xiaoliang.ltool.util.DialogUtil;
import xiaoliang.ltool.util.HttpTaskRunnable;
import xiaoliang.ltool.util.MeizhiUrlUtil;
import xiaoliang.ltool.util.MeizhiUtil;
import xiaoliang.ltool.util.NetTasks;

/**
 * 妹子图的二级列表页面
 */
public class MeizhiListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,MeizhiAdapter.OnCardClickListener,View.OnClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MeizhiType type;
    private MeizhiBean bean;
    private ArrayList<MeizhiBean> urlList;
    private boolean isLoading = false;
    private int page = 1;
    private LToolApplication app;
    private MeizhiAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    private static final int startPage= 1;
    private FloatingActionButton toTop;
    private int maxPage = 1;
    private int activityPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meizhi_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_meizhi_list_toolbar);
        toTop = (FloatingActionButton) findViewById(R.id.activity_meizhi_list_fab);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        type = (MeizhiType) intent.getSerializableExtra("type");
        bean = (MeizhiBean) intent.getSerializableExtra("bean");
        activityPage = intent.getIntExtra("activityPage",0);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if(!TextUtils.isEmpty(bean.title))
                getSupportActionBar().setTitle(bean.title);
        }
        app = (LToolApplication) getApplicationContext();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_meizhi_list_swiperefreshlayout);
        recyclerView = (RecyclerView) findViewById(R.id.activity_meizhi_list_recyclerview);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);
        toTop.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.activity_meizhi_list_fab:
                if(recyclerView!=null)
                    recyclerView.smoothScrollToPosition(0);
                break;
        }
    }

    private class OnMeizhiScrollDownListener extends OnScrollDownListener {
        public OnMeizhiScrollDownListener(StaggeredGridLayoutManager manager) {
            super(manager);
        }

        @Override
        public void onScroll(boolean down, int newState) {
            if(down){
                toTop.setVisibility(View.VISIBLE);
            }else{
                toTop.setVisibility(View.GONE);
            }
        }
        @Override
        public void onMore() {
            onLoadMore();
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
                            maxPage = data.get(0).pagination;
                            if(!TextUtils.isEmpty(data.get(0).other)){
                                DialogUtil.getAlertDialog(MeizhiListActivity.this,data.get(0).other);
                            }
                        }
                        urlList.addAll(data);
                        if(adapter!=null)
                            adapter.notifyDataSetChanged();
                        if(page<maxPage&&urlList.size()<8){
                            onLoadMore();
                        }
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
        if(maxPage<=page){
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
        switch (type){
            case MM_Label:
                if(activityPage==0){
                    return bean.page+"/"+page;
                }else{
                    return bean.page;
                }
            case Meitulu_Recommend:
            case Meitulu_Japan:
            case Meitulu_Hokon:
            case Meitulu_Domestic:
            case Meitulu_Highest:
            case Meitulu_God:
            case Meitulu_Model:
            case Meitulu_Net:
            case Meitulu_Mores:
            case Meitulu_Temperament:
            case Meitulu_Stunner:
            case Meitulu_Milk:
            case Meitulu_Sex:
            case Meitulu_Tempt:
            case Meitulu_Xiong:
            case Meitulu_Woman:
            case Meitulu_Tui:
            case Meitulu_Bud:
            case Meitulu_Loli:
            case Meitulu_Cute:
            case Meitulu_Outdoors:
            case Meitulu_Bikini:
            case Meitulu_Pure:
            case Meitulu_Aestheticism:
            case Meitulu_Fresh:
                if(page>startPage){
                    String str = bean.page.substring(0, bean.page.length()-5);
                    str = str+"_"+page+".html";
                    return str;
                }else{
                    return bean.page;
                }
            default:
                return bean.page;
        }
    }
    @Override
    public void onRefresh() {
        String url = getUrl();
        if(!isLoading&&!TextUtils.isEmpty(url)){
            isLoading = true;
            page = startPage;
            getData(getUrl());
        }
    }

    @Override
    public void OnCardClick(MeizhiBean bean) {
        Intent intent = null;
        if(type == MeizhiType.MM_Label){
            if(activityPage == 0){
                intent = new Intent(this,MeizhiListActivity.class);
                intent.putExtra("bean",bean);
                intent.putExtra("type",type);
                intent.putExtra("activityPage",1);
            }else{
                intent = new Intent(this,MeizhiDetailedActivity.class);
                intent.putExtra("bean",bean);
                intent.putExtra("type",type);
            }
        }else{
//        if(type.getValue()<MeizhiType.Meizhi_Japan.getValue()){
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
        Log.d("数据加载","Type:"+type.getName()+url);
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
                    case MM_All:
                    case MM_Ranking:
                    case MM_Recommended:
                        return MeizhiUtil.getMMImgListUrl(str);
                    case MM_Label:
                        if(activityPage==0){
                            return MeizhiUtil.getMMLableListUrl(str);
                        }else{
                            return MeizhiUtil.getMMImgListUrl(str);
                        }
                    case Meizhi_all:
                    case Meizhi_Sex:
                    case Meizhi_Private:
                    case Meizhi_Pure:
                    case Meizhi_Bud:
                    case Meizhi_Fresh:
                    case Meizhi_God:
                    case Meizhi_Temperament:
                    case Meizhi_Model:
                    case Meizhi_Bikini:
                    case Meizhi_Football:
                    case Meizhi_Loli:
                    case Meizhi_90:
                    case Meizhi_Japan:
                        return MeizhiUtil.getMeizhituImgListUrl(str);
                    case Meitulu_Recommend:
                    case Meitulu_Japan:
                    case Meitulu_Hokon:
                    case Meitulu_Domestic:
                    case Meitulu_Highest:
                    case Meitulu_God:
                    case Meitulu_Model:
                    case Meitulu_Net:
                    case Meitulu_Mores:
                    case Meitulu_Temperament:
                    case Meitulu_Stunner:
                    case Meitulu_Milk:
                    case Meitulu_Sex:
                    case Meitulu_Tempt:
                    case Meitulu_Xiong:
                    case Meitulu_Woman:
                    case Meitulu_Tui:
                    case Meitulu_Bud:
                    case Meitulu_Loli:
                    case Meitulu_Cute:
                    case Meitulu_Outdoors:
                    case Meitulu_Bikini:
                    case Meitulu_Pure:
                    case Meitulu_Aestheticism:
                    case Meitulu_Fresh:
                        return MeizhiUtil.getMeituluImgListUrl(str);
                }
                return null;
            }
        });
    }
    /****s数据加载结束***/
}
