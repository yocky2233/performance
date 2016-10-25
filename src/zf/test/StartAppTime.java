package zf.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class StartAppTime {
	private static int maxTime;
	private static String COMMAND1 = "adb shell am start -S -W -n ";
	private static String COMMAND2 = "adb shell am start -W -n ";
	private static String FILTER = "| grep ThisTime";
	private static int R = 5;
	private static int x;
	private static int minTime;
	private static int avgTime;
	private static String start = null;
	public void GetFirstTime(String COMMAND, String packageName,
			String activity) {
		// 定义一个长度为R的数组，用来存放每次启动的时间
		int[] arrayTime = new int[R];
		// 定义一个sumTime，循环累加
		int sumTime = 0;
		for (int i = 0; i < arrayTime.length; i++) {

			Runtime runtime = Runtime.getRuntime();
			try {
				Process proc = runtime.exec(COMMAND + packageName + "/"
						+ activity + " " + FILTER);
				System.out.println(COMMAND + packageName + "/"
						+ activity + " " + FILTER);
				proc.waitFor();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						proc.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while ((line = in.readLine()) != null) {
					stringBuffer.append(line + " ");
				}
				String str1 = stringBuffer.toString();
				String str2 = str1.split(" ")[1];
				// 强制转换为int类型
				int str3 = Integer.parseInt(str2);
				// 将str3的值传到数组中
				arrayTime[i] = str3;
				sumTime += arrayTime[i];
				Thread.sleep(1000);
				runtime.exec("adb shell input keyevent 4");
				System.out.println("点击返回");
				Thread.sleep(1000);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		// 数组排序
		Arrays.sort(arrayTime);
		maxTime = arrayTime[R - 1];
		minTime = arrayTime[0];
		avgTime = sumTime / R;
	}

	
	public static void main(String[] args) throws IOException {
		try {
            // 创建文件
            WritableWorkbook book = Workbook.createWorkbook(new File("d:\\AppStartTime.xls"));
            WritableCellFormat titleFormate = new WritableCellFormat();
            titleFormate.setAlignment(jxl.format.Alignment.CENTRE);
            titleFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            WritableSheet sheet = book.createSheet("第一页", 0);
            sheet.mergeCells(3, 0, 5, 0); //合并单元格
            sheet.mergeCells(9, 0, 11, 0);
            Label tob = new Label(3, 0, "启动耗时（单位：毫秒）",titleFormate);
            sheet.addCell(tob);
            Label tob2 = new Label(9, 0, "启动耗时（单位：毫秒）",titleFormate);
            sheet.addCell(tob2);
            Label label = new Label(0, 0, "一次启动",titleFormate);
            sheet.addCell(label);
            Label labe2 = new Label(1, 1, "模块",titleFormate);
            sheet.addCell(labe2);
            Label labe21 = new Label(2, 1, "测试次数",titleFormate);
            sheet.addCell(labe21);
            Label labe3 = new Label(3, 1, "最小值",titleFormate);
            sheet.addCell(labe3);
            Label labe4 = new Label(4, 1, "最大值",titleFormate);
            sheet.addCell(labe4);
            Label labe5 = new Label(5, 1, "平均值",titleFormate);
            sheet.addCell(labe5);
            Label labe6 = new Label(6, 0, "二次启动",titleFormate);
            sheet.addCell(labe6);
            Label labe7 = new Label(7, 1, "模块",titleFormate);
            sheet.addCell(labe7);
            Label labe71 = new Label(8, 1, "测试次数",titleFormate);
            sheet.addCell(labe71);
            Label labe8 = new Label(9, 1, "最小值",titleFormate);
            sheet.addCell(labe8);
            Label labe9 = new Label(10, 1, "最大值",titleFormate);
            sheet.addCell(labe9);
            Label labe10 = new Label(11, 1, "平均值",titleFormate);
            sheet.addCell(labe10);
		
		for (int ii = 1; ii < 3; ii++) {
			if(ii == 1){
				x = 1;
				start = COMMAND1;
				System.err.println("第一次启动");
			}else{
				x = 7;
				start = COMMAND2;
				System.err.println("第二次启动");
			}

			StartAppTime sa3 = new StartAppTime();
			String[][] activity = {
					{ "com.meitu.shanliao", ".app.startup.activity.SplashActivity","闪聊"}};
			String[][][] BB = new String[activity.length][4][2];
			for (int i = 0; i < activity.length; i++) {
				sa3.GetFirstTime(start,activity[i][0], activity[i][1]);
				BB[i][0][0] = "模块";
				BB[i][1][0] = "maxTime";
				BB[i][2][0] = "minTime";
				BB[i][3][0] = "avgTime";
				BB[i][0][1] = activity[i][2];
				BB[i][1][1] = Integer.toString(maxTime);
				BB[i][2][1] = Integer.toString(minTime);
				BB[i][3][1] = Integer.toString(avgTime);
			}
			System.out.println("第"+ii+"次启动"+"\n" + Arrays.deepToString(BB));
	
		    for (int i = 0; i < BB.length; i++) {
		    
				Label labe11 = new Label(x, i+2, BB[i][0][1],titleFormate);
				sheet.addCell(labe11);
				jxl.write.Number number0 = new jxl.write.Number(x+1,i+2,R,titleFormate);
		    	sheet.addCell(number0);
				jxl.write.Number number = new jxl.write.Number(x+2,i+2,Integer.parseInt(BB[i][2][1]),titleFormate);
		    	sheet.addCell(number);
		    	jxl.write.Number number1 = new jxl.write.Number(x+3,i+2,Integer.parseInt(BB[i][1][1]),titleFormate);
		    	sheet.addCell(number1);
		    	jxl.write.Number number2 = new jxl.write.Number(x+4,i+2,Integer.parseInt(BB[i][3][1]),titleFormate);
		    	sheet.addCell(number2);
			}
		}
		book.write();
        System.out.println("==============生成Excel成功======================");
        book.close();
		} catch (Exception e) {
            System.out.println(e);
        }
	}
}