package com.TCA.service;

import com.TCA.common.model.LimitUserTool;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * LimitUserTool 管理
 * 描述：
 */
public class LimitUserToolService {

    private static final Log log = Log.getLog(LimitUserToolService.class);

    public static final LimitUserToolService me = new LimitUserToolService();
    private final LimitUserTool dao = new LimitUserTool().dao();

    /**
     * 列表-分页
     */
    public Page<LimitUserTool> paginate(int pageNumber, int pageSize) {
	
	Page<LimitUserTool> resultPage = dao.paginate(pageNumber, pageSize, "SELECT * ", "FROM TCA_LIMIT_USER_TOOL");
	for(LimitUserTool l : resultPage.getList()){
	    //加载员工名称
	    l.getUSERNAME();
	}
	
	return resultPage;
    }
    
    /**
     * 保存
     */
    public boolean save(LimitUserTool limitUserTool) {
	boolean result = false;
	try {
	    LimitUserTool temp = find(limitUserTool.getUSERCODE(), limitUserTool.getTOOLID(), limitUserTool.getTYPE());
	    if(temp == null){ // not exist
		limitUserTool.remove("ID");
		result = limitUserTool.save();
	    }else{// exist
		temp.setSTATE(limitUserTool.getSTATE());
		temp.setCOUNT(limitUserTool.getCOUNT());
		temp.setNOTE(limitUserTool.getNOTE());
		result = temp.update();
	    }
	    
	} catch (Exception e) {
	    log.error("LimitUserTool Save Error", e);
	}
	return result;
    }

    /**
     * 更新
     */
    public boolean update(LimitUserTool limitUserTool) {
	return limitUserTool.update();
    }

    /*
     *  仅更新状态
     */
    public boolean updateState(String ID, Boolean s){
	int state = 0;
	if(s){
	    state = 1;
	}
	
	return dao.findById(ID).set("STATE", state).update();
    }
    
    /**
     * 查询
     */
    public LimitUserTool findById(int ID) {
	return dao.findFirst("SELECT * FROM TCA_LIMIT_USER_TOOL WHERE ID=?", ID);
    }
    
    /**
     * 查询
     */
    public LimitUserTool find(String uCode, String toolID, int toolType) {
	return dao.findFirst("SELECT * FROM TCA_LIMIT_USER_TOOL WHERE USERCODE=? AND TOOLID=? AND TYPE=? ", uCode, toolID, toolType);
    }

    /**
     * 删除
     */
    public boolean delete(int ID) {
	int result = Db.update("DELETE FROM TCA_LIMIT_USER_TOOL WHERE ID=?", ID);
	if(result>0){
	    return true;
	}else{
	    return false;
	}
    }


}