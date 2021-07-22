package org.codingeasy.shiroplus.loader.admin.server.models.request;

/**
* 权限配置请求条件  
* @author : KangNing Hu
*/
public class PermissionConfigRequest extends RequestPage{

	/**
	 * 路径
	 */
	private String path;

	/**
	 * 请求方式
	 *
	 */
	private Integer method;

	/**
	 * 权限模式 1.角色授权模式 2.权限授权模式 3票据授权模式4.认证状态授权模式5.用户信息存在状态的授权模式
	 */
	private Integer permiModel;


	/**
	 * 权限编码
	 */
	private String permiCode;


	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getMethod() {
		return method;
	}

	public void setMethod(Integer method) {
		this.method = method;
	}

	public Integer getPermiModel() {
		return permiModel;
	}

	public void setPermiModel(Integer permiModel) {
		this.permiModel = permiModel;
	}

	public String getPermiCode() {
		return permiCode;
	}

	public void setPermiCode(String permiCode) {
		this.permiCode = permiCode;
	}
}
