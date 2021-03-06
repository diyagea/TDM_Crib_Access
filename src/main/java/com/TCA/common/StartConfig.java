package com.TCA.common;

import com.TCA.common.model._MappingKit;
import com.TCA.controller.IssueRecordController;
import com.TCA.controller.IssueTermController;
import com.TCA.controller.IssueUserController;
import com.TCA.controller.LimitDeviceToolController;
import com.TCA.controller.LimitTimeToolController;
import com.TCA.controller.LimitUserToolController;
import com.TCA.index.IndexController;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.SqlServerDialect;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;

/**
 * API引导式配置
 */
public class StartConfig extends JFinalConfig {

    /**
     * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
     * 
     * 使用本方法启动过第一次以后，会在开发工具的 debug、run config 中自动生成
     * 一条启动配置，可对该自动生成的配置再添加额外的配置项，例如 VM argument 可配置为：
     * -XX:PermSize=64M -XX:MaxPermSize=256M
     */
    public static void main(String[] args) {
	JFinal.start("src/main/webapp", PropKit.use("config.ini").getInt("port"), "/", 5);
    }

    /**
     * 配置常量
     */
    public void configConstant(Constants me) {
	// 加载少量必要配置，随后可用PropKit.get(...)获取值
	PropKit.use("config.ini");
	me.setDevMode(PropKit.getBoolean("devMode", false));

	// 配置Url参数连接符为:"&"
	me.setUrlParaSeparator("&");
    }

    /**
     * 配置路由，需要手动添加Controller
     */
    public void configRoute(Routes me) {
	me.add("/", IndexController.class, "/");	// 第三个参数为该Controller的视图存放路径

	// TDM Crib Access Controller
	me.add("/issueRecord", IssueRecordController.class, "/page/issueRecord/");
	me.add("/issueTerm", IssueTermController.class, "/page/issueTerm/");
	me.add("/issueUser", IssueUserController.class, "/page/issueUser/");
	me.add("/limitDeviceTool", LimitDeviceToolController.class, "/page/limitDeviceTool/");
	me.add("/limitTimeTool", LimitTimeToolController.class, "/page/limitTimeTool/");
	me.add("/limitUserTool", LimitUserToolController.class, "/page/limitUserTool/");
    }

    public void configEngine(Engine me) {
	me.addSharedFunction("/common/_layout.html");
	me.addSharedFunction("/common/_paginate.html");
    }

    /**
     * 配置插件
     */
    public void configPlugin(Plugins me) {
	// 配置C3p0数据库连接池插件
	DruidPlugin druidPlugin = createDruidPlugin();
	me.add(druidPlugin);

	// 配置ActiveRecord插件
	ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);

	// 配置MSSQL方言
	arp.setDialect(new SqlServerDialect());

	// 所有映射在 MappingKit 中自动化搞定
	_MappingKit.mapping(arp);
	me.add(arp);

	// cron4j 定时器插件
	Cron4jPlugin cp = new Cron4jPlugin("cron4j.ini", "TODO");
	me.add(cp);
    }

    public static DruidPlugin createDruidPlugin() {
	return new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
    }

    /**
     * 配置全局拦截器
     */
    public void configInterceptor(Interceptors me) {

    }

    /**
     * 配置处理器
     */
    public void configHandler(Handlers me) {

    }
}
