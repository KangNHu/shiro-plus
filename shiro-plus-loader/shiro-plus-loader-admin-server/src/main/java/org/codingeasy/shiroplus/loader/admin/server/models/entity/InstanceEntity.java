package org.codingeasy.shiroplus.loader.admin.server.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.codingeasy.shiroplus.loader.admin.server.logs.LogsProducer;
import org.codingeasy.streamrecord.core.annotation.Param;

/**
* 事件处理记录  
* @author : KangNing Hu
*/
@TableName("sp_instance")
public class InstanceEntity {

	@TableId(type = IdType.AUTO)
	@Param(LogsProducer.BUSINESS_ID_KEY)
	private Long id;


	/**
	 * 编码
	 */
	private String code;


	/**
	 * 事件id
	 */
	private Long lastPingTime;


	/**
	 * 实例ip
	 */
	private String ip;


	/**
	 * 服务名称
	 */
	private String name;
	/**
	 * 创建时间
	 */
	private Long createTm;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getLastPingTime() {
		return lastPingTime;
	}



	public void setLastPingTime(Long lastPingTime) {
		this.lastPingTime = lastPingTime;
	}

	public Long getCreateTm() {
		return createTm;
	}

	public void setCreateTm(Long createTm) {
		this.createTm = createTm;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
