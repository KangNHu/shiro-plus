package org.codingeasy.shiroplus.loader.admin.client;

import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.jackson.JacksonDecoder;

import java.io.IOException;
import java.lang.reflect.Type;

/**
* 响应处理器  
* @author : KangNing Hu
*/
public class ResponseDecoder extends JacksonDecoder {



	@Override
	public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
		if (response.status() == 401){
			throw new IllegalStateException("admin server 授权失败 ，请求检查client token是否正确");
		}
		else if (response.status() <200 || response.status() > 300){
			throw new IllegalStateException(Util.toString(response.body().asReader(Util.UTF_8)));
		} else {
			org.codingeasy.shiroplus.loader.admin.client.model.Response re = (org.codingeasy.shiroplus.loader.admin.client.model.Response) super.decode(response, type);
			if (!re.isSucceed()){
				throw new IllegalStateException(re.getMsg());
			}
			return re;
		}
	}


}
