package xiaoliang.ltool.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import xiaoliang.ltool.bean.MeizhiBean;

/**
 * Created by liuj on 2016/10/3.
 * 妹纸图的数据解析类
 */

public class MeizhiUtil {

    public static ArrayList<MeizhiBean> getGankImgUrl(String json){
        ArrayList<MeizhiBean> beans = new ArrayList<>();
        if(json==null||json.length()<1) {
            return beans;
        }
        try{
            JSONObject jsonObject = new JSONObject(json);
            if(jsonObject.getBoolean("error")){
                return beans;
            }
            JSONArray results = jsonObject.getJSONArray("results");
            for(int i = 0;i<results.length();i++){
                MeizhiBean bean = new MeizhiBean(results.getJSONObject(i).getString("url"));
                beans.add(bean);
            }
        }catch (Exception e){
            Log.d("getGankImgUrl",e.getMessage());
        }
        return beans;
    }

    public static ArrayList<MeizhiBean> getDoubanImgUrl(String json){
        ArrayList<MeizhiBean> beans = new ArrayList<>();
        try{
            json = json.substring(json.indexOf("<body>"),json.indexOf("</body>"));
            String[] imgs = json.split("<div class=\"img_single\">");
            for(String img : imgs){
                if(!img.contains("bmiddle"))
                    continue;
                MeizhiBean bean = new MeizhiBean();
                img = img.substring(img.indexOf("href=\"")+6);
                bean.from = img.substring(0,img.indexOf("\""));
                img = img.substring(img.indexOf("title=\"")+7);
                bean.title = img.substring(0,img.indexOf("\""));
                img = img.substring(img.indexOf("bmiddle/")+8);
                bean.url = "http://ww2.sinaimg.cn/large/"+img.substring(0,img.indexOf("\""));
                beans.add(bean);
//                Log.d("getDoubanImgUrl",bean.toString());
            }
        }catch (Exception e){
            Log.d("getDoubanImgUrl",e.getMessage());
        }
        return beans;
    }

}
