package org.codingeasy.shiroplus.loader.admin.server.exception;

import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Map;

/**
* 业务断言  
* @author : KangNing Hu
*/
public class BusinessAssert {

	/**
	 * 断言 是否为空
	 * @param object 断言的对象
	 * @param message 异常信息
	 */
	public static void isNull(@Nullable Object object, String message) {
		if (object != null) {
			throw new BusinessException(message);
		}
	}

	/**
	 * 断言 不为空
	 * @param object 断言的对象
	 * @param message 异常信息
	 */
	public static void notNull(@Nullable Object object, String message) {
		if (object == null) {
			throw new BusinessException(message);
		}
	}

	/**
	 * 断言 不为空元素 如 空 map ,set ,list ,array
	 * @param object 断言对象
	 * @param message 异常信息
	 */
	public static void notEmpty(@Nullable Object object, String message) {
		if (!isNotEmpty(object)){
			throw new BusinessException(message);
		}

	}


	/**
	 * 断言 不为空元素 如 空 map ,set ,list ,array
	 * @param object 断言对象
	 * @param message 异常信息
	 */
	public static void isEmpty(@Nullable Object object, String message) {
		if (isNotEmpty(object)) {
			throw new BusinessException(message);
		}
	}

	/**
	 * 断言状态
	 * @param ex 状态 true 或者 false
	 * @param message 异常信息
	 */
	public static void state(@NotNull boolean ex , String message ){
		if (!ex){
			throw new BusinessException(message);
		}
	}

	/**
	 * 判断是否为空元素
	 * @param object 判断对象
	 * @return 如果 类型为 list set map array 且不为 null 和 空对象时 则返回true 否则 false 普通Object类型 按照 是否 等于 null来判断
	 */
	private static  boolean isNotEmpty(Object object){
		boolean fls = true;
		if (object == null){
			return false;
		}
		if (object instanceof Collection){
			fls = !CollectionUtils.isEmpty((Collection<?>) object);
		}else if (object instanceof Map){
			fls = !CollectionUtils.isEmpty((Map<?, ?>) object);
		}else if (object.getClass().isArray()){
			fls = ((Object[])object).length > 0;
		}
		return fls;
	}


}
