package xiaoliang.ltool.util;

import xiaoliang.ltool.constant.Constant;
import xiaoliang.ltool.constant.MeizhiType;

/**
 * Created by liuj on 2016/10/7.
 * 返回妹子图的Url（毕竟太多了）
 */

public class MeizhiUrlUtil {
    public static String getUrl(MeizhiType type,int page){
        String url = "";
        switch (type){
            case GANK:
                url = Constant.Gank_Meizi_Url+"30/"+page;
                break;
            case DOUBAN_ALL:
                url = Constant.Douban_Meizhi_All_Url+page;
                break;
            case DOUBAN_LIAN:
                url = Constant.Douban_Meizhi_Lian_Url+page;
                break;
            case DOUBAN_OTHER:
                url = Constant.Douban_Meizhi_Other_Url+page;
                break;
            case DOUBAN_SIWA:
                url = Constant.Douban_Meizhi_Siwa_Url+page;
                break;
            case DOUBAN_TUI:
                url = Constant.Douban_Meizhi_Tui_Url+page;
                break;
            case DOUBAN_TUN:
                url = Constant.Douban_Meizhi_Tun_Url+page;
                break;
            case DOUBAN_XIONG:
                url = Constant.Douban_Meizhi_Xiong_Url+page;
                break;
            case MEIZHI51_ALL:
                url = Constant.Meizhi_51_All_Url+page+".html";
                break;
            case MEIZHI51_COMIC:
                url = Constant.Meizhi_51_Comic_Url+page+".html";
                break;
            case MEIZHI51_JAPAN:
                url = Constant.Meizhi_51_Japan_Url+page+".html";
                break;
            case MEIZHI51_KITTY:
                url = Constant.Meizhi_51_Kitty_Url+page+".html";
                break;
            case MEIZHI51_LIU:
                url = Constant.Meizhi_51_Liufeier_Url+page+".html";
                break;
            case MEIZHI51_PURE:
                url = Constant.Meizhi_51_Pure_Url+page+".html";
                break;
            case MEIZHI51_SEX:
                url = Constant.Meizhi_51_Sex_Url+page+".html";
                break;
            case MEIZHI51_TAIWAN:
                url = Constant.Meizhi_51_Taiwan_Url+page+".html";
                break;
            case MEIZHI51_WEIBO:
                url = Constant.Meizhi_51_Weibo_Url+page+".html";
                break;
            case MEIZHI51_WOMAN:
                url = Constant.Meizhi_51_Woman_Url+page+".html";
                break;
            case MEIZHI51_ZHAO:
                url = Constant.Meizhi_51_Zhaoxiaomi_Url+page+".html";
                break;
            case MEIZHI51_B:
                url = Constant.Meizhi_51_B_Url+page+".html";
                break;
        }
        return url;
    }
}
