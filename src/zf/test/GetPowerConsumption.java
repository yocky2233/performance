package zf.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android.net.AndroidRunable;
import com.zf.AT.Port;
import com.zf.AT.SengAT;

public class GetPowerConsumption {
	int repetition = 0;
	public static boolean getQuantity = true;

	// 获取应用的UID
	private String getUid(String Package) {
		String uid = null;
		StringBuffer uidCmd = cmd("adb shell ps | grep " + Package);
		// System.out.println("adb shell ps | findstr "+Package);
		Pattern p = Pattern.compile(".+" + Package);
		Matcher m = p.matcher(uidCmd);
		while (m.find()) {
			String StrUid = m.group();
			// System.out.println(StrUid);
			uid = StrUid.split(" ")[0].trim().replace("_", "");
			break;
		}
		System.out.println(uid);
		return uid;
	}

	// 获取功耗值
	public void getPowerConsumption(String Package) {
		String haoDian = null;
		
		
		String uid = getUid(Package);
		StringBuffer PowerConsumption = cmd("adb shell dumpsys batterystats " + Package + " | grep  " + uid);
		Pattern p = Pattern.compile(".+:.*\\d+.\\d+");
		Matcher m = p.matcher(PowerConsumption);
		while (m.find()) {
			String gh = m.group();
			haoDian = gh.split(":")[1].trim();
			System.out.println(Package + "功耗为：" + haoDian + "mAh");
		}

		// 重置数据
		try {
			if (haoDian != null) {
				System.out.println("清除数据");
				Runtime.getRuntime().exec("adb shell dumpsys batterystats --reset");
			}else {
				
				//重试次数大于3次就不再重试
				if(repetition <= 3) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					repetition++;
					System.out.println("再次取功耗值");
					getPowerConsumption(Package);
				}else {
					repetition = 0;
				}
				
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 获取窗口内容
	private StringBuffer cmd(String cmd) {
		String str = "";
		StringBuffer sb = null;

		try {
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			sb = new StringBuffer(str);
			while ((line = br.readLine()) != null) {
				sb.append(line.toString() + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.out.println(sb);
		return sb;
	}

	public static void main(String[] args) {
		
		
		//开启socket服务监听中断信号
		SocketStart run = new SocketStart();
		run.start();
		
		
		GetPowerConsumption t = new GetPowerConsumption();
//		t.getPowerConsumption("com.meitu.shanliao");
		// t.getPowerConsumption("com.tencent.mm");
		
		//循环几次获取电量
		for(int i=0; i<5; i++) {
			//初始化串口类
			SengAT test = new SengAT(); 
			// 连接端口
			Port port = test.connectCOM("COM4");
			
			try {
				//运行uiautomator
//				Process p = Runtime.getRuntime().exec("");
				Thread.sleep(500);
				//程序运行后断开继电器连接
				String feedback = port.sendAT("AT+ON");
				port.close();
				
				//循环等待机器的自动化脚本跑完
				while(getQuantity) {
					System.out.println("等待中……");
					Thread.sleep(5000);
				}
				getQuantity = true;
				Thread.sleep(5000);
				//获取应用功耗值
				t.getPowerConsumption("com.meitu.shanliao");
				
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		System.out.println("测试结束");
		
	}

}




//开启socket服务
class SocketStart extends Thread {

	public void run() {
		System.out.println("开启socket服务");
		ServerSocket serivce = null;
		try {
			serivce = new ServerSocket(30000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (true) {
			// 等待客户端连接
			Socket socket = null;
			try {
				socket = serivce.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			new Thread(new AndroidRunable(socket)).start();
		}
	}
}
