package xiaoliang.ltool.adapter.note;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.NoteListBean;
import xiaoliang.ltool.fragment.note.NoteFragment;
import xiaoliang.ltool.listener.LItemTouchHelper;

/**
 * Created by liuj on 2016/11/28.
 * 列表Item的Holder
 */

public class ListHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnClickListener {

    private TextView dataView,dayView,timeView;
    private CardView cardView;
    private ImageView typeColorView;
    private TextView titleView;
    private TextView moneyView;
    private TextView msgView;
//    private ImageView itemTypeView;
    private Calendar calendar,lastCalendar;
    private LItemTouchHelper helper;
    private ColorDrawable colorDrawable;
    private DecimalFormat decimalFormat;

    public ListHolder(View itemView) {
        super(itemView);
        dataView = (TextView) itemView.findViewById(R.id.item_note_month);
        dayView = (TextView) itemView.findViewById(R.id.item_note_day);
        cardView = (CardView) itemView.findViewById(R.id.item_note_card);
        typeColorView = (ImageView) itemView.findViewById(R.id.item_note_color);
        titleView = (TextView) itemView.findViewById(R.id.item_note_title);
        moneyView = (TextView) itemView.findViewById(R.id.item_note_money);
        msgView = (TextView) itemView.findViewById(R.id.item_note_msg);
        timeView = (TextView) itemView.findViewById(R.id.item_note_time);
//        itemTypeView = (ImageView) itemView.findViewById(R.id.item_note_icon);
        calendar = Calendar.getInstance();
        decimalFormat = new DecimalFormat("#0.00");
        colorDrawable = new ColorDrawable();
        typeColorView.setImageDrawable(colorDrawable);
        lastCalendar = Calendar.getInstance();
        itemView.setOnLongClickListener(this);
        cardView.setOnClickListener(this);
    }
    public void onBind(NoteListBean bean, LItemTouchHelper helper) {
        this.helper = helper;
        calendar.setTimeInMillis(bean.time);
        lastCalendar.setTimeInMillis(bean.lastTime);
        if(bean.holderType== NoteFragment.TYPE_CALENDAR){
            if(calendar.get(Calendar.YEAR)!=lastCalendar.get(Calendar.YEAR)||calendar.get(Calendar.MONTH)!=lastCalendar.get(Calendar.MONTH)){
                dataView.setVisibility(View.VISIBLE);
                dataView.setText(getDateFormat(Calendar.YEAR)+"/"+getDateFormat(Calendar.MONTH));
            }else{
                dataView.setVisibility(View.GONE);
            }
            if(calendar.get(Calendar.DAY_OF_MONTH)!=lastCalendar.get(Calendar.DAY_OF_MONTH)){
                dayView.setVisibility(View.VISIBLE);
                dayView.setText(getDateFormat(Calendar.DAY_OF_MONTH));
            }else{
                dayView.setVisibility(View.INVISIBLE);
            }
            timeView.setText(getDateFormat(Calendar.HOUR_OF_DAY)+":"+getDateFormat(Calendar.MINUTE));
        }else{
            dataView.setVisibility(View.GONE);
            dayView.setVisibility(View.GONE);
            timeView.setText(getDateFormat(Calendar.YEAR)+"/"+getDateFormat(Calendar.MONTH)+"/"+
                    getDateFormat(Calendar.DAY_OF_MONTH)+" "+
                    getDateFormat(Calendar.HOUR_OF_DAY)+":"+getDateFormat(Calendar.MINUTE));
        }
        colorDrawable.setColor(bean.typeColor);
        titleView.setText(bean.name);
        if(!TextUtils.isEmpty(bean.money)&&Double.parseDouble(bean.money)>=0.01){
            moneyView.setText(decimalFormat.format(Double.parseDouble(bean.money)));
        }else{
            moneyView.setText("");
        }
        msgView.setText(bean.msg);

    }

    private String getDateFormat(int type){
        String out;
        switch (type){
            case Calendar.MONTH:
                out = calendar.get(type)+1+"";
                break;
            default:
                out = calendar.get(type)+"";
                break;
        }
        if(out.length()<2){
            out = "0"+out;
        }
        return out;
    }

    public void setTypeface(Context context) {
        //将字体文件保存在assets/fonts/目录下，创建Typeface对象
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/century_gothic_std.otf");
        //使用字体
        setTypeface(typeFace);
    }
    public void setTypeface(Typeface typeFace) {
        //使用字体
        if(dataView!=null)
            dataView.setTypeface(typeFace);
        if(dayView!=null)
            dayView.setTypeface(typeFace);
        if(timeView!=null)
            timeView.setTypeface(typeFace);
    }

    @Override
    public void onClick(View view) {
        if(helper!=null)
            helper.onItemViewClick(this,view);
    }

    @Override
    public boolean onLongClick(View view) {
        if(helper!=null){
            helper.startSwipe(this);
            return true;
        }
        return false;
    }
}
