package org.codingeasy.shiroplus.loader.admin.server.logs;

import org.checkerframework.checker.units.qual.C;
import org.codingeasy.shiroplus.loader.admin.server.dao.LogsDao;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.LogsEntity;
import org.codingeasy.streamrecord.core.Pipeline;
import org.codingeasy.streamrecord.core.matedata.RecordInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
* 日志管道  
* @author : KangNing Hu
*/
@Component
public class LogsPipeline implements Pipeline {

	@Autowired
	private LogsDao logsDao;


	@Override
	public void doConsume(RecordInfoWrapper recordInfoWrapper) {
		if (!recordInfoWrapper.isMultiple()) {
			logsDao.insert((LogsEntity) recordInfoWrapper.getRecordInfo());
		}
	}
}
