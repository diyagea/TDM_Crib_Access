package com.TCA.controller;

import com.TCA.common.model.IssueRecord;
import com.TCA.service.IssueRecordService;
import com.TCA.validator.IssueRecordValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * IssueRecord 管理	
 * 描述：
 * 
 */
public class IssueRecordController extends Controller {

	//private static final Log log = Log.getLog(IssueRecordController.class);
	
	static IssueRecordService srv = IssueRecordService.me;
	
	/**
	 * 列表
	 * /demo/issueRecord/list
	 */
	public void list() {
		setAttr("page", srv.paginate(getParaToInt("p", 1), 40));
		render("issueRecordList.html");
	}
	
	/**
	 * 准备添加
	 * /demo/issueRecord/add
	 */
	public void add() {
		render("issueRecordAdd.html");
	}
	
	/**
	 * 保存
	 * /demo/issueRecord/save
	 */
	@Before({IssueRecordValidator.class})
	public void save() {
		srv.save(getModel(IssueRecord.class));
		renderJson("isOk", true);
	}

	/**
	 * 准备更新
	 * /demo/issueRecord/edit
	 */
	public void edit() {
		IssueRecord issueRecord = srv.findById(getParaToInt("id"));
		setAttr("issueRecord", issueRecord);
		render("issueRecordEdit.html");
	}

	/**
	 * 更新
	 * /demo/issueRecord/update
	 */
	@Before(IssueRecordValidator.class)
	public void update() {
		srv.update(getModel(IssueRecord.class));
		renderJson("isOk", true);
	}

	/**
	 * 查看
	 * /demo/issueRecord/view
	 */
	public void view() {
		IssueRecord issueRecord = srv.findById(getParaToInt("id"));
		setAttr("issueRecord", issueRecord);
		render("issueRecordView.html");
	}
	 
	/**
	 * 删除
	 * /demo/issueRecord/delete
	 */
	public void delete() {
		srv.delete(getParaToInt("id"));
		renderJson("isOk", true);
	}
	
}