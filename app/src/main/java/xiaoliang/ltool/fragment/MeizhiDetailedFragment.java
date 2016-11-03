package xiaoliang.ltool.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.MeizhiBean;
import xiaoliang.ltool.constant.MeizhiType;
import xiaoliang.ltool.util.HttpTaskRunnable;
import xiaoliang.ltool.util.MeizhiUtil;
import xiaoliang.ltool.util.NetTasks;
import xiaoliang.ltool.util.ToastUtil;
import xiaoliang.ltool.view.ZoomImageView;

public class MeizhiDetailedFragment extends Fragment {

    private static final String ARG_BEAN = "ARG_BEAN";
    private static final String ARG_TYPE = "ARG_TYPE";
    private static final int GET_URL = 203;
    private static final int GET_URL_ERROR = 204;
    private ZoomImageView imageView;
    private MeizhiDetailedFragmentListener listener;
    private RequestManager requestManager;
    private MeizhiBean bean;
    private MeizhiType type;
    private String url;

    public MeizhiDetailedFragment() {
    }
    public static MeizhiDetailedFragment newInstance(MeizhiBean bean,MeizhiType type) {
        MeizhiDetailedFragment fragment = new MeizhiDetailedFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BEAN,bean);
        args.putSerializable(ARG_TYPE,type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bean = (MeizhiBean) getArguments().getSerializable(ARG_BEAN);
            type = (MeizhiType) getArguments().getSerializable(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_meizhi_detailed,container,false);
        imageView = (ZoomImageView) root.findViewById(R.id.activity_meizhi_detailed_img);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestManager = Glide.with(getActivity());
        getUrl();
    }

    private void loadImg(String url){
        this.url = url;
        if(TextUtils.isEmpty(url))
            return;
        if(listener!=null)
            listener.onLoadImgae(true);
        requestManager.load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(){
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(resource);
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener(){
                            @Override
                            public void onGenerated(Palette palette) {
                                if (listener!=null)
                                    listener.onPaletteColorChange(palette.getLightMutedColor(0x41DDAF));
                            }
                        });
                        if(listener!=null)
                            listener.onLoadImgae(false);
                    }
                });
    }

    public interface MeizhiDetailedFragmentListener{
        void onPaletteColorChange(int color);
        void onLoadImgae(boolean b);
    }

    public void setUrl(MeizhiBean bean){
        if(bean!=null&&!bean.url.equals(this.bean.url)){
            this.bean = bean;
            getUrl();
        }
    }

    private void getUrl(){
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

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_URL:
                    String url = (String) msg.obj;
                    if(!TextUtils.isEmpty(url)){
                        loadImg(url);
                        break;
                    }
                case GET_URL_ERROR:
                    ToastUtil.T(getActivity(),"对不起，获取图片地址失败");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MeizhiDetailedFragmentListener) {
            listener = (MeizhiDetailedFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MeizhiDetailedFragmentListener");
        }
    }
    public String getImageUrl(){
        return url;
    }
}
