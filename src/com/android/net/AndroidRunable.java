package com.android.net;

import java.io.BufferedReader;  
import java.io.BufferedWriter;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.io.OutputStream;  
import java.io.OutputStreamWriter;  
import java.io.PrintWriter;  
import java.net.Socket;

import com.zf.AT.Port;
import com.zf.AT.SengAT;

import zf.test.GetPowerConsumption;  
  
public class AndroidRunable implements Runnable {  
  
    Socket socket = null;  
  
    public AndroidRunable(Socket socket) {  
        this.socket = socket;  
    }  
  
    @Override  
    public void run() {  
		// ��ʼ��������
		SengAT test = new SengAT();
		// ���Ӷ˿�
		Port port = test.connectCOM("COM4");
    	
    	GetPowerConsumption gc = new GetPowerConsumption();
        // ��android�ͻ������hello worild  
        String line = null;  
        InputStream input;  
        OutputStream output;  
        String str = "hello world!";  
        try {  
            //��ͻ��˷�����Ϣ  
            output = socket.getOutputStream();  
            input = socket.getInputStream();  
            BufferedReader bff = new BufferedReader(  
                    new InputStreamReader(input));  
            output.write(str.getBytes("gbk"));  
            output.flush();  
            //��ر�socket    
            socket.shutdownOutput();  
            //��ȡ�ͻ��˵���Ϣ  
            while ((line = bff.readLine()) != null) {  
                System.out.print(line);  
                //��ȡ��apk��������Ϣ�����Ӽ̵���
                String feedback = port.sendAT("AT+OFF");
                
                gc.getQuantity = false;
                break;
            }  
            port.close();
            //�ر����������  
            output.close();  
            bff.close();  
            input.close();  
            socket.close();  
  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
  
    }  
}  
