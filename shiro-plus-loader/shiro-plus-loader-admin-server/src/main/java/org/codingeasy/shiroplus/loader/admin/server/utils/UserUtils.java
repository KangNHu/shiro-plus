package org.codingeasy.shiroplus.loader.admin.server.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
* 用户工具类  
* @author : KangNing Hu
*/
public class UserUtils  {


	/**
	 * 获取当前登录用户的userId
	 * @return 返回userId
	 */
	public static Long getUserId(){
		//设置当前操作人id
		Subject subject = SecurityUtils.getSubject();
		return (Long) subject.getPrincipal();
	}




}
