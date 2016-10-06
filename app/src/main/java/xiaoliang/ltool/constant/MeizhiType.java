package xiaoliang.ltool.constant;

/**
 * Created by liuj on 2016/10/4.
 * 妹子图的类型
 */

public enum MeizhiType {
    NULL(-1,"空"),//空
    GANK(0,"GANK"),//GANK
    DOUBAN_ALL(1,"豆瓣-全部"),//豆瓣全部
    DOUBAN_XIONG(2,"豆瓣-美胸"),//豆瓣美胸
    DOUBAN_TUN(3,"豆瓣-美臀"),//豆瓣美腿
    DOUBAN_SIWA(4,"豆瓣-丝袜"),//豆瓣丝袜
    DOUBAN_TUI(5,"豆瓣-美腿"),//豆瓣美腿
    DOUBAN_LIAN(6,"豆瓣-颜值"),//豆瓣颜值
    DOUBAN_OTHER(7,"豆瓣-其他"),//豆瓣其他
    MEIZHI51_ALL(8,"51-全部"),
    MEIZHI51_LIU(9,"51-刘飞儿"),
    MEIZHI51_ZHAO(10,"51-赵小米"),
    MEIZHI51_SEX(11,"51-性感"),
    MEIZHI51_PURE(12,"51-清新"),
    MEIZHI51_JAPAN(13,"51-日本"),
    MEIZHI51_COMIC(14,"51-漫画"),
    MEIZHI51_TAIWAN(15,"51-台湾"),
    MEIZHI51_WOMAN(16,"51-少妇"),
    MEIZHI51_WEIBO(17,"51-微博"),
    Meizhi51_B(18,"51-乙女"),
    MEIZHI51_KITTY(19,"51-Kitty"),
    MM_All(20,"MM-全部"),
    MM_Ranking(21,"MM-排行"),
    MM_Recommended(22,"MM-推荐"),
    MM_Label(23,"MM-标签"),
    Meizhi_all(24,"妹子-全部"),
    Meizhi_Sex(25,"妹子-性感"),
//    Meizhi_Bath(26,"妹子-浴室"),
    Meizhi_Private(27,"妹子-私房"),
//    Meizhi_Tui(28,"妹子-美腿"),
    Meizhi_Pure(29,"妹子-清纯"),
//    Meizhi_Sweet(30,"妹子-甜美"),
//    Meizhi_Cure(31,"妹子-治愈"),
    Meizhi_Bud(32,"妹子-萌"),
    Meizhi_Fresh(33,"妹子-清新"),
    Meizhi_God(34,"妹子-女神"),
    Meizhi_Temperament(35,"妹子-气质"),
    Meizhi_Model(36,"妹子-嫩模"),
//    Meizhi_Car(37,"妹子-车模"),
    Meizhi_Bikini(38,"妹子-比基尼"),
    Meizhi_Football(39,"妹子-足球"),
    Meizhi_Loli(40,"妹子-萝莉"),
    Meizhi_90(41,"妹子-90后"),
    Meizhi_Japan(42,"妹子-日韩"),
//    Petals_All(43,"花瓣");
    /*
    女神
    极品
    嫩模
    网络红人
    风俗娘
    气质
    尤物
    爆乳
    性感
    诱惑
    美胸
    少妇
    长腿
    萌妹子
    萝莉
    可爱
    户外
    比基尼
    清纯
    唯美
    清新
	推荐
	日韩
	港台
	国产
     */
//    Meitulu_God(42,"美图-女神");
//    Meitulu_God(43,"美图-女神");
//    Meitulu_God(44,"美图-女神");
//    Meitulu_God(45,"美图-女神");
//    Meitulu_God(46,"美图-女神");
//    Meitulu_God(47,"美图-女神");
//    Meitulu_God(48,"美图-女神");
//    Meitulu_God(49,"美图-女神");
//    Meitulu_God(50,"美图-女神");
//    Meitulu_God(51,"美图-女神");
//    Meitulu_God(52,"美图-女神");
//    Meitulu_God(53,"美图-女神");
//    Meitulu_God(54,"美图-女神");
//    Meitulu_God(55,"美图-女神");
//    Meitulu_God(56,"美图-女神");
//    Meitulu_God(57,"美图-女神");
//    Meitulu_God(58,"美图-女神");
//    Meitulu_God(59,"美图-女神");
//    Meitulu_God(60,"美图-女神");
//    Meitulu_God(61,"美图-女神");
    Meitulu_God(62,"美图-女神");
    private int value;
    private String name;
    MeizhiType(int value,String name) {
        this.value = value;
        this.name = name;
    }
    public int getValue(){
        return value;
    }

    public String getName() {
        return name;
    }

    public static MeizhiType getType(int i){
        switch (i){
            case 0:
                return GANK;
            case 1:
                return DOUBAN_ALL;
            case 2:
                return DOUBAN_XIONG;
            case 3:
                return DOUBAN_TUN;
            case 4:
                return DOUBAN_SIWA;
            case 5:
                return DOUBAN_TUI;
            case 6:
                return DOUBAN_LIAN;
            case 7:
                return DOUBAN_OTHER;
        }
        return NULL;
    }
}
