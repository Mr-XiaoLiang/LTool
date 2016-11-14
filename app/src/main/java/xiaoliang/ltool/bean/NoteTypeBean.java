package xiaoliang.ltool.bean;

import android.graphics.Color;

/**
 * Created by liuj on 2016/11/14.
 * 笔记类型的bean
 */

public class NoteTypeBean {
    public int id;
    public int color;
    public String typeName;

    public NoteTypeBean() {
        this(-1, Color.BLACK,"");
    }

    public NoteTypeBean(int id, int color, String typeName) {
        this.id = id;
        this.color = color;
        this.typeName = typeName;
    }
}
