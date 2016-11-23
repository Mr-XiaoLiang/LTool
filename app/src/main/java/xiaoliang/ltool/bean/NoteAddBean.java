package xiaoliang.ltool.bean;

import java.io.Serializable;

/**
 * Created by liuj on 2016/11/22.
 * 在内存中编辑的笔记Bean
 */

public class NoteAddBean implements Serializable {
    public String note;//内容
    public boolean income = false;//是否是收入
    public long startTime = 0;//开始时间
    public long endTime = 0;//结束时间
    public long advance = 0;//提前时间
    public int advanceUnit = ADVANCE_UNIT_MINUTE;//提前单位
    public boolean alert = false;//提醒
    public boolean oneDay = false;//一整天
    public boolean isChecked = false;
    public int index = 0;
    public int maxIndex = 0;
    public int type= TEXT;
    public static final int LIST = 0;
    public static final int TODO = 1;
    public static final int TEXT = 2;
    public static final int MONEY = 3;
    public static final int TIME = 4;
    public static final int ADDRESS = 5;
    public static final int ADDITEM = 6;

    public static final int ADVANCE_UNIT_MINUTE = 0;
    public static final int ADVANCE_UNIT_HOUR = 1;
    public static final int ADVANCE_UNIT_DAY = 2;
    public static final int ADVANCE_UNIT_WEEK = 3;

    public NoteAddBean(int type) {
        this.type = type;
        note = "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteAddBean that = (NoteAddBean) o;

        return type == that.type;

    }

    @Override
    public int hashCode() {
        return type;
    }
}
