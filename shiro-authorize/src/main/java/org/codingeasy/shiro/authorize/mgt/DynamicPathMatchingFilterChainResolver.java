package org.codingeasy.shiro.authorize.mgt;

import org.apache.shiro.util.PatternMatcher;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.filter.mgt.SimpleNamedFilterList;
import org.codingeasy.shiro.authorize.interceptor.WebInvoker;
import org.codingeasy.shiro.authorize.metadata.AuthMetadataManager;
import org.codingeasy.shiro.authorize.metadata.GlobalMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
