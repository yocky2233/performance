package zf.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Arrays;


public class FlowTest {

	private String getUid(String packageName) {
		StringBuffer uidCmd = cmd("adb shell dumpsys package " + packageName + " | grep userId=");
		String uid = uidCmd.toString().trim().split(" ")[0].trim().split("=")[1].trim();
		
        System.out.println("uid:"+uid);
		return uid;
	}
	
	private double getFlow(String cmd) {
		StringBuffer FlowStr = cmd(cmd);
		int FlowNumr = Integer.parseInt(FlowStr.toString().trim());
		double Flow = FlowNumr/1024.0;
//		String retain = new DecimalFormat("0.00").format(f);
//		float FlowF = Float.parseFloat(retain);
		
//		System.out.println(FlowF + "KB");
		return Flow;
	}

	// ��ȡ��������
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
//		 System.out.println(sb);
		return sb;
	}
	
	
	public String[] getFlowRate(String Package) {
		String []list = new String[3];
		
		String UID = getUid(Package);
		
		String sendFlowCmd = "adb shell cat /proc/uid_stat/" + UID + "/tcp_snd";
		String receiveFlowCmd = "adb shell cat /proc/uid_stat/" + UID + "/tcp_rcv";
		
		double getUidReceiveFlow = getFlow(sendFlowCmd);
		double getUidSendFlow = getFlow(receiveFlowCmd);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double getUidReceiveFlow2 = getFlow(sendFlowCmd);
		double getUidSendFlow2 = getFlow(receiveFlowCmd);
		
		String UidReceiveFlow = new DecimalFormat("0").format((getUidReceiveFlow2 - getUidReceiveFlow)/1024);
		String UidSendFlow = new DecimalFormat("0").format((getUidSendFlow2 - getUidSendFlow)/1024);
		String UidTotal = new DecimalFormat("0").format(((getUidReceiveFlow2 - getUidReceiveFlow)+(getUidSendFlow2 - getUidSendFlow))/1024);
		
		list[0] = UidReceiveFlow;
		list[1] = UidSendFlow;
		list[2] = UidTotal;
		
		return list;
	}
	
	
	public static void main(String[] args) {
		FlowTest T = new FlowTest();
		
		String UID = T.getUid("com.meitu.shanliao");
//		String UID = T.getUid("com.tencent.mm");
		
		String sendFlowCmd = "adb shell cat /proc/uid_stat/" + UID + "/tcp_snd";
		String receiveFlowCmd = "adb shell cat /proc/uid_stat/" + UID + "/tcp_rcv";
		
		double initialSendFlow = T.getFlow(sendFlowCmd);
		double initialReceiveFlow = T.getFlow(receiveFlowCmd);
//		System.out.println("��������:"+initialSendFlow);
//		System.out.println("��������:"+initialReceiveFlow);
		
		while(true) {
//			double SendFlow = T.getFlow(sendFlowCmd) - initialSendFlow;
//			double ReceiveFlow = T.getFlow(receiveFlowCmd) - initialReceiveFlow;
			
			String SendFlow = new DecimalFormat("0.0").format(T.getFlow(sendFlowCmd) - initialSendFlow);
			String ReceiveFlow = new DecimalFormat("0.0").format(T.getFlow(receiveFlowCmd) - initialReceiveFlow);
			
			System.out.println("��������:" + ReceiveFlow + "kb");
			System.out.println("��������:" + SendFlow + "kb");
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}

	
}
