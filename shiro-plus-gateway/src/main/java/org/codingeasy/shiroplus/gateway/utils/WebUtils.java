package org.codingeasy.shiroplus.gateway.utils;

import com.google.common.base.Charsets;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
* 网关相关工具类
* @author : KangNing Hu
*/
public class WebUtils {

	private static final Logger log = LoggerFactory.getLogger(WebUtils.class);

	/**
	 * 重定向
	 * @param url 重定向url
	 * @param serverHttpResponse 网关响应对象
	 */
	public static void sendRedirect(String url , ServerHttpResponse serverHttpResponse){
		HttpHeaders headers = serverHttpResponse.getHeaders();
		try {
			serverHttpResponse.setStatusCode(HttpStatus.FOUND);
			headers.setLocation(new URI(url));
		}catch (Exception e){
			write(String.format("重定向错误[%s]", url) , serverHttpResponse ,INTERNAL_SERVER_ERROR );
		}
	}


	/**
	 *
	 * @param body 响应内容
	 * @param serverHttpResponse 网关响应对象
	 * @param httpStatus 响应状态
	 */
	public static void write(String body , ServerHttpResponse serverHttpResponse , HttpStatus httpStatus){
		HttpHeaders headers = serverHttpResponse.getHeaders();
		serverHttpResponse.setStatusCode(httpStatus);
		headers.setContentType(MediaType.ALL);
		DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
		DataBuffer wrap = dataBufferFactory.wrap(body.getBytes(Charsets.UTF_8));
		serverHttpResponse
				.writeWith(Mono.just(wrap))
				.doOnSuccess(item -> log.info("写响应流成功"))
				.doOnError(item -> log.warn("写响应流错误", item))
				.block();
	}
}
