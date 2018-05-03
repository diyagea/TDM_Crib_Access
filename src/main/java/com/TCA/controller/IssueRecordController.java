package com.TCA.controller;

import java.util.HashMap;

import com.TCA.service.IssueRecordService;
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
     * pieData
     */
    public void pieData() {
	renderJson(srv.getPieData());
    }
    
    /**
     * 执行TDM领取操作
     * params: url~issueRecord/doIssue/costunit & workplace & uCode & toolID & toolType & count & issueState
     */
    public void doIssue(){
	String costunit = getPara(0);
	String workplace = getPara(1);
	String uCode = getPara(2);
	String toolID = getPara(3);
	int toolType = getParaToInt(4);
	int count = getParaToInt(5);
	int issueState = getParaToInt(6);

	renderJson(srv.doIssue(costunit, workplace, uCode, toolID, toolType, count, issueState));
    }
    
    /**
     * 查询列表（条件筛选）
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

}