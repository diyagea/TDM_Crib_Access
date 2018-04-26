package com.TCA.service;

import com.TCA.common.model.LimitDeviceTool;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * LimitDeviceTool 管理
 * 描述：
 */
public class LimitDeviceToolService {

    private static final Log log = Log.getLog(LimitDeviceToolService.class);

    public static final LimitDeviceToolService me = new LimitDeviceToolService();
    private final LimitDeviceTool dao = new LimitDeviceTool().dao();

    /**
     * 列表-分页
     */
    public Page<LimitDeviceTool> paginate(int pageNumber, int pageSize) {
	return dao.paginate(pageNumber, pageSize, "SELECT * ", "FROM TCA_LIMIT_DEVICE_TOOL");
    }

    /**
     * 保存
     */
    public boolean save(LimitDeviceTool limitDeviceTool) {
	boolean result = false;
	try {
	    limitDeviceTool.remove("ID");
	    result = limitDeviceTool.save();
	} catch (Exception e) {
	    log.error("LimitDeviceTool Save Error", e);
	}
	return result;
    }

    /**
     * 更新
     */
    public boolean update(LimitDeviceTool limitDeviceTool) {
	return limitDeviceTool.update();
    }
    
    /**
     * 更新
     */
    public boolean updateState(int ID, boolean s) {
	int state = 0;
	if(!s){
	    state = 1;
	}
	
	return dao.findById(ID).set("STATE", state).update();
    }

    /**
     * 查询
     */
    public LimitDeviceTool findById(int ID) {
	return dao.findFirst("select * from TCA_LIMIT_DEVICE_TOOL where ID=?", ID);
    }

    /**
     * 删除
     */
    public boolean delete(int ID) {
	int result = Db.update("delete from TCA_LIMIT_DEVICE_TOOL where ID=?", ID);
	
	if(result>0){
	    return true;
	}else{
	    return false;
	}
	
    }

}