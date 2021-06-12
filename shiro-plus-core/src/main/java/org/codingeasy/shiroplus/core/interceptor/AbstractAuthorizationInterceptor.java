package org.codingeasy.shiroplus.core.interceptor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.util.Assert;
import org.codingeasy.shiroplus.core.event.AuthorizeEvent;
import org.codingeasy.shiroplus.core.event.CommonEventType;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.handler.*;
import org.codingeasy.shiroplus.core.metadata.AuthMetadataManager;
import org.codingeasy.shiroplus.core.metadata.PermiModel;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;
import org.codingeasy.shiroplus.core.mgt.DefaultTenantIdGenerator;
import org.codingeasy.shiroplus.core.mgt.TenantIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
*  抽象的动态授权校验
* @author : kangning <a>2035711178@qq.com</a>
*/
public class AbstractAuthorizationInterceptor implements AuthInterceptor {

	private final static Logger logger = LoggerFactory.getLogger(AbstractAuthorizationInterceptor.class);


	private AuthorizationHandler authorizationHandler = new ProxyAuthorizationHandler();

	protected AuthMetadataManager authMetadataManager;

	private AuthExceptionHandler authExceptionHandler;

	private EventManager eventManager;

	protected TenantIdGenerator tenantIdGenerator = new DefaultTenantIdGenerator();


	public AbstractAuthorizationInterceptor(AuthMetadataManager authMetadataManager ,
	                                        AuthExceptionHandler authExceptionHandler ,
	                                        EventManager eventManager){
		Assert.notNull(authExceptionHandler , "权限异常处理器不能为空");
		Assert.notNull(authMetadataManager , "权限元数据管理器不能为空");
		Assert.notNull(eventManager , "事件管理器不能为空");

		this.authExceptionHandler = authExceptionHandler;
		this.authMetadataManager = authMetadataManager;
		this.eventManager = eventManager;
		addDefaultAuthorizationHandlers();
	}



	public void setTenantIdGenerator(TenantIdGenerator tenantIdGenerator) {
		this.tenantIdGenerator = tenantIdGenerator;
	}

	/**
	 * 添加默认的授权处理器
	 */
	private void addDefaultAuthorizationHandlers() {
		ProxyAuthorizationHandler authorizationHandler = (ProxyAuthorizationHandler)  this.authorizationHandler;
		authorizationHandler.addAuthorizationHandler(new PermissionAuthorizationHandler());
		authorizationHandler.addAuthorizationHandler(new RoleAuthorizationHandler());
		authorizationHandler.addAuthorizationHandler(new PrincipalAuthorizationHandler());
		authorizationHandler.addAuthorizationHandler(new AuthenticatedStateAuthorizationHandler());
		authorizationHandler.addAuthorizationHandler(new UserAuthorizationHandler());
	}

	/**
	 * 新增授权处理器
	 * @param authorizationHandler 授权处理器
	 */
	public void addAuthorizationHandler(AuthorizationHandler authorizationHandler){
		((ProxyAuthorizationHandler)this.authorizationHandler).addAuthorizationHandler(authorizationHandler);
	}


	@Override
	public Object invoke(Invoker invoker){
		//是否开启授权
		if (isEnableAuthorization(invoker)) {
			//获取权限元信息
			PermissionMetadata permissionMetadata = getPermissionMetadata(invoker);
			//元信息不为空，则进行接口权限校验
			if (permissionMetadata != null) {
				List<String> permis = permissionMetadata.getPermis();
				//权限标识不为空则进行校验
				if (!CollectionUtils.isEmpty(permis)) {
					if (logger.isDebugEnabled()) {
						logger.debug("请求 [{},{},{}] 权限校验 {}",
								permissionMetadata.getPath(),
								permissionMetadata.getMethod(),
								permissionMetadata.getPermiModel(),
								permissionMetadata.toString());
					}
					try {
						authorizationHandler.authorize(permissionMetadata);
						//发送授权成功事件
						eventManager.asyncPublish(new AuthorizeEvent(invoker , CommonEventType.SUCCEED));
					} catch (AuthorizationException a) {
						logger.warn("请求 [{},{},{}] 权限校验失败 {}",
								permissionMetadata.getPath(),
								permissionMetadata.getMethod(),
								permissionMetadata.getPermiModel(),
								permissionMetadata.toString());
						//发送授权失败事件
						eventManager.asyncPublish(new AuthorizeEvent(invoker , CommonEventType.FAIL));
						//异常处理
						this.authExceptionHandler.authorizationFailure(invoker, a);
					}
				}
			}
		}
		try {
			return invoker.invoke();
		}catch (Exception e){
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 是否开启授权
	 * @return 如果开启返回true否则false
	 */
	protected boolean isEnableAuthorization(Invoker invoker) {
		return true;
	}


	/**
	 * 获取权限元信息
	 * <p>从 {@link AuthMetadataManager}中获取</p>
	 * @param invoker 调用器
	 * @return 返回权限元信息
	 */
	protected PermissionMetadata getPermissionMetadata(Invoker invoker){
		String cacheKey = invoker.getPermissionMetadataKey();
		//获取 permission 授权模式
		PermissionMetadata permissionMetadata = this.authMetadataManager.getPermissionMetadata(cacheKey, PermiModel.PERMISSION);
		if (permissionMetadata != null){
			return permissionMetadata;
		}
		//获取 role 授权模式
		permissionMetadata = this.authMetadataManager.getPermissionMetadata(cacheKey, PermiModel.ROLE);
		if (permissionMetadata != null){
			return permissionMetadata;
		}
		//获取 user 授权模式
		permissionMetadata = this.authMetadataManager.getPermissionMetadata(cacheKey, PermiModel.USER);
		if (permissionMetadata != null){
			return permissionMetadata;
		}
		//获取 authentication 授权模式
		permissionMetadata = this.authMetadataManager.getPermissionMetadata(cacheKey , PermiModel.AUTHENTICATION);
		if (permissionMetadata != null){
			return permissionMetadata;
		}
		//获取 principal 授权模式
		return this.authMetadataManager.getPermissionMetadata(cacheKey , PermiModel.PRINCIPAL);
	}
}
