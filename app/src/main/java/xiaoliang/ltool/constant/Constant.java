package xiaoliang.ltool.constant;

import android.content.Context;

/**
 * Created by liuj on 2016/9/12.
 * 这里是应用常量的管理类
 */
public class Constant {
    /**
     * 今日天气访问接口
     * 采用的时候中华万年历的接口
     */
    public static final String etouch_WeatherApi = "http://wthrcdn.etouch.cn/WeatherApi?city=";
    /**
     * mob的appkey
     */
    public static final String MOB_APP_Key = "16f2114442598";
    /**
     * 获取城市列表的地址
     */
    public static final String MOB_Citys_url = "http://apicloud.mob.com/v1/weather/citys?key="+MOB_APP_Key;

    /**
     * 网络访问异常
     */
    public static final int HttpTaskError = 10000;
    /**
     * 下载出现异常
     */
    public static final int DownLoadError = -60000;
    /**
     * 文件大小无法获取
     */
    public static final int DownLoadSizeNotFind = -60001;
    /**
     * 下载链接为空
     */
    public static final int DownLoadStreamIsNull = -60002;
    /**
     * 每日一图，720P
     */
    public static final String head_img_url_720 = "http://www.dujin.org/sys/bing/1366.php";
    /**
     * 每日一图，1080p
     */
    public static final String head_img_url_1080 = "http://www.dujin.org/sys/bing/1920.php";
    /**
     * 必应每日一图地址
     */
    public static final String bing_image_url = "http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";

    /**
     * 获取本地图片地址
     * @param context
     * @return
     */
    public static String getBabkgroundPath(Context context){
        return "file:///"+context.getFilesDir().getAbsolutePath()+"/bg/background.png";
    }

    /**
     * 检查更新地址（酷安的app地址）
     */
    public static String UPDATE_URL = "http://www.coolapk.com/apk/xiaoliang.ltool";

