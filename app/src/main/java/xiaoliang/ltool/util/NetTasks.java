package xiaoliang.ltool.util;


import xiaoliang.ltool.constant.Constant;

public class NetTasks {
	/**
	 * 获取中华天气
	 * @param callBack
	 */
	public static void getEtouchWeather(HttpTaskRunnable.CallBack callBack,String city){
		RequestParameters parameters = new RequestParameters();
		parameters.setAccessType(RequestParameters.ACCESS_TYPE_OBJECT);
		parameters.setUrl(Constant.etouch_WeatherApi+city);
		HttpUtil.createTask(callBack, parameters);
	}

	/**
	 * 获取城市列表
	 * @param callBack
     */
	public static void getCitys(HttpTaskRunnable.CallBack callBack){
		RequestParameters parameters = new RequestParameters();
		parameters.setAccessType(RequestParameters.ACCESS_TYPE_OBJECT);
		parameters.setUrl(Constant.MOB_Citys_url);
		HttpUtil.createTask(callBack, parameters);
	}

	/**
	 * 简单的获取数据
	 * @param url
	 * @param callBack
     */
	public static void getSimpleData(String url,HttpTaskRunnable.CallBack callBack){
		RequestParameters parameters = new RequestParameters();
		parameters.setAccessType(RequestParameters.ACCESS_TYPE_OBJECT);
		parameters.setUrl(url);
		HttpUtil.createTask(callBack, parameters);
	}
}
