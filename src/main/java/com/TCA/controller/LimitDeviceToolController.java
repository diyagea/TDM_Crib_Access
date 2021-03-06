package com.TCA.controller;

import java.util.HashMap;

import com.TCA.common.model.LimitDeviceTool;
import com.TCA.service.LimitDeviceToolService;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

/**
 * LimitDeviceTool 管理
 * 描述：
 * 
 */
public class LimitDeviceToolController extends Controller {

    // private static final Log log = Log.getLog(LimitDeviceToolController.class);

    static LimitDeviceToolService srv = LimitDeviceToolService.me;

    /**
     * 列表
     * /demo/limitDeviceTool/list
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
     * /demo/limitDeviceTool/save
     */
    public void save() {
	boolean result = srv.save(getModel(LimitDeviceTool.class));
	renderJson("isOk", result);
    }

    /**
     * 更新
     * /demo/limitDeviceTool/update
     */
    public void update() {
	boolean result = srv.update(getModel(LimitDeviceTool.class));
	renderJson("isOk", result);
    }
    
    /**
     * 更新状态
     */
    public void updateState() {
	boolean result = srv.updateState(getParaToInt("ID"), getParaToBoolean("SWITCH"));
	renderJson("isOk", result);
    }

    /**
     * 删除
     * /demo/limitDeviceTool/delete
     */
    public void delete() {
	boolean result = srv.delete(getParaToInt("ID"));
	renderJson("isOk", result);
    }

}