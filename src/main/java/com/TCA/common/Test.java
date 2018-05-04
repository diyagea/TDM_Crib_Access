package com.TCA.common;

import java.util.Date;

import com.jfinal.ext.kit.DateKit;

public class Test {
    public static void main(String[] args) {
	System.out.println(DateKit.toStr(new Date(), "hh:mm:ss"));
    }
    

}
