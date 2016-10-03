package xiaoliang.ltool.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.logging.Handler;

import xiaoliang.ltool.R;
import xiaoliang.ltool.activity.LToolApplication;
import xiaoliang.ltool.constant.Constant;
import xiaoliang.ltool.util.HttpTaskRunnable;
import xiaoliang.ltool.util.MeizhiUtil;
import xiaoliang.ltool.util.NetTasks;

/**
 * 妹纸图片分页
 */
public class MeizhiFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private OnFragmentInteractionListener mListener;
    private MeizhiType type;
    private ArrayList<String> urlList;
    private boolean isLoading = false;
    private int page = 0;
    private LToolApplication app;
    private MeizhiAdapter adapter;

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
        View root = inflater.inflate(R.layout.content_meizhi,container);
        app = (LToolApplication) getActivity().getApplicationContext();
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.activity_meizhi_swiperefreshlayout);
        recyclerView = (RecyclerView) root.findViewById(R.id.activity_meizhi_recyclerview);
        swipeRefreshLayout.setOnRefreshListener(this);
        //设置layoutManager
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        //设置adapter
        recyclerView.setAdapter(adapter = new MeizhiAdapter(ImageLoader.getInstance()));
        //设置滑动监听器
        recyclerView.addOnScrollListener(new OnScrollDownListener());
        return root;
    }

    private class MeizhiAdapter extends RecyclerView.Adapter<MeizhiAdapter.MeizhiHolder>{

        private ImageLoader imageLoader;

         MeizhiAdapter(ImageLoader imageLoader) {
            this.imageLoader = imageLoader;
        }

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

        private String getItem(int position){
            return urlList.get(position);
        }

        class MeizhiHolder extends RecyclerView.ViewHolder{

            private ImageView img;

             void onBind(final String url){
                imageLoader.displayImage(url,img);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onButtonPressed(url);
                    }
                });
            }

             MeizhiHolder(View itemView) {
                super(itemView);
                img = (ImageView) itemView.findViewById(R.id.item_meizhi_img);
            }
        }
    }

    private void onMore(){
        if(!isLoading){
            isLoading = true;
            page++;
            mListener.onLoad(this,getUrl());
        }
    }

    private String getUrl(){
        String url = "";
        switch (type){
            case GANK:
                url = Constant.Gank_Meizi_Url+"10/"+page;
                break;
            case DOUBAN_ALL:break;
            case DOUBAN_LIAN:break;
            case DOUBAN_OTHER:break;
            case DOUBAN_SIWA:break;
            case DOUBAN_TUI:break;
            case DOUBAN_TUN:break;
            case DOUBAN_XIONG:break;

        }
        return url;
    }

    public void setData(ArrayList<String> data){
        if(page==0){
            urlList.clear();
        }
        urlList.addAll(data);
        adapter.notifyDataSetChanged();
    }

    public String getTitle(){
        String title = "";
        switch (type){
            case GANK:
                return"GANK";
            case DOUBAN_ALL:
                return"豆瓣全部";
            case DOUBAN_LIAN:
                return"豆瓣颜值";
            case DOUBAN_OTHER:
                return"豆瓣其他";
            case DOUBAN_SIWA:return"豆瓣丝袜";
            case DOUBAN_TUI:return"豆瓣美腿";
            case DOUBAN_TUN:return"豆瓣翘臀";
            case DOUBAN_XIONG:return"豆瓣凶器";

        }
        return title;
    }

    @Override
    public void onStart() {
        super.onStart();
        //在这里加载数据
        onRefresh();
    }

    public void selectedToTop(){
        if(recyclerView!=null)
            recyclerView.smoothScrollToPosition(0);
    }

    private class OnScrollDownListener extends RecyclerView.OnScrollListener{
        //用来标记是否正在向最后一个滑动，既是否向下滑动
        boolean isSlidingToLast = false;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            // 当不滚动时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                //获取最后一个完全显示的ItemPosition
                int[] lastVisiblePositions = manager.findLastVisibleItemPositions(new int[manager.getSpanCount()]);
                int lastVisiblePos = getMaxElem(lastVisiblePositions);
                int totalItemCount = manager.getItemCount();

                // 判断是否滚动到底部
                if (lastVisiblePos == (totalItemCount -1) && isSlidingToLast) {
                    //加载更多功能的代码
                    onMore();
                }
            }
        }
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
//            if(dy > 0){
//                //大于0表示，正在向下滚动
//                isSlidingToLast = true;
//            }else{
//                //小于等于0 表示停止或向上滚动
//                isSlidingToLast = false;
//            }
            isSlidingToLast = dy>0;
        }
    }

    private int getMaxElem(int[] arr) {
//        int size = arr.length;
        int maxVal = Integer.MIN_VALUE;
//        for (int i = 0; i < size; i++) {
//            if (arr[i]>maxVal)
//                maxVal = arr[i];
//        }
        for(int a:arr){
            if(a>maxVal)
                maxVal = a;
        }
        return maxVal;
    }

    public void onButtonPressed(String uri) {
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
        if(!isLoading){
            isLoading = true;
            page = 0;
            mListener.onLoad(this,getUrl());
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
        void onCardClick(Fragment fragment,String uri);
        void onError(Fragment fragment,String msg);
        void onLoad(Fragment fragment,String url);
    }

    /**
     * 加载类型
     * 这里使用了enum
     * 更加构造时传入的类型来选择性加载内容
     */
    public enum MeizhiType{
        NULL(-1),
        GANK(0),
        DOUBAN_ALL(1),
        DOUBAN_XIONG(2),
        DOUBAN_TUN(3),
        DOUBAN_SIWA(4),
        DOUBAN_TUI(5),
        DOUBAN_LIAN(6),
        DOUBAN_OTHER(7);
        private int value;
        MeizhiType(int value) {
            this.value = value;
        }
        public int getValue(){
            return value;
        }
        public static MeizhiType getType(int i){
            switch (i){
                case 0:
                    return GANK;
                case 1:
                    return DOUBAN_ALL;
                case 2:
                    return DOUBAN_XIONG;
                case 3:
                    return DOUBAN_TUN;
                case 4:
                    return DOUBAN_SIWA;
                case 5:
                    return DOUBAN_TUI;
                case 6:
                    return DOUBAN_LIAN;
                case 7:
                    return DOUBAN_OTHER;
            }
            return NULL;
        }
    }

}
