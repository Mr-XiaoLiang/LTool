package xiaoliang.ltool.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import xiaoliang.ltool.bean.NoteAddBean;
import xiaoliang.ltool.bean.NoteBean;
import xiaoliang.ltool.bean.NoteListBean;
import xiaoliang.ltool.fragment.note.NoteFragment;

/**
 * Created by liuj on 2016/11/25.
 * 笔记编码解码代码
 */

public class NoteUtil {

    private static final String BEAN_TYPE = "BEAN_TYPE";
    private static final String BEAN_STATUS = "BEAN_STATUS";
    private static final String BEAN_NOTE = "BEAN_NOTE";
    private static long UNIT_MINUTES = 1000*60;
    private static long UNIT_HOURS = UNIT_MINUTES*60;
    private static long UNIT_DAY = UNIT_HOURS*24;
    private static long UNIT_WEEK = UNIT_DAY*7;

    public static NoteBean coding(ArrayList<NoteAddBean> beans){
        NoteBean bean = new NoteBean();
        try{
            JSONArray array = new JSONArray();
            for(NoteAddBean b : beans){
                if(b.type==NoteAddBean.LIST||b.type==NoteAddBean.TODO||b.type==NoteAddBean.TEXT){
                    JSONObject object = new JSONObject();
                    object.put(BEAN_TYPE,b.type);
                    object.put(BEAN_STATUS,b.isChecked);
                    object.put(BEAN_NOTE,b.note);
                    array.put(object);
                }else if(b.type==NoteAddBean.TIME){
                    bean.oneDay = b.oneDay;
                    bean.startTime = b.startTime;
                    bean.endTime = b.endTime;
                    bean.advance = codingAdvanceTime(b.advance,b.advanceUnit);
                    bean.alert = b.alert;
                }else if(b.type==NoteAddBean.MONEY){
                    bean.money = Float.parseFloat(b.note);
                    bean.income = b.income;
                }else if(b.type==NoteAddBean.ADDRESS){
                    bean.address = b.note;
                }//排除无用bean
            }
            bean.note = array.toString();
        }catch (Exception e){
            Log.e("NoteUtil.coding",e.getMessage());
        }
        return bean;
    }

    public static ArrayList<NoteAddBean> decoding(NoteBean bean){
        ArrayList<NoteAddBean> beans = new ArrayList<>();
        try{
            JSONArray array = new JSONArray(bean.note);
            NoteAddBean b;
            for(int i = 0;i<array.length();i++){
                JSONObject object = array.getJSONObject(i);
                b = new NoteAddBean(object.getInt(BEAN_TYPE));
                b.note = object.getString(BEAN_NOTE);
                b.isChecked = object.getBoolean(BEAN_STATUS);
                beans.add(b);
            }
            beans.add(new NoteAddBean(NoteAddBean.ADDITEM));
            if(!TextUtils.isEmpty(bean.address)){
                b = new NoteAddBean(NoteAddBean.ADDRESS);
                b.note = bean.address;
                beans.add(b);
            }
            if(bean.startTime>0||bean.startTime!=bean.endTime){
                b = new NoteAddBean(NoteAddBean.TIME);
                int[] ad = decodingAdvanceTime(bean.advance);
                b.advance = ad[0];
                b.advanceUnit = ad[1];
                b.alert = bean.alert;
                b.oneDay = bean.oneDay;
                b.startTime = bean.startTime;
                b.endTime = bean.endTime;
                beans.add(b);
            }
            if(bean.money!=0){
                b = new NoteAddBean(NoteAddBean.MONEY);
                b.note = String.valueOf(bean.money);
                b.income = bean.income;
                beans.add(b);
            }
        }catch (Exception e){
            Log.e("NoteUtil.decoding",e.getMessage());
        }
        return beans;
    }

    private static long codingAdvanceTime(long num,int unit){
        long time = num;
        switch (unit){
            case NoteAddBean.ADVANCE_UNIT_MINUTE:
                time *= UNIT_MINUTES;
                break;
            case NoteAddBean.ADVANCE_UNIT_HOUR:
                time *= UNIT_HOURS;
                break;
            case NoteAddBean.ADVANCE_UNIT_DAY:
                time *= UNIT_DAY;
                break;
            case NoteAddBean.ADVANCE_UNIT_WEEK:
                time *= UNIT_WEEK;
                break;
            default:
                time = 0;
                break;
        }
        return time;
    }

    private static int[] decodingAdvanceTime(long advance){
        int[] time = new int[2];
        if(advance==0){
            time[0] = 0;
            time[1] = NoteAddBean.ADVANCE_UNIT_MINUTE;
        }else if(advance%UNIT_WEEK==0){
            time[0] = (int) (advance/UNIT_WEEK);
            time[1] = NoteAddBean.ADVANCE_UNIT_WEEK;
        }else if(advance%UNIT_DAY==0){
            time[0] = (int) (advance/UNIT_DAY);
            time[1] = NoteAddBean.ADVANCE_UNIT_DAY;
        }else if(advance%UNIT_HOURS==0){
            time[0] = (int) (advance/UNIT_HOURS);
            time[1] = NoteAddBean.ADVANCE_UNIT_HOUR;
        }else if(advance%UNIT_MINUTES==0){
            time[0] = (int) (advance/UNIT_MINUTES);
            time[1] = NoteAddBean.ADVANCE_UNIT_MINUTE;
        }
            return time;
    }

