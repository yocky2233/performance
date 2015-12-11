package zf.test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;


public class GetCpuMam {
	/**
	 * @param args
	 */
	public static String getCpu(String Package) {
		String CMD = "adb shell top -n 1 | grep "+Package;
		String out = getCmd(CMD);
		String sub = out.substring(0,out.indexOf("%"));
		String[] split = sub.split(" ");
		String PCT = split[split.length-1].trim();
		return PCT;
	}
	
	public static String getMam(String Package) {
		String CMD = "adb shell procrank | grep "+Package;
		String out = getCmd(CMD);
		String[] split = out.split("K");
		String MAM = split[3].trim();
		double mam = Integer.parseInt(MAM)/1024.0;
		String aa = new DecimalFormat("0.00").format(mam);
		return aa;
	}
	
	public static String getPackage() {
		String CMD = "adb shell dumpsys activity top | grep ACTIVITY";
		String out = getCmd(CMD);
		String sub = out.substring(out.indexOf("Y")+1,out.indexOf("/"));
		return sub;
	}
	
	
	public static String getCmd(String CMD) {
		String output = null;
		try {
			Process p = Runtime.getRuntime().exec(CMD);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuffer a = new StringBuffer();
			String b = null;
			while((b=in.readLine())!=null){
				a.append(b);
			}
			output = a.toString();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}
	
	
	
	public static void main(String[] args) {
		String Package = "";
		while(true){
			if(!Package.equals(getPackage())) {
				Package = getPackage();
				System.out.println("当前应用包名："+getPackage());
			}
			String CPU = getCpu(getPackage());
//			String MAM = getMam(getPackage());
//			String MAM = getMam("com.android.browser");
//			System.out.println("CPU占用:"+CPU+"%"+"; 内存占用:"+MAM+"M"+";");
			System.out.println("CPU占用:"+CPU+"%");
		}
	}

}
