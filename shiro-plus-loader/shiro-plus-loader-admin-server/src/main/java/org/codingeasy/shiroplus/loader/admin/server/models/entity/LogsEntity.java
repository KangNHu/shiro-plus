package org.codingeasy.shiroplus.loader.admin.server.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.BusinessCode;
import org.codingeasy.streamrecord.core.matedata.RecordInfo;

/**
 * 操作日志表
 *
 * @author chenbutao chenbutao@qipeipu.com
 * @since 1.0.0 2021-07-01
 */
@TableName("sp_logs")
public class LogsEntity  implements RecordInfo {


	@TableId(type = IdType.AUTO)
	private Long id;
    /**
     * 
     */
	private String oldData;
    /**
     * 日志内容
     */
	private String context;
    /**
     * 1 删除 2 新增 3 更新
     */
	private Integer operationType;
    /**
     * 创建时间
     */
	private Long createTm;
    /**
     * 操作人
     */
	private Long operationId;
    /**
     * 业务编码
     * @see  BusinessCode
     */
	private Integer businessCode;

	/**
	 * 业务主键
	 */
	private Long businessId;
	/**
	 * 扩展字段
	 */
	private String extend;




	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Object id) {

	}

	@Override
	public Long getOperationTime() {
		return this.createTm;
	}

	@Override
	public void setOperationTime(Long time) {
		this.createTm = time;
	}

	@Override
	public String getMessage() {
		return this.context;
	}

	@Override
	public void setMessage(String message) {
		this.context = message;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public String getOldData() {
		return oldData;
	}

	public void setOldData(String oldData) {
		this.oldData = oldData;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Integer getOperationType() {
		return operationType;
	}

	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}

	public Long getCreateTm() {
		return createTm;
	}

	public void setCreateTm(Long createTm) {
		this.createTm = createTm;
	}

	public Long getOperationId() {
		return operationId;
	}

	public void setOperationId(Long operationId) {
		this.operationId = operationId;
	}

	public Integer getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(Integer businessCode) {
		this.businessCode = businessCode;
	}
}