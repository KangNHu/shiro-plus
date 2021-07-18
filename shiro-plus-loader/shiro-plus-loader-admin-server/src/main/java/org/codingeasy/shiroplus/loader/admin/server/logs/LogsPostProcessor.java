package org.codingeasy.shiroplus.loader.admin.server.logs;

import org.codingeasy.shiroplus.core.metadata.RequestMethod;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.OperationType;
import org.codingeasy.shiroplus.loader.admin.server.utils.UserUtils;
import org.codingeasy.shiroplus.loader.admin.server.utils.WebUtils;
import org.codingeasy.streamrecord.core.AttributeAccess;
import org.codingeasy.streamrecord.core.CurrentContext;
import org.codingeasy.streamrecord.core.processor.RecordPostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.codingeasy.shiroplus.loader.admin.server.logs.LogsProducer.EXTEND_IP_KEY;
import static org.codingeasy.shiroplus.loader.admin.server.logs.LogsProducer.IGNORE_KEY;

/**
* 日志后置处理器  
* @author : KangNing Hu
*/
@Component
public class LogsPostProcessor implements RecordPostProcessor {

	private static final Logger logger = LoggerFactory.getLogger(LogsPostProcessor.class);
	private static final  String HEAD_BUSINESS_CODE = "x-business-code";


	/**
	 * 前置处理
	 * @param currentContext 当前上下文
	 */
	@Override
	public void preProcessor(CurrentContext currentContext) {
		//获取修改前的值
		AttributeAccess attributeAccess = currentContext.getAttributeAccess();
		//设置当前登录人
		attributeAccess.setAttribute(LogsProducer.OPERATION_ID_KEY , UserUtils.getUserId());
		//获取请求上下文
		HttpServletRequest request = WebUtils.getRequest();
		//设置当前业务码
		String bc = request.getHeader(HEAD_BUSINESS_CODE);
		attributeAccess.setAttribute(LogsProducer.BUSINESS_CODE_KEY ,bc == null ? null : Integer.valueOf(bc));
		attributeAccess.setAttribute(EXTEND_IP_KEY , WebUtils.getRemoteIp(request));
		//设置操作类型
		Integer operationType = getOperationType(request);
		if (operationType == null){
			logger.warn("当前操作记录未知的类型");
			attributeAccess.setAttribute(IGNORE_KEY , Boolean.TRUE);
		}
		attributeAccess.setAttribute(LogsProducer.OPERATION_TYPE_KEY , null);
	}

	@Override
	public void postProcessor(CurrentContext currentContext) {

	}


	/**
	 * 根据当前请求获取操作类型
	 * @param request 请求对象
	 * @return 返回操作类型值
	 */
	private Integer getOperationType(HttpServletRequest request){
		String method = request.getMethod();
		if (RequestMethod.POST.name().equalsIgnoreCase(method)){
			return OperationType.ADD.getValue();
		}
		else if (RequestMethod.DELETE.name().equalsIgnoreCase(method)){
			return OperationType.DELETE.getValue();
		}
		else if (RequestMethod.PUT.name().equalsIgnoreCase(method)){
			return OperationType.UPDATE.getValue();
		}
		return null;
	}
}
