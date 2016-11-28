package xiaoliang.ltool.bean;

import android.graphics.Color;

/**
 * Created by liuj on 2016/11/28.
 * 笔记列表的bean
 */
public class NoteListBean {
    public int holderType;
    public String name;
    public String msg;
    public int itemType;
    public long time;
    public long lastTime;
    public int id;
    public int typeColor;
    public String money;

    public NoteListBean(int holderType) {
        this.holderType = holderType;
        this.name = "";
        this.msg = "";
        this.itemType = -1;
        this.time = 0;
        this.lastTime = 0;
        this.id = 0;
        this.money = "";
        this.typeColor = Color.BLACK;
    }
}
