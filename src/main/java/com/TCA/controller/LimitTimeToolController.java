package com.TCA.controller;

import com.TCA.common.model.LimitTimeTool;
import com.TCA.service.LimitTimeToolService;
import com.TCA.validator.LimitTimeToolValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * LimitTimeTool 管理	
 * 描述：
 * 
 */
public class LimitTimeToolController extends Controller {

	//private static final Log log = Log.getLog(LimitTimeToolController.class);
	
	static LimitTimeToolService srv = LimitTimeToolService.me;
	
	/**
	 * 列表
	 * /demo/limitTimeTool/list
	 */
	public void list() {
		setAttr("page", srv.paginate(getParaToInt("p", 1), 40));
		render("limitTimeToolList.html");
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
	@Before({LimitTimeToolValidator.class})
	public void save() {
		srv.save(getModel(LimitTimeTool.class));
		renderJson("isOk", true);
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
	 * /demo/limitTimeTool/update
	 */
	@Before(LimitTimeToolValidator.class)
	public void update() {
		srv.update(getModel(LimitTimeTool.class));
		renderJson("isOk", true);
	}

	/**
	 * 查看
	 * /demo/limitTimeTool/view
	 */
	public void view() {
		LimitTimeTool limitTimeTool = srv.findById(getParaToInt("id"));
		setAttr("limitTimeTool", limitTimeTool);
		render("limitTimeToolView.html");
	}
	 
	/**
	 * 删除
	 * /demo/limitTimeTool/delete
	 */
	public void delete() {
		srv.delete(getParaToInt("id"));
		renderJson("isOk", true);
	}
	
}