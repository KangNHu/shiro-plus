package org.codingeasy.shiroplus.loader.admin.server.listener;

import org.codingeasy.shiroplus.loader.admin.server.dao.RoleDao;
import org.codingeasy.shiroplus.loader.admin.server.dao.SystemDao;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.RoleEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.SystemEntity;
import org.codingeasy.shiroplus.loader.admin.server.utils.JwtUtils;
import org.codingeasy.shiroplus.loader.admin.server.utils.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
* 系统初始化  
* @author : KangNing Hu
*/
@Component
public class SystemInitListener implements ApplicationListener<ContextRefreshedEvent> {

	private static Logger log = LoggerFactory.getLogger(SystemInitListener.class);

	@Autowired
	private RoleDao roleDao;

	private volatile boolean init = false;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		//initRole();
		if (init){
			return;
		}
		//初始化系统基本信息
		SystemDao source = SystemUtils.getSource();
		SystemEntity systemEntity = source.selectById(SystemEntity.getVERSION());
		if (systemEntity != null){
			return;
		}
		systemEntity = new SystemEntity();
		systemEntity.setVersion(SystemEntity.VERSION);
		systemEntity.setClientToken("123456");
		//初始化密码
		systemEntity.setInitPassword("admin123456");
		//1天
		systemEntity.setEventTime(1*24*3600*1000L);
		//2小时
		systemEntity.setLoginValidTime(120);
		//系统 token签名密钥
		systemEntity.setKeyPair(JwtUtils.createKeyPair().toJSONString());
		source.insert(systemEntity);
		this.init = true;
		log.info("系统初始化成功");
	}


//	public void initRole(){
//
//		String[] names = {
//				"用户管理(读)",
//				"用户管理(读写)",
//				"系统配置(读)",
//				"系统配置(读写)",
//				"权限管理(读)",
//				"权限管理(读写)",
//				"全局元数据管理(读)",
//				"全局元数据管理(读写)",
//				"权限元数据管理(读)",
//				"权限元数据管理(读写)"
//		};
//		String[] roleCodes = {
//				"admin:user:read",
//				"admin:user:read:write",
//				"admin:systemSetting:read",
//				"admin:systemSetting:read:write",
//				"admin:role:read",
//				"admin:role:read:write",
//				"admin:globalMetadata:read",
//				"admin:globalMetadata:read:write",
//				"admin:permiMetadata:read",
//				"admin:permiMetadata:read:write"
//		};
//		String[] descriptions ={
//				"只有用户管理查看权限",
//				"用户管理的查看和编辑权限",
//				"只有系统设置-全局配置的查看权限",
//				"系统设置-全局配置的产看和编辑权限",
//				"只有权限管理的查看权限",
//				"权限管理的查看和编辑权限",
//				"只有全局元数据的查看权限",
//				"只有全局元数据的查看和编辑的权限",
//				"权限元数据管理的查看权限",
//				"权限元数据管理的查看和编辑权限"
//		};
//		for (int i = 0 ; i < names.length ; i ++){
//			RoleEntity roleEntity = new RoleEntity();
//			roleEntity.setName(names[i]);
//			roleEntity.setCode(roleCodes[i]);
//			roleEntity.setDescription(descriptions[i]);
//			roleDao.insert(roleEntity);
//		}
//	}


}
