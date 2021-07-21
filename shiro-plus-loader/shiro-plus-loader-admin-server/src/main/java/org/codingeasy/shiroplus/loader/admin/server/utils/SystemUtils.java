package org.codingeasy.shiroplus.loader.admin.server.utils;

import com.nimbusds.jose.jwk.RSAKey;
import org.codingeasy.shiroplus.loader.admin.server.dao.SystemDao;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.SystemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;

/**
* 系统相关工具类  
* @author : KangNing Hu
*/
@Component
public class SystemUtils {

	private static  SystemDao systemDao;

	@Autowired
	public void setSystemDao(SystemDao systemDao){
		SystemUtils.systemDao = systemDao;
	}

	/**
	 * 返回系统源
	 * @return
	 */
	public static SystemDao getSource(){
		return systemDao;
	}

	/**
	 * 获取系统版本号
	 * @return 返回版本号
	 */
	public static String getVersion(){
		return SystemEntity.VERSION;
	}

	/**
	 * 获取系统信息
	 * @return 返回系统信息
	 */
	public static SystemEntity getSystemInfo(){
		return getSource().selectById(getVersion());
	}



	/**
	 * 获取客户端访问token
	 * @return 返回token
	 */
	public static String getClientToken(){
		return getSystemInfo().getClientToken();
	}

	/**
	 * 返回初始化密码
	 * @return 返回初始化密码
	 */
	public static String getInitPassword(){
		return getSystemInfo().getInitPassword();
	}

	/**
	 * 获取事件有效时间
	 * @return 返回有效时长 单位 毫秒
	 */
	public static long getEventTime(){
		return getSystemInfo().getLoginValidTime();
	}


	/**
	 * 获取登录的有效时长
	 * @return 返回登录有效时长 单位 分
	 */
	public static int getLoginValidTime(){
		return getSystemInfo().getLoginValidTime();
	}


	/**
	 * 获取token密钥对
	 * @return 凡您好密钥对象
	 */
	public static RSAKey getRSAKey() throws ParseException {
		return RSAKey.parse(getSystemInfo().getKeyPair());
	}


}
