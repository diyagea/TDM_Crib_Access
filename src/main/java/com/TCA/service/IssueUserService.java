package com.TCA.service;

import com.TCA.common.model.IssueUser;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;

/**
 * IssueUser 管理
 * 描述：
 */
public class IssueUserService {

    private static final Log log = Log.getLog(IssueUserService.class);

    public static final IssueUserService me = new IssueUserService();
    private final IssueUser dao = new IssueUser().dao();

    /**
     * 列表-分页
     */
    public Page<IssueUser> paginate(int pageNumber, int pageSize) {
	return dao.paginate(pageNumber, pageSize, "SELECT * ", "FROM TCA_ISSUE_USER");
    }

    /**
     * 保存
     */
    public boolean save(IssueUser issueUser) {
	try {
	    issueUser.save();
	} catch (Exception e) {
	    log.error("Save IssueUser Error", e);
	    return false;
	}
	return true;
    }

    /**
     * 更新
     */
    public void update(IssueUser issueUser) {
	issueUser.update();
    }

    /*
     * 更新状态
     */
    public boolean updateState(String userCode, Boolean s) {
	int state = 0;
	if (!s) {
	    state = 1;
	}
	// return update sql result
	return dao.findById(userCode).set("STATE", state).update();
    }

    /**
     * 查询
     */
    public IssueUser findById(int issueUserId) {
	return dao.findFirst("select * from TCA_ISSUE_USER where id=?", issueUserId);
    }

    /**
     * 删除
     */
    public void delete(String issueUserId) {
	// Db.update("delete from TCA_ISSUE_USER where id=?", issueUserId);
	dao.deleteById(issueUserId);
    }

}