    /**
     * 网络类型
     * 无网络
     */
    public static final int NetWord_NULL = 100000;
    /**
     * 网络类型
     * wifi
     */
    public static final int NetWord_WIFI = 100001;
    /**
     * 网络类型
     * 手机数控
     */
    public static final int NetWord_MOBILE= 100002;
    /**
     * 网络类型
     * 其他
     */
    public static final int NetWord_Other= 100003;
    /**
     * gank.io的福利图
     * http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/2
     * 10：每页数量
     * 2：页码
     */
    public static final String Gank_Meizi_Url = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/";
    /**
     * 豆瓣妹纸
     */
    public static final String Douban_Meizhi_All_Url = "http://www.dbmeinv.com/dbgroup/show.htm?pager_offset=";
    public static final String Douban_Meizhi_Xiong_Url = "http://www.dbmeinv.com/dbgroup/show.htm?cid=2&pager_offset=";
    public static final String Douban_Meizhi_Tun_Url = "http://www.dbmeinv.com/dbgroup/show.htm?cid=6&pager_offset=";
    public static final String Douban_Meizhi_Siwa_Url = "http://www.dbmeinv.com/dbgroup/show.htm?cid=7&pager_offset=";
    public static final String Douban_Meizhi_Tui_Url = "http://www.dbmeinv.com/dbgroup/show.htm?cid=3&pager_offset=";
    public static final String Douban_Meizhi_Lian_Url = "http://www.dbmeinv.com/dbgroup/show.htm?cid=4&pager_offset=";
    public static final String Douban_Meizhi_Other_Url = "http://www.dbmeinv.com/dbgroup/show.htm?cid=5&pager_offset=";
    /**
     * 51妹子
     * http://www.51xw.net/meizi/index_1.html
     */
    public static final String Meizhi_51_All_Url = "http://www.51xw.net/meizi/index_";
    public static final String Meizhi_51_Liufeier_Url = "http://www.51xw.net/meizi/liufeier/index_";
    public static final String Meizhi_51_Zhaoxiaomi_Url = "http://www.51xw.net/meizi/zhaoxiaomi/index_";
    public static final String Meizhi_51_Sex_Url = "http://www.51xw.net/meizi/xinggan/index_";
    public static final String Meizhi_51_Pure_Url = "http://www.51xw.net/meizi/freash/index_";
    public static final String Meizhi_51_Japan_Url = "http://www.51xw.net/meizi/japan/index_";
    public static final String Meizhi_51_Comic_Url = "http://www.51xw.net/meizi/shoujo-manga/index_";
    public static final String Meizhi_51_Taiwan_Url = "http://www.51xw.net/meizi/taiwan/index_";
    public static final String Meizhi_51_Woman_Url = "http://www.51xw.net/meizi/shaofu/index_";
    public static final String Meizhi_51_Weibo_Url = "http://www.51xw.net/ooxx/index_";
    public static final String Meizhi_51_B_Url = "http://www.51xw.net/meizi/tag/%E6%B9%BF%E8%BA%AB/index_";
    public static final String Meizhi_51_Kitty_Url = "http://www.51xw.net/meizi/zhaoxiaomi-kitty/index_";
    /**
     * MM妹子
     * http://www.mmjpg.com/home/1
     */
    public static final String MM_All_Url = "http://www.mmjpg.com/home/";
    public static final String MM_All_Hot_Url = "http://www.mmjpg.com/hot/";//无分页
    public static final String MM_All_Recommended_Url = "http://www.mmjpg.com/getmore.php?page=";
    public static final String MM_All_Label_Url = "http://www.mmjpg.com/more/";//无分页
    /**
     * 妹子图
     * http://www.meizitu.com/a/list_1_2.html
     * 1：无意义
     * 2：页码
     */
    public static final String Meizi_All_Url = "http://www.meizitu.com/a/list_1_";
    public static final String Meizi_SEX_Url = "http://www.meizitu.com/a/xinggan_2_";
//    public static final String Meizi_Bath_Url = "";
    public static final String Meizi_Private_Url = "http://www.meizitu.com/a/sifang_5_";
//    public static final String Meizi_Tui_Url = "";
    public static final String Meizi_Pure_Url = "http://www.meizitu.com/a/qingchun_3_";
//    public static final String Meizi_Sweet_Url = "";
//    public static final String Meizi_Cure_Url = "";
    public static final String Meizi_Bud_Url = "http://www.meizitu.com/a/meizi_4_";
    public static final String Meizi_Fresh_Url = "http://www.meizitu.com/a/xiaoqingxin_6_";
    public static final String Meizi_God_Url = "http://www.meizitu.com/a/nvshen_7_";
    public static final String Meizi_Temperament_Url = "http://www.meizitu.com/a/qizhi_8_";
    public static final String Meizi_Model_Url = "http://www.meizitu.com/a/mote_9_";
//    public static final String Meizi_Car_Url = "";
    public static final String Meizi_Bikini_Url = "http://www.meizitu.com/a/bijini_10_";
    public static final String Meizi_Football_Url = "http://www.meizitu.com/a/baobei_11_";
    public static final String Meizi_Loli_Url = "http://www.meizitu.com/a/luoli_12_";
    public static final String Meizi_90_Url = "http://www.meizitu.com/a/wangluo_13_";
    public static final String Meizi_Japan_Url = "http://www.meizitu.com/a/rihan_14_";
    /**
     * 花瓣妹子
     */
//    public static final String Petals_All_Url = "";
    /**
     * 美图录
     * http://www.meitulu.com/t/nvshen/2.html
     */
    public static final String Meitulu_Recommend_Url = "http://www.meitulu.com/xihuan/index.php";//反复调用即分页
    public static final String Meitulu_Japan_Url = "http://www.meitulu.com/rihan/";
    public static final String Meitulu_Hokon_Url = "http://www.meitulu.com/gangtai/";
    public static final String Meitulu_Domestic_Url = "http://www.meitulu.com/guochan/";
    public static final String Meitulu_Highest_Url = "http://www.meitulu.com/t/jipin/";
    public static final String Meitulu_God_Url = "http://www.meitulu.com/t/nvshen/";
    public static final String Meitulu_Model_Url = "http://www.meitulu.com/t/nenmo/";
    public static final String Meitulu_Net_Url = "http://www.meitulu.com/t/wangluohongren/";
    public static final String Meitulu_Mores_Url = "http://www.meitulu.com/t/fengsuniang/";
    public static final String Meitulu_Temperament_Url = "http://www.meitulu.com/t/qizhi/";
    public static final String Meitulu_Stunner_Url = "http://www.meitulu.com/t/youwu/";
    public static final String Meitulu_Milk_Url = "http://www.meitulu.com/t/baoru/";
    public static final String Meitulu_Sex_Url = "http://www.meitulu.com/t/xinggan/";
    public static final String Meitulu_Tempt_Url = "http://www.meitulu.com/t/youhuo/";
    public static final String Meitulu_Xiong_Url = "http://www.meitulu.com/t/meixiong/";
    public static final String Meitulu_Woman_Url = "http://www.meitulu.com/t/shaofu/";
    public static final String Meitulu_Tui_Url = "http://www.meitulu.com/t/changtui/";
    public static final String Meitulu_Bud_Url = "http://www.meitulu.com/t/mengmeizi/";
    public static final String Meitulu_Loli_Url = "http://www.meitulu.com/t/loli/";
    public static final String Meitulu_Cute_Url = "http://www.meitulu.com/t/keai/";
    public static final String Meitulu_Outdoors_Url = "http://www.meitulu.com/t/huwai/";
    public static final String Meitulu_Bikini_Url = "http://www.meitulu.com/t/bijini/";
    public static final String Meitulu_Pure_Url = "http://www.meitulu.com/t/qingchun/";
    public static final String Meitulu_Aestheticism_Url = "http://www.meitulu.com/t/weimei/";
    public static final String Meitulu_Fresh_Url = "http://www.meitulu.com/t/qingxin/";
}
