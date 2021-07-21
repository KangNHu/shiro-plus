package org.codingeasy.shiroplus.loader.admin.server.security;

import org.codingeasy.shiroplus.core.realm.processor.AuthProcessor;
import org.codingeasy.shiroplus.core.realm.RequestToken;
import org.codingeasy.shiroplus.core.realm.processor.HttpServletAuthProcessor;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.UserRoleCodesEntity;
import org.codingeasy.shiroplus.loader.admin.server.service.UserService;
import org.codingeasy.shiroplus.loader.admin.server.utils.JwtUtils;
import org.codingeasy.shiroplus.loader.admin.server.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
* 权限处理器  
* @author : KangNing Hu
*/
@Component
public class AdminServerAuthProcessor extends HttpServletAuthProcessor {


	@Autowired
	@Lazy
	private UserService userService;


	/**
	 * 获取token
	 * <p>获取当前请求的token</p>
	 * @return 返回token
	 */
	@Override
	public String getToken(HttpServletRequest request) {
		return WebUtils.getToken(request);
	}

	/**
	 * 校验token 并返回token主体
	 * @param requestToken token
	 * @return 返回token主体 用于后续权限查询
	 */
	@Override
	public Long validate(RequestToken<HttpServletRequest> requestToken) {
		String token = (String) requestToken.getPrincipal();
		//校验token
		return JwtUtils.getSubjectAndVerify(token, requestToken.getRequest());
	}


	/**
	 * 获取权限列表
	 * <p>一般为权限标识</p>
	 * @param primaryPrincipal token 主体
	 * @return 返回权限列表 ，可以返回空
	 */
	@Override
	public Set<String> getPermissions(Object primaryPrincipal) {
		return getRoles(primaryPrincipal);
	}


	/**
	 * 获取角色列表
	 * @param primaryPrincipal  token 主体
	 * @return 返回角色列表 ，可以返回空
	 */
	@Override
	public Set<String> getRoles(Object primaryPrincipal) {
		return new HashSet<>(
				Optional
						.of(getUserPermissions((Long) primaryPrincipal).getRoles())
						.orElse(new ArrayList<>())
		);
	}


	/**
	 * 获取用户权限信息
	 * @param userId userId
	 * @return 返回用户权限信息
	 */
	public UserRoleCodesEntity getUserPermissions(Long userId){
		UserRoleCodesEntity userPermissions = userService.getUserPermissions(userId);
		return userPermissions == null ? new UserRoleCodesEntity() : userPermissions;
	}
}
