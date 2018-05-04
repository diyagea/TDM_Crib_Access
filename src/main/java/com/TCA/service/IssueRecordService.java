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
    
    public String doIssue(String uCode, String toolID, int toolType, int count, String costunitFrom, String workplaceFrom, String costunitTo, String workplaceTo){
	/*limit check*/
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
	LimitDeviceTool lDevice = lDeviceSrv.find(costunitTo, workplaceTo, toolID, toolType);
	if(lDevice != null && lDevice.getSTATE() == 1){//存在并启用
	    if(lDevice.getCOUNT() == 0){//限制数量为0
		return "失败原因：该设备被禁止领取此刀具["+toolID+"]";
	    }else if(lDevice.getCOUNT() > 0 && count > lDevice.getCOUNT()){
		return "失败原因：领取数量已超过最大限制数量[条件：设备限制，数量："+lDevice.getCOUNT()+"]";
	    }
	}
	//4 check Time-Tool limit
	String nowTime = DateKit.toStr(new Date(), "hh:mm:ss");
	LimitTimeTool lTime = lTimeSrv.find(nowTime, toolID, toolType);
	if(lTime != null && lTime.getSTATE() == 1){//存在并启用
	    if(lTime.getCOUNT() == 0){//限制数量为0
		return "失败原因：当前时间被禁止领取此刀具["+toolID+"]";
	    }else if(lTime.getCOUNT() > 0 && count > lTime.getCOUNT()){
		return "失败原因：领取数量已超过最大限制数量[条件：时间限制，数量："+lTime.getCOUNT()+"]";
	    }
	}
	//add issue record / clear record state
	//check issueType (issue out/return)
	if(checkIssueType(uCode, toolID, toolType, costunitFrom, workplaceFrom, costunitTo, workplaceTo)){//领取
	    try {
		newRecord(uCode, toolID, toolType, count, costunitFrom, workplaceFrom, costunitTo, workplaceTo);
	    } catch (Exception e) {
		log.error("刀具["+toolID+"]添加领取记录失败！", e);
		return "失败原因：服务器内部错误，添加领取记录失败！";
	    }
	}else{//归还
	    try {
		returnRecord(uCode, toolID, toolType, count, costunitFrom, workplaceFrom, costunitTo, workplaceTo);
	    } catch (Exception e) {
		log.error("刀具["+toolID+"]更新返还领取记录失败！", e);
		return "失败原因：服务器内部错误，更新返还领取记录失败！";
	    }
	}
	return "TRUE";
    }
    
    //判断领取类型
    private boolean checkIssueType(String uCode, String toolID, int toolType, String costunitFrom, String workplaceFrom, String costunitTo, String workplaceTo){
	//check row count 
	Record r = Db.findFirst("SELECT COUNT(1) ROWCOUNT, COUNT FROM TCA_ISSUE_RECORD WHERE USERCODE=? AND TOOLID=? AND TYPE=? AND COSTUNITFROM=? AND WORKPLACEFROM=? AND COSTUNITTO=? AND WORKPLACETO=? AND STATE=0 ", costunitTo, workplaceFrom, costunitFrom, workplaceTo, uCode, toolID, toolType);
	boolean result = false;
	int rowCount = r.getInt("ROWCOUNT");
	if(rowCount == 0){//no row = new issue out 
	    result = true;
	}	
	return result;
    }
    
    //新增领取记录
    private void newRecord(String uCode, String toolID, int toolType, int count, String costunitFrom, String workplaceFrom, String costunitTo, String workplaceTo) throws Exception{
	//init model
	IssueRecord iRecord = new IssueRecord();
	iRecord.setUSERCODE(uCode);
	iRecord.setCOSTUNITFROM(costunitFrom);
	iRecord.setWORKPLACEFROM(workplaceFrom);
	iRecord.setCOSTUNITTO(costunitTo);
	iRecord.setWORKPLACETO(workplaceTo);
	iRecord.setTOOLID(toolID);
	iRecord.setSTATE(0);
	iRecord.setCOUNT(count);
	
	//查询刀具时间期限
	IssueTerm issueTerm = termSrv.findByToolID(toolID);
	
	//计算时间期限毫秒
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
    
    //返还刀具
    private void returnRecord(String uCode, String toolID, int toolType, int countBack, String costunitFrom, String workplaceFrom, String costunitTo, String workplaceTo) throws Exception{
	List<IssueRecord> records = dao.find("SELECT * FROM TCA_ISSUE_RECORD WHERE USERCODE=? AND TOOLID=? AND TYPE=? AND COUNT=? AND COSTUNITFROM=? AND WORKPLACEFROM=? AND COSTUNITTO=? AND WORKPLACETO=? AND STATE=0 ", uCode, toolID, toolType, countBack, costunitFrom, workplaceFrom, costunitTo, workplaceTo);
	for(IssueRecord r : records){
	    //剩余为归还数量=借出数量-已归还数量
	    int countOut = r.getCOUNT() - r.getCOUNTBACK();
	    if(countBack < countOut){//返还数量小于（剩余）未归还数量
		r.setCOUNTBACK(countBack + r.getCOUNTBACK());
		r.update();
		break;//当前归还流程结束
	    }else if(countBack == countOut){//返还数量等于（剩余）未归还数量
		r.setCOUNTBACK(countBack + r.getCOUNTBACK());
		r.setSTATE(1);//此记录已还清
		r.update();
		break;//当前归还流程结束
	    }else if(countBack > countOut){//返还数量大于（剩余）未归还数量
		r.setCOUNTBACK(countOut + r.getCOUNTBACK());//归还数量 = 此记录总借出数量
		r.setSTATE(1);//此记录已还清
		countBack = countBack - countOut;
		r.update();
		continue;//未还清，继续归还流程
	    }
	}
    }
    
    
    
    /**
     * query list
     * @param state
     * @return
     */
    @SuppressWarnings("unchecked")
    public Kv getPieData(){
	
	List<Record> userRecord = Db.find("SELECT R.USERCODE, U.NAME, SUM(COUNT) TOOLCOUNT FROM TCA_ISSUE_RECORD R  INNER JOIN TCA_ISSUE_USER U ON U.USERCODE=R.USERCODE AND R.STATE<>0 GROUP BY R.USERCODE, U.NAME ");
	List<Record> deviceRecord = Db.find("SELECT COSTUNITTO, SUM(COUNT)TOOLCOUNT FROM (SELECT * FROM TCA_ISSUE_RECORD WHERE STATE != 0) TCA_ISSUE_RECORD GROUP BY COSTUNITTO ");
	
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
	    deviceLegend.add(r.getStr("COSTUNITTO"));
	    
	    Kv temp = new Kv();
	    temp.put("name", r.getStr("COSTUNITTO"));
	    temp.put("value", r.getStr("TOOLCOUNT"));
	    deviceData.add(temp);
	}
	
	returnData.put("userLegend", userLegend);
	returnData.put("userData", userData);
	returnData.put("deviceLegend", deviceLegend);
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
		condStr += " AND COSTUNITTO = " + costunit;
	    }else{
		condStr += " COSTUNITTO = " + costunit;
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
	
	return dao.paginate(pageNumber, pageSize, "SELECT * ", "FROM TCA_ISSUE_RECORD " + condStr + " ORDER BY ISSUETIME DESC");
    }

    /**
     * 查询
     */
    public IssueRecord findById(int issueRecordId) {
	return dao.findFirst("select * from TCA_ISSUE_RECORD where id=?", issueRecordId);
    }
    
    /**
     * 查询列表
     */
    public List<IssueRecord> findListByState(int state) {
	return dao.find("SELECT * FROM TCA_ISSUE_RECORD WHERE STATE=? ", state);
    }


}