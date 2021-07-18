package org.codingeasy.shiroplus.core.interceptor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.util.Assert;
import org.codingeasy.shiroplus.core.event.AuthorizeEvent;
import org.codingeasy.shiroplus.core.event.CommonEventType;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.handler.*;
import org.codingeasy.shiroplus.core.metadata.*;
import org.codingeasy.shiroplus.core.realm.processor.AuthProcessor;
import org.codingeasy.shiroplus.core.realm.processor.DefaultAuthProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
*  抽象的动态授权校验
* @author : kangning <a>2035711178@qq.com</a>
*/
public abstract class AbstractAuthorizationInterceptor<R , S> implements AuthInterceptor<R , S> {

	private final static Logger logger = LoggerFactory.getLogger(AbstractAuthorizationInterceptor.class);


	private AuthorizationHandler authorizationHandler = new ProxyAuthorizationHandler();
	protected AuthProcessor<R ,S> authProcessor = new DefaultAuthProcessor<>();
	protected AuthMetadataManager authMetadataManager;
	private EventManager eventManager;


	public AbstractAuthorizationInterceptor(AuthMetadataManager authMetadataManager , EventManager eventManager){
		Assert.notNull(authMetadataManager , "authMetadataManager is not null");
		Assert.notNull(eventManager , "eventManager is not null");
		this.authMetadataManager = authMetadataManager;
		this.eventManager = eventManager;
		addDefaultAuthorizationHandlers();
	}


	public void setAuthProcessor(AuthProcessor<R, S> authProcessor) {
		Assert.notNull(authProcessor , "authProcessor is not null");
		this.authProcessor = authProcessor;
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
	public Object invoke(Invoker<R ,S> invoker){
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
					} catch (AuthorizationException e) {
						logger.warn("请求 [{},{},{}] 权限校验失败 {}",
								permissionMetadata.getPath(),
								permissionMetadata.getMethod(),
								permissionMetadata.getPermiModel(),
								permissionMetadata.toString());
						//发送授权失败事件
						eventManager.asyncPublish(new AuthorizeEvent(invoker , CommonEventType.FAIL));
						//异常处理
						this.authProcessor.authorizationFailure(invoker.getRequest() , invoker.getResponse(), e);
						//异常后置处理
						Object result = authExceptionAfterProcessor(invoker , e);
						if (result != null){
							return result;
						}
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
	 * 权限异常处理后置处理
	 * @param invoker 调用器对象
	 * @param e 授权异常
	 */
	protected Object authExceptionAfterProcessor(Invoker<R ,S> invoker , AuthorizationException e) {
		return null;
	}

	/**
	 * 是否开启授权
	 * @return 如果开启返回true否则false
	 */
	protected boolean isEnableAuthorization(Invoker<R ,S> invoker ) {
		return true;
	}


	/**
	 * 获取全局元信息
	 * @param r 请求对象
	 * @return 返回全局元信息
	 */
	public GlobalMetadata getGlobalMetadata(R r){
		//获取全局配置
		String tenantId = this.authProcessor.getTenantId(r);
		GlobalMetadata globalMetadata = this.authMetadataManager.getGlobalMetadata(tenantId);
		MetadataContext.setCurrentGlobalMetadata(globalMetadata);
		return globalMetadata;
	}

	/**
	 * 获取权限元信息
	 * <p>从 {@link AuthMetadataManager}中获取</p>
	 * @param invoker 调用器
	 * @return 返回权限元信息
	 */
	protected PermissionMetadata getPermissionMetadata(Invoker<R ,S> invoker ){
		String cacheKey = getPermissionMetadataKey(invoker.getRequest());
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

	/**
	 * 获取权限元数据key
	 * @return 返回key
	 */
	protected abstract String  getPermissionMetadataKey(R r);

}
