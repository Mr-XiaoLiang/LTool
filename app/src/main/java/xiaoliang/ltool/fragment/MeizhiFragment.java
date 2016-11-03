package xiaoliang.ltool.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import java.util.ArrayList;

import xiaoliang.ltool.R;
import xiaoliang.ltool.activity.LToolApplication;
import xiaoliang.ltool.adapter.MeizhiAdapter;
import xiaoliang.ltool.bean.MeizhiBean;
import xiaoliang.ltool.constant.MeizhiType;
import xiaoliang.ltool.listener.OnScrollDownListener;
import xiaoliang.ltool.util.HttpTaskRunnable;
import xiaoliang.ltool.util.MeizhiUrlUtil;
import xiaoliang.ltool.util.MeizhiUtil;
import xiaoliang.ltool.util.NetTasks;
import xiaoliang.ltool.util.OtherUtil;

/**
 * 妹纸图片分页
 */
public class MeizhiFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,MeizhiAdapter.OnCardClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private OnFragmentInteractionListener mListener;
    private MeizhiType type;
    private ArrayList<MeizhiBean> urlList;
    private boolean isLoading = false;
    private int page = 0;
    private LToolApplication app;
    private MeizhiAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    private static final int startPage= 1;
    private boolean isAuto = true;
    private Handler handler;

    public MeizhiFragment() {
        // Required empty public constructor
        urlList = new ArrayList<>();
    }

    public static MeizhiFragment newInstance(MeizhiType type) {
        MeizhiFragment fragment = new MeizhiFragment();
        fragment.setType(type);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_meizhi,container,false);
        app = (LToolApplication) getActivity().getApplicationContext();
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.activity_meizhi_swiperefreshlayout);
        recyclerView = (RecyclerView) root.findViewById(R.id.activity_meizhi_recyclerview);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);
        //设置layoutManager
        layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);
        //设置adapter
        recyclerView.setAdapter(adapter = new MeizhiAdapter(urlList, this, this));
        //设置滑动监听器
        recyclerView.addOnScrollListener(new OnMeizhiScrollDownListener(layoutManager));
        handler = new MeizhiFragmentHandler(this);
        return root;
    }

    @Override
    public void onPause() {
        isAuto = false;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onResume","Type:"+type.getName());
        isAuto = true;
        if(urlList==null)
            urlList = new ArrayList<>();
        if(urlList.size()<1)
            onRefresh();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

    public void onError(String s){
        app.T(s);
    }

    private void onLoadMore(){
        switch(type){
            case MM_Label:
            case MM_Ranking:
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
        return MeizhiUrlUtil.getUrl(type,page);
    }

    public synchronized void setData(ArrayList<MeizhiBean> data){
        isLoading = false;
        if(swipeRefreshLayout!=null)
            swipeRefreshLayout.setRefreshing(false);
        if(data==null||data.size()<1)
            return;
        if(page==startPage){
            urlList.clear();
        }
        urlList.addAll(data);
        if(adapter!=null)
            adapter.notifyDataSetChanged();
//        if(isAuto)
//            onMore();//在这里循环的去主动获取数据
    }

    public String getTitle(){
        return type.getName();
    }

    public void selectedToTop(){
        if(recyclerView!=null)
            recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void OnCardClick(MeizhiBean bean,int p) {
        onButtonPressed(bean);
    }

    private class OnMeizhiScrollDownListener extends OnScrollDownListener{

        public OnMeizhiScrollDownListener(StaggeredGridLayoutManager manager) {
            super(manager);
        }

        @Override
        public void onScroll(boolean down, int newState) {
            if (mListener != null) {
                mListener.onScrollStateChanged(down);
            }
        }

        @Override
        public void onMore() {
            onLoadMore();
        }
    }

    public void onButtonPressed(MeizhiBean uri) {
        if (mListener != null) {
            mListener.onCardClick(this,uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setType(MeizhiType type) {
        this.type = type;
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

    public interface OnFragmentInteractionListener {
        void onCardClick(MeizhiFragment fragment,MeizhiBean uri);
        void onScrollStateChanged(boolean show);
    }

    public MeizhiType getType() {
        return type;
    }

    /****数据加载开始***/

    private void getData(String url){
        Log.d("数据加载","Type:"+type.getName()+",URL:"+url);
        if(swipeRefreshLayout!=null)
            swipeRefreshLayout.setRefreshing(true);
        NetTasks.getSimpleData(url, new HttpTaskRunnable.CallBack<ArrayList<MeizhiBean>>(){
            @Override
            public void success(ArrayList<MeizhiBean> object) {
                Message message = handler.obtainMessage(MeizhiFragmentHandler.getdata);
                message.obj = object;
                handler.sendMessage(message);
            }
            @Override
            public void error(int code, String msg) {
                Message message = handler.obtainMessage(MeizhiFragmentHandler.error);
                message.obj = msg;
                handler.sendMessage(message);
            }
            @Override
            public ArrayList<MeizhiBean> str2Obj(String str) {
                switch (type){
                    case GANK:
                        return MeizhiUtil.getGankImgUrl(str);
                    case DOUBAN_ALL:
                    case DOUBAN_LIAN:
                    case DOUBAN_OTHER:
                    case DOUBAN_SIWA:
                    case DOUBAN_TUI:
                    case DOUBAN_TUN:
                    case DOUBAN_XIONG:
                        return MeizhiUtil.getDoubanImgUrl(str);
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
                        return MeizhiUtil.getMeizhi51ImgUrl(str);
                    case MM_All:
                        return MeizhiUtil.getMMHomeImgUrl(str);
                    case MM_Ranking:
                        return MeizhiUtil.getMMHotImgUrl(str);
                    case MM_Recommended:
                        return MeizhiUtil.getMMRecommendedImgUrl(str);
                    case MM_Label:
                        return MeizhiUtil.getMMLabelImgUrl(str);
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
                        return MeizhiUtil.getMeizhituImgUrl(str);
                    case Meitulu_Recommend:
                        return MeizhiUtil.getMeituluHotImgUrl(str);
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
                        return MeizhiUtil.getMeituluImgUrl(str);
                }
                return null;
            }
        });
    }

    /****s数据加载结束***/
}
