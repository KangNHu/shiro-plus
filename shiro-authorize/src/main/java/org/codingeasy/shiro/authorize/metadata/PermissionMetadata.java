package org.codingeasy.shiro.authorize.metadata;

import java.util.ArrayList;
import java.util.List;

/**
* 权限元信息  
* @author : KangNing Hu
*/
public class PermissionMetadata {

	/**
	 * controller 方法的映射路径
	 */
	private String path;


	/**
	 * 方法的请求方式
	 * @see RequestMethod
	 */
	private RequestMethod method;

	/**
	 * 权限标识列表
	 * <p>1.接口对应权限的标识</p>
	 * <p>2.角色对应的标识</p>
	 */
	private List<String> permis;


	/**
	 * 校验模式
	 * @see PermiModel
	 */
	private PermiModel permiModel;


	@Override
	protected PermissionMetadata clone() throws CloneNotSupportedException {
		PermissionMetadata permissionMetadata = new PermissionMetadata();
		permissionMetadata.setMethod(this.method);
		permissionMetadata.setPath(this.path);
		permissionMetadata.setPermiModel(this.permiModel);
		permissionMetadata.setPermis(new ArrayList<>(this.permis));
		return permissionMetadata;
	}


	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public RequestMethod getMethod() {
		return method;
	}

	public void setMethod(RequestMethod method) {
		this.method = method;
	}


	public List<String> getPermis() {
		return permis;
	}

	public void setPermis(List<String> permis) {
		this.permis = permis;
	}

	public PermiModel getPermiModel() {
		return permiModel;
	}

	public void setPermiModel(PermiModel permiModel) {
		this.permiModel = permiModel;
	}


	@Override
	public String toString() {
		return "PermissionMetadata{" +
				"path='" + path + '\'' +
				", method=" + method +
				", permis=" + permis +
				", permiModel=" + permiModel +
				'}';
	}
}
