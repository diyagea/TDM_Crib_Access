package com.TCA.service;

import java.util.ArrayList;
import java.util.List;

import com.TCA.common.model.IssueRecord;
import com.TCA.common.model.IssueUser;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * IssueRecord 管理
 * 描述：
 */
public class IssueRecordService {

    // private static final Log log = Log.getLog(IssueRecordService.class);

    public static final IssueRecordService me = new IssueRecordService();
    private final IssueRecord dao = new IssueRecord().dao();

    /**
     * query list
     * @param state
     * @return
     */
    @SuppressWarnings("unchecked")
    public Kv getPieData(){
	
	List<Record> userRecord = Db.find("SELECT R.USERCODE, U.NAME, SUM(COUNT) TOOLCOUNT FROM TCA_ISSUE_RECORD R  INNER JOIN TCA_ISSUE_USER U ON U.USERCODE=R.USERCODE AND R.STATE<>0 GROUP BY R.USERCODE, U.NAME ");
	List<Record> deviceRecord = Db.find("SELECT COSTUNIT, SUM(COUNT)TOOLCOUNT FROM (SELECT * FROM TCA_ISSUE_RECORD WHERE STATE != 0) TCA_ISSUE_RECORD GROUP BY COSTUNIT ");
	
	List<String> userLegend = new ArrayList<String>();
	List<String> deviceLegend = new ArrayList<String>();
	List<Kv> userData = new ArrayList<Kv>();
	List<Kv> deviceData = new ArrayList<Kv>();
	Kv returnData = new Kv();
	
	//userData
	for(Record r : userRecord){
	    userLegend.add(r.getStr("NAME"));
	    
	    Kv temp = new Kv();
	    temp.put("name", r.getStr("NAME"));
	    temp.put("value", r.getStr("TOOLCOUNT"));
	    userData.add(temp);
	}

	//deviceData
	for(Record r : deviceRecord){
	    deviceLegend.add(r.getStr("COSTUNIT"));
	    
	    Kv temp = new Kv();
	    temp.put("name", r.getStr("COSTUNIT"));
	    temp.put("value", r.getStr("TOOLCOUNT"));
	    deviceData.add(temp);
	}
	
	returnData.put("userLegend", userLegend);
	//returnData.put("userLegendSelected", userLegend.subList(0, 9));
	returnData.put("userData", userData);
	returnData.put("deviceLegend", deviceLegend);
	//returnData.put("deviceLegendSelected", deviceLegend.subList(0, 9));
	returnData.put("deviceData", deviceData);
	
	return returnData;
    }
    
    /**
     * 列表-分页
     */
    public Page<IssueRecord> paginate(int pageNumber, int pageSize) {
	return dao.paginate(pageNumber, pageSize, "SELECT * ", "FROM TCA_ISSUE_RECORD");
    }

    /**
     * 列表-分页
     */
    public Page<IssueRecord> paginate(int pageNumber, int pageSize, String toolID, String costunit, String userCode, String state) {
	String condStr = "WHERE";
	boolean condFlag = false;
	
	if (StrKit.notBlank(toolID)) {
	    condStr += " TOOLID = " + toolID;
	    condFlag = true;
	}
	if (StrKit.notBlank(costunit)) {
	    if(condFlag){
		condStr += " AND COSTUNIT = " + costunit;
	    }else{
		condStr += " COSTUNIT = " + costunit;
		condFlag = true;
	    }
	}
	if (StrKit.notBlank(userCode)) {
	    if(condFlag){
		condStr += " AND USERCODE = " + userCode;
	    }else{
		condStr += " USERCODE = " + userCode;
		condFlag = true;
	    }
	}
	
	if (StrKit.notBlank(state)) {//查询未归还
	    state = state.substring(0, state.length()-1);
	    if (condFlag) {
		condStr += " AND STATE in (" + state + ") ";
	    } else {
		condStr += " STATE in (" + state + ") ";
	    }
	    condFlag = true;
	}
	
	if(!condFlag){
	    condStr = "";
	}
	
	return dao.paginate(pageNumber, pageSize, "SELECT * ", "FROM TCA_ISSUE_RECORD " + condStr);
    }

    /**
     * 保存
     */
    public boolean save(IssueRecord issueRecord) {
	issueRecord.remove("ID");
	boolean result = issueRecord.save();
	return result;
    }

    /**
     * 更新
     */
    public void update(IssueRecord issueRecord) {
	issueRecord.update();
    }

    /**
     * 查询
     */
    public IssueRecord findById(int issueRecordId) {
	return dao.findFirst("select * from TCA_ISSUE_RECORD where id=?", issueRecordId);
    }

    /**
     * 删除
     */
    public void delete(int issueRecordId) {
	Db.update("delete from TCA_ISSUE_RECORD where id=?", issueRecordId);
    }

}