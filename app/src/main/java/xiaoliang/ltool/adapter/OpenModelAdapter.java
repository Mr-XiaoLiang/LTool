package xiaoliang.ltool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.AppInfo;

/**
 * Created by liuj on 2016/11/10.
 * 快捷方式打开目标的适配器
 */

public class OpenModelAdapter extends BaseAdapter {

    private ArrayList<AppInfo> appInfoList;
    private LayoutInflater inflater;//得到一个LayoutInfalter对象用来导入布局

    public OpenModelAdapter(ArrayList<AppInfo> appInfoList, Context context) {
        this.appInfoList = appInfoList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(appInfoList==null||appInfoList.size()<1)
            return 0;
        return appInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        if(appInfoList==null||appInfoList.size()<1)
            return null;
        return appInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_shortcut_model,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);//绑定ViewHolder对象
        }else{
            holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
        }
        holder.onBind(appInfoList.get(position));
        return convertView;
    }

    private class ViewHolder{
        private ImageView imageView;
        private TextView name;
        private TextView pkg;

        public ViewHolder(View item){
            imageView = (ImageView) item.findViewById(R.id.item_shortcut_model_icon);
            name = (TextView) item.findViewById(R.id.item_shortcut_model_text);
            pkg = (TextView) item.findViewById(R.id.item_shortcut_model_pkg);
        }
        public void onBind(AppInfo info){
            imageView.setImageDrawable(info.appIcon);
            name.setText(info.name);
            pkg.setText(info.pkgName);
        }
    }

}
