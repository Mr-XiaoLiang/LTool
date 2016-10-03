package xiaoliang.ltool.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by liuj on 2016/10/3.
 * 妹纸图的数据解析类
 */

public class MeizhiUtil {

    public static ArrayList<String> getGankImgUrl(String json){
        ArrayList<String> urls = new ArrayList<>();
        if(json==null||json.length()<1) {
            return urls;
        }
        try{
            JSONObject jsonObject = new JSONObject(json);
            if(jsonObject.getBoolean("error")){
                return urls;
            }
            JSONArray results = jsonObject.getJSONArray("results");
            for(int i = 0;i<results.length();i++){
                urls.add(results.getJSONObject(i).getString("url"));
            }
        }catch (Exception e){
            Log.d("getGankImgUrl",e.getMessage());
        }
        return urls;
    }

}
