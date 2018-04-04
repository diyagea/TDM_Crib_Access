package com.TCA.controller;

import com.TCA.common.model.LimitUserTool;
import com.TCA.service.LimitUserToolService;
import com.TCA.validator.LimitUserToolValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * LimitUserTool 管理	
 * 描述：
 * 
 */
public class LimitUserToolController extends Controller {

	//private static final Log log = Log.getLog(LimitUserToolController.class);
	
	static LimitUserToolService srv = LimitUserToolService.me;
	
	/**
	 * 列表
	 * /demo/limitUserTool/list
	 */
	public void list() {
		setAttr("page", srv.paginate(getParaToInt("p", 1), 40));
		render("limitUserToolList.html");
	}
	
	/**
	 * 准备添加
	 * /demo/limitUserTool/add
	 */
	public void add() {
		render("limitUserToolAdd.html");
	}
	
	/**
	 * 保存
	 * /demo/limitUserTool/save
	 */
	@Before({LimitUserToolValidator.class})
	public void save() {
		srv.save(getModel(LimitUserTool.class));
		renderJson("isOk", true);
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
	@Before(LimitUserToolValidator.class)
	public void update() {
		srv.update(getModel(LimitUserTool.class));
		renderJson("isOk", true);
	}

	/**
	 * 查看
	 * /demo/limitUserTool/view
	 */
	public void view() {
		LimitUserTool limitUserTool = srv.findById(getParaToInt("id"));
		setAttr("limitUserTool", limitUserTool);
		render("limitUserToolView.html");
	}
	 
	/**
	 * 删除
	 * /demo/limitUserTool/delete
	 */
	public void delete() {
		srv.delete(getParaToInt("id"));
		renderJson("isOk", true);
	}
	
}