package xiaoliang.ltool.bean;

import android.graphics.Color;

import java.io.Serializable;

/**
 * Created by Liuj on 2016/11/4.
 * 记事本的Bean
 */

public class NoteBean implements Serializable {
    public int id;
    public String title;//标题
    public String note;//内容
    public float money;//金额
    public boolean income = false;//是否是收入
    public long startTime;//开始时间
    public long endTime;//结束时间
    public long advance;//提前时间
    public int noteType;//颜色ID
    public int color;//颜色色值
    public boolean alert;//提醒
    public boolean oneDay;//一整天
    public String address;//地址

    public NoteBean() {
        this(-1,"","",0,false,-1,-1,-1,-1,Color.BLACK,false,false,"");
    }

    public NoteBean(int id,String title, String note, float money, boolean income, long startTime, long endTime, long advance, int noteType, int color, boolean alert, boolean oneDay, String address) {
        this.id = id;
        this.title = title;
        this.note = note;
        this.money = money;
        this.income = income;
        this.startTime = startTime;
        this.endTime = endTime;
        this.advance = advance;
        this.noteType = noteType;
        this.color = color;
        this.alert = alert;
        this.oneDay = oneDay;
        this.address = address;
    }
}
