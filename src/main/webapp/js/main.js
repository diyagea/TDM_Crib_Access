//获取系统时间
var newDate = '';
getLangDate();
//值小于10时，在前面补0
function dateFilter(date){
    if(date < 10){return "0"+date;}
    return date;
}
function getLangDate(){
    var dateObj = new Date(); //表示当前系统时间的Date对象
    var year = dateObj.getFullYear(); //当前系统时间的完整年份值
    var month = dateObj.getMonth()+1; //当前系统时间的月份值
    var date = dateObj.getDate(); //当前系统时间的月份中的日
    var day = dateObj.getDay(); //当前系统时间中的星期值
    var weeks = ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"];
    var week = weeks[day]; //根据星期值，从数组中获取对应的星期字符串
    var hour = dateObj.getHours(); //当前系统时间的小时值
    var minute = dateObj.getMinutes(); //当前系统时间的分钟值
    var second = dateObj.getSeconds(); //当前系统时间的秒钟值
    var timeValue = "" +((hour >= 12) ? (hour >= 18) ? "晚上" : "下午" : "上午" ); //当前时间属于上午、晚上还是下午
    newDate = dateFilter(year)+"年"+dateFilter(month)+"月"+dateFilter(date)+"日 "+" "+dateFilter(hour)+":"+dateFilter(minute)+":"+dateFilter(second);
    document.getElementById("nowTime").innerHTML = "亲爱的管理员，"+timeValue+"好！ 欢迎使用 <b> TDM库房权限管理系统 </b>。当前时间为： "+newDate+"　"+week;
    setTimeout("getLangDate()",1000);
}

layui.use(['form','element','layer','jquery'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        element = layui.element;
        $ = layui.jquery;
    //上次登录时间【此处应该从接口获取，实际使用中请自行更换】
    $(".loginTime").html(newDate.split("日")[0]+"日</br>"+newDate.split("日")[1]);
    //icon动画
    $(".panel a").hover(function(){
        $(this).find(".layui-anim").addClass("layui-anim-scaleSpring");
    },function(){
        $(this).find(".layui-anim").removeClass("layui-anim-scaleSpring");
    })
    $(".panel a").click(function(){
        parent.addTab($(this));
    })

    //饼状图
    var userPie = echarts.init(document.getElementById("userToolPie"));
    var devicePie = echarts.init(document.getElementById("deviceToolPie"));
    var userOption = null, deviceOption = null;
    userOption = {
    	    title : {
    	        text: '当前员工领用刀具数量占比',
    	        subtext: '（仅包括未归还刀具）',
    	        x:'center'
    	    },
    	    tooltip : {
    	        trigger: 'item',
    	        formatter: "{a} <br/>{b} : {c} ({d}%)"
    	    },
    	    legend: {
    	        type: 'scroll',
    	        orient: 'vertical',
    	        left: 10,
    	        top: 20,
    	        bottom: 20,
    	        //data: pieData.userLegend,
    	        //selected: pieData.userLegendSelected
    	    },
    	    series : [
    	        {
    	            name: '刀具数量占比',
    	            type: 'pie',
    	            radius : '55%',
    	            center: ['50%', '60%'],
    	            //data: pieData.userData,
    	            itemStyle: {
    	                emphasis: {
    	                    shadowBlur: 10,
    	                    shadowOffsetX: 0,
    	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
    	                }
    	            }
    	        }
    	    ]
    	};
    deviceOption = {
    	    title : {
    	        text: '当前设备使用刀具数量占比',
    	        subtext: '（仅包括未归还刀具）',
    	        x:'center'
    	    },
    	    tooltip : {
    	        trigger: 'item',
    	        formatter: "{a} <br/>{b} : {c} ({d}%)"
    	    },
    	    legend: {
    	        type: 'scroll',
    	        orient: 'vertical',
    	        left: 10,
    	        top: 20,
    	        bottom: 20,
    	        //data: pieData.deviceLegend,
    	        //selected: pieData.deviceLegendSelected
    	    },
    	    series : [
    	        {
    	            name: '刀具数量占比',
    	            type: 'pie',
    	            radius : '55%',
    	            center: ['50%', '60%'],
    	            //data: pieData.deviceData,
    	            itemStyle: {
    	                emphasis: {
    	                    shadowBlur: 10,
    	                    shadowOffsetX: 0,
    	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
    	                }
    	            }
    	        }
    	    ]
    	};
    	if (userOption && typeof userOption === "object") {
    		userPie.setOption(userOption, true);
    		devicePie.setOption(deviceOption, true);
    	}
    	
    	/*自适应*/
		$(window).resize(function() {
			userPie.resize();
			devicePie.resize();
		});
		
	    function getPieData(){
	    	var data;
	    	$.ajax({
	            type: "GET",
	            url: "/issueRecord/pieData",
	            data: {},
	            dataType: "json",
	            success: function(data){
	            	devicePie.setOption({
	            		legend: {
	            	        data: data.deviceLegend,
	            	        //selected: data.deviceLegendSelected
	            	    },
	            	    series: [{
	                        data: data.deviceData
	                    }]
	            	});
	            	userPie.setOption({
	            		legend: {
	            	        data: data.userLegend,
	            	        //selected: data.deviceLegendSelected
	            	    },
	            	    series: [{
	                        data: data.userData
	                    }]
	            	});
	            }
	        });
	    	return data;
	    };
	    
	    //init
	    getPieData();
	    //loadData per 1 mins
	    setInterval(getPieData, 1000 * 60);
		
		
}); // layui range end
