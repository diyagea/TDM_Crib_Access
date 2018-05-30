package com.TCA.util;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jbarcode.JBarcode;
import org.jbarcode.JBarcodeFactory;
import org.jbarcode.encode.Code128Encoder;
import org.jbarcode.encode.InvalidAtributeException;
import org.jbarcode.paint.TextPainter;
import org.jbarcode.util.ImageUtil;

public class JbarcodeUtil {

	//设置条形码高度  
	private static final int BARCODE_HEIGHT = 40;
	//设置条形码默认分辨率  
	private static final int BARCODE_DPI = ImageUtil.DEFAULT_DPI;
	//设置条形码字体样式  
	//private static final String FONT_FAMILY = "";
	//设置条形码字体大小  
	//private static final int FONT_SIZE = 15;
	//设置条形码文本  
	public static String TITLE = "";
	//创建jbarcode  
	private static JBarcode jbc = null;

	static JBarcode getJBarcode() throws InvalidAtributeException {
		if (jbc == null) {
			//生成code128  
			jbc = JBarcodeFactory.getInstance().createCode128();
			//设置编码
			jbc.setEncoder(Code128Encoder.getInstance());
			//设置Painter
			jbc.setTextPainter(CustomTextPainter.getInstance());
			//设置高度
			jbc.setBarHeight(BARCODE_HEIGHT);
			//设置尺寸，大小
			jbc.setXDimension(Double.valueOf(0.8).doubleValue());
			//设置显示文本
			jbc.setShowText(true);
		}
		return jbc;
	}

	/**
	 * 
	 * @param content
	 * @param filePath
	 * @param title
	 */

	public static void createBarcode(String content, String filePath, String title) {
		FileOutputStream fos = null;
		try {
			File file = new File(filePath);
			fos = new FileOutputStream(file);

			//设置条形码文本（子类里调用）
			TITLE = title;

			//创建条形码的BufferedImage图像  
			BufferedImage image = getJBarcode().createBarcode(content);
			ImageUtil.encodeAndWrite(image, ImageUtil.PNG, fos, BARCODE_DPI, BARCODE_DPI);
			fos.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 静态内部类 自定义的 TextPainter， 允许定义字体，大小，文本等
	 * 参考底层实现：BaseLineTextPainter.getInstance()
	 */
	protected static class CustomTextPainter implements TextPainter {
		private static CustomTextPainter instance = new CustomTextPainter();

		public static CustomTextPainter getInstance() {
			return instance;
		}

		public void paintText(BufferedImage barCodeImage, String text, int width) {
			 //绘图    
        Graphics g2d = barCodeImage.getGraphics();    
        //创建字体    
        //Font font = new Font(FONT_FAMILY, Font.PLAIN, FONT_SIZE * width);    
        //g2d.setFont(font);    
        FontMetrics fm = g2d.getFontMetrics();    
        int height = fm.getHeight();    
        int center = (barCodeImage.getWidth() - fm.stringWidth(TITLE+text)) / 2;    
        g2d.setColor(Color.WHITE);    
        g2d.fillRect(0, 0, barCodeImage.getWidth(), barCodeImage.getHeight() * 1 / 20);    
        g2d.fillRect(0, barCodeImage.getHeight() - (height * 9 / 10), barCodeImage.getWidth(), (height * 9 / 10));    
        g2d.setColor(Color.BLACK);    
        
        int h = barCodeImage.getHeight() - (height / 10) - 2;
        //绘制文本    
        //g2d.drawString(TITLE, 0, h);    
        g2d.drawString(TITLE+text, center, h);    
		}
	}

	//测试  
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("KJ4.1-0127-0001");
		list.add("KJ4.1-0128-0001");
		list.add("KJ4.1-0129-0001");
		list.add("KJ4.1-0130-0001");
		if (list != null && list.size() > 0) {
			for (String content : list) {
				JbarcodeUtil.createBarcode(content, "C:/Users/diyagea/Desktop/" + content + ".png", "刀具ID：");
			}
		}
		System.out.println("OK");

	}
}
