package com.TCA.controller;

import java.util.HashMap;

import com.TCA.common.model.LimitTimeTool;
import com.TCA.common.model.LimitUserTool;
import com.TCA.service.LimitTimeToolService;
import com.TCA.validator.LimitTimeToolValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

/**
 * LimitTimeTool 管理
 * 描述：
 * 
 */
public class LimitTimeToolController extends Controller {

    // private static final Log log = Log.getLog(LimitTimeToolController.class);

    static LimitTimeToolService srv = LimitTimeToolService.me;

    /**
     * 列表
     * /demo/limitTimeTool/list
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
     * 准备添加
     * /demo/limitTimeTool/add
     */
    public void add() {
	render("limitTimeToolAdd.html");
    }

    /**
     * 保存
     * /demo/limitTimeTool/save
     */
    public void save() {
	boolean result = srv.save(getModel(LimitTimeTool.class));
	renderJson("isOk", result);
    }

    /**
     * 准备更新
     * /demo/limitTimeTool/edit
     */
    public void edit() {
	LimitTimeTool limitTimeTool = srv.findById(getParaToInt("id"));
	setAttr("limitTimeTool", limitTimeTool);
	render("limitTimeToolEdit.html");
    }

    /**
     * 更新
     * /demo/limitUserTool/update
     */
    public void update() {
	boolean result = srv.update(getModel(LimitTimeTool.class));
	renderJson("isOk", result);
    }

    /**
     * 更新状态
     */
    public void updateState() {
	boolean result = srv.updateState(getPara("ID"), getParaToBoolean("SWITCH"));
	renderJson("isOk", result);
    }

    /**
     * 删除
     * /demo/limitUserTool/delete
     */
    public void delete() {
	boolean result = srv.delete(getParaToInt("ID"));
	renderJson("isOk", result);
    }

}