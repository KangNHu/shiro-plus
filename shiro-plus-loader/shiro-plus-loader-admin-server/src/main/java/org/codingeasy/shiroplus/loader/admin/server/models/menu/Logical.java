package org.codingeasy.shiroplus.loader.admin.server.models.menu;

import org.codingeasy.shiroplus.loader.admin.server.models.Dict;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 逻辑
 * @author hukangning
 */
public enum Logical{


	//角色授权模式
	AND( 1, org.codingeasy.shiroplus.core.metadata.Logical.AND.name()),
	//权限授权模式
	OR(2 , org.codingeasy.shiroplus.core.metadata.Logical.OR.name());


	private int value;

	private String name;


	Logical(int value , String name){
		this.value = value;
		this.name = name;
	}


	/**
	 * 转字典
	 * @return
	 */
	public static List<Dict> toDict(){
		return Arrays
				.asList(Logical.values())
				.stream()
				.map(item -> new Dict(item.value , item.name))
				.collect(Collectors.toList());
	}
	/**
	 * 值转换为名称
	 * @param value 值
	 * @return 名称
	 */
	public static String form(int value){
		for (Logical logical : Logical.values()){
			if (logical.value == value){
				return logical.name;
			}
		}
		return null;
	}
}
