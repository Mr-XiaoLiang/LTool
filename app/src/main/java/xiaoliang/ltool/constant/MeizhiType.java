package xiaoliang.ltool.constant;

/**
 * Created by liuj on 2016/10/4.
 * 妹子图的类型
 */

public enum MeizhiType {
    NULL(-1),//空
    GANK(0),//GANK
    DOUBAN_ALL(1),//豆瓣全部
    DOUBAN_XIONG(2),//豆瓣美胸
    DOUBAN_TUN(3),//豆瓣美腿
    DOUBAN_SIWA(4),//豆瓣丝袜
    DOUBAN_TUI(5),//豆瓣美腿
    DOUBAN_LIAN(6),//豆瓣颜值
    DOUBAN_OTHER(7);//豆瓣其他
    private int value;
    MeizhiType(int value) {
        this.value = value;
    }
    public int getValue(){
        return value;
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
