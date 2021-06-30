package org.codingeasy.shiroplus.gateway.utils;

import com.google.common.base.Charsets;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;

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
	public static Mono<Void> sendRedirect(@NotNull String url , @NotNull ServerHttpResponse serverHttpResponse ){
		HttpHeaders headers = serverHttpResponse.getHeaders();
		try {
			serverHttpResponse.setStatusCode(HttpStatus.FOUND);
			headers.setLocation(new URI(url));
			return serverHttpResponse.setComplete();
		}catch (Exception e){
			return write(String.format("重定向错误[%s] error:[%s]", url , e.getMessage()) , serverHttpResponse ,INTERNAL_SERVER_ERROR );
		}
	}


	/**
	 *
	 * @param body 响应内容
	 * @param serverHttpResponse 网关响应对象
	 * @param httpStatus 响应状态
	 */
	public static Mono<Void> write(@NotNull String body , @NotNull ServerHttpResponse serverHttpResponse , HttpStatus httpStatus){
		DataBufferFactory dataBufferFactory = serverHttpResponse.bufferFactory();
		HttpHeaders headers = serverHttpResponse.getHeaders();
		serverHttpResponse.setStatusCode(httpStatus);
		headers.setContentType(MediaType.parseMediaType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"));
		return serverHttpResponse
				.writeAndFlushWith(Mono.just(Mono.just(dataBufferFactory.wrap(body.getBytes(Charsets.UTF_8)))))
				.doOnError(e -> log.warn("写响应失败 {}" ,body , e ))
				.doOnSuccess(item -> {
					if (log.isDebugEnabled()) {
						log.debug("写响应成功 {}", body);
					}
				});
	}


	/**
	 * 获取请求的host
	 * @param request 网关请你去对象
	 * @return 返回host 如果没有返回 null
	 */
	public static String getHost(@NotNull ServerHttpRequest request){
		InetSocketAddress remoteAddress = request.getRemoteAddress();
		if (remoteAddress == null){
			return null;
		}
		return remoteAddress.getHostString();
	}
}
