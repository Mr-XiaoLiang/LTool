package xiaoliang.ltool.util;


import java.io.File;
import java.util.List;

import xiaoliang.ltool.bean.StringNameValueBean;

public class RequestParameters {
	/**
	 * 直接返回字符串
	 */
	public static final int ACCESS_TYPE_STRING = 0;
	/**
	 * 返回对象
	 */
	public static final int ACCESS_TYPE_OBJECT = 1;
	/**
	 * 上传
	 */
	public static final int ACCESS_TYPE_UPLOAD = 2;
	/**
	 * 下载
	 */
	public static final int ACCESS_TYPE_DOWNLOAD = 3;
	/**
	 * 访问类型
	 */
	private int accessType = ACCESS_TYPE_STRING;
	/**
	 * 访问地址
	 */
	private String url = "";
	/**
	 * 参数
	 */
	private List<StringNameValueBean> parameters;
	/**
	 * 下载地址
	 */
	private String downloadPath = "";
	/**
	 * 上传的文件
	 */
	private File uploadFile = null;
	/**
	 * 进度监听回调
	 */
	private Progress progress;
	/**
	 * 返回对象的反射
	 */
	private Class cla;


	public int getAccessType() {
		return accessType;
	}



	public void setAccessType(int accessType) {
		this.accessType = accessType;
	}



	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}



	public List<StringNameValueBean> getParameters() {
		return parameters;
	}



	public void setParameters(List<StringNameValueBean> parameters) {
		this.parameters = parameters;
	}



	public String getDownloadPath() {
		return downloadPath;
	}



	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}



	public File getUploadFile() {
		return uploadFile;
	}



	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}



	public Progress getProgress() {
		return progress;
	}

	public void setProgress(Progress progress) {
		this.progress = progress;
	}

	public void setProgress(float pro) {
		if(progress!=null){
			progress.onProgress(pro);
		}
	}

	public void onLoadSeccess(String path){
		if(progress!=null){
			progress.onLoadSeccess(path);
		}
	}

	public void onLoadError(Exception e,int type){
		if(progress!=null){
			progress.onLoadError(e, type);
		}
	}

	public Class getCla() {
		return cla;
	}



	public void setCla(Class cla) {
		this.cla = cla;
	}



	/**
	 * 上传下载进度监听器
	 * @author LiuJ
	 */
	public interface Progress{
		/**
		 * 进度更新
		 * @param pro
		 */
		public void onProgress(float pro);
		/**
		 * 上传下载完毕
		 * @param path 下载文件地址
		 */
		public void onLoadSeccess(String path);
		/**
		 * 下载失败
		 * @param e
		 * @param type
		 */
		public void onLoadError(Exception e, int type);
	}
}
