package zf.test;
//package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class StartAppHtml {
	private static String maxTime;
	private static String COMMAND1 = "adb shell am start -S -W -n ";
	private static String COMMAND2 = "adb shell am start -W -n ";
	private static String FILTER = "| grep ThisTime";
	private static int R = 5;
	private static String minTime;
	private static String avgTime;
	private static String start = null;
	public void GetFirstTime(String COMMAND, String packageName,
			String activity) {
		// ����һ������ΪR�����飬�������ÿ��������ʱ��
		int[] arrayTime = new int[R];
		// ����һ��sumTime��ѭ���ۼ�
		int sumTime = 0;
		for (int i = 0; i < arrayTime.length; i++) {

			Runtime runtime = Runtime.getRuntime();
			try {
				Process proc = runtime.exec(COMMAND + packageName + "/"
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
				// ǿ��ת��Ϊint����
				int str3 = Integer.parseInt(str2);
				// ��str3��ֵ����������
				arrayTime[i] = str3;
				sumTime += arrayTime[i];
				Thread.sleep(1000);
				runtime.exec("adb shell input keyevent 4");
				Thread.sleep(1000);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		// ��������
		Arrays.sort(arrayTime);
		maxTime = Integer.toString(arrayTime[R - 1]);
		minTime = Integer.toString(arrayTime[0]);
		avgTime = Integer.toString(sumTime / R);
	}

	
	public static void main(String[] args) throws IOException {
		File file = new File("D:\\style-table.html");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream out = new FileOutputStream(file);
		StringBuffer sb = new StringBuffer();
		sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"+"\n");
		sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">"+"\n");
		sb.append("<head>"+"\n");
		sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"+"\n");
		sb.append("<title>App���ܲ���</title>"+"\n");
		sb.append("<style type=\"text/css\">"+"\n");
		sb.append("<!-- "
				+ "/* ---- Body ---- */ "
				+ "body {margin:0;padding:20px; font:13px;} "
				+ "/* ---- Table ---- */ "
				+ "table {border-collapse:collapse;margin-bottom:15px;width:90%;} "
				+ "caption {text-align:left;font-size:15px;padding-bottom:10px;} "
				+ "table td,table th {padding:5px;border:1px solid #fff;border-width:0 1px 1px 0;} "
				+ "thead th {background:#91c5d4;} "
				+ "thead th[colspan],thead th[rowspan] {background:#66a9bd;} "
				+ "tbody th,tfoot th {text-align:left;background:#91c5d4;} "
				+ "tbody td,tfoot td {text-align:center;background:#d5eaf0;} "
				+ "tfoot th {background:#b0cc7f;} "
				+ "tfoot td {background:#d7e1c5;font-weight:bold;} "
				+ "tbody tr.odd td { background:#bcd9e1;} " + "-->"+"\n");
		
		sb.append("</style>"+"\n");
		sb.append("</head>"+"\n");
		sb.append("<body>"+"\n");
		sb.append("<table id=\"travel\" summary=\"\">"+"\n");
		sb.append("<thead>"+"\n");
		sb.append("<tr>"+"\n");
		sb.append("<th scope=\"col\" rowspan=\"2\">ģ��</th>"+"\n");
		sb.append("<th scope=\"col\" colspan=\"10\">������ʱ��ms��</th>"+"\n");
		sb.append("</tr>"+"\n");
		sb.append("<tr>"+"\n");
		sb.append("<th scope=\"col\">������ʽ</th>"+"\n");
		sb.append("<th scope=\"col\">���Դ���</th>"+"\n");
		sb.append("<th scope=\"col\">ƽ��ֵ</th>"+"\n");
		sb.append("<th scope=\"col\">��Сֵ</th>"+"\n");
		sb.append("<th scope=\"col\">���ֵ</th>"+"\n");
		sb.append("</tr>"+"\n");
		sb.append("</thead>"+"\n");
		sb.append("<tbody>"+"\n");
		
		for (int ii = 1; ii < 3; ii++) {
			if(ii == 1){
				start = COMMAND1;
			}else{
				start = COMMAND2;
			}

			StartAppHtml sa3 = new StartAppHtml();
			String[][] activity = {
					{ "com.android.music", "com.android.auroramusic.ui.AuroraMediaPlayHome" }};//,
//					{ "com.android.gallery3d", ".app.Gallery" },
//					{ "com.android.music",
//							"com.android.auroramusic.ui.AuroraMediaPlayHome" } };
			String[][][] BB = new String[activity.length][4][2];
			for (int i = 0; i < activity.length; i++) {
				sa3.GetFirstTime(start,activity[i][0], activity[i][1]);
				BB[i][0][0] = "����";
				BB[i][1][0] = "maxTime";
				BB[i][2][0] = "minTime";
				BB[i][3][0] = "avgTime";
				BB[i][0][1] = activity[i][0];
				BB[i][1][1] = maxTime;
				BB[i][2][1] = minTime;
				BB[i][3][1] = avgTime;
			}
			System.out.println("��"+ii+"������"+"\n" + Arrays.deepToString(BB));
	
		    for (int i = 0; i < BB.length; i++) {
				sb.append("<tr>"+"\n");
				sb.append("<th scope=\"row\">" + BB[i][0][1] + "</th>"+"\n");
				sb.append("<td>" + "��" + ii + "������" + "</td>"+"\n");
				sb.append("<td>" + R + "</td>"+"\n");
				sb.append("<td><font size=\"3\" color=\"red\">"+ BB[i][3][1] +"</font></td>");	//�ı��б��������С����ɫ
//				sb.append("<td>" + BB[i][3][1] + "</td>"+"\n");
				sb.append("<td>" + BB[i][2][1] + "</td>"+"\n");
				sb.append("<td>" + BB[i][1][1] + "</td>"+"\n");
				sb.append("</tr>"+"\n");
			}

		}
		sb.append("</tbody>"+"\n");
		sb.append("</table>"+"\n");
		sb.append("</body>"+"\n");
		sb.append("</html>"+"\n");
		out.write(sb.toString().getBytes("utf-8"));
		out.close();
	}
}
