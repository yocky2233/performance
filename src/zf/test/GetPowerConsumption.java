package zf.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetPowerConsumption {
	
	//获取应用的UID
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
	
	//获取功耗值
	public void getPowerConsumption(String Package) {
		String uid = getUid(Package);
		StringBuffer PowerConsumption = cmd("adb shell dumpsys batterystats "+Package+" | grep  "+uid);
		Pattern p = Pattern.compile(".+:.*\\d+.\\d+");
		Matcher m = p.matcher(PowerConsumption);
		while(m.find()) {
			String gh = m.group();
			String haoDian = gh.split(":")[1].trim();
			System.out.println("功耗为："+haoDian+"mAh");
		}
		
		//重置数据
		try {
			Runtime.getRuntime().exec("adb shell dumpsys batterystats --reset");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	//获取窗口内容
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
//		t.getUid("com.meitu.shanliao");
		t.getPowerConsumption("com.meitu.shanliao");
//		t.cmd("adb devices");
	}

}
