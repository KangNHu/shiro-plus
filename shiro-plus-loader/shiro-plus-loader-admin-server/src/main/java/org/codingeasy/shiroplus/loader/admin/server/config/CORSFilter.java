package org.codingeasy.shiroplus.loader.admin.server.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter {
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }



  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
  	HttpServletResponse response = (HttpServletResponse) servletResponse;
  	HttpServletRequest request = (HttpServletRequest) servletRequest;
  	response.setHeader("Access-Control-Allow-Origin", "*");
    if ("OPTIONS".equals(request.getMethod())){//这里通过判断请求的方法，判断此次是否是预检请求，如果是，立即返回一个204状态吗，标示，允许跨域；预检后，正式请求，这个方法参数就是我们设置的post了

      response.setStatus(HttpStatus.NO_CONTENT.value()); //HttpStatus.SC_NO_CONTENT = 204

      response.setHeader("Access-Control-Allow-Methods", "*");//当判定为预检请求后，设定允许请求的方法

      response.setHeader("Access-Control-Allow-Headers", "*"); //当判定为预检请求后，设定允许请求的头部类型

      response.addHeader("Access-Control-Max-Age", "1"); // 预检有效保持时间
	    return;
    }
    filterChain.doFilter(servletRequest, servletResponse);
  }

  @Override
  public void destroy() {
  }
}