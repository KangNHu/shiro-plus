package org.codingeasy.shiroplus.loader.admin.server.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.codingeasy.shiroplus.loader.admin.server.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
* 缓存管理  
* @author : KangNing Hu
*/
@Component
public class CacheManager extends CacheLoader<String , Object> {


	private LoadingCache<String , Object> cache = CacheBuilder
								.newBuilder()
								.maximumSize(1000)
								.expireAfterAccess(5, TimeUnit.MINUTES)
								.build(this);

	private ThreadLocal<Supplier<Object>> supplier = new ThreadLocal<>();

	/**
	 * 获取缓存
	 * @param key key
	 * @param supplier 缓存不存在时，调用该回调获取值并再次缓存
	 * @param <T> 类型
	 * @return 返回缓存值
	 */
	public <T>T getCache(String key , Supplier<Object> supplier){
		this.supplier.set(supplier);
		try {
			return (T) cache.get(key);
		}catch (Exception e){
			throw new BusinessException("获取缓存是吧" ,e);
		}finally {
			this.supplier.remove();
		}
	}



	@Override
	public Object load(String s) throws Exception {
		return this.supplier.get().get();
	}
}
