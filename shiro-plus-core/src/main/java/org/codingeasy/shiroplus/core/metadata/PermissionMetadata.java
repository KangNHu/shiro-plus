package org.codingeasy.shiroplus.core.metadata;

import org.codingeasy.shiroplus.core.utils.CopyUtils;

import java.util.ArrayList;
import java.util.List;

/**
* 权限元信息  
* @author : KangNing Hu
*/
public class PermissionMetadata extends AbstractMetadata {


	public PermissionMetadata(String path, RequestMethod method, List<String> permis, Logical logical, PermiModel permiModel) {
		this.path = path;
		this.method = method;
		this.permis = permis;
		this.logical = logical;
		this.permiModel = permiModel;
	}

	public PermissionMetadata() {
	}

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
	 * 逻辑类型
	 */
	private Logical logical;

	/**
	 * 校验模式
	 * @see PermiModel
	 */
	private PermiModel permiModel;


	@Override
	protected PermissionMetadata clone() throws CloneNotSupportedException {
		PermissionMetadata clone = (PermissionMetadata)super.clone();
		clone.setPermis(CopyUtils.copyList(this.permis));
		return clone;
	}


	public String getPath() {
		return path;
	}

	public Logical getLogical() {
		return logical;
	}

	public void setLogical(Logical logical) {
		this.logical = logical;
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
