package xiaoliang.ltool.bean;

import java.util.ArrayList;

/**
 * Created by liuj on 2016/9/18.
 * 城市bean中的市级
 */
public class CityBean4City {

    private String name;
    private ArrayList<String > areaNames;

    public void addArea(String area){
        areaNames.add(area);
    }

    public String getArea(int index){
        return areaNames.get(index);
    }

    public CityBean4City(String name) {
        this.name = name;
        areaNames = new ArrayList<>();
    }

    public ArrayList<String> getAreaNames() {
        return areaNames;
    }

    public void setAreaNames(ArrayList<String> areaNames) {
        this.areaNames = areaNames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
