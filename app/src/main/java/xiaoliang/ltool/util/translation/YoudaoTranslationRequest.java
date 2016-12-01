package xiaoliang.ltool.util.translation;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import xiaoliang.ltool.bean.StringNameValueBean;
import xiaoliang.ltool.bean.TranslationLanguageBean;
import xiaoliang.ltool.constant.TranslationConstant;
import xiaoliang.ltool.util.HttpUtil;

/**
 * Created by liuj on 2016/12/1.
 * 有道的翻译处理类
 */

public class YoudaoTranslationRequest implements Translation.TranslationRequest {

    private static final String URL = "http://fanyi.youdao.com/openapi.do";
    private static final int SECCESS = 0;//成功
    private static final int LONG_QUERY_ERR = 20;//翻译文本过长
    private static final int SYS_ERR = 30 ;//无法翻译
    private static final int LANGUAGE_ERR = 40;//不支持的语言类型
    private static final int UNAUTHORIZED = 50;//未授权
    private static final int DICTIONARY_ERR = 60;//ip异常
    private ArrayList<TranslationLanguageBean> languageBeens;
    private String from;
    private String keyfrom = TranslationConstant.YOUDAO_KEYFROM;
    private String key = TranslationConstant.YOUDAO_KEY;
    private StringBuilder stringBuilder;
    private StringBuilder outBuilder;

    public YoudaoTranslationRequest() {
        languageBeens = new ArrayList<>();
        languageBeens.add(new TranslationLanguageBean("auto","自动检测"));
        outBuilder = new StringBuilder();
    }

    @Override
    public List<StringNameValueBean> getParameters() {
        return null;
    }

    @Override
    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String getURL() {
        if(stringBuilder==null)
            stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(URL);
        stringBuilder.append("?keyfrom=");
        stringBuilder.append(keyfrom);
        stringBuilder.append("&key=");
        stringBuilder.append(key);
        stringBuilder.append("&type=data&doctype=json&version=1.1&q=");
        stringBuilder.append(HttpUtil.URLEncoder(from));
        return stringBuilder.toString();
    }

    @Override
    public String decode(String str) {
        String out = "";
        try{
            JSONObject object = new JSONObject(str);
            switch (object.getInt("errorCode")){
                case SECCESS:
                    out = decodeJson(object);
                    break;
                case LONG_QUERY_ERR:
                    out = "翻译的文本过长，请缩短文本";
                    break;
                case SYS_ERR:
                    out = "无法进行翻译，可能是有道服务器错误，请重试";
                    break;
                case LANGUAGE_ERR:
                    out = "有道翻译暂时只支持中英互译";
                    break;
                case UNAUTHORIZED:
                    out = "密钥错误，请检查密钥或更换密钥，也可能是访问量超额（每小时1000次），建议点击屏幕右上角，申请并设置自己的密钥";
                    break;
                case DICTIONARY_ERR:
                    out = "无词典结果";
                    break;
            }
        }catch (Exception e){
            Log.e("YoudaoTranslationReques","decode:"+e.getMessage());
            out = "数据返回异常";
        }
        out = HttpUtil.unicodeDecode(out);
        out = HttpUtil.URLDecoder(out);
        return out;
    }

    private String decodeJson(JSONObject object)throws Exception{
        outBuilder.setLength(0);
        outBuilder.append(object.getString("translation"));
        outBuilder.append("\n");
        JSONObject basic = object.getJSONObject("basic");
        if(basic!=null){
            outBuilder.append("发音:");
            outBuilder.append(basic.getString("phonetic"));
            outBuilder.append("\n");
            outBuilder.append("词典:");
            outBuilder.append("\n");
            JSONArray explains = basic.getJSONArray("explains");
            for(int i = 0;i<explains.length();i++){
                outBuilder.append("  ");
                outBuilder.append(explains.getString(i));
                outBuilder.append("\n");
            }
        }
        JSONArray web = object.getJSONArray("web");
        outBuilder.append("网络释义:\n");
        for(int i = 0;i<web.length();i++){
            JSONObject o = web.getJSONObject(i);
            outBuilder.append("  ");
            outBuilder.append(o.getString("key"));
            outBuilder.append("\n");
            JSONArray values = o.getJSONArray("value");
            outBuilder.append("    ");
            for(int j = 0;j<values.length();j++){
                outBuilder.append(values.getString(j));
                outBuilder.append("; ");
            }
            outBuilder.append("\n");
        }
        outBuilder.append("\n");
        return outBuilder.toString();
    }

    @Override
    public List<TranslationLanguageBean> getFromLanguage() {
        return languageBeens;
    }

    @Override
    public List<TranslationLanguageBean> getToLanguage() {
        return languageBeens;
    }

    @Override
    public void setFromLanguage(String id) {
        //Do nothing
    }

    @Override
    public void setToLanguage(String id) {
        //Do nothing
    }

    @Override
    public int maxFromLength() {
        return 200;
    }

    public void setKey(String key) {
        if(TextUtils.isEmpty(key))
            this.key = TranslationConstant.YOUDAO_KEY;
        this.key = key;
    }

    public void setKeyfrom(String keyfrom) {
        if(TextUtils.isEmpty(keyfrom))
            this.keyfrom = TranslationConstant.YOUDAO_KEYFROM;
        this.keyfrom = keyfrom;
    }
}
