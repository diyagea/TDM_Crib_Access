package com.TCA.service;

import com.TCA.common.model.IssueTerm;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * IssueTerm 管理
 * 描述：
 */
public class IssueTermService {

    private static final Log log = Log.getLog(IssueTermService.class);

    public static final IssueTermService me = new IssueTermService();
    private final IssueTerm dao = new IssueTerm().dao();

    /**
     * 列表-分页
     */
    public Page<IssueTerm> paginate(int pageNumber, int pageSize) {
	return dao.paginate(pageNumber, pageSize, "SELECT * ", "FROM TCA_ISSUE_TERM");
    }

    /**
     * 保存
     */
    public boolean save(IssueTerm issueTerm) {
	boolean result = false;
	try {
	    result = issueTerm.save();
	} catch (Exception e) {
	    log.error("Save IssueTerm Error", e);
	}
	return result;
    }

    /**
     * 更新
     */
    public boolean update(IssueTerm issueTerm) {
	return issueTerm.update();
    }

    /**
     * 修改状态
     */
    public boolean updateState(String key, Boolean s) {
	int state = 0;
	if (s) {
	    state = 1;
	}
	// return update sql result
	return dao.findById(key).set("STATE", state).update();
    }

    /**
     * 查询
     */
    public IssueTerm findByToolID(String toolID) {
	return dao.findFirst("select * from TCA_ISSUE_TERM where TOOLID=?", toolID);
    }

    /**
     * 删除
     */
    public boolean delete(String issueTermId) {
	int result = Db.update("delete from TCA_ISSUE_TERM where TOOLID=?", issueTermId);
	if(result > 0){
	    return true;
	}else {
	    return false;
	}
    }


}