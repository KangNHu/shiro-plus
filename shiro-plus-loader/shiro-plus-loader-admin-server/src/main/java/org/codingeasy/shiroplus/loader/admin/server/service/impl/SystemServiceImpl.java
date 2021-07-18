package org.codingeasy.shiroplus.loader.admin.server.service.impl;

import com.nimbusds.jose.jwk.RSAKey;
import org.apache.commons.lang3.StringUtils;
import org.codingeasy.shiroplus.loader.admin.server.dao.SystemDao;
import org.codingeasy.shiroplus.loader.admin.server.exception.BusinessException;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.SystemEntity;
import org.codingeasy.shiroplus.loader.admin.server.service.SystemService;
import org.codingeasy.shiroplus.loader.admin.server.utils.JwtUtils;
import org.codingeasy.shiroplus.loader.admin.server.utils.SystemUtils;
import org.codingeasy.streamrecord.core.annotation.Record;
import org.codingeasy.streamrecord.core.annotation.RecordService;
import org.codingeasy.streamrecord.core.annotation.Search;
import org.springframework.beans.factory.annotation.Autowired;

/**
* 系统管理  
* @author : KangNing Hu
*/
@RecordService
public class SystemServiceImpl  implements SystemService {

	@Autowired
	private SystemDao systemDao;

	/**
	 * 获取系统配置信息
	 * @return 返回系统配置信息
	 */
	@Override
	public SystemEntity get() {
		return SystemUtils.getSystemInfo();
	}

	/**
	 * 修改系统配置
	 * @param systemEntity 请求对象
	 * @return 返回响应对象
	 */
	@Record("修改系统全局配置")
	@Override
	public int update(SystemEntity systemEntity) {
		String keyPair = systemEntity.getKeyPair();
		//校验密钥的合法性
		if (!StringUtils.isEmpty(keyPair)){
			try {
				JwtUtils.validateRSAKey(keyPair);
			}catch (Exception e){
				throw new BusinessException("登录token的签名密钥无效或无法被解析");
			}
		}
		systemEntity.setVersion(SystemEntity.VERSION);
		return systemDao.updateById(systemEntity);
	}
}
