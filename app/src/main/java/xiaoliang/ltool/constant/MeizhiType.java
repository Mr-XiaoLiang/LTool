package xiaoliang.ltool.constant;

import java.io.Serializable;

/**
 * Created by liuj on 2016/10/4.
 * 妹子图的类型
 */

public enum MeizhiType implements Serializable {
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
    MEIZHI51_B(18,"51-湿身"),
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
    Meitulu_Recommend(42,"美图-推荐"),
    Meitulu_Japan(43,"美图-日韩"),
    Meitulu_Hokon(44,"美图-港台"),
    Meitulu_Domestic(45,"美图-国产"),
    Meitulu_Highest(46,"美图-极品"),
    Meitulu_God(47,"美图-女神"),
    Meitulu_Model(48,"美图-嫩模"),
    Meitulu_Net(49,"美图-网红"),
    Meitulu_Mores(50,"美图-风俗"),
    Meitulu_Temperament(51,"美图-气质"),
    Meitulu_Stunner(52,"美图-尤物"),
    Meitulu_Milk(53,"美图-爆乳"),
    Meitulu_Sex(54,"美图-性感"),
    Meitulu_Tempt(55,"美图-诱惑"),
    Meitulu_Xiong(56,"美图-美胸"),
    Meitulu_Woman(57,"美图-少妇"),
    Meitulu_Tui(58,"美图-长腿"),
    Meitulu_Bud(59,"美图-萌妹"),
    Meitulu_Loli(60,"美图-萝莉"),
    Meitulu_Cute(61,"美图-可爱"),
    Meitulu_Outdoors(62,"美图-户外"),
    Meitulu_Bikini(63,"美图-比基尼"),
    Meitulu_Pure(64,"美图-清纯"),
    Meitulu_Aestheticism(65,"美图-唯美"),
    Meitulu_Fresh(66,"美图-清新");
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

    public boolean equals(int type){
        return value == type;
    }

}
