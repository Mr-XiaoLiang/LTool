package xiaoliang.ltool.bean;

import java.util.ArrayList;

/**
 * Created by liuj on 2016/9/18.
 * 这是城市的集合
 */
public class CityBean {
    private ArrayList<CityBean4Province> provinces;
    private ArrayList<String> provinceNames;
    private boolean seccess;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSeccess() {
        return seccess;
    }

    public void setSeccess(boolean seccess) {
        this.seccess = seccess;
    }

    public ArrayList<String> getCitys(int index){
        return provinces.get(index).getCityName();
    }

    public CityBean4City getCity(int index){
        return getProvinces(index).getCity(0);
    }

    public CityBean4Province getProvinces(int index){
        return provinces.get(index);
    }

    public CityBean() {
        provinces = new ArrayList<>();
        provinceNames = new ArrayList<>();
    }

    public CityBean(boolean seccess) {
        this();
        this.seccess = seccess;
    }

    public void add(CityBean4Province province,String name){
        provinces.add(province);
        provinceNames.add(name);
    }

    public void add(CityBean4Province province){
        add(province,province.getName());
    }

    public CityBean4Province get(int index){
        return provinces.get(index);
    }

    public ArrayList<String> getProvinceNames() {
        return provinceNames;
    }

    public void setProvinceNames(ArrayList<String> provinceNames) {
        this.provinceNames = provinceNames;
    }

    public ArrayList<CityBean4Province> getProvinces() {
        return provinces;
    }

    public void setProvinces(ArrayList<CityBean4Province> provinces) {
        this.provinces = provinces;
    }
}
