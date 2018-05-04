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
	String costunitFrom = getPara(0);
	String workplaceFrom = getPara(1);
	String costunitTo = getPara(2);
	String workplaceTo = getPara(3);
	String uCode = getPara(4);
	String toolID = getPara(5);
	int toolType = getParaToInt(6);
	int count = getParaToInt(7);
	int issueState = getParaToInt(8);

	renderJson(srv.doIssue(costunitFrom, workplaceFrom, costunitTo, workplaceTo, uCode, toolID, toolType, count, issueState));
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
	String costunitTo = getPara("COSTUNITTO");
	String userCode = getPara("USERCODE");
	String state = getPara("STATE");
	
	Page p = srv.paginate(page, limit, toolID, costunitTo, userCode, state);
	HashMap m = new HashMap();
	m.put("code", 0);
	m.put("msg", "");
	m.put("count", p.getTotalRow());
	m.put("data", p.getList());

	
	renderJson(m);
    }

}