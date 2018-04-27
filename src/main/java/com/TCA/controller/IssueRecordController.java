package com.TCA.controller;

import java.util.HashMap;

import com.TCA.common.model.IssueRecord;
import com.TCA.service.IssueRecordService;
import com.TCA.validator.IssueRecordValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

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
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void list() {
	int page = getParaToInt("page");
	int limit = getParaToInt("limit");
	Page p = srv.paginate(page, limit);
	HashMap m = new HashMap();
	m.put("code", 0);
	m.put("msg", "");
	m.put("count", p.getTotalRow());
	m.put("data", p.getList());

	renderJson(m);
    }
    
    /**
     * 查询列表（条件）
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void listWithFilter() {
	int page = getParaToInt("page");
	int limit = getParaToInt("limit");
	
	//filter
	String toolID = getPara("TOOLID");
	String costunit = getPara("COSTUNIT");
	String userCode = getPara("USERCODE");
	String state = getPara("STATE");
	
	Page p = srv.paginate(page, limit, toolID, costunit, userCode, state);
	HashMap m = new HashMap();
	m.put("code", 0);
	m.put("msg", "");
	m.put("count", p.getTotalRow());
	m.put("data", p.getList());

	
	renderJson(m);
    }

    public void add() {
	render("issueRecordAdd.html");
    }

    /**
     * 保存实体
     */
    @Before({ IssueRecordValidator.class })
    public void save() {
	// get model from web view
	IssueRecord issueRecord = getModel(IssueRecord.class);

	// call service method
	boolean result = srv.save(issueRecord);

	// return result
	// renderJson("isOk", result);
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