    public static class GetNoteDetailRunnable implements Runnable{

        private Context context;
        private int noteId;
        private GetNoteDetailCallback callback;

        public GetNoteDetailRunnable(Context context, int noteId, GetNoteDetailCallback callback) {
            this.context = context;
            this.noteId = noteId;
            this.callback = callback;
        }

        @Override
        public void run() {
            if(callback!=null){
                NoteBean noteBean = DatabaseHelper.findNoteById(context,noteId);
                ArrayList<NoteAddBean> beans = NoteUtil.decoding(noteBean);
                callback.onGetNoteCallback(noteId,noteBean.title,noteBean.noteType,noteBean.color,beans);
            }
        }
    }

    public static class SaveNoteDetailRunnable implements Runnable{
        private Context context;
        private int id;
        private String title;
        private int typeId;
        private ArrayList<NoteAddBean> beans;
        private SaveNoteDetailCallback callback;

        public SaveNoteDetailRunnable(Context context, int id, String title, int typeId, ArrayList<NoteAddBean> beans, SaveNoteDetailCallback callback) {
            this.context = context;
            this.id = id;
            this.title = title;
            this.typeId = typeId;
            this.beans = beans;
            this.callback = callback;
        }

        @Override
        public void run() {
            NoteBean noteBean = NoteUtil.coding(beans);
            noteBean.id = id;
            noteBean.title = title;
            noteBean.noteType = typeId;
            if(id>0){
                int lines = DatabaseHelper.updateNote(context,noteBean);
                if(callback!=null)
                    callback.onSaveNoteCallback(lines);
            }else{
                int ids = DatabaseHelper.addNote(context,noteBean);
                if(callback!=null)
                    callback.onSaveNoteCallback(ids);
            }
        }
    }

    public interface GetNoteDetailCallback{
        void onGetNoteCallback(int id,String title,int typeId,int typeColor,ArrayList<NoteAddBean> beans);
    }

    public interface SaveNoteDetailCallback{
        void onSaveNoteCallback(int i);
    }

    /**
     * 获取笔记
     */
    public static void getNoteDetail(Context context, int noteId, GetNoteDetailCallback callback){
        HttpUtil.getThread(new GetNoteDetailRunnable(context,noteId,callback));
    }

    /**
     * 储存笔记
     */
    public static void saveNoteDetail(Context context, int id, String title, int typeId, ArrayList<NoteAddBean> beans, SaveNoteDetailCallback callback){
        HttpUtil.getThread(new SaveNoteDetailRunnable(context,id,title,typeId,beans,callback));
    }


    public static class GetNoteListRunnable implements Runnable{

        private Context context;
        private int type;
        private GetNoteListCallback callback;

        public GetNoteListRunnable(Context context, int type, GetNoteListCallback callback) {
            this.context = context;
            this.type = type;
            this.callback = callback;
        }

        @Override
        public void run() {
            if(callback!=null)
                callback.onGetNoteListCallback(getNoteList(context,type));
        }
    }

    public interface GetNoteListCallback{
        void onGetNoteListCallback(ArrayList<NoteListBean> beans);
    }

    /**
     * 获取笔记列表
     * @param context
     * @param type
     * @return
     */
    public static ArrayList<NoteListBean> getNoteList(Context context, int type){
        ArrayList<NoteListBean> list = new ArrayList<>();
        ArrayList<NoteBean> notes = null;
        switch (type){
            case NoteFragment.TYPE_NOTE:
                notes = DatabaseHelper.selectNote(context);
                break;
            case NoteFragment.TYPE_CALENDAR:
                notes = DatabaseHelper.selectCalendar(context);
                break;
        }
        if(notes==null)
            return list;
        for(NoteBean b:notes){
            NoteListBean bean = new NoteListBean(type);
            bean.name = b.title;
            bean.msg = getNoteSynopsis(b.note);
            bean.money = String.valueOf(b.money);
            switch (type){
                case NoteFragment.TYPE_NOTE:
                    bean.time = b.createTime;
                    break;
                case NoteFragment.TYPE_CALENDAR:
                    bean.time = b.startTime;
                    break;
            }

            bean.id = b.id;
            bean.typeColor = b.color;
            bean.holderType = type;
            bean.itemType = 0;
            list.add(bean);
        }
        return  list;
    }

    private static String getNoteSynopsis(String json){
        String out = "";
        try{
            JSONArray array = new JSONArray(json);
            for(int i = 0;i<array.length()&&i<5;i++){//最多拿前5条记录
                out += array.getJSONObject(i).getString(BEAN_NOTE);
                out += ";";
            }
        }catch (Exception e){
            Log.e("getNoteSynopsis",e.getMessage());
        }
        return out;
    }

    /**
     * 异步线程获取笔记列表
     * @param context
     * @param type
     * @param callback
     */
    public static void getNoteListOnBackground(Context context, int type, GetNoteListCallback callback){
        HttpUtil.getThread(new GetNoteListRunnable(context,type,callback));
    }

}
