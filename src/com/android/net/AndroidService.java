package com.android.net;

import java.io.IOException;  
import java.net.ServerSocket;  
import java.net.Socket;  
import java.util.ArrayList;  
import java.util.List;  
  
public class AndroidService {  
  
    public static void main(String[] args) throws IOException {  
//        ServerSocket serivce = new ServerSocket(30000);  
    	ServerSocket serivce = new ServerSocket(); 
        while (true) {  
            //�ȴ��ͻ�������  
            Socket socket = serivce.accept();  
            new Thread(new AndroidRunable(socket)).start();  
        }  
    }  
}  