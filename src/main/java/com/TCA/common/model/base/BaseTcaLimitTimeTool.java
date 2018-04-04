package com.TCA.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseTcaLimitTimeTool<M extends BaseTcaLimitTimeTool<M>> extends Model<M> implements IBean {

	public M setID(java.lang.Integer ID) {
		set("ID", ID);
		return (M)this;
	}

	public java.lang.Integer getID() {
		return getInt("ID");
	}

	public M setSTARTTIME(java.lang.String STARTTIME) {
		set("STARTTIME", STARTTIME);
		return (M)this;
	}

	public java.lang.String getSTARTTIME() {
		return getStr("STARTTIME");
	}

	public M setENDTIME(java.lang.String ENDTIME) {
		set("ENDTIME", ENDTIME);
		return (M)this;
	}

	public java.lang.String getENDTIME() {
		return getStr("ENDTIME");
	}

	public M setTOOLID(java.lang.String TOOLID) {
		set("TOOLID", TOOLID);
		return (M)this;
	}

	public java.lang.String getTOOLID() {
		return getStr("TOOLID");
	}

	public M setCOUNT(java.lang.Integer COUNT) {
		set("COUNT", COUNT);
		return (M)this;
	}

	public java.lang.Integer getCOUNT() {
		return getInt("COUNT");
	}

	public M setTYPE(java.lang.Short TYPE) {
		set("TYPE", TYPE);
		return (M)this;
	}

	public java.lang.Short getTYPE() {
		return getShort("TYPE");
	}

	public M setSTATE(java.lang.Short STATE) {
		set("STATE", STATE);
		return (M)this;
	}

	public java.lang.Short getSTATE() {
		return getShort("STATE");
	}

	public M setNOTE(java.lang.String NOTE) {
		set("NOTE", NOTE);
		return (M)this;
	}

	public java.lang.String getNOTE() {
		return getStr("NOTE");
	}

}
