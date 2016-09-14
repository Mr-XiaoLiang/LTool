package xiaoliang.ltool.util;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import xiaoliang.ltool.bean.StringNameValueBean;

public class HttpUtil {
	/**
	 * 线程池
	 */
	private static ThreadPoolExecutor threadPool = null;
	public static String TIME_OUT = "1413";// 请求超时
	private static OkHttpClient mOkHttpClient;

	/**
	 * 以Post方式访问网络
	 * @param params
	 * @param url
	 * @param callback
	 * @throws Exception
	 */
	public static void doPost(List<StringNameValueBean> params, String url, Callback callback)
			throws IOException {
		//创建okHttpClient对象
		if(mOkHttpClient==null)
			mOkHttpClient = new OkHttpClient();
		FormEncodingBuilder feb = new FormEncodingBuilder();
		for(StringNameValueBean stringNameValueBean : params){
			feb.add(stringNameValueBean.getName(), stringNameValueBean.getValue());
		}
		RequestBody body = feb.build();
		Request request = new Request.Builder()
				.url(url)
				.post(body)
				.build();
		mOkHttpClient.newCall(request).enqueue(callback);

	}

	/**
	 * 直接返回结果的Post
	 * @param params 参数集合
	 * @param url 访问地址
	 * @return 返回结果
	 * @throws Exception 访问出错
	 */
	public static String doPost(List<StringNameValueBean> params, String url)
			throws IOException {
		//创建okHttpClient对象
		if(mOkHttpClient==null)
			mOkHttpClient = new OkHttpClient();
		FormEncodingBuilder feb = new FormEncodingBuilder();
		for(StringNameValueBean stringNameValueBean : params){
			feb.add(stringNameValueBean.getName(), stringNameValueBean.getValue());
		}
		RequestBody body = feb.build();
		Request request = new Request.Builder()
				.url(url)
				.post(body)
				.build();
		Response response = mOkHttpClient.newCall(request).execute();
		if (response.isSuccessful()){
			return response.body().string();
		}else {
			throw new IOException("Unexpected code " + response);
		}
	}

	/**
	 * 以get方式访问网络
	 * @param url 访问地址
	 * @param callback 回调函数
	 */
	public static void doGet(String url,Callback callback) {
		//创建okHttpClient对象
		if(mOkHttpClient==null)
			mOkHttpClient = new OkHttpClient();
		//创建一个Request
		final Request request = new Request.Builder()
				.url(url)
				.build();
		//new call
		Call call = mOkHttpClient.newCall(request);
		//请求加入调度
		call.enqueue(callback);
	}

	/**
	 * 直接返回结果的Get访问
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String doGet(String url) throws IOException {
		//创建okHttpClient对象
		if(mOkHttpClient==null)
			mOkHttpClient = new OkHttpClient();
		//创建一个Request
		final Request request = new Request.Builder()
				.url(url)
				.build();
		//new call
		Call call = mOkHttpClient.newCall(request);
		//请求加入调度
		Response response = call.execute();
		if (response.isSuccessful()){
			return response.body().string();
		}else {
			throw new IOException("Unexpected code " + response);
		}
	}



	/**
	 * 获取线程来执行任务
	 * @param run 任务对象
	 */
	public static void getThread(Runnable run) {
		if (threadPool == null) {
			threadPool = new ThreadPoolExecutor(2, 10, 3, TimeUnit.SECONDS,
					new ArrayBlockingQueue<Runnable>(30),
					new ThreadPoolExecutor.CallerRunsPolicy());
		}
		threadPool.execute(run);
	}
	/**
	 * 创建任务
	 * @param callBack 回调函数
	 * @param parameters 参数类
	 */
	public static void createTask(HttpTaskRunnable.CallBack callBack, RequestParameters parameters){
		getThread(new HttpTaskRunnable(callBack, parameters));
	}

}
