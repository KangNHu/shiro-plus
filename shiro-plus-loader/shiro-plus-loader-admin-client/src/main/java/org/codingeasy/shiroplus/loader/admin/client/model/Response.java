package org.codingeasy.shiroplus.loader.admin.client.model;

/**
* 响应对象  
* @author : KangNing Hu
*/
public class Response<T> {

	/**
	 * 响应数据
	 */
	private T data;

	/**
	 * 响应消息
	 */
	private String msg;

	/**
	 * 是否成功
	 */
	private boolean succeed = true;


	/**
	 * 成功
	 * @param data 响应数据
	 * @param <T> 响应数据类型
	 * @return 返回响应体
	 */
	public static <T>Response<T> ok(T data){
		 Response<T> response = new Response<>();
		 response.setData(data);
		 return response;
	}

	/**
	 * 失败
	 * @param msg 失败消息
	 * @param <T> 响应数据类型
	 * @return 返回响应体
	 */
	public static <T>Response<T> failure(String msg){
		Response<T> response = new Response<>();
		response.setMsg(msg);
		response.setSucceed(false);
		return response;
	}


	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSucceed() {
		return succeed;
	}

	public void setSucceed(boolean succeed) {
		this.succeed = succeed;
	}
}
