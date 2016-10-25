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

import zf.test.GetPowerConsumption;  
  
public class AndroidRunable implements Runnable {  
  
    Socket socket = null;  
    Port port;
  
    public AndroidRunable(Socket socket) {  
        this.socket = socket;  
    }  
  
    @Override  
    public void run() {  
    	GetPowerConsumption gc = new GetPowerConsumption();
        // 向android客户端输出hello worild  
        String line = null;  
        InputStream input;  
        OutputStream output;  
        String str = "hello world!";  
        try {  
            //向客户端发送信息  
            output = socket.getOutputStream();  
            input = socket.getInputStream();  
            BufferedReader bff = new BufferedReader(  
                    new InputStreamReader(input));  
            output.write(str.getBytes("gbk"));  
            output.flush();  
            //半关闭socket    
            socket.shutdownOutput();  
            //获取客户端的信息  
            while ((line = bff.readLine()) != null) {  
                System.out.print(line);  
                //获取到apk发来的信息后连接继电器
                String feedback = port.sendAT("AT+ON");
                
                gc.getQuantity = false;
            }  
            //关闭输入输出流  
            output.close();  
            bff.close();  
            input.close();  
            socket.close();  
  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
  
    }  
}  
