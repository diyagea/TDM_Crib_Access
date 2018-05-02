package com.TCA.controller;

import java.util.HashMap;

import com.TCA.common.model.IssueUser;
import com.TCA.service.IssueUserService;
import com.TCA.validator.IssueUserValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * IssueUser 管理
 * 描述：
 * 
 */
public class IssueUserController extends Controller {

    // private static final Log log = Log.getLog(IssueUserController.class);

    static IssueUserService srv = IssueUserService.me;

    /**
     * 列表
     * /demo/issueUser/list
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void list() {
	int page = getParaToInt("page");
	int limit = getParaToInt("limit");
	String key = getPara("key");
	System.out.println(key);
	Page p = srv.paginate(page, limit);
	HashMap m = new HashMap();
	m.put("code", 0);
	m.put("msg", "");
	m.put("count", p.getTotalRow());
	m.put("data", p.getList());

	renderJson(m);

	// setAttr("page", srv.paginate(getParaToInt("p", 1), 40));

	// render("issueUserList.html");
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
    public void save() {
	boolean result = srv.save(getModel(IssueUser.class));
	renderJson("isOk", result);
    }

    /**
     * 更新
     * /demo/issueUser/update
     */
    public void update() {
	boolean result = srv.update(getModel(IssueUser.class));
	renderJson("isOk", result);
    }
    
    /**
     * 更新状态
     */
    public void updateState() {
	boolean result = srv.updateState(getPara("USERCODE"), getParaToBoolean("SWITCH"));
	renderJson("isOk", result);
    }

    /**
     * 查看
     * /demo/issueUser/view
     */
    public void view() {
	IssueUser issueUser = srv.findByUCode(getPara("USERCODE"));
	setAttr("issueUser", issueUser);
	render("issueUserView.html");
    }

    /**
     * 删除
     * /demo/issueUser/delete
     */
    public void delete() {
	srv.delete(getPara("USERCODE"));
	renderJson("isOk", true);
    }

}