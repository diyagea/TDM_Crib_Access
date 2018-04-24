package com.TCA.controller;

import java.util.HashMap;
import java.util.Map;

import com.TCA.common.model.IssueRecord;
import com.TCA.service.IssueRecordService;
import com.TCA.validator.IssueRecordValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * IssueRecord 管理
 * 描述：
 * 
 */
public class IssueRecordController extends Controller {

    // private static final Log log = Log.getLog(IssueRecordController.class);

    static IssueRecordService srv = IssueRecordService.me;

    /**
     * 默认跳转list
     */
    public void index() {
	this.list();
    }

    /**
     * 查询列表
     */
    public void list() {
	setAttr("page", srv.paginate(getParaToInt("p", 1), 10));
	render("issueRecordList2.html");
    }

    public void add() {
	render("issueRecordAdd.html");
    }

    /**
     * 保存实体
     */
    @Before({ IssueRecordValidator.class })
    public void save() {
	//get model from web view
	IssueRecord issueRecord = getModel(IssueRecord.class);
	
	//call service method
	boolean result = srv.save(issueRecord);
	
	// return result
	//renderJson("isOk", result);
	redirect("/issueRecord");
    }

    /**
     * 更新跳转
     */
    public void edit() {
	IssueRecord issueRecord = srv.findById(getParaToInt());
	setAttr("issueRecord", issueRecord);
	// 修改跳转
	render("issueRecordEdit.html");
    }

    /**
     * 更新
     */
    @Before(IssueRecordValidator.class)
    public void update() {
	srv.update(getModel(IssueRecord.class));
	// renderJson("isOk", true);
	redirect("/issueRecord");
    }

    /**
     * 查询实体详情
     */
    public void view() {
	IssueRecord issueRecord = srv.findById(getParaToInt());
	setAttr("issueRecord", issueRecord);
	render("issueRecordView.html");
    }

    /**
     * 删除
     */
    public void delete() {
	srv.delete(getParaToInt());
	// renderJson("isOk", true);
	redirect("/issueRecord");
    }

}