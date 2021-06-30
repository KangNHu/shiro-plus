package org.codingeasy.shiroplus.gateway.factory;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.handler.AuthExceptionHandler;
import org.codingeasy.shiroplus.core.interceptor.Invoker;
import org.codingeasy.shiroplus.core.metadata.AuthMetadataManager;
import org.codingeasy.shiroplus.core.mgt.TenantIdGenerator;
import org.codingeasy.shiroplus.gateway.AuthExceptionRedirectHandler;
import org.codingeasy.shiroplus.gateway.GatewayInvoker;
import org.codingeasy.shiroplus.gateway.TokenGenerator;
import org.codingeasy.shiroplus.gateway.filter.AuthGatewayFilter;
import org.codingeasy.shiroplus.gateway.utils.WebUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;

import java.util.regex.Pattern;

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
	 * 权限异常处理器
	 */
	private AuthExceptionHandler authExceptionHandler;
	/**
	 * 事件管理器
	 */
	private EventManager eventManager;

	/**
	 * token 生成器
	 */
	private TokenGenerator tokenGenerator;
	/**
	 * 租户id生成器
	 */
	private TenantIdGenerator tenantIdGenerator;

	/**
	 * 配置
	 */
	private  static Config config;


	public AuthGatewayFilterFactory(AuthMetadataManager authMetadataManager ,
	                                AuthExceptionHandler authExceptionHandler ,
	                                EventManager eventManager){
		super(Config.class);
		this.authExceptionHandler = authExceptionHandler;
		this.authMetadataManager = authMetadataManager;
		this.eventManager = eventManager;
	}


	public void setTokenGenerator(TokenGenerator tokenGenerator) {
		this.tokenGenerator = tokenGenerator;
	}

	public void setTenantIdGenerator(TenantIdGenerator tenantIdGenerator) {
		this.tenantIdGenerator = tenantIdGenerator;
	}


	@Override
	public GatewayFilter apply(Config config) {
		checkConfig(config);
		if (authExceptionHandler == null){
			authExceptionHandler = new AuthExceptionRedirectHandler(tenantIdGenerator , authMetadataManager);
		}
		AuthGatewayFilter authGatewayFilter = new AuthGatewayFilter(authMetadataManager, authExceptionHandler, eventManager);
		if (tenantIdGenerator != null){
			authGatewayFilter.setTenantIdGenerator(tenantIdGenerator);
		}
		if (tokenGenerator != null){
			authGatewayFilter.setTokenGenerator(tokenGenerator);
		}
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
		if(this.tenantIdGenerator == null){
			this.tenantIdGenerator = new DynamicStrategyTenantIdGenerator();
		}
		if (this.tokenGenerator == null){
			this.tokenGenerator = new DynamicStrategyTokenGenerator();
		}
		AuthGatewayFilterFactory.config = config;
	}


	/**
	 * 动态测试租户id生成器
	 */
	static class DynamicStrategyTenantIdGenerator implements TenantIdGenerator{


		@Override
		public String generate(Invoker invoker) {
			GatewayInvoker gatewayInvoker = (GatewayInvoker) invoker;
			ServerWebExchange serverWebExchange = gatewayInvoker.getServerWebExchange();
			ServerHttpRequest request = serverWebExchange.getRequest();
			String tenantId = null;
			switch (config.tenantStrategy){
				case HEAD:
					tenantId = request.getHeaders().getFirst(config.tenantName);
					break;
				case QUERY:
					tenantId = request.getQueryParams().getFirst(config.tenantName);
					break;
				case COOKIE:
					HttpCookie tenantHttpCookie = request.getCookies().getFirst(config.tenantName);
					tenantId = tenantHttpCookie != null ? tenantHttpCookie.getValue() : null;
					break;
				case PATH:
				default:
					String path = request.getPath().pathWithinApplication().value();
					String[] split = path.split("/");
					if (split.length >= config.tenantPathIndex){
						tenantId = split[config.tenantPathIndex + 1];
					}

			}
			return tenantId == null ? getDefault() : tenantId;
		}
	}


	/**
	 * 动态策略token 生成器
	 */
	static class DynamicStrategyTokenGenerator implements TokenGenerator{

		@Override
		public AuthenticationToken generate(ServerHttpRequest request) {
			String token;
			switch (config.tokenStrategy){
				case HEAD:
					token = request.getHeaders().getFirst(config.tokenName);
					break;
				case COOKIE:
					HttpCookie tenantHttpCookie = request.getCookies().getFirst(config.tokenName);
					token = tenantHttpCookie != null ? tenantHttpCookie.getValue() : null;
					break;
				case QUERY:
				case PATH:
				default:
					token = request.getQueryParams().getFirst(config.tokenName);
			}
			return !StringUtils.isEmpty(token)? new BearerToken(token, WebUtils.getHost(request)) : null;
		}


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
