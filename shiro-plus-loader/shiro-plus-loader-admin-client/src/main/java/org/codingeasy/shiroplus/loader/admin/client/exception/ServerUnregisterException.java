package org.codingeasy.shiroplus.loader.admin.client.exception;

/**
* 服务未注册  
* @author : KangNing Hu
*/
public class ServerUnregisterException extends   IllegalStateException{


	public ServerUnregisterException() {
		super();
	}

	public ServerUnregisterException(String s) {
		super(s);
	}

	public ServerUnregisterException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServerUnregisterException(Throwable cause) {
		super(cause);
	}
}

