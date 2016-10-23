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
	 * ʹ�ýű�ǰ�������ֻ�������ģʽ�п���GPU����ģʽ
	 * @param args
	 */
	public static int getCmd(String CMD) {
		FpsTest ft = new FpsTest();
		
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
//					if(number>0 && a.length()<500) { //<500Ϊ�˱����ȡ����ֵҲ��ɾ��
//						a.delete(0, a.length());
//					}
					
//					System.out.println("����Process	Execute");
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
			
//			System.out.println("ץȡ�����ģ�"+a);
			
			Pattern pt = Pattern.compile("\\d+\\.\\d+\\s+\\d+\\.\\d+.*\\d+\\.\\d+/n/n");
	        Matcher m = pt.matcher(a);//�������
	        while(m.find()) {    
//	            System.out.println("���˵�ֵ��"+m.group());  
	            b = m.group();
	        }

//			b = a.substring(a.indexOf("Execute"),a.indexOf("/n/n/n/n"));
	        String[] b1 = null;
	        try{
	        	b1 = b.split("/n/n");
	        }catch(Exception e) {
	        	System.out.println("�뿪��GPU����ģʽ");
	        	ft.killMonkey();
				System.exit(0);
	        }
            
            
//            System.out.println(Arrays.toString(b1));
            float sum = 0;
            int framesDropped = 0;
            int vsync_overtime = 0;
			for(int i1=0;i1<b1.length;i1++) {	
//				System.out.println(b1[i1]);
				String c = b1[i1].trim();
				String[] d = c.split("	");
//				System.out.println(Arrays.toString(d));
				double b2 = 0;
				
				if(d.length >= 3) {
					for(int ii=0; ii<d.length; ii++) {
						b2 +=  Float.parseFloat(d[ii].trim());
					}
				}
				
//				System.out.println("֡��ʱ�䣺"+b2);
				
				//ʱ��С��16ms����Ϊ16ms
				if(b2 < 16.67) {
					b2 = 16.67;
				}
				sum += b2;
				if(b2 > 16.67) {
					if(b2 > 33.34) {
						System.out.println("��"+i1+"֡��֡ʱ�䣺"+b2+" ����3֡ʱ��");
					}
					
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
			System.out.println("��֡����"+sumTime);
//			System.out.println("ÿ֡ƽ�����ʱ�䣺"+AVG+"ms");
//			System.out.println("��֡�ʣ�"+fps+"fps");
			System.out.println("֡�ʣ�"+fps2+"fps");
			System.out.println("��֡����"+framesDropped);
			System.out.println("��ֱͬ����֡����"+vsync_overtime);
			System.out.println("��֡�ʣ�"+pct+"%");
			
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
		String at2 = at.split(" ")[0].trim();
		activity = at2.split("/")[1].trim();
//		System.out.println("activity�ǣ�"+activity);
		String sub = out.substring(out.indexOf("Y")+1,out.indexOf("/"));
		return sub;
	}
	
	public static int getFtp(String Package) {
		String CMD = "adb shell dumpsys gfxinfo "+Package;
		int out = getCmd(CMD);
		return out;
	}
	
	
	public void test(int times) throws IOException {
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
			System.out.println("��ǰӦ�ð�����"+Package);
		}
		
//		Runtime.getRuntime().exec("adb shell dumpsys gfxinfo "+Package);//�����һ������
		for(int i=1; i<times; i++) {
			System.out.println("��"+i+"��ȡֵ");
			try {
				int fps = getFtp(Package); //��ȡ֡��
				fw.write(String.valueOf(fps)+"\n");
				fw.flush(); 
				Thread.sleep(1500);
			}catch(Exception e) {
				e.printStackTrace();
				try {
					fw.write("0"+"\n");
					fw.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.out.println("֡�ʣ�0fps");
			}
		}
		
		fw.close();
	}
	
	
	public void create(String direction) {
		String []xy = getXY();
		int x = Integer.parseInt(xy[0]);
		int y = Integer.parseInt(xy[1]);
		String startX = null;
		String startY = null;
		String stopX = null;
		String stopY = null;
		
		String BS;
		
		
		
		File moneky = new File("d:/monekyFile");
		if(!moneky.exists()) {
			try {
				moneky.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(direction.equals("horizontal")) {
			
			double HY = 0.5;
			double startHX = 0.8;
			double stopHX = 0.2;
			
			startY = stopY = new DecimalFormat("0").format(HY*y);
			startX = new DecimalFormat("0").format(startHX*x);
			stopX = new DecimalFormat("0").format(stopHX*x);
			
			double BSH = Math.abs(startHX*x - stopHX*x)/8;
			BS = new DecimalFormat("0").format(BSH);
		}else {
			double VX = 0.5;
			double startVY = 0.7;
			double stopVY = 0.3;
			
			startX = stopX = new DecimalFormat("0").format(VX*x);
			startY = new DecimalFormat("0").format(startVY*y);
			stopY = new DecimalFormat("0").format(stopVY*y);
			
			double BSV = Math.abs(startVY*y - stopVY*y)/8;
			BS = new DecimalFormat("0").format(BSV);
		}
		
		try {
			String sleep = "300";
			FileWriter fw = new FileWriter("d:/monekyFile");
			fw.write("type = user");
			fw.write("\n");
			fw.write("count = 10");
			fw.write("\n");
			fw.write("speed = 1.0");
			fw.write("\n");
			fw.write("start data >>");
			fw.write("\n");
			fw.write("UserWait("+sleep+")");
			fw.write("\n");
			fw.write("Drag("+startX+","+startY+","+stopX+","+stopY+","+BS+")");
			fw.write("\n");
			fw.write("UserWait("+sleep+")");
			fw.write("\n");
			fw.write("Drag("+stopX+","+stopY+","+startX+","+startY+","+BS+")");
			fw.write("\n");
			fw.write("UserWait("+sleep+")");
			fw.write("\n");
			fw.write("Drag("+startX+","+startY+","+stopX+","+stopY+","+BS+")");
			fw.write("\n");
			fw.write("UserWait("+sleep+")");
			fw.write("\n");
			fw.write("Drag("+stopX+","+stopY+","+startX+","+startY+","+BS+")");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String[] getXY() {
		String[] XY = new String[2];
		try {
			Process p = Runtime.getRuntime().exec("adb shell wm size");
			
			BufferedReader read = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			String a;
			while((a=read.readLine())!=null) {
				if(a.contains("Physical size")) {
					String resolution = a.split(":")[1].trim();
					XY[0] = resolution.split("x")[0].trim();
					XY[1] = resolution.split("x")[1].trim();
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(Arrays.toString(XY));
		return XY;
		
	}
	
	// ��ȡ�����������
		public StringBuffer cmd(String cmd, String split) {
			Process p = null;
			StringBuffer sb = null;
			try {
				p = Runtime.getRuntime().exec(cmd);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						p.getInputStream()));
				sb = new StringBuffer();
				String a = null;
				while ((a = br.readLine()) != null) {
					sb.append(a + split);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return sb;
		}
	
	// ��ȡmonkey��pid
		public void killMonkey() {
			String pid = null;
			Process p = null;
			StringBuffer sb = null;
			
			String sbPid = cmd("adb  shell ps | grep monkey"," ").toString().trim();
			
			Pattern p1 = Pattern.compile("[0-9]+");
			Matcher m = p1.matcher(sbPid);
			while (m.find()) {
				String id = m.group();
				if (id.length() > 2) {
					pid = id;
					break;
				}
			}
			
			try {
				Runtime.getRuntime().exec("adb shell kill "+ pid);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	
	public void runTest() throws InterruptedException  {
		FpsTest t = new FpsTest();
		t.create("h");
		
		try {
			Process p = Runtime.getRuntime().exec("adb push d:/monekyFile /mnt/sdcard/");
            BufferedReader read = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			String a;
			while((a=read.readLine())!=null) {
				System.out.println(a);
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Process run = null;
		try {
			run = Runtime.getRuntime().exec("adb shell monkey -v -f /mnt/sdcard/monekyFile 99999999");
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		Thread.sleep(3000);
		
		try {
			t.test(11);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Thread.sleep(2000);
		t.killMonkey();
		System.out.println("���Խ�������");
	}
	
	public void aloneRun() {
		String Package = "";
		if(!Package.equals(getPackage())) {
			Package = getPackage();
			System.out.println("��ǰӦ�ð�����"+Package);
		}
		
			try {
				int fps = getFtp(Package); //��ȡ֡��
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("֡�ʣ�0fps");
			}
		
	}
	
	public static void main(String[] args) {
		FpsTest t = new FpsTest();
		try {
			t.runTest();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
//		t.aloneRun();
		
	}
	
	
	
}
