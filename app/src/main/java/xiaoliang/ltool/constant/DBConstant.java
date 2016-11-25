package xiaoliang.ltool.constant;

/**
 * Created by liuj on 2016/11/14.
 * 数据库的常量类
 */

public class DBConstant {

    /**
     * 数据库名称
     */
    public static final String DB_NAME = "LTool";
    /**
     * 数据库版本
     */
    public static final int version = 1;
    /**
     * 笔记类型表结构：
     * id int PRIMARY KEY
     * color int 颜色
     * typeName varchar 类型名
     */
    public static final String NOTE_TYPE_TABLE = "NOTE_TYPE_TABLE";
    public static final String NTT_id = "id";
    public static final String NTT_color = "color";
    public static final String NTT_typeName = "typeName";
    /**
     * 笔记表结构：
     * id int PRIMARY KEY
     * oneDay int 是否全天
     * startTime varchar 开始时间(long)
     * endTime varchar  结束时间(long)
     * alert int 是否提醒
     * advance varchar 提醒提前时间(long)
     * address varchar 地址
     * money REAL 金额
     * income int 收支类型
     * title varchar 标题
     * note varchar 内容（自定义内容协议）
     * noteType int 笔记类型（ID）
     */
    public static final String NOTE_TABLE = "NOTE_TABLE";
    public static final String NT_id = "id";
    public static final String NT_oneDay = "oneDay";
    public static final String NT_startTime = "startTime";
    public static final String NT_endTime = "endTime";
    public static final String NT_alert = "alert";
    public static final String NT_advance = "advance";
    public static final String NT_address = "address";
    public static final String NT_money = "money";
    public static final String NT_income = "income";
    public static final String NT_title = "title";
    public static final String NT_note = "note";
    public static final String NT_noteType = "noteType";
    /**
     * 创建笔记表的SQL
     */
    public static final String CREATE_NOTE_TABLE_SQL = "create table "+NOTE_TABLE+"(" +
            NT_id+" INTEGER PRIMARY KEY, " +
            NT_oneDay+" int," +
            NT_startTime+" varchar," +
            NT_endTime+" varchar," +
            NT_alert+" int," +
            NT_advance+" varchar, " +
            NT_address+" varchar, " +
            NT_money+" REAL, " +
            NT_income+" int, " +
            NT_title+" varchar, " +
            NT_note+" varchar, " +
            NT_noteType+" int " +
            ");";
    /**
     * 创建类型表的SQL
     */
    public static final String CREATE_NOTE_TYPE_TABLE_SQL = "create table "+NOTE_TYPE_TABLE+"(" +
            NTT_id+" INTEGER PRIMARY KEY, " +
            NTT_color+" int, " +
            NTT_typeName+" varchar" +
            ");";
    /**
     * 查询笔记表SQL
     */
    public static final String SELECT_NOTE_SQL = "select " +
            NOTE_TABLE+"."+NT_id+", " +
            NT_oneDay+", " +
            NT_startTime+", " +
            NT_endTime+", " +
            NT_alert+", " +
            NT_advance+", " +
            NT_address+", " +
            NT_money+", " +
            NT_income+", " +
            NT_title+", " +
            NT_note+", " +
            NT_noteType+", " +
            NTT_color+
            " from "+NOTE_TABLE+
            " left join "+NOTE_TYPE_TABLE+
            " on "+ NOTE_TABLE+"."+NT_id+" = "+
            NOTE_TYPE_TABLE+"."+NTT_id+" where "+
            NT_startTime+" <= 0";

    /**
     * 查询笔记表SQL
     */
    public static final String SELECT_CALENDAR_SQL = "select " +
            NOTE_TABLE+"."+NT_id+", " +
            NT_oneDay+", " +
            NT_startTime+", " +
            NT_endTime+", " +
            NT_alert+", " +
            NT_advance+", " +
            NT_address+", " +
            NT_money+", " +
            NT_income+", " +
            NT_title+", " +
            NT_note+", " +
            NT_noteType+", " +
            NTT_color+
            " from "+NOTE_TABLE+
            " left join "+NOTE_TYPE_TABLE+
            " on "+ NOTE_TABLE+"."+NT_id+" = "+
            NOTE_TYPE_TABLE+"."+NTT_id+" where "+
            NT_startTime+" > 0";

    public static final String SELECT_NOTE_BY_ID_SQL = "select " +
            NOTE_TABLE+"."+NT_id+", " +
            NT_oneDay+", " +
            NT_startTime+", " +
            NT_endTime+", " +
            NT_alert+", " +
            NT_advance+", " +
            NT_address+", " +
            NT_money+", " +
            NT_income+", " +
            NT_title+", " +
            NT_note+", " +
            NT_noteType+", " +
            NTT_color+
            " from "+NOTE_TABLE+
            " left join "+NOTE_TYPE_TABLE+
            " on "+ NOTE_TABLE+"."+NT_id+" = "+
            NOTE_TYPE_TABLE+"."+NTT_id+" where "+
            NT_id+" = ?";
    public static final String SELECT_NOTE_TYPE_SQL = "select " +
            NTT_id+", " +
            NTT_color+", " +
            NTT_typeName+" " +
            "from "+NOTE_TYPE_TABLE;

    public static final String SELECT_LAST_NOTE_ID = "select last_insert_rowid() from "+DBConstant.NOTE_TABLE;

}
