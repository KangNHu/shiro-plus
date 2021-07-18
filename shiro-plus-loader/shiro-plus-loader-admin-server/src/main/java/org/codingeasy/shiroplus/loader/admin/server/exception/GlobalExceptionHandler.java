package org.codingeasy.shiroplus.loader.admin.server.exception;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.codingeasy.shiroplus.loader.admin.server.models.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

/**
* 全局异常处理  
* @author : KangNing Hu
*/
@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * 鉴权失败
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(AuthenticationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public Response authenticationHandler(AuthenticationException exception){
		logger.warn("鉴权异常" , exception);
		return Response.failure(exception.getMessage());
	}

	/**
	 * 授权失败
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(AuthorizationException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public Response authorizationExceptionHandler(AuthorizationException exception){
		logger.warn("授权失败" , exception);
		return Response.failure(exception.getMessage());
	}

	/**
	 * 业务异常处理
	 * @param e
	 * @return
	 */
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.OK)
	public Response businessExceptionHandler(BusinessException e){
		logger.warn("业务处理失败" , e);
		return Response.failure(e.getMessage());
	}


	/**
	 * 处理参数校验异常端异常
	 * @param e jsr 标准{@link ValidationException} 或者 {@link MethodArgumentNotValidException}spring validation
	 * @return 异常响应结果
	 */
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler({ValidationException.class , MethodArgumentNotValidException.class})
	public Response extendHandlerExceptionResolvers(Exception e) {
		logger.warn("参数校验未通过 " , e);
		String errorMsg = null;
		//如果是 jsr标准bean校验异常
		if (e instanceof ValidationException){
			errorMsg = e.getMessage();
		}
		//如果是spring bean validation 校验异常
		else {
			MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
			BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
			if (bindingResult == null || CollectionUtils.isEmpty(bindingResult.getAllErrors())){
				errorMsg = e.getMessage();
			}else {
				List<ObjectError> allErrors = bindingResult.getAllErrors();
				errorMsg = allErrors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(";"));
			}
		}
		return Response.failure(errorMsg);
	}


	/**
	 * 系统异常
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Response systemErrorHandler(Throwable exception){
		logger.error("系统异常" , exception);
		return Response.failure("系统异常！");
	}
}
