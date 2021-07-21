package org.codingeasy.shiroplus.loader.admin.server.models;

/**
* 操作日志信息  
* @author : KangNing Hu
*/
public class Log {

	//日志id
	private Long id;

	//日志内容
	private String context;

	//操作人
	private String operateName;

	//创建时间
	private Long createTime;


	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getOperateName() {
		return operateName;
	}

	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}
}
