package com.TCA.service;

import com.TCA.common.model.IssueTerm;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * IssueTerm 管理	
 * 描述：
 */
public class IssueTermService {

	//private static final Log log = Log.getLog(IssueTermService.class);
	
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
	public void save(IssueTerm issueTerm) {
		issueTerm.save();
	}
	
	/**
	* 更新
	*/
	public void update(IssueTerm issueTerm) {
		issueTerm.update();
	}
	
	/**
	* 查询
	*/
	public IssueTerm findById(int issueTermId) {
		return dao.findFirst("select * from TCA_ISSUE_TERM where id=?", issueTermId);
	}
	
	/**
	* 删除
	*/
	public void delete(int issueTermId) {
		Db.update("delete from TCA_ISSUE_TERM where id=?", issueTermId);
	}
	
	
}