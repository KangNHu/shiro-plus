package org.codingeasy.shiroplus.nacos.excption;

/**
* 基于nacos元数据加载异常  
* @author : kangning <a>2035711178@qq.com</a>
*/
public class NacosMetadataLoaderException extends IllegalStateException{

	public NacosMetadataLoaderException() {
		super();
	}

	public NacosMetadataLoaderException(String s) {
		super(s);
	}

	public NacosMetadataLoaderException(String message, Throwable cause) {
		super(message, cause);
	}

	public NacosMetadataLoaderException(Throwable cause) {
		super(cause);
	}
}
