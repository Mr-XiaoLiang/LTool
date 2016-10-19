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
            case MM_All:
                url = Constant.MM_All_Url+page;
                break;
            case MM_Ranking:
                url = Constant.MM_All_Hot_Url;
                break;
            case MM_Recommended:
                url = Constant.MM_All_Recommended_Url+page;
                break;
            case MM_Label:
                url = Constant.MM_All_Label_Url;
                break;
            case Meizhi_all:
                url = Constant.Meizi_All_Url+page+".html";
                break;
            case Meizhi_Sex:
                url = Constant.Meizi_SEX_Url+page+".html";
                break;
            case Meizhi_Private:
                url = Constant.Meizi_Private_Url+page+".html";
                break;
            case Meizhi_Pure:
                url = Constant.Meizi_Pure_Url+page+".html";
                break;
            case Meizhi_Bud:
                url = Constant.Meizi_Bud_Url+page+".html";
                break;
            case Meizhi_Fresh:
                url = Constant.Meizi_Fresh_Url+page+".html";
                break;
            case Meizhi_God:
                url = Constant.Meizi_God_Url+page+".html";
                break;
            case Meizhi_Temperament:
                url = Constant.Meizi_Temperament_Url+page+".html";
                break;
            case Meizhi_Model:
                url = Constant.Meizi_Model_Url+page+".html";
                break;
            case Meizhi_Bikini:
                url = Constant.Meizi_Bikini_Url+page+".html";
                break;
            case Meizhi_Football:
                url = Constant.Meizi_Football_Url+page+".html";
                break;
            case Meizhi_Loli:
                url = Constant.Meizi_Loli_Url+page+".html";
                break;
            case Meizhi_90:
                url = Constant.Meizi_90_Url+page+".html";
                break;
            case Meizhi_Japan:
                url = Constant.Meizi_Japan_Url+page+".html";
                break;
            case Meitulu_Recommend:
                url = Constant.Meitulu_Recommend_Url;
                break;
            case Meitulu_Japan:
                if(page==1)
                    url = Constant.Meitulu_Japan_Url;
                else
                    url = Constant.Meitulu_Japan_Url+page+".html";
                break;
            case Meitulu_Hokon:
                if(page==1)
                    url = Constant.Meitulu_Hokon_Url;
                else
                url = Constant.Meitulu_Hokon_Url+page+".html";
                break;
            case Meitulu_Domestic:
                if(page==1)
                    url = Constant.Meitulu_Domestic_Url;
                else
                url = Constant.Meitulu_Domestic_Url+page+".html";
                break;
            case Meitulu_Highest:
                if(page==1)
                    url = Constant.Meitulu_Highest_Url;
                else
                url = Constant.Meitulu_Highest_Url+page+".html";
                break;
            case Meitulu_God:
                if(page==1)
                    url = Constant.Meitulu_God_Url;
                else
                url = Constant.Meitulu_God_Url+page+".html";
                break;
            case Meitulu_Model:
                if(page==1)
                    url = Constant.Meitulu_Model_Url;
                else
                url = Constant.Meitulu_Model_Url+page+".html";
                break;
            case Meitulu_Net:
                if(page==1)
                    url = Constant.Meitulu_Net_Url;
                else
                url = Constant.Meitulu_Net_Url+page+".html";
                break;
            case Meitulu_Mores:
                if(page==1)
                    url = Constant.Meitulu_Mores_Url;
                else
                url = Constant.Meitulu_Mores_Url+page+".html";
                break;
            case Meitulu_Temperament:
                if(page==1)
                    url = Constant.Meitulu_Temperament_Url;
                else
                url = Constant.Meitulu_Temperament_Url+page+".html";
                break;
            case Meitulu_Stunner:
                if(page==1)
                    url = Constant.Meitulu_Stunner_Url;
                else
                url = Constant.Meitulu_Stunner_Url+page+".html";
                break;
            case Meitulu_Milk:
                if(page==1)
                    url = Constant.Meitulu_Milk_Url;
                else
                url = Constant.Meitulu_Milk_Url+page+".html";
                break;
            case Meitulu_Sex:
                if(page==1)
                    url = Constant.Meitulu_Sex_Url;
                else
                url = Constant.Meitulu_Sex_Url+page+".html";
                break;
            case Meitulu_Tempt:
                if(page==1)
                    url = Constant.Meitulu_Tempt_Url;
                else
                url = Constant.Meitulu_Tempt_Url+page+".html";
                break;
            case Meitulu_Xiong:
                if(page==1)
                    url = Constant.Meitulu_Xiong_Url;
                else
                url = Constant.Meitulu_Xiong_Url+page+".html";
                break;
            case Meitulu_Woman:
                if(page==1)
                    url = Constant.Meitulu_Woman_Url;
                else
                url = Constant.Meitulu_Woman_Url+page+".html";
                break;
            case Meitulu_Tui:
                if(page==1)
                    url = Constant.Meitulu_Tui_Url;
                else
                url = Constant.Meitulu_Tui_Url+page+".html";
                break;
            case Meitulu_Bud:
                if(page==1)
                    url = Constant.Meitulu_Bud_Url;
                else
                url = Constant.Meitulu_Bud_Url+page+".html";
                break;
            case Meitulu_Loli:
                if(page==1)
                    url = Constant.Meitulu_Loli_Url;
                else
                url = Constant.Meitulu_Loli_Url+page+".html";
                break;
            case Meitulu_Cute:
                if(page==1)
                    url = Constant.Meitulu_Cute_Url;
                else
                url = Constant.Meitulu_Cute_Url+page+".html";
                break;
            case Meitulu_Outdoors:
                if(page==1)
                    url = Constant.Meitulu_Outdoors_Url;
                else
                url = Constant.Meitulu_Outdoors_Url+page+".html";
                break;
            case Meitulu_Bikini:
                if(page==1)
                    url = Constant.Meitulu_Bikini_Url;
                else
                url = Constant.Meitulu_Bikini_Url+page+".html";
                break;
            case Meitulu_Pure:
                if(page==1)
                    url = Constant.Meitulu_Pure_Url;
                else
                url = Constant.Meitulu_Pure_Url+page+".html";
                break;
            case Meitulu_Aestheticism:
                if(page==1)
                    url = Constant.Meitulu_Aestheticism_Url;
                else
                url = Constant.Meitulu_Aestheticism_Url+page+".html";
                break;
            case Meitulu_Fresh:
                if(page==1)
                    url = Constant.Meitulu_Fresh_Url;
                else
                url = Constant.Meitulu_Fresh_Url+page+".html";
                break;
        }
        return url;
    }
}
