package xiaoliang.ltool.fragment.note;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import xiaoliang.ltool.R;
import xiaoliang.ltool.adapter.NoteListAdapter;
import xiaoliang.ltool.bean.NoteListBean;
import xiaoliang.ltool.listener.LItemTouchCallback;
import xiaoliang.ltool.listener.LItemTouchHelper;
import xiaoliang.ltool.listener.OnNoteFragmentListener;
import xiaoliang.ltool.util.NoteUtil;

public class NoteFragment extends NoteInterface implements
        SwipeRefreshLayout.OnRefreshListener,
        LItemTouchCallback.OnItemTouchCallbackListener,
        NoteUtil.GetNoteListCallback{

    private OnNoteFragmentListener mListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private String title;
    private int type;
    private static final String ARG_TYPE = "ARG_TYPE";
    public static final int TYPE_NOTE = 0;
    public static final int TYPE_CALENDAR = 1;
    private NoteListAdapter adapter;
    private ArrayList<NoteListBean> beans;
    private boolean onRefresh = false;
    private View nullList;
    private LinearLayoutManager linearLayoutManager;

    public NoteFragment() {
        // Required empty public constructor
    }

    public static NoteFragment newInstance(int type) {
        NoteFragment fragment = new NoteFragment();
        switch (type){
            case TYPE_NOTE:
                fragment.setTitle("备忘录");
                break;
            case TYPE_CALENDAR:

                fragment.setTitle("日程表");
                break;
        }
        Bundle args = new Bundle();
        args.putSerializable(ARG_TYPE,type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = (int) getArguments().getSerializable(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_note, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.fragment_note_swiperefreshlayout);
        recyclerView = (RecyclerView) root.findViewById(R.id.fragment_note_recyclerview);
        nullList = root.findViewById(R.id.fragment_note_null);
        recyclerView.addOnScrollListener(new OnNoteListScrollListener());
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);
        beans = new ArrayList<>();//初始化数据集
        linearLayoutManager = new LinearLayoutManager(getContext());//初始化列表layout管理器
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设定为纵向
        recyclerView.setLayoutManager(linearLayoutManager);//将管理器设置进列表
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置列表item动画
        LItemTouchHelper helper = LItemTouchHelper.newInstance(recyclerView,this);//设置控制帮助类
        adapter = new NoteListAdapter(getContext(),beans,helper);//初始化列表适配器
        recyclerView.setAdapter(adapter);//为列表设置适配器
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        swipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    public void onButtonPressed(int noteId) {
        if (mListener != null) {
            mListener.onNoteClick(noteId);
        }
    }

    private void onScrollStateChanged(boolean b){
        if(mListener!=null){
            mListener.OnScrollChange(b);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNoteFragmentListener) {
            mListener = (OnNoteFragmentListener) context;
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

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle(){
        return title;
    }

    @Override
    public void onRefresh() {
        if(!onRefresh){
            NoteUtil.getNoteListOnBackground(getContext(),type,this);
            onRefresh = true;
        }
    }

    @Override
    public void onSwiped(int adapterPosition) {
        if(beans!=null)
            beans.remove(adapterPosition);
        if(adapter!=null){
            adapter.notifyItemRemoved(adapterPosition);
        }
    }

    @Override
    public boolean onMove(int srcPosition, int targetPosition) {
        return false;
    }

    @Override
    public void onItemViewClick(RecyclerView.ViewHolder holder, View v) {
        onButtonPressed(beans.get(holder.getAdapterPosition()).id);
    }

    @Override
    public void onGetNoteListCallback(ArrayList<NoteListBean> beans) {
        onRefresh = false;
        this.beans.clear();
        this.beans.addAll(beans);
        handler.sendEmptyMessage(200);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 200:
                    swipeRefreshLayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                    if(beans.size()>0){
                        nullList.setVisibility(View.INVISIBLE);
                    }else{
                        nullList.setVisibility(View.VISIBLE);
                    }
                    if(type==TYPE_CALENDAR){
                        selectedToToday();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
    //滚动列表到当前最近的时间
    private void selectedToToday(){
        int index = 0;
        long gap = Long.MAX_VALUE;
        long now = System.currentTimeMillis();
        for(int i = 0;i<beans.size();i++){
            long time = Math.abs(beans.get(i).time-now);
            if(gap>time){
                gap = time;
                index = i;
            }
        }
        linearLayoutManager.scrollToPositionWithOffset(index,-recyclerView.getHeight());
    }

    public class OnNoteListScrollListener extends OnScrollListener{
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if(newState==RecyclerView.SCROLL_STATE_IDLE){
                NoteFragment.this.onScrollStateChanged(false);
            }else{
                NoteFragment.this.onScrollStateChanged(true);
            }
        }
    }
}
