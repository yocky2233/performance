package zf.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FpsTest {

	/**
	 * ʹ�ýű�ǰ�������ֻ�������ģʽ�п���GPU����ģʽ
	 * @param args
	 */
	public static String[] getCmd(String CMD) {
		String[] output = null;
		boolean aa = false;
		try {
			Process p = Runtime.getRuntime().exec(CMD);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuffer a = new StringBuffer();
			String b = null;
			int i = 0;
			int number = 0;
			while((b=in.readLine())!=null){
				if(b.contains("Process	Execute")){
					if(number>0 && a.length()<500) { //<500Ϊ�˱����ȡ����ֵҲ��ɾ��
						a.delete(0, a.length());
					}
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

			b = a.substring(a.indexOf("Execute"),a.indexOf("/n/n/n/n"));
            String[] b1 = b.split("/n/n");
            float sum = 0;
            int framesDropped = 0;
			for(int i1=1;i1<b1.length;i1++) {				
				String c = b1[i1].trim();
				String[] d = c.split("	");
				float b2 = 0;
				for(int ii=0; ii<d.length; ii++) {
					b2 +=  Float.parseFloat(d[ii].trim());
					sum += b2;
				}
				if(b2 > 16) {
					framesDropped++;
				}
			}	
			int sumTime = b1.length;
			float A = sum/sumTime;
			float pctFramesDropped = (float)framesDropped/sumTime*100;
			String pct = new DecimalFormat("0.00").format(pctFramesDropped); 
			String AVG = new DecimalFormat("0.00").format(A);
			System.out.println("��֡����"+sumTime);
			System.out.println("ÿ֡ƽ�����ʱ�䣺"+AVG+"ms");
			System.out.println("��֡����"+framesDropped);
			System.out.println("��֡�ʣ�"+pct+"%");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
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
		String sub = out.substring(out.indexOf("Y")+1,out.indexOf("/"));
		return sub;
	}
	
	public static String getFtp(String Package) {
		String CMD = "adb shell dumpsys gfxinfo "+Package;
		String[] out = getCmd(CMD);
		return null;
	}
	
	
	public static void main(String[] args) {
		String Package = "";
		if(!Package.equals(getPackage())) {
			Package = getPackage();
			System.out.println("��ǰӦ�ð�����"+getPackage());
		}
		getFtp(getPackage());

	}

}
