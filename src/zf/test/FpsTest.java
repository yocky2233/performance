package zf.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FpsTest {

	static String activity;
	
	/**
	 * 使用脚本前请先在手机开发者模式中开启GPU呈现模式
	 * @param args
	 */
	public static int getCmd(String CMD) {
		String[] output = null;
		int fps2 = 0;
		boolean aa = false;
		try {
			Process p = Runtime.getRuntime().exec(CMD);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuffer a = new StringBuffer();
			String b = null;
			int i = 0;
			int number = 0;
			while((b=in.readLine())!=null){
//				System.out.println(b);
				
				if(b.contains(activity)){
//					if(number>0 && a.length()<500) { //<500为了避免把取到的值也给删除
//						a.delete(0, a.length());
//					}
					
					System.out.println("包含Process	Execute");
					aa = true;
					number += 1;
				}
				if(b.contains("hierarchy")) {
					aa = false;
				}
				if(aa){
					a.append(b+"/n");
				}
			}
			
			System.out.println(a);
			
			Pattern pt = Pattern.compile("\\d+\\.\\d+\\s+\\d+\\.\\d+.*\\d+\\.\\d+/n/n");
	        Matcher m = pt.matcher(a);//正则过滤
	        while(m.find()) {    
	            System.out.println("过滤的值："+m.group());  
	            b = m.group();
	        }

//			b = a.substring(a.indexOf("Execute"),a.indexOf("/n/n/n/n"));
            String[] b1 = b.split("/n/n");
            System.out.println(Arrays.toString(b1));
            float sum = 0;
            int framesDropped = 0;
            int vsync_overtime = 0;
			for(int i1=0;i1<b1.length;i1++) {	
//				System.out.println(b1[i1]);
				String c = b1[i1].trim();
				String[] d = c.split("	");
				System.out.println(Arrays.toString(d));
				double b2 = 0;
				
				if(d.length >= 3) {
					for(int ii=0; ii<d.length; ii++) {
						b2 +=  Float.parseFloat(d[ii].trim());
					}
				}
				
				System.out.println("帧率时间："+b2);
				
				//时间小于16ms的视为16ms
				if(b2 < 16.67) {
					b2 = 16.67;
				}
				sum += b2;
				if(b2 > 16.67) {
					System.out.println("掉帧时间："+b2);
					if(b2%16.67 == 0) {
						vsync_overtime = (int) (b2/16.67 - 1)+vsync_overtime;
					}else {
						double dz = b2/16.67;
						vsync_overtime = (int)dz+vsync_overtime;
					}
					framesDropped++;
				}
			}	
			
			int sumTime = b1.length-1;
			fps2 = sumTime * 60 / (sumTime + vsync_overtime);
			float A = sum/sumTime;
			float pctFramesDropped = (float)vsync_overtime/(sumTime + vsync_overtime)*100;
			String pct = new DecimalFormat("0.00").format(pctFramesDropped); 
			String AVG = new DecimalFormat("0.00").format(A);
			String fps = new DecimalFormat("0.00").format(1000/A);
			System.out.println("总帧数："+sumTime);
//			System.out.println("每帧平均完成时间："+AVG+"ms");
//			System.out.println("旧帧率："+fps+"fps");
			System.out.println("帧率："+fps2+"fps");
			System.out.println("掉帧数："+framesDropped);
			System.out.println("垂直同步掉帧数："+vsync_overtime);
			System.out.println("掉帧率："+pct+"%");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fps2;
	}
	
	public static String getPackage() {
		String CMD = "adb shell dumpsys activity top | grep ACTIVITY";
		String out = null;
		try {
			Process p = Runtime.getRuntime().exec(CMD);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String b = null;
			while((b=br.readLine())!=null) {
				sb.append(b);
			}
			out = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String at = out.substring(out.indexOf("Y")+1,out.indexOf("pid")).trim();
		activity = at.split(" ")[0].trim();
		System.out.println(activity);
		String sub = out.substring(out.indexOf("Y")+1,out.indexOf("/"));
		return sub;
	}
	
	public static int getFtp(String Package) {
		String CMD = "adb shell dumpsys gfxinfo "+Package;
		int out = getCmd(CMD);
		return out;
	}
	
	
	public void test() throws IOException {
//		String Package = "com.android.systemui";
		String Package = "";
		FileWriter fw = null;
		File f = new File("d:/fps.csv");
		try {
			fw = new FileWriter("d:/fps.csv");
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		if(!f.exists()) {
			f.createNewFile();
		}else {
			f.delete();
			f.createNewFile();
		}
		
		if(!Package.equals(getPackage())) {
			Package = getPackage();
			System.out.println("当前应用包名："+getPackage());
		}
		for(int i=0; i<1; i++) {
			try {
				int fps = getFtp(Package);
				fw.write(String.valueOf(fps)+"\n");
				fw.flush(); 
			}catch(Exception e) {
				e.printStackTrace();
				try {
					fw.write("0"+"\n");
					fw.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.out.println("帧率：0fps");
			}
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
		fw.close();
	}
	
	
	public static void main(String[] args)  {
		FpsTest t = new FpsTest();
		try {
			t.test();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		getPackage();
		
	}
}
