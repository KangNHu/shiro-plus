package org.codingeasy.shiroplus.loader.admin.server.logs.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import okhttp3.*;
import org.codingeasy.shiroplus.loader.admin.server.logs.LogsProducer;
import org.codingeasy.shiroplus.loader.admin.server.security.LoginInfoToken;
import org.codingeasy.streamrecord.core.AttributeAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
* 登录路由目标  
* @author : KangNing Hu
*/
@Component
public class LoginRouteTarget {

	private final static Logger logger = LoggerFactory.getLogger(LoginRouteTarget.class);

	private static final Set<String> LOCAL_IP = Sets.newHashSet("0:0:0:0:0:0:0:1" ,"localhost");

	private static final String IP_INFO_URL = "http://ip-api.com/json/%s?lang=zh-CN";

	private static final  OkHttpClient client = new OkHttpClient();
	/**
	 * 登录日志处理
	 * @param attributeAccess 属性
	 * @param token 登录参数
	 * @return 返回日志内容
	 */
	public String login(LoginInfoToken token , AttributeAccess attributeAccess){
		//设置主体
		attributeAccess.setAttribute(LogsProducer.OPERATION_ID_KEY , token.getPrincipal());
		attributeAccess.setAttribute(LogsProducer.BUSINESS_ID_KEY , token.getPrincipal());
		//获取ip
		String ip = attributeAccess.getAttribute(LogsProducer.EXTEND_IP_KEY);
		if (LOCAL_IP.contains(ip)) {
			return "本地登录";
		}
		Request get = new Request
				.Builder()
				.get()
				.url(String.format(IP_INFO_URL, ip))
				.build();
		try {
			Response response = client.newCall(get).execute();
			ResponseBody body = response.body();
			if (body == null){
				return "未知的登录地点";
			}
			ObjectMapper objectMapper = new ObjectMapper();
			Map map = objectMapper.readValue(body.string(), Map.class);
			if ("success".equals(map.get("status"))){
				return map.get("city") + "  " + map.get("regionName");
			}
		}catch (Exception e){
			logger.warn("获取登录地址失败" ,e);
		}
		return "未知的登录地点";
	}



}
