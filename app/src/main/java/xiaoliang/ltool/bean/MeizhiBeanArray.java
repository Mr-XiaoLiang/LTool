package xiaoliang.ltool.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liuj on 2016/11/3.
 * 妹子尸体的集合
 */

public class MeizhiBeanArray implements Serializable {
    private ArrayList<MeizhiBean> beans;
    public ArrayList<MeizhiBean> beans(){
        return  beans;
    }
    public MeizhiBean bean(int index){
        return  beans.get(index);
    }
    public int size(){
        return beans.size();
    }

    public boolean isEmpty(){
        return (beans==null||beans.size()<1);
    }

    public MeizhiBeanArray() {
        beans = new ArrayList<>();
    }

    public MeizhiBeanArray(MeizhiBean bean) {
        beans = new ArrayList<>();
        beans.add(bean);
    }

    public MeizhiBeanArray(ArrayList<MeizhiBean> beans) {
        this.beans = beans;
    }
}
