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
 * 金山词霸
 */

public class KingsoftTranslationRequest implements Translation.TranslationRequest {

    private static final String URL = "http://dict-co.iciba.com/api/dictionary.php";
    private ArrayList<TranslationLanguageBean> languageBeens;
    private String from;
    private String key = TranslationConstant.KINGSOFT_KEY;
    private StringBuilder urlBuilder;
    private StringBuilder outBuilder;

    public KingsoftTranslationRequest() {
        languageBeens = new ArrayList<>();
        languageBeens.add(new TranslationLanguageBean("auto","自动检测"));
        outBuilder = new StringBuilder();
        urlBuilder = new StringBuilder();
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
        urlBuilder.setLength(0);
        urlBuilder.append(URL);
        urlBuilder.append("?type=json&w=");
        urlBuilder.append(HttpUtil.URLEncoder(from));
        urlBuilder.append("&key=");
        urlBuilder.append(key);
        return urlBuilder.toString();
    }

    @Override
    public String decode(String str) {
        String out;
        try{
            JSONObject object = new JSONObject(str);
            outBuilder.setLength(0);
            if(object.has("word_name")){
                if(object.has("exchange")){
                    JSONObject exchange = object.getJSONObject("exchange");
                    outBuilder.append("复数:");
                    outBuilder.append(exchange.getString("word_pl"));
                    outBuilder.append("\n");
                    outBuilder.append("过去时:");
                    outBuilder.append(exchange.getString("word_past"));
                    outBuilder.append("\n");
                    outBuilder.append("完成时:");
                    outBuilder.append(exchange.getString("word_done"));
                    outBuilder.append("\n");
                    outBuilder.append("进行时:");
                    outBuilder.append(exchange.getString("word_ing"));
                    outBuilder.append("\n");
                    outBuilder.append("第三人称单数");
                    outBuilder.append(exchange.getString("word_third"));
                    outBuilder.append("\n");
                    outBuilder.append("名词性:");
                    outBuilder.append(exchange.getString("word_er"));
                    outBuilder.append("\n");
                    outBuilder.append("比较级:");
                    outBuilder.append(exchange.getString("word_est"));
                    outBuilder.append("\n");
                }
                JSONArray symbols = object.getJSONArray("symbols");
                for(int i = 0;i<symbols.length();i++){
                    JSONObject symbol = symbols.getJSONObject(i);
                    if(symbol.has("ph_en")){
                        outBuilder.append("英式音标:");
                        outBuilder.append(symbol.getString("ph_en"));
                        outBuilder.append("\n");
                    }
                    if(symbol.has("ph_am")){
                        outBuilder.append("美式音标:");
                        outBuilder.append(symbol.getString("ph_am"));
                        outBuilder.append("\n");
                    }
                    if(symbol.has("ph_other")){
                        outBuilder.append("其他音标:");
                        outBuilder.append(symbol.getString("ph_other"));
                        outBuilder.append("\n");
                    }
                    if(symbol.has("ph_en_mp3")){
                        outBuilder.append("英式发音:");
                        outBuilder.append(symbol.getString("ph_en_mp3"));
                        outBuilder.append("\n");
                    }
                    if(symbol.has("ph_am_mp3")){
                        outBuilder.append("美式发音:");
                        outBuilder.append(symbol.getString("ph_am_mp3"));
                        outBuilder.append("\n");
                    }
                    if(symbol.has("ph_tts_mp3")){
                        outBuilder.append("合成发音:");
                        outBuilder.append(symbol.getString("ph_tts_mp3"));
                        outBuilder.append("\n");
                    }
                    if(symbol.has("word_symbol")){
                        outBuilder.append("拼音:");
                        outBuilder.append(symbol.getString("word_symbol"));
                        outBuilder.append("\n");
                    }
                    if(symbol.has("symbol_mp3")){
                        outBuilder.append("发音:");
                        outBuilder.append(symbol.getString("symbol_mp3"));
                        outBuilder.append("\n");
                    }
                    if(symbol.has("parts")){
                        outBuilder.append("解释:");
                        outBuilder.append("\n");
                        JSONArray parts = symbol.getJSONArray("parts");
                        for(int j = 0;j<parts.length();j++){
                            JSONObject part = parts.getJSONObject(j);
                            if(part.has("part")){
                                outBuilder.append("  ");
                                if(!TextUtils.isEmpty(part.getString("part"))){
                                    outBuilder.append(part.getString("part"));
                                    outBuilder.append(":");
                                }
                                JSONArray means = part.getJSONArray("means");
                                for(int k = 0;k<means.length();k++){
                                    outBuilder.append(means.getString(k));
                                    outBuilder.append("; ");
                                }
                                outBuilder.append("\n");
                            }else if(part.has("part_name")){
                                outBuilder.append("  ");
                                if(!TextUtils.isEmpty(part.getString("part_name"))){
                                    outBuilder.append(part.getString("part_name"));
                                    outBuilder.append(":");
                                }
                                if(part.has("means")){
                                    JSONArray means = part.getJSONArray("means");
                                    for(int k = 0;k<means.length();k++){
                                        JSONObject mean = means.getJSONObject(k);
                                        if(mean.has("word_mean")){
                                            outBuilder.append(mean.getString("word_mean"));
                                            outBuilder.append("; ");
                                        }
                                    }
                                }
                                outBuilder.append("\n");
                            }
                        }
                    }
                }
                out = outBuilder.toString();
            }else{
                out = "无查询结果";
            }
        }catch (Exception e){
            Log.e("KingsoftTranslationRequ","decode"+e.getMessage());
            out = "金山词霸数据解析失败，以下为返回原文:(如果是Key错误，请点击屏幕右上角生成并填写Key)\n"+str;
        }
        out = HttpUtil.unicodeDecode(out);
        out = HttpUtil.URLDecoder(out);
        return out;
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
        return 2000;
    }
}
