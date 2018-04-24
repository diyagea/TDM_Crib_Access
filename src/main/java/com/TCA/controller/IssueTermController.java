package com.TCA.controller;

import com.TCA.common.model.IssueTerm;
import com.TCA.service.IssueTermService;
import com.TCA.validator.IssueTermValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * IssueTerm 管理	
 * 描述：
 * 
 */
public class IssueTermController extends Controller {

	//private static final Log log = Log.getLog(IssueTermController.class);
	
	static IssueTermService srv = IssueTermService.me;
	
	/**
	 * 列表
	 */
	public void list() {
		setAttr("page", srv.paginate(getParaToInt("p", 1), 40));
		render("issueTermList.html");
	}
	
	/**
	 * 准备添加
	 */
	public void add() {
		render("issueTermAdd.html");
	}
	
	/**
	 * 保存
	 */
	@Before({IssueTermValidator.class})
	public void save() {
		srv.save(getModel(IssueTerm.class));
		renderJson("isOk", true);
	}

	/**
	 * 准备更新
	 */
	public void edit() {
		IssueTerm issueTerm = srv.findById(getParaToInt());
		setAttr("issueTerm", issueTerm);
		render("issueTermEdit.html");
	}

	/**
	 * 更新
	 */
	@Before(IssueTermValidator.class)
	public void update() {
		srv.update(getModel(IssueTerm.class));
		renderJson("isOk", true);
	}

	/**
	 * 查看
	 */
	public void view() {
		IssueTerm issueTerm = srv.findById(getParaToInt());
		setAttr("issueTerm", issueTerm);
		render("issueTermView.html");
	}
	 
	/**
	 * 删除
	 */
	public void delete() {
		srv.delete(getParaToInt());
		renderJson("isOk", true);
	}
	
}