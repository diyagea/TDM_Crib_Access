layui.use(['form','layer','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;

    //用户列表
    var tableIns = table.render({
        elem: '#dataList',
        url : '/issueTerm/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 15,
        id : "dataListTable",
        cols : [[
            //{type: "checkbox", fixed:"left", width:50},
            {type:'numbers'},
            {field: 'TOOLID', title: '刀具ID', width:150, align:"center"},
            {field: 'TYPE', title: '刀具类别', width:120, align:"center", templet:function(d){
            	var title;
            	if(d.TYPE==0){
            		title='单项刀具';
            	}else if(d.TYPE==1){
            		title='组装刀具';
            	}
            	return title;
            }},
            {field: 'TIMEVAL', title: '限制时间值', minWidth:150, align:"center"},
            {field: 'TIMETYPE', title: '时间类型', minWidth:150, align:"center", templet:function(d){
            	var title;
            	if(d.TIMETYPE==0){
            		title='分钟';
            	}else if(d.TIMETYPE==1){
            		title='小时';
            	}else if(d.TIMETYPE==2){
            		title='天';
            	}else if(d.TIMETYPE==3){
            		title='月';
            	}
            	return title;
            }},
            {field: 'NOTE', title: '备注', align:'center',minWidth:200},
            {field: 'STATE', title: '状态',  align:'center',templet:'#stateSwitch', width:100, unresize: true},
            {title: '操作', width:150, templet:'#dataListBar',fixed:"right",align:"center"}
        ]]
    });

    //data operation method
    function addData(data){
    	var index;
    	if(data){//update view， init data
	    	index = layui.layer.open({
	            title : "修改数据",
	            type : 2,
	            content : "issueTermAdd.html",
	            success : function(layero, index){
	            	//get body in layer
	                var body = layui.layer.getChildFrame('body', index);
	                
	                // init data
	                //console.log(body.find(".userCode"));
                    body.find(".toolID").val(data.TOOLID).attr("disabled", "disabled");  //主键
                    body.find(".toolType input[value="+data.TYPE+"]").prop("checked","checked");  //radio
                    body.find(".state").val(data.STATE);    
                    body.find(".timeVal").val(data.TIMEVAL);  
                    body.find(".timeType").val(data.TIMETYPE); 
                    body.find(".note").val(data.NOTE); 
                    //body.find(".userSex input[value="+edit.userSex+"]").prop("checked","checked");  //性别
                    //body.find(".userGrade").val(edit.userGrade);  //会员等级
                    
                    //change submit button event
                    var btn = body.find(".submitBtn").attr("lay-filter", "updateBtn").text("提交修改"); 
                    
                    form.render();
	                setTimeout(function(){
	                    layui.layer.tips('点击此处返回列表', '.layui-layer-setwin .layui-layer-close', {
	                        tips: 3
	                    });
	                },500)
	            }
	        });
    	}else{//add view
	    	index = layui.layer.open({
	            title : "添加数据",
	            type : 2,
	            content : "issueTermAdd.html",
	            success : function(layero, index){
	                setTimeout(function(){
	                    layui.layer.tips('点击此处返回列表', '.layui-layer-setwin .layui-layer-close', {
	                        tips: 3
	                    });
	                },500)
	            }
	        });
    	}
        layui.layer.full(index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(index);
        })
    };
    
    //bind add-btn action
    $(".addNews_btn").click(function(){
        addData();
    })

    //bing switch action
    form.on('switch(stateSwitch)', function(obj){
		//update state HttpRequest
	    $.post("/issueTerm/updateState",{
	         'TOOLID' : this.value,  //primary key
	         'SWITCH' : obj.elem.checked
	     },function(result){
	    	 if(result.isOk==true){
	    		 layer.msg("修改成功！");
	    		 //table reload
	    		 tableIns.reload();
	    	 }else{
	    		 layer.msg("修改失败，请稍后重试！");
	    	 }
	     });
	 });
    
    //bind table toolbar action
    table.on('tool(dataList)', function(obj){
    	//event object
        var layEvent = obj.event,
            data = obj.data;

        //exec method
        if(layEvent === 'edit'){//update-btn
            addData(data);
        }else if(layEvent === 'del'){ //Delete-btn
            layer.confirm('确定删除此数据？',{icon:3, title:'提示信息'},function(index){
                 $.get("/issueTerm/delete",{
                     TOOLID : data.TOOLID  //将需要删除的newsId作为参数传入
                 },function(result){
	            	 if(result.isOk==true){
	    	    		 layer.msg("删除成功！");
	    	    		 //table reload
	    	    		 tableIns.reload();
	    	    	 }else{
	    	    		 layer.msg("删除失败，请稍后重试！");
	    	    	 }
                     layer.close(index);
                 });
            });
        }
    });

})