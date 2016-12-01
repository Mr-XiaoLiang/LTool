package xiaoliang.ltool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.TranslationLanguageBean;

/**
 * Created by liuj on 2016/11/30.
 * 语言选择下拉框的适配器
 */

public class LanguageSpinnerAdapter extends BaseAdapter {

    private List<TranslationLanguageBean> beans;
    private LayoutInflater inflater;//得到一个LayoutInfalter对象用来导入布局

    public LanguageSpinnerAdapter(List<TranslationLanguageBean> beans, Context context) {
        this.beans = beans;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(beans==null)
            return 0;
        return beans.size();
    }

    @Override
    public Object getItem(int i) {
        return beans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Holder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_simple_spinner_text,viewGroup,false);
            holder = new Holder(convertView);
            convertView.setTag(holder);//绑定ViewHolder对象
        }else{
            holder = (Holder)convertView.getTag();//取出ViewHolder对象
        }
        holder.onBind(beans.get(position));
        return convertView;
    }
    private class Holder{
        TextView textView;

        Holder(View itemView) {
            textView = (TextView) itemView.findViewById(R.id.item_simple_spinner_text_text);
        }
        public void onBind(TranslationLanguageBean bean){
            textView.setText(bean.name);
        }
    }
}
