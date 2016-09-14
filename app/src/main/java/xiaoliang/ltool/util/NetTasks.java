package xiaoliang.ltool.util;


import xiaoliang.ltool.constant.Constant;

public class NetTasks {
	/**
	 * 获取中华天气
	 * @param callBack
	 */
	public static void getEtouchWeather(HttpTaskRunnable.CallBack callBack,String city){
		RequestParameters parameters = new RequestParameters();
		parameters.setAccessType(RequestParameters.ACCESS_TYPE_STRING);
		parameters.setUrl(Constant.etouch_WeatherApi+city);
		HttpUtil.createTask(callBack, parameters);
	}

}
