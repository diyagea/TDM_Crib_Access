package com.TCA.jobs;

import java.util.Date;
import java.util.List;

import com.TCA.common.model.IssueRecord;
import com.TCA.service.IssueRecordService;
import com.jfinal.ext.kit.DateKit;

/**
 * 实现定时更新IssueRecord超时状态
 * 
 * @author diyagea
 */
public class IssueRecordUpdate implements Runnable {
    static IssueRecordService recordSrv = IssueRecordService.me;

    public void run() {
	List<IssueRecord> records = recordSrv.findListByState(0);
	for (IssueRecord record : records) {
	    String deadline = record.getDEADLINE();
	    Date dead = DateKit.toDate(deadline);
	    Date now = new Date();

	    if (now.after(dead)) {// 超时
		record.setSTATE((short) -1);
		record.update();// 保存到数据库
	    }
	}
    }
}
