package xiaoliang.ltool.bean;

import java.util.ArrayList;

/**
 * Created by liuj on 2016/9/18.
 * 城市bean的省级bean
 */
public class CityBean4Province {
    private String name;
    private ArrayList<CityBean4City> citys;
    private ArrayList<String> cityName;

    public CityBean4Province(String name) {
        this.name = name;
        citys = new ArrayList<>();
        cityName = new ArrayList<>();
    }

    public void addCity(String name,CityBean4City city){
        citys.add(city);
        cityName.add(name);
    }

    public void addCity(CityBean4City city){
        addCity(city.getName(),city);
    }

    public CityBean4City getCity(int index){
        return citys.get(index);
    }

    public ArrayList<String> getCityName() {
        return cityName;
    }

    public void setCityName(ArrayList<String> cityName) {
        this.cityName = cityName;
    }

    public ArrayList<CityBean4City> getCitys() {
        return citys;
    }

    public void setCitys(ArrayList<CityBean4City> citys) {
        this.citys = citys;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
