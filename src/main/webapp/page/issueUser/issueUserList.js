layui.use(['form','layer','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;

    //用户列表
    var tableIns = table.render({
        elem: '#userList',
        url : '/issueUser/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 15,
        id : "userListTable",
        cols : [[
            //{type: "checkbox", fixed:"left", width:50},
            {type:'numbers'},
            {field: 'USERCODE', title: '用户编号', width:150, align:"center"},
            {field: 'NAME', title: '姓名', width:120, align:"center"},
            {field: 'DEPT', title: '部门', minWidth:150, align:"center"},
            {field: 'STATE', title: '用户状态',  align:'center',templet:'#stateSwitch', width:100, unresize: true},
            {field: 'NOTE', title: '备注', align:'center',minWidth:200},
            {title: '操作', width:150, templet:'#userListBar',fixed:"right",align:"center"}
        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】 TODO
    $(".search_btn").on("click",function(){
        if($(".searchVal").val() != ''){
        	layer.msg($(".searchVal").val());
            table.reload("userListTable",{
            	url: '/api/search', // TODO
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    key : $(".searchVal").val()  //搜索的关键字 
                }
            })
        }else{
            layer.msg("请输入搜索的内容");
        }
    });

    //添加用户
    function addUser(edit){
    	var index;
    	if(edit){
	    	index = layui.layer.open({
	            title : "修改用户信息",
	            type : 2,
	            content : "issueUserAdd.html",
	            success : function(layero, index){
	            	//get body in layer
	                var body = layui.layer.getChildFrame('body', index);
	                
	                // init data
	                //console.log(body.find(".userCode"));
                    body.find(".userCode").val(edit.USERCODE).attr("disabled", "disabled");  //员工号
                    body.find(".userName").val(edit.NAME);  //姓名
                    body.find(".userDept").val(edit.DEPT);  //部门
                    body.find(".userStatus").val(edit.STATE);    //用户状态
                    body.find(".userDesc").val(edit.NOTE);    //用户简介
                    //body.find(".userSex input[value="+edit.userSex+"]").prop("checked","checked");  //性别
                    //body.find(".userGrade").val(edit.userGrade);  //会员等级
                    
                    //change submit button event
                    var btn = body.find(".submitBtn").attr("lay-filter", "updateUser").text("提交修改"); 
                    
                    form.render();
	                setTimeout(function(){
	                    layui.layer.tips('点击此处返回用户列表', '.layui-layer-setwin .layui-layer-close', {
	                        tips: 3
	                    });
	                },500)
	            }
	        });
    	}else{
	    	index = layui.layer.open({
	            title : "添加用户",
	            type : 2,
	            content : "issueUserAdd.html",
	            success : function(layero, index){
	                setTimeout(function(){
	                    layui.layer.tips('点击此处返回用户列表', '.layui-layer-setwin .layui-layer-close', {
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
    }
    $(".addNews_btn").click(function(){
        addUser();
    })

  //监听switch操作
    form.on('switch(stateSwitch)', function(obj){
    	//console.log(data);
    	//layer.tips(this.value + ' ' + this.name + '：'+ obj.elem.checked, obj.othis);
		//update state
	    $.post("/issueUser/updateState",{
	         'USERCODE' : this.value,  //用户编号
	         'SWITCH' : obj.elem.checked
	     },function(result){
	    	 if(result.isOk==true){
	    		 layer.msg("修改成功！");
	    		 //刷新父页面
	    		 //parent.location.reload();
	    		 //location.reload();
	    		 tableIns.reload();
	    	 }else{
	    		 layer.msg("修改失败，请稍后重试！");
	    	 }
	     });
	 });
    
    //列表操作
    table.on('tool(userList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;

        if(layEvent === 'edit'){ //Update
            addUser(data);
            
        }else if(layEvent === 'del'){ //Delete 
            layer.confirm('确定删除此用户？',{icon:3, title:'提示信息'},function(index){
                 $.get("/issueUser/delete",{
                     USERCODE : data.USERCODE  //将需要删除的newsId作为参数传入
                 },function(data){
                    tableIns.reload();
                    layer.close(index);
                 });
            });
        }
    });

})