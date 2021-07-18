package org.codingeasy.shiroplus.gateway.factory;

import org.apache.commons.lang3.StringUtils;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.interceptor.Invoker;
import org.codingeasy.shiroplus.core.metadata.AuthMetadataManager;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.gateway.GatewayInvoker;
import org.codingeasy.shiroplus.gateway.HttpGatewayAuthProcessor;
import org.codingeasy.shiroplus.gateway.TokenGenerator;
import org.codingeasy.shiroplus.gateway.filter.AuthGatewayFilter;
import org.codingeasy.shiroplus.gateway.token.GatewayAuthenticationToken;
import org.codingeasy.shiroplus.gateway.token.SimpleGatewayAuthenticationToken;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;

import static org.codingeasy.shiroplus.gateway.factory.AuthGatewayFilterFactory.CarryStrategy.*;

/**
* 认证网关过滤器工厂  
* @author : KangNing Hu
*/
public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthGatewayFilterFactory.Config> {

	/**
	 * 权限元信息管理器
	 */
	private AuthMetadataManager authMetadataManager;
	/**
	 * 事件管理器
	 */
	private EventManager eventManager;

	/**
	 * token 生成器
	 */
	private TokenGenerator tokenGenerator;
	/**
	 * 配置
	 */
	private static Config config;


	private HttpGatewayAuthProcessor httpGatewayAuthProcessor;


	public AuthGatewayFilterFactory(AuthMetadataManager authMetadataManager ,
	                                HttpGatewayAuthProcessor httpGatewayAuthProcessor ,
	                                EventManager eventManager){
		super(Config.class);
		this.httpGatewayAuthProcessor = httpGatewayAuthProcessor;
		this.authMetadataManager = authMetadataManager;
		this.eventManager = eventManager;
	}

	public void setHttpGatewayAuthProcessor(HttpGatewayAuthProcessor httpGatewayAuthProcessor) {
		this.httpGatewayAuthProcessor = httpGatewayAuthProcessor;
	}

	@Override
	public GatewayFilter apply(Config config) {
		checkConfig(config);
		if (this.httpGatewayAuthProcessor == null){
			this.httpGatewayAuthProcessor = new HttpGatewayAuthProcessor(config);
		}
		AuthGatewayFilter authGatewayFilter = new AuthGatewayFilter(authMetadataManager,httpGatewayAuthProcessor , eventManager);
		authGatewayFilter.setPathPrefix(config.authorizationPathPrefix);
		return authGatewayFilter;
	}

	/**
	 * 校验配置
	 * @param config 配置对象
	 */
	private void checkConfig(Config config) {
		Assert.notNull(config.tenantName , "AuthGatewayFilterFactory$config#tenantName not is null");
		Assert.notNull(config.tenantStrategy , "AuthGatewayFilterFactory$config#tenantStrategy not is null");
		Assert.notNull(config.tenantPathIndex , "AuthGatewayFilterFactory$config#tenantPathIndex not is null");
		Assert.notNull(config.tokenName , "AuthGatewayFilterFactory$config#tokenName not is null");
		Assert.notNull(config.tokenStrategy , "AuthGatewayFilterFactory$config#tokenStrategy not is null");
		Assert.notNull(config.authorizationPathPrefix , "AuthGatewayFilterFactory$config#authorizationPathPrefix not is null");
		AuthGatewayFilterFactory.config = config;
	}




	/**
	 * 配置
	 * @author hukangning
	 */
	public static class Config {

		static final String AUTHENTICATION = "Authentication";
		static final String TENANT = "tenantId";
		/**
		 * 租户参数名称
		 */
		private String tenantName = TENANT;


		/**
		 * 租户携带策略
		 */
		private CarryStrategy tenantStrategy = CarryStrategy.PATH;

		/**
		 * {@link this#tenantStrategy}为{@link CarryStrategy#PATH}时，取路径第几级
		 */
		private Integer tenantPathIndex = 0;

		/**
		 * token参数名称
		 */
		private String tokenName = AUTHENTICATION;

		/**
		 * token携带策略
		 */
		private CarryStrategy tokenStrategy = HEAD;

		/**
		 * 授权路径前缀
		 */
		private String authorizationPathPrefix = "";

		public Config setTenantName(String tenantName) {
			this.tenantName = tenantName;
			return this;
		}

		public void setAuthorizationPathPrefix(String authorizationPathPrefix) {
			this.authorizationPathPrefix = authorizationPathPrefix;
		}

		public Config setTenantStrategy(CarryStrategy tenantStrategy) {
			this.tenantStrategy = tenantStrategy;
			return this;
		}

		public String getTenantName() {
			return tenantName;
		}

		public CarryStrategy getTenantStrategy() {
			return tenantStrategy;
		}

		public void setTenantPathIndex(Integer tenantPathIndex) {
			this.tenantPathIndex = tenantPathIndex;
		}

		public String getTokenName() {
			return tokenName;
		}

		public CarryStrategy getTokenStrategy() {
			return tokenStrategy;
		}

		public String getAuthorizationPathPrefix() {
			return authorizationPathPrefix;
		}

		public int getTenantPathIndex() {
			return tenantPathIndex;
		}

		public void setTenantPathIndex(int tenantPathIndex) {
			this.tenantPathIndex = tenantPathIndex;
		}

		public void setTokenName(String tokenName) {
			this.tokenName = tokenName;
		}

		public void setTokenStrategy(CarryStrategy tokenStrategy) {
			this.tokenStrategy = tokenStrategy;
		}
	}

	/**
	 * 租户参数的携带策略
	 * @author hukangning
	 */
	public enum  CarryStrategy{
		/**
		 * 请求路径，如 http://{域名}/{tenantParamVaue}/{path}
		 */
		PATH,
		/**
		 * 请求头 如 {tenantParamName}:{tenantParamVaue}
		 */
		HEAD,
		/**
		 * 请求参数 如 ?{tenantParamName}={tenantParamVaue}
		 */
		QUERY,
		/**
		 * cookie {tenantParamName}={tenantParamVaue}
		 */
		COOKIE

	}



}
