package org.codingeasy.shiroplus.springboot;

import org.codingeasy.shiroplus.springboot.annotaion.ShiroFilter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

import javax.servlet.Filter;
import java.io.IOException;
import java.util.*;

/**
 * shiroplus filter扫描器
 * <p>只扫描添加{@link ShiroFilter}注解的{@link Filter}</p>
 *
 * @author : kangning <a>2035711178@qq.com</a>
 */
public class ShiroFilterClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

	private AbstractAutowireCapableBeanFactory beanFactory;

	private AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(ShiroFilter.class);
	private AssignableTypeFilter assignableTypeFilter = new AssignableTypeFilter(Filter.class);

	public ShiroFilterClassPathBeanDefinitionScanner(AbstractAutowireCapableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
		//重置spring 扫描注解
		resetFilters(false);
		//添加shiroFilter
		addIncludeFilter(this::matchType);
	}


	/**
	 * 匹配类型
	 * @param var1  元数据读取器
	 * @param var2 元数据读取器工厂
	 * @return 如果匹配返回true 否则flase
	 * @throws IOException
	 */
	private boolean matchType(MetadataReader var1, MetadataReaderFactory var2) throws IOException {
		return annotationTypeFilter.match(var1, var2) && assignableTypeFilter.match(var1, var2);
	}
	/**
	 * 根据基础包名获取符合条件的 shiroplus filter
	 * <p>并对filter进行自动注入</p>
	 * <p>只对bean class做处理 ，非bean class则忽略</p>
	 *
	 * @param basePackages 扫描的包路径列表
	 * @return
	 */
	public Map<String, Filter> getFiltersByPackage(List<String> basePackages) {
		Set<BeanDefinition> beanDefinitions = new HashSet<BeanDefinition>();
		for (String basePackage : basePackages) {
			beanDefinitions.addAll(findCandidateComponents(basePackage));
		}
		Map<String, Filter> filters = new HashMap<String, Filter>();
		for (BeanDefinition beanDefinition : beanDefinitions) {
			String beanClassName = beanDefinition.getBeanClassName();
			try {
				Class<?> beanClass = getClassLoader().loadClass(beanClassName);

				Filter filter = (Filter) beanFactory.createBean(beanClass);
				beanFactory.autowireBean(filter);
				filters.put(getFilterName(beanClass), filter);
			} catch (Exception e) {
				logger.warn("加载class失败", e);
			}

		}
		return filters;
	}


	/**
	 * 获取类加载器
	 *
	 * @return 返回一个类加载器
	 */
	private ClassLoader getClassLoader() {
		ClassLoader beanClassLoader = this.beanFactory.getBeanClassLoader();
		return beanClassLoader == null ? Thread.currentThread().getContextClassLoader() :
				beanClassLoader;
	}

	/**
	 * 获取filter 名称
	 *
	 * @param beanClass filter class对象
	 * @return 返回 filter名称
	 */
	protected String getFilterName(Class<?> beanClass) {
		ShiroFilter shiroFilter = beanClass.getAnnotation(ShiroFilter.class);
		return shiroFilter.value();
	}

}
