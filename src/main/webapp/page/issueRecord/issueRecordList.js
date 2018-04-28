layui.use(['form','layer','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;

    //用户列表
    var tableIns = table.render({
    	id : "dataListTable",
        elem: '#dataList',
        url : '/issueRecord/list',
        cellMinWidth : 95,
        page : true,
        height : "full-250",
        limits : [10,15,20,25],
        limit : 10,
        cols : [[
            //{type: "checkbox", fixed:"left", width:50},
            {type:'numbers'},
            {field: 'USERCODE', title: '员工编号', minWidth:150, align:"center"},
            {field: 'COSTUNIT', title: '成本单元', minWidth:150, align:"center"},
            {field: 'WORKPLACE', title: '车间', minWidth:150, align:"center"},
            {field: 'TOOLID', title: '刀具ID', minWidth:150, align:"center"},
            {field: 'TYPE', title: '刀具类别', width:100, align:"center", templet:function(d){
            	var title='组装刀具';
            	if(d.TYPE==0){
            		title='单项刀具';
            	}
            	return title;
            }},
            {field: 'COUNT', title: '领取数量', width:100, align:"center"},
            {field: 'ISSUETIME', title: '领取时间', width:150, align:'center'},
            {field: 'DEADLINE', title: '到期时间', width:150, align:'center'},
            {field: 'STATE', title: '状态', width:100, unresize: true,  align:'center',templet:function(d){
            	var title;
            	if(d.STATE==0){
            		title='<p class="layui-bg-green">已归还</p>';
            	}else if(d.STATE==1){
            		title='<p class="layui-bg-orange">未归还</p>';
            	}else{
            		title='<p class="layui-bg-red">已超时</p>';
            	}
            	return title;
            }},
        ]]
    });

    //add method
    form.on("submit(addBtn)",function(data){
    	var state = '';
		$.each($('input:checkbox:checked'),function(){
	      state += $(this).val()+",";
	    });
		console.log(state);
    	table.reload('dataListTable', {
    		  url: '/issueRecord/listWithFilter',
    		  where: {
    			  "COSTUNIT":data.field.costunit, 
    			  "USERCODE":data.field.userCode, 
    			  "TOOLID":data.field.toolID, 
    			  "STATE": state, 
    		  },
    		  page: {
    		    curr: 1 //重新从第 1 页开始
    		  }
  	  	});
    	layer.msg('加载成功', {
    		  time: 1000
    	});   
	    // cancel form action
	    return false;
    });
    
    
    //bind add-btn action
    $(".resetBtn").click(function(){
    	location.reload();
    });
    
})