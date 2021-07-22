package org.codingeasy.shiroplus.loader.admin.server.models.menu;

import org.codingeasy.shiroplus.loader.admin.server.models.Dict;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
* 字典编码  
* @author : KangNing Hu
*/
public enum  DictCode {

	//请求方法
	REQUEST_METHOD_CODE(() -> RequestMethod.toDict() ,"method_code"),
	//授权权限模型
	PERMISSION_MODEL_CODE(() -> PermissionModel.toDict() , "permission_model_code"),
	//逻辑
	LOGICAL_CODE(() -> Logical.toDict() , "logical_code");
	/**
	 * 字典的枚举
	 */
	private Supplier<List<Dict>> dictSupplier;

	/**
	 * 字典编码
	 */
	private String dictCode;


	DictCode(Supplier<List<Dict>> dictSupplier , String dictCode){
		this.dictSupplier = dictSupplier;
		this.dictCode = dictCode;
	}


	/**
	 * 转换为字典映射
	 * @return 字典编码 -> 字典列表
	 */
	public static Map<String , List<Dict>> toDictMapping(){
		Map<String , List<Dict>> map = new HashMap<>();
		for (DictCode dictCode : DictCode.values()){
			map.put(dictCode.dictCode , dictCode.dictSupplier.get());
		}
		return map;
	}
}
