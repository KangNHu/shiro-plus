package org.codingeasy.shiroplus.loader.admin.server.listener;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
* 无意义的session 监听器 处理报错问题  
* @author : KangNing Hu
*/
@Component
public class UnmeaningSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {

	}
}
