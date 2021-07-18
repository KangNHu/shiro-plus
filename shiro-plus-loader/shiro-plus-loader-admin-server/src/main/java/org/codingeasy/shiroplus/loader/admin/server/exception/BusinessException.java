package org.codingeasy.shiroplus.loader.admin.server.exception;

/**
* ouath异常  
* @author : KangNing Hu
*/
public class BusinessException extends IllegalStateException{

	/**
	 * 异常消息
	 */
	private String message;


	public BusinessException() {
	}

	public BusinessException(String s) {
		super(s);
		this.message =s;
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
		this.message = buildMessage(message , cause);
	}



	public BusinessException(Throwable cause) {
		this(null ,cause);
	}


	/**
	 * 构建异常自定义异常消息
	 * @param message 源消息
	 * @param cause 源异常对象
	 * @return 返回自定义消息
	 */
	private String buildMessage(String message, Throwable cause) {
		if (message == null){
			message = "";
		}
		if (cause != null){
			message = message + ":" + cause.getLocalizedMessage();
		}
		return message;
	}


	@Override
	public String getMessage() {
		return message;
	}
}
