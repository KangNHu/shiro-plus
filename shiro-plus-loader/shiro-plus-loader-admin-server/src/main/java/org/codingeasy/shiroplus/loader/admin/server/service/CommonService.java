package org.codingeasy.shiroplus.loader.admin.server.service;

import org.codingeasy.shiroplus.loader.admin.server.models.Dict;

import java.util.List;
import java.util.Map;

public interface CommonService {


	/**
	 * 获取所以字典数据
	 * @return 返回字典数据 字典编码->[字典值:字典名称]
	 */
	Map<String , List<Dict>> getDict();

}
