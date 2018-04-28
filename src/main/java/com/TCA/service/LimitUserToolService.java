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
	return dao.paginate(pageNumber, pageSize, "SELECT * ", "FROM TCA_LIMIT_USER_TOOL");
    }
    
    /**
     * 保存
     */
    public boolean save(LimitUserTool limitUserTool) {
	boolean result = false;
	try {
	    limitUserTool.remove("ID");
	    result = limitUserTool.save();
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
	if(!s){
	    state = 1;
	}
	
	return dao.findById(ID).set("STATE", state).update();
    }
    
    /**
     * 查询
     */
    public LimitUserTool findById(int ID) {
	return dao.findFirst("select * from TCA_LIMIT_USER_TOOL where ID=?", ID);
    }

    /**
     * 删除
     */
    public boolean delete(int ID) {
	int result = Db.update("delete from TCA_LIMIT_USER_TOOL where ID=?", ID);
	if(result>0){
	    return true;
	}else{
	    return false;
	}
    }


}