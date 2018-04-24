package com.TCA.service;

import com.TCA.common.model.LimitUserTool;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * LimitUserTool 管理
 * 描述：
 */
public class LimitUserToolService {

    // private static final Log log = Log.getLog(LimitUserToolService.class);

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
    public void save(LimitUserTool limitUserTool) {
	limitUserTool.remove("ID");
	limitUserTool.save();
    }

    /**
     * 更新
     */
    public void update(LimitUserTool limitUserTool) {
	limitUserTool.update();
    }

    /**
     * 查询
     */
    public LimitUserTool findById(int limitUserToolId) {
	return dao.findFirst("select * from TCA_LIMIT_USER_TOOL where id=?", limitUserToolId);
    }

    /**
     * 删除
     */
    public void delete(int limitUserToolId) {
	Db.update("delete from TCA_LIMIT_USER_TOOL where id=?", limitUserToolId);
    }

}