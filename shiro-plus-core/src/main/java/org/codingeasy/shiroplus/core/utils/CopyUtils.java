package org.codingeasy.shiroplus.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
*
 *   copy 工具类
* @author : KangNing Hu
*/
public class CopyUtils {


	/**
	 * 拷贝map
	 * @param map 待拷贝的 map
	 * @return 返回一个副本
	 */
	public static Map copyMap(Map map){
		if (map == null){
			return new HashMap();
		}
		return new HashMap(map);
	}

	/**
	 * 拷贝 list
	 * @param list 待拷贝的 list
	 * @return 返回一个副本
	 */
	public static List copyList(List list){
		if (list == null){
			return new ArrayList();
		}
		return new ArrayList(list);
	}
}
