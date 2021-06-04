package org.codingeasy.shiro.springboot;

import org.codingeasy.shiro.springboot.annotaion.ShiroFilter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionCustomizer;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.Filter;
import java.util.*;

/**
 * shiro filter扫描器
 * <p>只扫描添加{@link ShiroFilter}注解的{@link Filter}</p>
 *
 * @author : KangNing Hu
 */
public class ShiroFilterClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

	private AbstractAutowireCapableBeanFactory beanFactory;

	public ShiroFilterClassPathBeanDefinitionScanner(AbstractAutowireCapableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
		//重置spring 扫描注解
		resetFilters(false);
		//添加shiroFilter
		addIncludeFilter(new AnnotationTypeFilter(ShiroFilter.class));
		addIncludeFilter(new AssignableTypeFilter(Filter.class));
	}

	/**
	 * 根据基础包名获取符合条件的 shiro filter
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
