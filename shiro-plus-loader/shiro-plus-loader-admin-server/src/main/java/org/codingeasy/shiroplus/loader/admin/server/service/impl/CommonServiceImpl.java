package org.codingeasy.shiroplus.loader.admin.server.service.impl;

import org.codingeasy.shiroplus.loader.admin.server.models.Dict;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.DictCode;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.RequestMethod;
import org.codingeasy.shiroplus.loader.admin.server.service.CommonService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
* 通用的接口  
* @author : KangNing Hu
*/
@Service
public class CommonServiceImpl implements CommonService {


	/**
	 * 获取所以字典数据
	 * @return 返回字典数据 字典编码->[字典值:字典名称]
	 */
	@Override
	public Map<String, List<Dict>> getDict() {
		return DictCode.toDictMapping();
	}
}
