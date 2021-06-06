package org.codingeasy.shiro.example.event;

import org.apache.shiro.event.Subscribe;
import org.apache.shiro.event.support.EventListener;
import org.codingeasy.shiro.core.event.AuthorizeEvent;
import org.codingeasy.shiro.core.event.CommonEventType;
import org.codingeasy.shiro.core.interceptor.WebInvoker;
import org.springframework.stereotype.Component;


/**
*   
* @author : KangNing Hu
*/
@Component
public class AuthEventListener {


	@Subscribe
	public void onAuthorSucceed(AuthorizeEvent authorizeEvent){
		CommonEventType type = authorizeEvent.getType();
		System.out.println("AuthorSucceed");
		Object source = authorizeEvent.getSource();
		if (source instanceof WebInvoker){
			System.out.println(((WebInvoker) source).getRequest().getRequestURI());
		}
	}

}
