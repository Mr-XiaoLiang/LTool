package xiaoliang.ltool.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import xiaoliang.ltool.bean.NoteBean;
import xiaoliang.ltool.bean.NoteTypeBean;
import xiaoliang.ltool.constant.DBConstant;

/**
 * Created by LiuJ on 2016/6/16.
 * 数据库帮助类
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase database;


    private DatabaseHelper(Context context) {
        super(context, DBConstant.DB_NAME,null,DBConstant.version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBConstant.CREATE_NOTE_TABLE_SQL);
        db.execSQL(DBConstant.CREATE_NOTE_TYPE_TABLE_SQL);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    private static synchronized DatabaseHelper getHelper(Context context){
        if(databaseHelper == null){
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    private static synchronized SQLiteDatabase DB(Context context){
        if(database==null){
            database = DatabaseHelper.getHelper(context).getWritableDatabase();
        }
        return database;
    }

    /**
     * 添加一条笔记类型
     */
    public static long addNoteType(Context context,NoteTypeBean bean){
        return addNoteType(context,bean.color,bean.typeName);
    }

    /**
     * 添加一条笔记类型
     * @param context 上下文
     * @param color 颜色
     * @param name 类型名
     * @return 当前插入记录的ID
     */
    public static synchronized long addNoteType(Context context,int color,String name){
        SQLiteDatabase sql = DB(context);
        ContentValues values = new ContentValues();
        values.put(DBConstant.NTT_color,color);
        values.put(DBConstant.NTT_typeName,name);
        return sql.insert(DBConstant.NOTE_TYPE_TABLE,null,values);
    }

    /**
     * 删除一条笔记类型记录
     * @param context 上下文
     * @param id id
     * @return 受影响行数
     */
    public static int delNoteType(Context context,int id){
        return DB(context).delete(DBConstant.NOTE_TYPE_TABLE,DBConstant.NTT_id+" = ?",new String[]{String.valueOf(id)});
    }

    /**
     * 更新一条笔记类型记录
     */
    public static int updateNoteType(Context context,NoteTypeBean bean){
        return updateNoteType(context,bean.id,bean.color,bean.typeName);
    }

    /**
     * 更新一条笔记类型记录
     * @param context 上下文
     * @param id id
     * @param color 颜色
     * @param name 名称
     */
    public static int updateNoteType(Context context,int id,int color,String name){
        SQLiteDatabase sql = DB(context);
        ContentValues values = new ContentValues();
        values.put(DBConstant.NTT_color,color);
        values.put(DBConstant.NTT_typeName,name);
        return sql.update(DBConstant.NOTE_TYPE_TABLE,values,DBConstant.NTT_id+" = ?",new String[]{String.valueOf(id)});
    }

    /**
     * 添加一条笔记
     * @param context 上下文
     * @param bean bean
     * @return id
     */
    public static int addNote(Context context, NoteBean bean){
        SQLiteDatabase sql = DB(context);
        ContentValues values = new ContentValues();
        values.put(DBConstant.NT_title,bean.title);
        values.put(DBConstant.NT_note,bean.note);
        values.put(DBConstant.NT_startTime,bean.startTime+"");
        values.put(DBConstant.NT_endTime,bean.endTime+"");
        values.put(DBConstant.NT_oneDay,b2i(bean.oneDay));
        values.put(DBConstant.NT_alert,b2i(bean.alert));
        values.put(DBConstant.NT_advance,bean.advance+"");
        values.put(DBConstant.NT_address,bean.address);
        values.put(DBConstant.NT_noteType,bean.noteType);
        values.put(DBConstant.NT_money,bean.money);
        values.put(DBConstant.NT_income,bean.income);
        sql.insert(DBConstant.NOTE_TABLE,null,values);
        Cursor cursor = sql.rawQuery(DBConstant.SELECT_LAST_NOTE_ID,null);
        int id = 0;
        if(cursor.moveToFirst())
            id = cursor.getInt(0);
        return id;
    }

    /**
     * 更新一条笔记记录
     * @param context 上下文
     * @param bean bean
     * @return 影响行数
     */
    public static int updateNote(Context context, NoteBean bean){
        SQLiteDatabase sql = DB(context);
        ContentValues values = new ContentValues();
        values.put(DBConstant.NT_title,bean.title);
        values.put(DBConstant.NT_note,bean.note);
        values.put(DBConstant.NT_startTime,bean.startTime+"");
        values.put(DBConstant.NT_endTime,bean.endTime+"");
        values.put(DBConstant.NT_oneDay,b2i(bean.oneDay));
        values.put(DBConstant.NT_alert,b2i(bean.alert));
        values.put(DBConstant.NT_advance,bean.advance+"");
        values.put(DBConstant.NT_address,bean.address);
        values.put(DBConstant.NT_noteType,bean.noteType);
        values.put(DBConstant.NT_money,bean.money);
        values.put(DBConstant.NT_income,bean.income);
        return sql.update(DBConstant.NOTE_TABLE,values,DBConstant.NT_id+" = ?",new String[]{String.valueOf(bean.id)});
    }

    /**
     * 删除一条笔记记录
     * @param context 上下文
     * @param id id
     * @return 删除的行数
     */
    public static int deleteNote(Context context,int id){
        SQLiteDatabase sql = DB(context);
        return  sql.delete(DBConstant.NOTE_TABLE,DBConstant.NT_id+" = ?",new String[]{String.valueOf(id)});
    }

    /**
     * 查询所有笔记
     * @param context 上下文
     * @return
     */
    public static ArrayList<NoteBean> selectNote(Context context){
        SQLiteDatabase sql = DB(context);
        Cursor c = sql.rawQuery(DBConstant.SELECT_NOTE_SQL,new String[]{});
        ArrayList<NoteBean> list = new ArrayList<>();
        putDate(c,list);
        c.close();
        return list;
    }

    /**
     * 查询所有日程
     * @param context 上下文
     */
    public static ArrayList<NoteBean> selectCalendar(Context context){
        SQLiteDatabase sql = DB(context);
        Cursor c = sql.rawQuery(DBConstant.SELECT_CALENDAR_SQL,new String[]{});
        ArrayList<NoteBean> list = new ArrayList<>();
        putDate(c,list);
        c.close();
        return list;
    }

    /**
     * 根据ID查询某一个笔记
     */
    public static NoteBean findNoteById(Context context,int id){
        SQLiteDatabase sql = DB(context);
        NoteBean bean = new NoteBean();
        Cursor c = sql.rawQuery(DBConstant.SELECT_NOTE_BY_ID_SQL,new String[]{String.valueOf(id)});
        if(c.moveToNext()){
            bean.id = c.getInt(c.getColumnIndex(DBConstant.NT_id));
            bean.color = c.getInt(c.getColumnIndex(DBConstant.NTT_color));
            bean.title = c.getString(c.getColumnIndex(DBConstant.NT_title));
            bean.note = c.getString(c.getColumnIndex(DBConstant.NT_note));
            bean.noteType = c.getInt(c.getColumnIndex(DBConstant.NT_noteType));
            bean.address = c.getString(c.getColumnIndex(DBConstant.NT_address));
            bean.advance = c.getInt(c.getColumnIndex(DBConstant.NT_advance));
            bean.alert = i2b(c.getInt(c.getColumnIndex(DBConstant.NT_alert)));
            bean.endTime = Long.valueOf(c.getString(c.getColumnIndex(DBConstant.NT_endTime)));
            bean.income = i2b(c.getInt(c.getColumnIndex(DBConstant.NT_income)));
            bean.startTime = Long.valueOf(c.getString(c.getColumnIndex(DBConstant.NT_startTime)));
            bean.oneDay = i2b(c.getInt(c.getColumnIndex(DBConstant.NT_oneDay)));
        }
        c.close();
        return bean;
    }

    /**
     * 遍历数据
     * @param c 数据来源
     * @param list 结果集
     */
    private static void putDate(Cursor c,ArrayList<NoteBean> list){
        NoteBean bean;
        while(c.moveToNext()){
            bean = new NoteBean();
            bean.id = c.getInt(c.getColumnIndex(DBConstant.NT_id));
            bean.color = c.getInt(c.getColumnIndex(DBConstant.NTT_color));
            bean.title = c.getString(c.getColumnIndex(DBConstant.NT_title));
            bean.note = c.getString(c.getColumnIndex(DBConstant.NT_note));
            bean.noteType = c.getInt(c.getColumnIndex(DBConstant.NT_noteType));
            bean.address = c.getString(c.getColumnIndex(DBConstant.NT_address));
            bean.advance = c.getInt(c.getColumnIndex(DBConstant.NT_advance));
            bean.alert = i2b(c.getInt(c.getColumnIndex(DBConstant.NT_alert)));
            bean.endTime = Long.valueOf(c.getString(c.getColumnIndex(DBConstant.NT_endTime)));
            bean.income = i2b(c.getInt(c.getColumnIndex(DBConstant.NT_income)));
            bean.startTime = Long.valueOf(c.getString(c.getColumnIndex(DBConstant.NT_startTime)));
            bean.oneDay = i2b(c.getInt(c.getColumnIndex(DBConstant.NT_oneDay)));
            list.add(bean);
        }
    }

    /**
     * 查询笔记类型列表
     * @param context
     * @return
     */
    public static ArrayList<NoteTypeBean> selectNoteType(Context context){
        SQLiteDatabase sql = DB(context);
        Cursor c = sql.rawQuery(DBConstant.SELECT_NOTE_TYPE_SQL,new String[]{});
        ArrayList<NoteTypeBean> list = new ArrayList<>();
        NoteTypeBean bean;
        while(c.moveToNext()){
            bean = new NoteTypeBean();
            bean.id = c.getInt(c.getColumnIndex(DBConstant.NTT_id));
            bean.color = c.getInt(c.getColumnIndex(DBConstant.NTT_color));
            bean.typeName = c.getString(c.getColumnIndex(DBConstant.NTT_typeName));
            list.add(bean);
        }
        c.close();
        return  list;
    }

    private static int b2i(boolean b){
        return b?1:0;
    }
    private static boolean i2b(int i){
        return i>0;
    }
}
