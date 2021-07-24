package org.codingeasy.shiroplus.loader.admin.client.exception;

/**
* admin服务宕机异常  
* @author : KangNing Hu
*/
public class ServerDownException extends IllegalStateException {


	public ServerDownException() {
		super();
	}

	public ServerDownException(String s) {
		super(s);
	}

	public ServerDownException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServerDownException(Throwable cause) {
		super(cause);
	}
}
