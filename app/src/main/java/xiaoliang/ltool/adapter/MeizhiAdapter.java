package xiaoliang.ltool.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.MeizhiBean;
import xiaoliang.ltool.view.RatioImageView;

/**
 * Created by liuj on 2016/10/7.
 * 妹子图的Adapter
 */

public class MeizhiAdapter extends RecyclerView.Adapter<MeizhiAdapter.MeizhiHolder>{

    private ArrayList<MeizhiBean> beans;
    private Activity context;
    private Fragment fragment;
    private OnCardClickListener listener;
    private RequestManager requestManager;

    public MeizhiAdapter(ArrayList<MeizhiBean> beans, Fragment fragment, OnCardClickListener listener) {
        this.beans = beans;
        this.fragment = fragment;
        this.listener = listener;
        requestManager = Glide.with(fragment);
    }

    public MeizhiAdapter(ArrayList<MeizhiBean> beans, Activity context, OnCardClickListener listener) {
        this.beans = beans;
        this.context = context;
        this.listener = listener;
        requestManager = Glide.with(context);
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
        return beans.size();
    }

    private MeizhiBean getItem(int position){
        return beans.get(position);
    }

    class MeizhiHolder extends RecyclerView.ViewHolder{

        private ImageView img;
        private TextView text;
        private CardView cardView;

        void onBind(final MeizhiBean bean){
            if (null!=bean.title&&!"".equals(bean.title)){
                text.setVisibility(View.VISIBLE);
                text.setText(bean.title);
            }else{
                text.setVisibility(View.GONE);
            }
            requestManager.load(bean.url).centerCrop().into(img);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                        listener.OnCardClick(bean);
                }
            });
        }
        MeizhiHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.item_meizhi_card);
            img = (ImageView) itemView.findViewById(R.id.item_meizhi_img);
            text = (TextView) itemView.findViewById(R.id.item_meizhi_text);
        }
    }

    public interface OnCardClickListener{
        void OnCardClick(MeizhiBean bean);
    }

}