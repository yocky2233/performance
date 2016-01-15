package zf.test;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.Session;
import javax.mail.MessagingException;
import javax.mail.Transport;
 
public class test3 {
     public static void sendMessage(String smtpHost,
                                    String from, String to,
                                    String subject, String messageText)
             throws MessagingException,java.io.UnsupportedEncodingException {
 
         // Step 1:  Configure the mail session
         System.out.println("Configuring mail session for: " + smtpHost);
         java.util.Properties props = new java.util.Properties();
         props.setProperty("mail.smtp.auth", "true");//ָ���Ƿ���ҪSMTP��֤
         props.setProperty("mail.smtp.host", smtpHost);//ָ��SMTP������
         props.put("mail.transport.protocol", "smtp");
         Session mailSession = Session.getDefaultInstance(props);
         mailSession.setDebug(true);//�Ƿ��ڿ���̨��ʾdebug��Ϣ
 
         // Step 2:  Construct the message
         System.out.println("Constructing message -  from=" + from + "  to=" + to);
         InternetAddress fromAddress = new InternetAddress(from);
         InternetAddress toAddress = new InternetAddress(to);
 
         MimeMessage testMessage = new MimeMessage(mailSession);
         testMessage.setFrom(fromAddress);
         testMessage.addRecipient(javax.mail.Message.RecipientType.TO, toAddress);
         testMessage.setSentDate(new java.util.Date());
         testMessage.setSubject(MimeUtility.encodeText(subject,"gb2312","B"));
 
        testMessage.setContent(messageText, "text/html;charset=gb2312");
         System.out.println("Message constructed");
 
        // Step 3:  Now send the message
         Transport transport = mailSession.getTransport("smtp");
         transport.connect(smtpHost, "yocky2233@163.com", "WYzzfSB02500346");  //�������˺�����
         transport.sendMessage(testMessage, testMessage.getAllRecipients());
         transport.close();
 
         System.out.println("Message sent!");
     }
 
     public static void main(String[] args) {
 
         String smtpHost = "smtp.163.com";  //smtp����˿�
         String from = "yocky2233@163.com";  //������
         String to = "471410616@qq.com";  //�ռ���
         String subject = "html�ʼ�����"; //�ʼ�����
         
         StringBuffer theMessage = new StringBuffer();
         theMessage.append("<BODY style=\"MARGIN: 10px\"><DIV>&nbsp;</DIV>"+"\n"
                 +"<DIV>����</DIV>"+"\n"   //�ɱ༭�ʼ�����
                 +"<DIV>&nbsp;</DIV>"+"\n"
         		+"<HR id=FMSigSeperator style=\"HEIGHT: 1px; WIDTH: 210px\" align=left color=#b5c4df SIZE=1>"+"\n");
         try {
 			BufferedReader b = new BufferedReader(new InputStreamReader(new FileInputStream("d:/style-table.html"),"UTF-8"));
 			String l;
 			
 			try {
 				while((l=b.readLine())!=null) {
// 					System.out.println(l);
 					theMessage.append(l+"\n");
 				}
 			} catch (IOException e) {
 				e.printStackTrace();
 			}
 		} catch (FileNotFoundException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
         theMessage.append("<DIV><SPAN id=_FoxFROMNAME>"+"\n"
 		        +"<DIV style=\"FONT-SIZE: 10pt; FONT-FAMILY: verdana; MARGIN: 10px\">"+"\n"
         		+"<DIV>֣�ٸ�</DIV>"+"\n"
         		+"<DIV>tim.zheng@iuni.com</DIV></DIV></SPAN></DIV></BODY>");
 
         try {
            test3.sendMessage(smtpHost, from, to, subject, theMessage.toString());
         }
         catch (javax.mail.MessagingException exc) {
             exc.printStackTrace();
         }
         catch (java.io.UnsupportedEncodingException exc) {
             exc.printStackTrace();
         }
     }
 }