package org.codingeasy.shiro.authorize.mgt;

import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.filter.mgt.SimpleNamedFilterList;
import org.codingeasy.shiro.authorize.interceptor.WebInvoker;
import org.codingeasy.shiro.authorize.metadata.AuthMetadataManager;
import org.codingeasy.shiro.authorize.metadata.GlobalMetadata;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
* 动态匹配器  
* @author : KangNing Hu
*/
public class DynamicPathMatchingFilterChainResolver extends PathMatchingFilterChainResolver {



	private AuthMetadataManager authMetadataManager;


	private NamedFilterList anonymousNamedFilterList;


	private TenantIdGenerator tenantIdGenerator = new DefaultTenantIdGenerator();


	public DynamicPathMatchingFilterChainResolver(AuthMetadataManager authMetadataManager){
		this.authMetadataManager = authMetadataManager;
		this.anonymousNamedFilterList = getAnonNamedFilterList();
	}

	public void setTenantIdGenerator(TenantIdGenerator tenantIdGenerator) {
		this.tenantIdGenerator = tenantIdGenerator;
	}

	@Override
	public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain) {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			GlobalMetadata globalMetadata = authMetadataManager.getGlobalMetadata(getTenantId(httpServletRequest, httpServletResponse));
			String requestURI = getPathWithinApplication(request);
			if (globalMetadata != null) {
				//如果不开启鉴权
				if (!isEnableAuthentication(globalMetadata)){
					return anonymousNamedFilterList.proxy(originalChain);
				}
				//对匿名访问的请求进行处理
				List<String> anons = globalMetadata.getAnons();
				for (String anon : anons){
					if (pathMatches(anon , requestURI)){
						return anonymousNamedFilterList.proxy(originalChain);
					}
				}

			}
		}
		return super.getChain(request, response, originalChain);
	}

	/**
	 * 是否开启前期
	 * @param globalMetadata 全局元数据
	 * @return 如果开启返回true 否则为false
	 */
	private boolean isEnableAuthentication(GlobalMetadata globalMetadata) {
		Boolean enableAuthentication = globalMetadata.getEnableAuthentication();
		return enableAuthentication == null ||enableAuthentication;
	}


	/**
	 * 获取Anon 过滤列表对象
	 * @return Anon 过滤列表对象
	 */
	private NamedFilterList getAnonNamedFilterList(){
		NamedFilterList namedFilterList = new SimpleNamedFilterList("/**");
		namedFilterList.add(new AnonymousFilter());
		return namedFilterList;
	}

	/**
	 * 获取租户id
	 * @param httpServletRequest 请求对象
	 * @param httpServletResponse  响应对象
	 * @return 返回租户id
	 */
	private String getTenantId(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		String tenantId = this.tenantIdGenerator.generate(new WebInvoker(httpServletRequest, httpServletResponse, ((request, response) -> {})));
		return tenantId == null ? this.tenantIdGenerator.getDefault() : tenantId;
	}
}
