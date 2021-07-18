package org.codingeasy.shiroplus.loader.admin.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
*   json 相关工具类
* @author : KangNing Hu
*/
public class JsonUtils {

	/**
	 * 对象转json字符串
	 * @param obj 对象
	 * @return 返回json字符串
	 */
	public static String toJsonString(Object obj){
		try {
			if (obj == null){
				return "{}";
			}
			return new ObjectMapper().writeValueAsString(obj);
		}catch (Exception e){
			throw new IllegalStateException(e);
		}
	}

}
