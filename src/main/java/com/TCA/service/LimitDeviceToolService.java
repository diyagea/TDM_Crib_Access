package com.TCA.service;

import com.TCA.common.model.LimitDeviceTool;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * LimitDeviceTool 管理	
 * 描述：
 */
public class LimitDeviceToolService {

	//private static final Log log = Log.getLog(LimitDeviceToolService.class);
	
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
	public void save(LimitDeviceTool limitDeviceTool) {
		limitDeviceTool.save();
	}
	
	/**
	* 更新
	*/
	public void update(LimitDeviceTool limitDeviceTool) {
		limitDeviceTool.update();
	}
	
	/**
	* 查询
	*/
	public LimitDeviceTool findById(int limitDeviceToolId) {
		return dao.findFirst("select * from TCA_LIMIT_DEVICE_TOOL where id=?", limitDeviceToolId);
	}
	
	/**
	* 删除
	*/
	public void delete(int limitDeviceToolId) {
		Db.update("delete from TCA_LIMIT_DEVICE_TOOL where id=?", limitDeviceToolId);
	}
	
	
}