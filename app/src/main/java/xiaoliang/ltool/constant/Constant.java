package xiaoliang.ltool.constant;

import android.content.Context;

/**
 * Created by liuj on 2016/9/12.
 * 这里是应用常量的管理类
 */
public class Constant {
    /**
     * 高德定位用的kay
     */
    public static final String amap_location_kay = "ad90d9a285507b7be97a91a7883a39ca";
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
    public static final String meizi_url = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/";

}
