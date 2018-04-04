package com.TCA.service;

import com.TCA.common.model.LimitTimeTool;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * LimitTimeTool 管理	
 * 描述：
 */
public class LimitTimeToolService {

	//private static final Log log = Log.getLog(LimitTimeToolService.class);
	
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
	public void save(LimitTimeTool limitTimeTool) {
		limitTimeTool.save();
	}
	
	/**
	* 更新
	*/
	public void update(LimitTimeTool limitTimeTool) {
		limitTimeTool.update();
	}
	
	/**
	* 查询
	*/
	public LimitTimeTool findById(int limitTimeToolId) {
		return dao.findFirst("select * from TCA_LIMIT_TIME_TOOL where id=?", limitTimeToolId);
	}
	
	/**
	* 删除
	*/
	public void delete(int limitTimeToolId) {
		Db.update("delete from TCA_LIMIT_TIME_TOOL where id=?", limitTimeToolId);
	}
	
	
}