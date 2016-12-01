package xiaoliang.ltool.util.translation;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import xiaoliang.ltool.bean.StringNameValueBean;
import xiaoliang.ltool.bean.TranslationLanguageBean;
import xiaoliang.ltool.constant.TranslationConstant;
import xiaoliang.ltool.util.HttpUtil;
import xiaoliang.ltool.util.TextToColor;

/**
 * Created by liuj on 2016/11/29.
 * 百度的翻译请求
 */

public class BaiduTranslationRequest implements Translation.TranslationRequest {

    private static final String URL = "http://api.fanyi.baidu.com/api/trans/vip/translate";
    private static final int SECCESS = 52000;//成功
    private static final int TIME_OUT = 52001;//请求超时
    private static final int SYS_ERR = 52002;//系统错误
    private static final int UNAUTHORIZED = 52003;//未授权
    private static final int PAR_ERR = 54000;//参数不足
    private static final int IP_ERR = 58000;//ip异常
    private static final int SIGNATURE_ERR = 54001;//签名异常
    private static final int VISIT_ERR = 54003;//访问频率过快
    private static final int LANGUAGE_ERR = 58001;//译文语言不支持
    private static final int BALANCE_ERR = 54004;//余额不足
    private static final int LONG_QUERY_ERR = 54005;//长query请求异常
    private ArrayList<TranslationLanguageBean> languageBeens;
    private String from;
    private Random random;
    private ArrayList<StringNameValueBean> parameters;
    private String fromLanguage = "auto";
    private String toLanguage = "zh";
    private String appid = TranslationConstant.BAIDU_APPID;
    private String appkey = TranslationConstant.BAIDU_KEY;

    public BaiduTranslationRequest() {
        languageBeens = new ArrayList<>();
        String languageId = "auto,zh,en,yue,wyw,jp,kor,fra,spa,th,ara,ru,pt,de,it,el,nl,pl,bul,est,dan,fin,cs,rom,slo,swe,hu,cht";
        String languageName = "自动检测,中文,英语,粤语,文言文,日语,韩语,法语,西班牙语,泰语,阿拉伯语,俄语,葡萄牙语,德语,意大利语,希腊语,荷兰语,波兰语,保加利亚语,爱沙尼亚语,丹麦语,芬兰语,捷克语,罗马尼亚语,斯洛文尼亚语,瑞典语,匈牙利语,繁体中文";
        String[] languageIds = languageId.split(",");
        String[] languageNames = languageName.split(",");
        for(int i = 0;i<languageIds.length&&i<languageNames.length;i++){
            languageBeens.add(new TranslationLanguageBean(languageIds[i],languageNames[i]));
        }
    }

    @Override
    public List<StringNameValueBean> getParameters() {
        //放弃post方式，改用get
//        StringBuilder url = new StringBuilder();
//        List<StringNameValueBean> beans = getPar();
//        for(StringNameValueBean bean:beans){
//            url.append(bean.getName());
//            url.append("=");
//            url.append(bean.getValue());
//            url.append("&");
//        }
//        String out = url.toString().substring(0,url.length()-1);
//        Log.d("getURL",out);
//        return beans;
        return null;
    }

    private List<StringNameValueBean> getPar(){
        if(parameters==null){
            parameters = new ArrayList<>();
        }
        parameters.clear();
        parameters.add(new StringNameValueBean("from",fromLanguage));
        parameters.add(new StringNameValueBean("to",toLanguage));
        parameters.add(new StringNameValueBean("appid",appid));
        parameters.add(new StringNameValueBean("q",HttpUtil.URLEncoder(from)));
        int salt = getSalt();
        parameters.add(new StringNameValueBean("salt",salt+""));
        parameters.add(new StringNameValueBean("sign",getSign(from,salt)));
        return parameters;
    }

    @Override
    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String getURL() {
        StringBuilder url = new StringBuilder(URL+"?");
        List<StringNameValueBean> beans = getPar();
        for(StringNameValueBean bean:beans){
            url.append(bean.getName());
            url.append("=");
            url.append(bean.getValue());
            url.append("&");
        }
        String out = url.toString().substring(0,url.length()-1);
//        Log.d("getURL",out);
        return out;
//        return URL;
    }

    @Override
    public String decode(String str) {
        String out = "";
        try{
            JSONObject object = new JSONObject(str);
            if(!str.contains("error_code")||object.getInt("error_code")==SECCESS){
                JSONArray array = object.getJSONArray("trans_result");
                if(array.length()<1){
                    out = "服务器未返回结果";
                }else if(array.length()<2){
                    out = array.getJSONObject(0).getString("dst");
                }else{
                    for(int i = 0;i<array.length();i++){
                        out += array.getJSONObject(i).getString("src");
                        out += ":";
                        out += array.getJSONObject(i).getString("dst");
                        out += "\n";
                    }
                }
            }else{
                switch (object.getInt("error_code")){
                    case TIME_OUT:
                        out = "请求超时";
                        break;
                    case SYS_ERR:
                        out = "服务器系统错误";
                        break;
                    case UNAUTHORIZED:
                        out = "未获得授权,请检查APPID";
                        break;
                    case PAR_ERR:
                        out = "请求参数错误";
                        break;
                    case IP_ERR:
                        out = "客户端IP异常";
                        break;
                    case SIGNATURE_ERR:
                        out = "签名错误,请使用正版应用";
                        break;
                    case VISIT_ERR:
                        out = "访问频率过快,请稍后重试";
                        break;
                    case LANGUAGE_ERR:
                        out = "译文语言不支持";
                        break;
                    case BALANCE_ERR:
                        out = "翻译长度已用完,下月可继续使用,或自行设置APPID";
                        break;
                    case LONG_QUERY_ERR:
                        out = "翻译文本过长,请缩短翻译内容,或分次翻译";
                        break;
                }
            }
        }catch (Exception e){
            Log.e("BaiduTranslationRequest","decode:"+e.getMessage());
        }
//        if(out.contains("\\u")){
//        }else{
//        }
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
        fromLanguage = id;
    }

    @Override
    public void setToLanguage(String id) {
        toLanguage = id;
        if(toLanguage.equals("auto")){
            toLanguage = "zh";
        }
    }

    @Override
    public int maxFromLength() {
        return 2000;
    }

    private String getSign(String query,int salt){
        String original = appid;
        original += query;
        original += salt;
        original += appkey;
        return TextToColor.MD5(original).toLowerCase();
    }

    private int getSalt(){
        if(random==null)
            random = new Random();
        return Math.abs(random.nextInt());
    }

    public void setAppid(String appid) {
        if(TextUtils.isEmpty(appid))
            this.appid = TranslationConstant.BAIDU_APPID;
        this.appid = appid;
    }

    public void setAppkey(String appkey) {
        if(TextUtils.isEmpty(appkey))
            this.appkey = TranslationConstant.BAIDU_KEY;
        this.appkey = appkey;
    }
}
