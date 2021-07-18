package org.codingeasy.shiroplus.loader.admin.server.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
*   
* @author : KangNing Hu
*/
@Configuration
public class MybatisConfig {

	/**
	 * mybatis plus 分页插件配置
	 * @return 注入分页插件
	 */
	@Bean
	public PaginationInterceptor mybatisPlusInterceptor() {
		PaginationInterceptor interceptor = new PaginationInterceptor();
		// 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
		// paginationInterceptor.setOverflow(false);
		// 设置最大单页限制数量，默认 500 条，-1 不受限制
		// paginationInterceptor.setLimit(500);
		// 开启 count 的 join 优化,只针对部分 left join
		interceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
		return interceptor;
	}
}
