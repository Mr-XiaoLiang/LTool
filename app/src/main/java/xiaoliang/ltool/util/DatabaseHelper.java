package xiaoliang.ltool.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import xiaoliang.ltool.bean.NoteBean;
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
     * @param context 上下文
     * @param id id
     * @param color 颜色
     * @param name 名称
     * @return
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
    public static long addNote(Context context, NoteBean bean){
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
        return sql.insert(DBConstant.NOTE_TABLE,null,values);
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

    public static ArrayList<NoteBean> selectNote(Context context){
        SQLiteDatabase sql = DB(context);
        Cursor c = sql.rawQuery(DBConstant.SELECT_NOTE_SQL,new String[]{});
        ArrayList<NoteBean> list = new ArrayList<>();
        NoteBean bean;
        while(c.moveToNext()){
            bean = new NoteBean();
            bean.id = c.getInt(c.getColumnIndex(DBConstant.NT_id));
            bean.color = c.getInt(c.getColumnIndex("color"));
            list.add(bean);
        }
        return list;
    }

    private static int b2i(boolean b){
        return b?1:0;
    }
    private static boolean i2b(int i){
        return i>0;
    }
}
