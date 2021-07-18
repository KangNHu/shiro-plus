package org.codingeasy.shiroplus.loader.admin.server.models.request;

/**
* 分页条件  
* @author : KangNing Hu
*/
public class RequestPage  {


	/**
	 * 当前页
	 */
	private Integer pageNo = 1;

	/**
	 * 页面容量
	 */
	private Integer pageSize = 10;


	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
