package com.TCA.controller;

import com.TCA.common.model.IssueUser;
import com.TCA.service.IssueUserService;
import com.TCA.validator.IssueUserValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * IssueUser 管理	
 * 描述：
 * 
 */
public class IssueUserController extends Controller {

	//private static final Log log = Log.getLog(IssueUserController.class);
	
	static IssueUserService srv = IssueUserService.me;
	
	/**
	 * 列表
	 * /demo/issueUser/list
	 */
	public void list() {
		setAttr("page", srv.paginate(getParaToInt("p", 1), 40));
		render("issueUserList.html");
	}
	
	/**
	 * 准备添加
	 * /demo/issueUser/add
	 */
	public void add() {
		render("issueUserAdd.html");
	}
	
	/**
	 * 保存
	 * /demo/issueUser/save
	 */
	@Before({IssueUserValidator.class})
	public void save() {
		srv.save(getModel(IssueUser.class));
		renderJson("isOk", true);
	}

	/**
	 * 准备更新
	 * /demo/issueUser/edit
	 */
	public void edit() {
		IssueUser issueUser = srv.findById(getParaToInt("id"));
		setAttr("issueUser", issueUser);
		render("issueUserEdit.html");
	}

	/**
	 * 更新
	 * /demo/issueUser/update
	 */
	@Before(IssueUserValidator.class)
	public void update() {
		srv.update(getModel(IssueUser.class));
		renderJson("isOk", true);
	}

	/**
	 * 查看
	 * /demo/issueUser/view
	 */
	public void view() {
		IssueUser issueUser = srv.findById(getParaToInt("id"));
		setAttr("issueUser", issueUser);
		render("issueUserView.html");
	}
	 
	/**
	 * 删除
	 * /demo/issueUser/delete
	 */
	public void delete() {
		srv.delete(getParaToInt("id"));
		renderJson("isOk", true);
	}
	
}