package xiaoliang.ltool.bean;

import java.io.Serializable;

/**
 * Created by Liuj on 2016/11/4.
 * 记事本的Bean
 */

public class NoteBean implements Serializable {
    public String title;//标题
    public String msg;//内容
    public float money;//金额
    public boolean income = false;//是否是收入
    public long time;//时间
    public int colorId;//颜色ID
    public int color;//颜色色值
    public String adress;//地址
    public boolean remind = false;//是否提醒
    public long remindAhead = 0;//提前时间

    public NoteBean() {
        this("",0XFFFFFFFF,0,false,0,"",false,0,0,"");
    }

    public NoteBean(String adress, int color, int colorId, boolean income, float money, String msg, boolean remind, long remindAhead, long time, String title) {
        this.adress = adress;
        this.color = color;
        this.colorId = colorId;
        this.income = income;
        this.money = money;
        this.msg = msg;
        this.remind = remind;
        this.remindAhead = remindAhead;
        this.time = time;
        this.title = title;
    }
}
