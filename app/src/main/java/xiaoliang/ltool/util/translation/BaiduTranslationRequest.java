package xiaoliang.ltool.util.translation;

import java.util.List;

import xiaoliang.ltool.bean.StringNameValueBean;

/**
 * Created by liuj on 2016/11/29.
 * 百度的翻译请求
 */

public class BaiduTranslationRequest implements Translation.TranslationRequest {
    @Override
    public List<StringNameValueBean> getParameters() {
        return null;
    }

    @Override
    public void setFrom(String from) {

    }

    @Override
    public String getURL() {
        return null;
    }

    @Override
    public String decode(String str) {
        return null;
    }
}
