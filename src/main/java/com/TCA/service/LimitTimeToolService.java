package com.TCA.service;

import com.TCA.common.model.LimitTimeTool;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * LimitTimeTool 管理
 * 描述：
 */
public class LimitTimeToolService {

    private static final Log log = Log.getLog(LimitTimeToolService.class);

    public static final LimitTimeToolService me = new LimitTimeToolService();
    private final LimitTimeTool dao = new LimitTimeTool().dao();

    /**
     * 列表-分页
     */
    public Page<LimitTimeTool> paginate(int pageNumber, int pageSize) {
	return dao.paginate(pageNumber, pageSize, "SELECT * ", "FROM TCA_LIMIT_TIME_TOOL");
    }

    /**
     * 保存
     */
    public boolean save(LimitTimeTool limitTimeTool) {
	boolean result = false;
	try {
	    limitTimeTool.remove("ID");
	    result = limitTimeTool.save();
	} catch (Exception e) {
	    log.error("LimitTimeTool Save Error", e);
	}
	return result;
    }

    /**
     * 更新
     */
    public boolean update(LimitTimeTool limitTimeTool) {
	return limitTimeTool.update();
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
    public LimitTimeTool findById(int limitTimeToolId) {
	return dao.findFirst("select * from TCA_LIMIT_TIME_TOOL where id=?", limitTimeToolId);
    }
    
    /**
     * 查询
     */
    public LimitTimeTool find(String time, String toolID, int toolType) {
	return dao.findFirst("SELECT * FROM TCA_LIMIT_TIME_TOOL WHERE STARTTIME>=? AND ENDTIME<=? AND TOOLID=? AND TYPE=? ", time, time, toolID, toolType);
    }

    /**
     * 删除
     */
    public boolean delete(int ID) {

	int result = Db.update("delete from TCA_LIMIT_TIME_TOOL where id=?", ID);
	if (result > 0) {
	    return true;
	} else {
	    return false;
	}
    }

}