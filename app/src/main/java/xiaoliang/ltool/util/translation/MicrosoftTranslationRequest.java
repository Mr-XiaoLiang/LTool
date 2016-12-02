package xiaoliang.ltool.util.translation;

import java.util.ArrayList;
import java.util.List;

import xiaoliang.ltool.bean.StringNameValueBean;
import xiaoliang.ltool.bean.TranslationLanguageBean;

/**
 * Created by liuj on 2016/12/2.
 * 微软的翻译接口
 */

public class MicrosoftTranslationRequest implements Translation.TranslationRequest {

    private ArrayList<TranslationLanguageBean> languageBeens;
    private static final String URL = "http://www.baidu.com";
    private String key = "af,ar,bs-Latn,bg,ca,zh-CHS,zh-CHT,hr,cs,da,nl,en,et,fi,fr,de,el,ht,he,hi,mww,hu,id,it,ja,sw,tlh,tlh-Qaak,ko,lv,lt,ms,mt,no,fa,pl,pt,otq,ro,ru,sr-Cyrl,sr-Latn,sk,sl,es,sv,th,tr,uk,ur,vi,cy,yua";
    private String value = "南非荷兰语,阿拉伯语,波斯尼亚(拉丁),保加利亚,加泰罗尼亚语,简体中文,繁体中文,克罗地亚,捷克语,丹麦语,荷兰语,英语,爱沙尼亚,芬兰,法国,德国,希腊,海地克里奥尔语,希伯来语,印地语,白苗语,匈牙利语、印度尼西亚、意大利、日本、斯瓦希里语,克林贡,克林贡(pIqaD),韩语,拉脱维亚,立陶宛,马来语、马耳他、挪威语、波斯,波兰语,葡萄牙语,克雷塔罗奥托米语、罗马尼亚、俄罗斯、塞尔维亚(西里尔),塞尔维亚(拉丁),斯洛伐克,斯洛文尼亚,西班牙,瑞典,泰国,土耳其,乌克兰,乌尔都语,越南语,威尔士语,尤卡坦人玛雅";

    public MicrosoftTranslationRequest() {
        languageBeens = new ArrayList<>();
        languageBeens.add(new TranslationLanguageBean("auto","无法使用"));
    }

    @Override
    public List<StringNameValueBean> getParameters() {
        return null;
    }

    @Override
    public void setFrom(String from) {

    }

    @Override
    public String getURL() {
        return URL;
    }

    @Override
    public String decode(String str) {
        return "暂未完成，敬请期待";
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

    }

    @Override
    public void setToLanguage(String id) {

    }

    @Override
    public int maxFromLength() {
        return 1;
    }
}
