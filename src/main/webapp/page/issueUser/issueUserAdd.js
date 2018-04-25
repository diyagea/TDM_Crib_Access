layui.use(['form','layer'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    //add method
    form.on("submit(addUser)",function(data){
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        // 实际使用时的提交信息
	     $.post("/issueUser/save",{
	         'issueUser.USERCODE' : $(".userCode").val(),  //用户编号
	         'issueUser.NAME' : $(".userName").val(),  //姓名
	         'issueUser.DEPT' : $(".userDept").val(),  //部门
	         'issueUser.STATE' : data.field.userStatus,    //用户状态
	         'issueUser.NOTE' : $(".userDesc").val(),    //用户简介
	         //userEmail : $(".userEmail").val(),  //邮箱
	         //userSex : data.field.sex,  //性别
	         //userGrade : data.field.userGrade,  //会员等级
	         //userStatus : data.field.userStatus,    //用户状态
	         //newsTime : submitTime,    //添加时间
	         //userDesc : $(".userDesc").text(),    //用户简介
	     },function(result){
	    	 //console.log(res);
	    	 if(result.isOk==true){
	    		 top.layer.msg("用户添加成功！");
	    		 //刷新父页面
	    		 parent.location.reload();
	    	 }else{
	    		 top.layer.msg("用户添加失败，请稍后重试！");
	    	 }
	     });
	     //close layer
	     top.layer.close(index);
	     layer.closeAll("iframe");

	     return false;
    });
    
    //update method
    form.on("submit(updateUser)",function(data){
    	//弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        // 实际使用时的提交信息
	     $.post("/issueUser/update",{
	         'issueUser.USERCODE' : $(".userCode").val(),  //用户编号
	         'issueUser.NAME' : $(".userName").val(),  //姓名
	         'issueUser.DEPT' : $(".userDept").val(),  //部门
	         'issueUser.STATE' : data.field.userStatus,    //用户状态
	         'issueUser.NOTE' : $(".userDesc").val(),    //用户简介
	     },function(result){
	    	 if(result.isOk==true){
	    		 top.layer.msg("修改成功！");
	    		 //刷新父页面
	    		 parent.location.reload();
	    	 }else{
	    		 top.layer.msg("修改失败，请稍后重试！");
	    	 }
	    	 top.layer.close(index);
	    	 layer.closeAll("iframe");
	     })
	    
	    // cancel form action
        return false;
    });

    //格式化时间
    function filterTime(val){
        if(val < 10){
            return "0" + val;
        }else{
            return val;
        }
    }
    //定时发布
    var time = new Date();
    var submitTime = time.getFullYear()+'-'+filterTime(time.getMonth()+1)+'-'+filterTime(time.getDate())+' '+filterTime(time.getHours())+':'+filterTime(time.getMinutes())+':'+filterTime(time.getSeconds());

})