package org.codingeasy.shiroplus.loader.admin.server.logs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.cp.internal.raft.impl.log.LogEntry;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.LogsEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.BusinessCode;
import org.codingeasy.shiroplus.loader.admin.server.utils.JsonUtils;
import org.codingeasy.streamrecord.core.AttributeAccess;
import org.codingeasy.streamrecord.core.CurrentContext;
import org.codingeasy.streamrecord.core.RecordProducer;
import org.codingeasy.streamrecord.core.matedata.RecordInfo;
import org.codingeasy.streamrecord.core.matedata.RecordInfoWrapper;
import org.springframework.stereotype.Component;


/**
* 日志生成器  
* @author : KangNing Hu
*/
@Component
public class LogsProducer  implements RecordProducer {

	public static final String EXTEND_KEY = "_extend";
	public static final String BUSINESS_CODE_KEY = "_business_code";
	public static final String OLD_VALUE_KEY = "_old_value";
	public static final String OPERATION_ID_KEY = "_operation_id";
	public static final String BUSINESS_ID_KEY = "_business_id";
	public static final String OPERATION_TYPE_KEY = "_operation_type";
	public static final String IGNORE_KEY = "_ignore_key";
	public static final String OPERATION_TM_KEY = "_operation_tm";
	public static final String EXTEND_IP_KEY = "extend_ip";
	@Override
	public RecordInfoWrapper doProduce(CurrentContext currentContext) {

		AttributeAccess attributeAccess = currentContext.getAttributeAccess();

		//校验是否为有效记录
		if (attributeAccess.containsKey(IGNORE_KEY)){
			return null;
		}
		LogsEntity logEntry = new LogsEntity();
		logEntry.setBusinessCode(attributeAccess.getAttribute(BUSINESS_CODE_KEY));
		//设置旧值，用于更新记录保存快照
		logEntry.setOldData(JsonUtils.toJsonString(attributeAccess.getAttribute(OLD_VALUE_KEY)));
		//扩展字段保存
		logEntry.setExtend(JsonUtils.toJsonString(attributeAccess.getAttribute(EXTEND_KEY)));
		//操作人id
		logEntry.setOperationId(attributeAccess.getAttribute(OPERATION_ID_KEY));
		//业务主键
		logEntry.setBusinessId(attributeAccess.getAttribute(BUSINESS_ID_KEY));
		//操作类型
		logEntry.setOperationType(attributeAccess.getAttribute(OPERATION_TYPE_KEY));
		//设置记录内容
		logEntry.setMessage((String) attributeAccess.get(RecordInfo.MASSAGE_ATTR));
		Long time = attributeAccess.getAttribute(OPERATION_TM_KEY);
		logEntry.setCreateTm(time == null ? System.currentTimeMillis() : time);
		return RecordInfoWrapper.ofOne(logEntry);
	}
}
