package org.codingeasy.shiroplus.loader.admin.server.models;

/**
* 字典  
* @author : KangNing Hu
*/
public class Dict {

	/**
	 * 字典值
	 */
	private Object value;

	/**
	 * 字典标签
	 */
	private String label;


	public Dict() {
	}

	public Dict(Object value, String label) {
		this.value = value;
		this.label = label;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
