package com.TCA.controller;

import java.util.HashMap;

import com.TCA.common.model.LimitTimeTool;
import com.TCA.service.LimitTimeToolService;
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
     * /demo/limitTimeTool/save
     */
    public void save() {
	boolean result = srv.save(getModel(LimitTimeTool.class));
	renderJson("isOk", result);
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