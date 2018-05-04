package com.TCA.controller;

import java.util.HashMap;

import com.TCA.common.model.IssueTerm;
import com.TCA.service.IssueTermService;
import com.TCA.validator.IssueTermValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

/**
 * IssueTerm 管理
 * 描述：
 * 
 */
public class IssueTermController extends Controller {

    // private static final Log log = Log.getLog(IssueTermController.class);

    static IssueTermService srv = IssueTermService.me;

    public void index(){
	this.list();
    }
    
    /**
     * 列表
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
    }

    /**
     * 保存
     */
    @Before({ IssueTermValidator.class })
    public void save() {
	boolean result = srv.save(getModel(IssueTerm.class));
	renderJson("isOk", result);
    }

    /**
     * 更新
     */
    @Before(IssueTermValidator.class)
    public void update() {
	boolean result = srv.update(getModel(IssueTerm.class));
	renderJson("isOk", result);
    }
    
    /*
     * 更新状态
     */
    public void updateState() {
	boolean result = srv.updateState(getPara("TOOLID"), getParaToBoolean("SWITCH"));
	renderJson("isOk", result);
    }

    /**
     * 删除
     */
    public void delete() {
	boolean result = srv.delete(getPara("TOOLID"));
	renderJson("isOk", result);
    }

}