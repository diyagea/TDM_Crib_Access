package com.TCA.controller;

import com.TCA.common.model.LimitDeviceTool;
import com.TCA.service.LimitDeviceToolService;
import com.TCA.validator.LimitDeviceToolValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * LimitDeviceTool 管理	
 * 描述：
 * 
 */
public class LimitDeviceToolController extends Controller {

	//private static final Log log = Log.getLog(LimitDeviceToolController.class);
	
	static LimitDeviceToolService srv = LimitDeviceToolService.me;
	
	/**
	 * 列表
	 * /demo/limitDeviceTool/list
	 */
	public void list() {
		setAttr("page", srv.paginate(getParaToInt("p", 1), 40));
		render("limitDeviceToolList.html");
	}
	
	/**
	 * 准备添加
	 * /demo/limitDeviceTool/add
	 */
	public void add() {
		render("limitDeviceToolAdd.html");
	}
	
	/**
	 * 保存
	 * /demo/limitDeviceTool/save
	 */
	@Before({LimitDeviceToolValidator.class})
	public void save() {
		srv.save(getModel(LimitDeviceTool.class));
		renderJson("isOk", true);
	}

	/**
	 * 准备更新
	 * /demo/limitDeviceTool/edit
	 */
	public void edit() {
		LimitDeviceTool limitDeviceTool = srv.findById(getParaToInt("id"));
		setAttr("limitDeviceTool", limitDeviceTool);
		render("limitDeviceToolEdit.html");
	}

	/**
	 * 更新
	 * /demo/limitDeviceTool/update
	 */
	@Before(LimitDeviceToolValidator.class)
	public void update() {
		srv.update(getModel(LimitDeviceTool.class));
		renderJson("isOk", true);
	}

	/**
	 * 查看
	 * /demo/limitDeviceTool/view
	 */
	public void view() {
		LimitDeviceTool limitDeviceTool = srv.findById(getParaToInt("id"));
		setAttr("limitDeviceTool", limitDeviceTool);
		render("limitDeviceToolView.html");
	}
	 
	/**
	 * 删除
	 * /demo/limitDeviceTool/delete
	 */
	public void delete() {
		srv.delete(getParaToInt("id"));
		renderJson("isOk", true);
	}
	
}