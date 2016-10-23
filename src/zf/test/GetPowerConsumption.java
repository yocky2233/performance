package zf.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetPowerConsumption {
	
	//��ȡӦ�õ�UID
	private String getUid(String Package) {
		String uid = null;
		StringBuffer uidCmd = cmd("adb shell ps | grep "+Package);
//		System.out.println("adb shell ps | findstr "+Package);
		Pattern p = Pattern.compile(".+"+Package);
		Matcher m = p.matcher(uidCmd);
		while(m.find()) {
			String StrUid = m.group();
//			System.out.println(StrUid);
			uid = StrUid.split(" ")[0].trim().replace("_", "");
			break;
		}
		System.out.println(uid);
		return uid;
	}
	
	//��ȡ����ֵ
	public void getPowerConsumption(String Package) {
		String haoDian = null;
		String uid = getUid(Package);
		StringBuffer PowerConsumption = cmd("adb shell dumpsys batterystats "+Package+" | grep  "+uid);
		Pattern p = Pattern.compile(".+:.*\\d+.\\d+");
		Matcher m = p.matcher(PowerConsumption);
		while(m.find()) {
			String gh = m.group();
			haoDian = gh.split(":")[1].trim();
			System.out.println(Package+"����Ϊ��"+haoDian+"mAh");
		}
		
		//��������
		try {
			if(!haoDian.isEmpty()) {
				System.out.println("�������");
				Runtime.getRuntime().exec("adb shell dumpsys batterystats --reset");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	//��ȡ��������
	private StringBuffer cmd(String cmd) {
		String str = "";
		StringBuffer sb = null;
		
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			sb = new StringBuffer(str);
			while((line = br.readLine())!=null) {
				sb.append(line.toString()+"\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println(sb);
		return sb;
	}
	
	
	public static void main(String[] args) {
		GetPowerConsumption t = new GetPowerConsumption();
//		t.getPowerConsumption("com.meitu.shanliao");
		t.getPowerConsumption("com.tencent.mm");
	}

}
