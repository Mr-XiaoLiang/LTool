package xiaoliang.ltool.fragment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import xiaoliang.ltool.R;
import xiaoliang.ltool.activity.LToolApplication;
import xiaoliang.ltool.constant.Constant;
import xiaoliang.ltool.constant.MeizhiType;
import xiaoliang.ltool.view.RatioImageView;

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
    private StaggeredGridLayoutManager layoutManager;

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

        private String getItem(int position){
            return urlList.get(position);
        }

        class MeizhiHolder extends RecyclerView.ViewHolder{

            private RatioImageView img;
            private CardView cardView;

             void onBind(final String url){
//                imageLoader.displayImage(url,img);
                 Glide.with(MeizhiFragment.this).load(url).centerCrop().into(img);
                 cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onButtonPressed(url);
                    }
                });
            }

             MeizhiHolder(View itemView) {
                super(itemView);
                 cardView = (CardView) itemView.findViewById(R.id.item_meizhi_card);
                 img = (RatioImageView) itemView.findViewById(R.id.item_meizhi_img);
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
                url = Constant.Gank_Meizi_Url+"20/"+page;
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

    public synchronized void setData(ArrayList<String> data){
        isLoading = false;
        swipeRefreshLayout.setRefreshing(false);
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
            layoutManager.invalidateSpanAssignments();
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
            page = 1;
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
        void onCardClick(MeizhiFragment fragment,String uri);
        void onError(MeizhiFragment fragment,String msg);
        void onLoad(MeizhiFragment fragment,String url);
    }

    public MeizhiType getType() {
        return type;
    }

    public void LoadImage(View view){
        new DownloadImageTask().execute("http://yourimageurl.png");
    }
    private class DownloadImageTask extends AsyncTask<String,Void,String>
    {
        protected String doInBackground(String...urls){
            return loadImageFromNetwork(urls[0]);
        }
        protected void onPostExecute(String result){
        }
    }
    private String loadImageFromNetwork(String url){
        try {
            URL m_url=new URL(url);
            HttpURLConnection con=(HttpURLConnection)m_url.openConnection();
            InputStream in=con.getInputStream();
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds=true;
            BitmapFactory.decodeStream(in,null,options);
            int height=options.outHeight;
            int width=options.outWidth;
            String s="高度是" + height + "宽度是" + width;
            return s;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
