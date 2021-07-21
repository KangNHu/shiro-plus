package org.codingeasy.shiroplus.loader.admin.server.models;

import java.util.List;

/**
* 分页  
* @author : KangNing Hu
*/
public class Page<T> {

	/**
	 * 总条数
	 */
	private Long total = 0L;

	/**
	 * 分页数据
	 */
	private List<T> list;


	public Page(){}

	public Page(com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page){
		this.list = page.getRecords();
		this.total = page.getTotal();
	}


	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
}
