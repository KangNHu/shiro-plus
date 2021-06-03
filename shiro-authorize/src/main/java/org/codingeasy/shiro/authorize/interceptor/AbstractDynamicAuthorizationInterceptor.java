package org.codingeasy.shiro.authorize.interceptor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.codingeasy.shiro.authorize.handler.*;
import org.codingeasy.shiro.authorize.metadata.AuthMetadataManager;
import org.codingeasy.shiro.authorize.metadata.PermissionMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
*  抽象的动态授权校验
* @author : KangNing Hu
*/
public class AbstractDynamicAuthorizationInterceptor implements DynamicAuthorizationInterceptor{

	private final static Logger logger = LoggerFactory.getLogger(AbstractDynamicAuthorizationInterceptor.class);


	private AuthorizationHandler authorizationHandler = new ProxyAuthorizationHandler();

	private AuthMetadataManager authMetadataManager;

	private AuthExceptionHandler authExceptionHandler;


	public AbstractDynamicAuthorizationInterceptor(AuthMetadataManager authMetadataManager ,AuthExceptionHandler authExceptionHandler ){
		this.authExceptionHandler = authExceptionHandler;
		this.authMetadataManager = authMetadataManager;
		addDefaultAuthorizationHandlers();
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
		//获取权限元信息
		PermissionMetadata permissionMetadata = getPermissionMetadata(invoker);
		//元信息不为空，则进行接口权限校验
		if (permissionMetadata != null){
			List<String> permis = permissionMetadata.getPermis();
			//权限标识不为空则进行校验
			if (!CollectionUtils.isEmpty(permis)){
				if (logger.isDebugEnabled()){
					logger.debug("请求 [{},{},{}] 权限校验 {}" ,
							permissionMetadata.getPath() ,
							permissionMetadata.getMethod() ,
							permissionMetadata.getPermiModel(),
							permissionMetadata.toString());
				}
				try {
					authorizationHandler.authorize(permissionMetadata);
				}catch (AuthorizationException a){
					logger.warn("请求 [{},{},{}] 权限校验失败 {}" ,
							permissionMetadata.getPath() ,
							permissionMetadata.getMethod() ,
							permissionMetadata.getPermiModel(),
							permissionMetadata.toString());
					this.authExceptionHandler.authorizationFailure(invoker , a);
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
	 * 获取权限元信息
	 * @param invoker 调用器
	 * @return 返回权限元信息
	 */
	protected PermissionMetadata getPermissionMetadata(Invoker invoker){
		return this.authMetadataManager.getPermissionMetadata(invoker.getPermissionMetadataKey());
	}
}
