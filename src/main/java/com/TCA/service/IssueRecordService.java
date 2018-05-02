package com.TCA.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.TCA.common.model.IssueRecord;
import com.TCA.common.model.IssueTerm;
import com.TCA.common.model.IssueUser;
import com.TCA.common.model.LimitDeviceTool;
import com.TCA.common.model.LimitTimeTool;
import com.TCA.common.model.LimitUserTool;
import com.jfinal.ext.kit.DateKit;
import com.jfinal.kit.Kv;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * IssueRecord 管理
 * 描述：
 */
public class IssueRecordService {

    private static final Log log = Log.getLog(IssueRecordService.class);

    public static final IssueRecordService me = new IssueRecordService();
    private final IssueRecord dao = new IssueRecord().dao();
    
    static IssueUserService userSrv = IssueUserService.me;
    static IssueTermService termSrv = IssueTermService.me;
    static LimitUserToolService lUserSrv = LimitUserToolService.me;
    static LimitDeviceToolService lDeviceSrv = LimitDeviceToolService.me;
    static LimitTimeToolService lTimeSrv = LimitTimeToolService.me;
    
    public String issueCheck(String costunit, String workplace, String uCode, String toolID, int toolType, int count, int issueState){
	//limit check
	//1 check IssueUser State
	IssueUser u = userSrv.findByUCode(uCode);
	if(u == null || u.getSTATE()==0){//不存在或禁用
	    return "失败原因：用户不存在或已被禁用，禁止领取刀具！";
	}
	//2 check User-Tool limit
	LimitUserTool lUser = lUserSrv.find(uCode, toolID, toolType);
	if(lUser != null && lUser.getSTATE() == 1){//存在并启用
	    if(lUser.getCOUNT() == 0){//限制数量为0
		return "失败原因：该用户被禁止领取此刀具["+toolID+"]!";
	    }else if(lUser.getCOUNT() > 0 && count > lUser.getCOUNT()){
		return "失败原因：领取数量已超过最大限制数量[条件：用户限制，数量："+lUser.getCOUNT()+"]";
		
	    }
	}
	//3 check Device-Tool limit
	LimitDeviceTool lDevice = lDeviceSrv.find(costunit, workplace, toolID, toolType);
	if(lDevice != null && lDevice.getSTATE() == 1){//存在并启用
	    if(lDevice.getCOUNT() == 0){//限制数量为0
		return "失败原因：该设备被禁止领取此刀具["+toolID+"]";
	    }else if(lDevice.getCOUNT() > 0 && count > lDevice.getCOUNT()){
		return "失败原因：领取数量已超过最大限制数量[条件：设备限制，数量："+lDevice.getCOUNT()+"]";
	    }
	}
	//4 check Time-Tool limit
	String time = "";
	LimitTimeTool lTime = lTimeSrv.find(time, toolID, toolType);
	if(lTime != null && lTime.getSTATE() == 1){//存在并启用
	    if(lTime.getCOUNT() == 0){//限制数量为0
		return "失败原因：当前时间被禁止领取此刀具["+toolID+"]";
	    }else if(lTime.getCOUNT() > 0 && count > lTime.getCOUNT()){
		return "失败原因：领取数量已超过最大限制数量[条件：时间限制，数量："+lTime.getCOUNT()+"]";
	    }
	}
	//add issue record / clear record state
	if(issueState == 0){//领取
	    try {
		addRecord(uCode, costunit, workplace, toolID, count);
	    } catch (Exception e) {
		log.error("刀具["+toolID+"]添加领取记录失败！", e);
		return "失败原因：服务器内部错误，添加领取记录失败！";
	    }
	}else{//归还
	    try {
		clearRecord(uCode, costunit, workplace, toolID);
	    } catch (Exception e) {
		log.error("刀具["+toolID+"]更新领取记录状态失败！", e);
		return "失败原因：服务器内部错误，更新领取记录状态失败！";
	    }
	}
	return "TRUE";
    }
    
    private void addRecord(String uCode, String costunit, String workplace, String toolID, int count) throws Exception{
	IssueRecord iRecord = new IssueRecord();
	//init
	iRecord.setUSERCODE(uCode);
	iRecord.setCOSTUNIT(costunit);
	iRecord.setWORKPLACE(workplace);
	iRecord.setTOOLID(toolID);
	iRecord.setSTATE((short) 0);
	iRecord.setCOUNT(count);
	
	//count date time and term
	IssueTerm issueTerm = termSrv.findByToolID(toolID);
	
	long timeTerm = 0;
	if(issueTerm != null){
	    int timeVal = issueTerm.getTIMEVAL();
	    int timeType = issueTerm.getTIMETYPE();
	    switch(timeType){
		case 0://minutes
		    timeTerm = timeVal * 60 * 1000;
		    break;
		case 1://hours
		    timeTerm = timeVal * 60 * 60 * 1000;
		    break;
		case 2://days
		    timeTerm = timeVal * 24 * 60 * 60 * 1000;
		    break;
		case 3://weeks
		    timeTerm = timeVal * 7 * 24 * 60 * 60 * 1000;
		    break;
	    }
	}
	
	if(timeTerm == 0){//默认设置
	    timeTerm = PropKit.getInt("defaultTimeTerm") * 24 * 60 * 60 * 1000;
	}
	
	//issue time
	Date now = new Date();
	String issueTime = DateKit.toStr(now, DateKit.timeStampPattern);
	
	//deadline
	Date dead = new Date(now.getTime() + timeTerm);
	String deadline = DateKit.toStr(dead, DateKit.timeStampPattern);
	
	iRecord.setISSUETIME(issueTime);
	iRecord.setDEADLINE(deadline);
	
	iRecord.save();
	
    }
    
    private void clearRecord(String uCode, String costunit, String workplace, String toolID) throws Exception{
	IssueRecord iRecord = dao.findFirst("SELECT * FROM TCA_ISSUE_RECORD WHERE USERCODE=? AND COSTUNIT=? AND WORKPLACE=? AND TOOLID=? ");
	iRecord.setSTATE((short) 1).update();
    }
    
    
    
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