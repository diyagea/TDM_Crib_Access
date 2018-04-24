package com.TCA.service;

import com.TCA.common.model.IssueRecord;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * IssueRecord 管理
 * 描述：
 */
public class IssueRecordService {

    // private static final Log log = Log.getLog(IssueRecordService.class);

    public static final IssueRecordService me = new IssueRecordService();
    private final IssueRecord dao = new IssueRecord().dao();

    /**
     * 列表-分页
     */
    public Page<IssueRecord> paginate(int pageNumber, int pageSize) {
	return dao.paginate(pageNumber, pageSize, "SELECT * ", "FROM TCA_ISSUE_RECORD");
    }

    /**
     * 保存
     */
    public boolean save(IssueRecord issueRecord) {
	issueRecord.remove("ID");
	boolean result = issueRecord.save();
	return result;
    }

    /**
     * 更新
     */
    public void update(IssueRecord issueRecord) {
	issueRecord.update();
    }

    /**
     * 查询
     */
    public IssueRecord findById(int issueRecordId) {
	return dao.findFirst("select * from TCA_ISSUE_RECORD where id=?", issueRecordId);
    }

    /**
     * 删除
     */
    public void delete(int issueRecordId) {
	Db.update("delete from TCA_ISSUE_RECORD where id=?", issueRecordId);
    }

}