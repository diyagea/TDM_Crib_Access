package com.TCA.common;

import java.util.Date;

import com.jfinal.ext.kit.DateKit;

public class Test {
    public static void main(String[] args) {
	    String deadline = "2018-05-03 11:06:00";
	    Date dead = DateKit.toDate(deadline);
	    Date now = new Date();

	    if (now.after(dead)) {// 超时
		System.out.println("OK");
	    }else{
		System.out.println("NO");
	    }
	
    }
    

}
