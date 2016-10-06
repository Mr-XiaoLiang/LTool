package xiaoliang.ltool.fragment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import xiaoliang.ltool.R;
import xiaoliang.ltool.activity.LToolApplication;
import xiaoliang.ltool.bean.MeizhiBean;
import xiaoliang.ltool.constant.Constant;
import xiaoliang.ltool.constant.MeizhiType;
import xiaoliang.ltool.util.DialogUtil;
import xiaoliang.ltool.util.HttpTaskRunnable;
import xiaoliang.ltool.util.MeizhiUtil;
import xiaoliang.ltool.util.NetTasks;
import xiaoliang.ltool.view.RatioImageView;

/**
 * 妹纸图片分页
 */
public class MeizhiFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

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
        recyclerView.setAdapter(adapter = new MeizhiAdapter());
        //设置滑动监听器
        recyclerView.addOnScrollListener(new OnScrollDownListener());
        handler = new MeizhiFragmentHandler(this);
        return root;
    }

    private class MeizhiAdapter extends RecyclerView.Adapter<MeizhiAdapter.MeizhiHolder>{

        @Override
        public MeizhiHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meizhi, parent, false);
            return new MeizhiHolder(view);
        }

        @Override
        public void onBindViewHolder(MeizhiHolder holder, int position) {
            holder.onBind(getItem(position));

        }

        @Override
        public int getItemCount() {
            return urlList.size();
        }

        private MeizhiBean getItem(int position){
            return urlList.get(position);
        }

        class MeizhiHolder extends RecyclerView.ViewHolder{

            private RatioImageView img;
            private TextView text;
            private CardView cardView;

             void onBind(final MeizhiBean bean){
                 if (null!=bean.title&&!"".equals(bean.title)){
                     text.setVisibility(View.VISIBLE);
                     text.setText(bean.title);
                 }else{
                     text.setVisibility(View.GONE);
                 }
                 Glide.with(MeizhiFragment.this).load(bean.url).centerCrop().into(img);
                 cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onButtonPressed(bean);
                    }
                });
            }
             MeizhiHolder(View itemView) {
                super(itemView);
                 cardView = (CardView) itemView.findViewById(R.id.item_meizhi_card);
                 img = (RatioImageView) itemView.findViewById(R.id.item_meizhi_img);
                 text = (TextView) itemView.findViewById(R.id.item_meizhi_text);
            }
        }
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

    private void onMore(){
        String url = getUrl();
        if(!isLoading&&!url.equals("")){
            isLoading = true;
            page++;
            getData(getUrl());
        }
    }

    private String getUrl(){
        String url = "";
        switch (type){
            case GANK:
                url = Constant.Gank_Meizi_Url+"30/"+page;
                break;
            case DOUBAN_ALL:
                url = Constant.Douban_Meizhi_All_Url+page;
                break;
            case DOUBAN_LIAN:
                url = Constant.Douban_Meizhi_Lian_Url+page;
                break;
            case DOUBAN_OTHER:
                url = Constant.Douban_Meizhi_Other_Url+page;
                break;
            case DOUBAN_SIWA:
                url = Constant.Douban_Meizhi_Siwa_Url+page;
                break;
            case DOUBAN_TUI:
                url = Constant.Douban_Meizhi_Tui_Url+page;
                break;
            case DOUBAN_TUN:
                url = Constant.Douban_Meizhi_Tun_Url+page;
                break;
            case DOUBAN_XIONG:
                url = Constant.Douban_Meizhi_Xiong_Url+page;
                break;
        }
        return url;
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
//        String title = "";
//        switch (type){
//            case GANK:return"GANK";
//            case DOUBAN_ALL:return"豆瓣全部";
//            case DOUBAN_LIAN:return"豆瓣颜值";
//            case DOUBAN_OTHER:return"豆瓣其他";
//            case DOUBAN_SIWA:return"豆瓣丝袜";
//            case DOUBAN_TUI:return"豆瓣美腿";
//            case DOUBAN_TUN:return"豆瓣翘臀";
//            case DOUBAN_XIONG:return"豆瓣凶器";
//        }
//        return title;
        return type.getName();
    }

    public void selectedToTop(){
        if(recyclerView!=null)
            recyclerView.smoothScrollToPosition(0);
    }

    private class OnScrollDownListener extends RecyclerView.OnScrollListener{
        //用来标记是否正在向最后一个滑动，既是否向下滑动
        boolean isSlidingToLast = false;
        StaggeredGridLayoutManager manager;

        public OnScrollDownListener() {
            manager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            layoutManager.invalidateSpanAssignments();
            // 当不滚动时
//            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//            }
        }
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            isSlidingToLast = dy>0;
            //获取最后一个完全显示的ItemPosition
            int[] lastVisiblePositions = manager.findLastVisibleItemPositions(new int[manager.getSpanCount()]);
            int lastVisiblePos = getMaxElem(lastVisiblePositions);
            int totalItemCount = manager.getItemCount();
            // 判断是否滚动到底部
            if (lastVisiblePos > (totalItemCount -5) && isSlidingToLast) {
                //加载更多功能的代码
                onMore();
            }
            //加载很多数据后，停止自动加载
//            if(lastVisiblePos<(totalItemCount - 30)){
//                isAuto = false;
//            }else{
//                isAuto = true;
//            }
            Log.d("onScrollStateChanged","ItemCount-----------------"+totalItemCount);
        }
    }

    private int getMaxElem(int[] arr) {
        int maxVal = Integer.MIN_VALUE;
        for(int a:arr){
            if(a>maxVal)
                maxVal = a;
        }
        return maxVal;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onCardClick(MeizhiFragment fragment,MeizhiBean uri);
    }

    public MeizhiType getType() {
        return type;
    }

    /****数据加载开始***/

    private void getData(String url){
        Log.d("数据加载","Type:"+type.getName());
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
                Message message = handler.obtainMessage(MeizhiFragmentHandler.getdata);
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
                }
                return null;
            }
        });
    }

    /****s数据加载结束***/
}
