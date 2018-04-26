layui.use(['form','layer'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    //add method
    form.on("submit(addBtn)",function(data){
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        // 实际使用时的提交信息
	     $.post("/limitTimeTool/save",{
	    	 'limitTimeTool.ID' : data.field.ID,  //primary key
	    	 'limitTimeTool.STARTTIME' : data.field.startTime,  
	    	 'limitTimeTool.ENDTIME' : data.field.endTime,  
	         'limitTimeTool.TOOLID' : data.field.toolID,  
	         'limitTimeTool.TYPE' : data.field.toolType,  //radio input
	         'limitTimeTool.STATE' : data.field.state,   //select input
	         'limitTimeTool.COUNT' : data.field.count, 
	         'limitTimeTool.NOTE' : data.field.note,    //textarea
	     },function(result){
	    	 if(result.isOk==true){
	    		 top.layer.msg("添加成功！");
	    		 //刷新父页面
	    		 parent.location.reload();
	    	 }else{
	    		 top.layer.msg("添加失败，请稍后重试！");
	    	 }
	    	 //close layer
	    	 top.layer.close(index);
	    	 layer.closeAll("iframe");
	     });
	     // cancel form action
	     return false;
    });
    
    //update method
    form.on("submit(updateBtn)",function(data){
    	//弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        // 实际使用时的提交信息
        $.post("/limitTimeTool/update",{
	    	 'limitTimeTool.ID' : data.field.ID,  //primary key
	    	 'limitTimeTool.STARTTIME' : data.field.startTime,  
	    	 'limitTimeTool.ENDTIME' : data.field.endTime,  
	         'limitTimeTool.TOOLID' : data.field.toolID,  
	         'limitTimeTool.TYPE' : data.field.toolType,  //radio input
	         'limitTimeTool.STATE' : data.field.state,   //select input
	         'limitTimeTool.COUNT' : data.field.count, 
	         'limitTimeTool.NOTE' : data.field.note,    //textarea
	     },function(result){
	    	 if(result.isOk==true){
	    		 top.layer.msg("修改成功！");
	    		 //刷新父页面
	    		 parent.location.reload();
	    	 }else{
	    		 top.layer.msg("修改失败，请稍后重试！");
	    	 }
	    	 //close layer
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