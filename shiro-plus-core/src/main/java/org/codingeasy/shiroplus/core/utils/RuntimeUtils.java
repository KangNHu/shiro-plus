package org.codingeasy.shiroplus.core.utils;

/**
* 运行时工具类  
* @author : KangNing Hu
*/
public class RuntimeUtils {

	/**
	 * 获取核心数
	 * @return 返回核心数
	 */
	public static  int availableProcessors(int defaultValue){
		int count = Runtime.getRuntime().availableProcessors();
		return count < 0 ? defaultValue : count;
	}
}
