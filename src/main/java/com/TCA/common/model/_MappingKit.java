package com.TCA.common.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * 
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {
    public static void mapping(ActiveRecordPlugin arp) {
	arp.addMapping("TCA_ISSUE_RECORD", "ID", IssueRecord.class);
	arp.addMapping("TCA_ISSUE_TERM", "TOOLID", IssueTerm.class);
	arp.addMapping("TCA_ISSUE_USER", "USERCODE", IssueUser.class);
	arp.addMapping("TCA_LIMIT_DEVICE_TOOL", "ID", LimitDeviceTool.class);
	arp.addMapping("TCA_LIMIT_TIME_TOOL", "ID", LimitTimeTool.class);
	arp.addMapping("TCA_LIMIT_USER_TOOL", "ID", LimitUserTool.class);
    }
}
