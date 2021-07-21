package org.codingeasy.shiroplus.loader.admin.server.models.request;

/**
* 日志查询条件  
* @author : KangNing Hu
*/
public class LogsRequest extends RequestPage{

	/**
	 * 业务码
	 */
	private Integer businessCode;

	/**
	 * 操作人id
	 */
	private Long operateId;


	public Integer getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(Integer businessCode) {
		this.businessCode = businessCode;
	}

	public Long getOperateId() {
		return operateId;
	}

	public void setOperateId(Long operateId) {
		this.operateId = operateId;
	}
}
