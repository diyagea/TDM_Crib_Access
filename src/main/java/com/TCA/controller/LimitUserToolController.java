package com.TCA.controller;

import java.util.HashMap;

import com.TCA.common.model.LimitUserTool;
import com.TCA.service.IssueUserService;
import com.TCA.service.LimitUserToolService;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

/**
 * LimitUserTool 管理
 * 描述：
 * 
 */
public class LimitUserToolController extends Controller {

    // private static final Log log = Log.getLog(LimitUserToolController.class);

    static LimitUserToolService srv = LimitUserToolService.me;
    static IssueUserService userSrv = IssueUserService.me;

    /**
     * 列表
     * /demo/limitUserTool/list
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void list() {
	int page = getParaToInt("page", 1);
	int limit = getParaToInt("limit", 10);
	Page p = srv.paginate(page, limit);
	HashMap m = new HashMap();
	m.put("code", 0);
	m.put("msg", "");
	m.put("count", p.getTotalRow());
	m.put("data", p.getList());
	
	for(Object o : p.getList()){
	    
	    System.out.println(((LimitUserTool)o).getUSERNAME());
	}

	renderJson(m);
    }

    /**
     * 准备添加
     * /demo/limitUserTool/add
     */
    public void addBefore() {
	renderJson("userList", userSrv.listWithState(0));
    }

    /**
     * 保存
     * /demo/limitUserTool/save
     */
    public void save() {
	boolean result = srv.save(getModel(LimitUserTool.class));
	renderJson("isOk", result);
    }

    /**
     * 准备更新
     * /demo/limitUserTool/edit
     */
    public void edit() {
	LimitUserTool limitUserTool = srv.findById(getParaToInt("id"));
	setAttr("limitUserTool", limitUserTool);
	render("limitUserToolEdit.html");
    }

    /**
     * 更新
     * /demo/limitUserTool/update
     */
    public void update() {
	boolean result = srv.update(getModel(LimitUserTool.class));
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