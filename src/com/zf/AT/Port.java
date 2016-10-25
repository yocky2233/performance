package com.zf.AT;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import gnu.io.*;

public class Port {
	private CommPortIdentifier portId;
	private SerialPort serialPort;
	private static OutputStreamWriter out;
    	private static InputStreamReader in;
	
   	public InputStreamReader getIn(){
    		return in;
    	}
    	/**
     	 * 打开com口
     `	 * @param portName
     	 * @return
     	 */
	public boolean open(String portName){		
		try {
			portId = CommPortIdentifier.getPortIdentifier(portName);			
			try {
				serialPort = (SerialPort) portId.open("Serial_Communication",10000);
			} catch (PortInUseException e) {
				e.printStackTrace();
				return false;
			}
			// 下面是得到用于和COM口通讯的输入、输出流。
			try {
				in = new InputStreamReader(serialPort.getInputStream());
				out =new OutputStreamWriter(serialPort.getOutputStream());
			} catch (IOException e) {
				System.out.println("IOException");
				return false;
			}
			// 下面是初始化COM口的传输参数，如传输速率：9600等。
			try {
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			} catch (UnsupportedCommOperationException e) {
				e.printStackTrace();
				return false;
			}
			
		}  catch (NoSuchPortException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//关闭流
	public boolean close(){		
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		serialPort.close();
		return true;
	}
	
	//写入AT指令
	public static void writeln(String s) throws Exception {
	    out.write(s);
//	    out.write('\r');
	    out.flush();
	  }
	
	//读取返回结果
	public static String read() throws Exception {
	    int n, i;
	    char c;
	    String answer ="";
	    for (i = 0; i <100; i++) {
	      while (in.ready()) {            
	        n = in.read();                
	        if (n != -1) {               
	          c = (char)n;                
	          answer = answer + c;        
	          Thread.sleep(1);            
	        } 
	        else break;                  
	      } 
	      if(answer.indexOf("OK")!=-1){
	    	  break;
	      }
	      Thread.sleep(100);              
	    }
	    return answer;                   
	  }
	
	//发送AT指令
	public String sendAT(String atcommand) throws java.rmi.RemoteException {
		System.out.println("at:"+atcommand);
	    String s="";
	    try {
	      Thread.sleep(300);    
	      writeln(atcommand); 
	      System.out.println(atcommand);
	      Thread.sleep(100);    
	      s = read();           
	      Thread.sleep(300);    
	    } 
	    catch (Exception e) {
	      System.out.println("ERROR: send AT command failed; " + "Command: " + atcommand + "; Answer: " + s + "  " + e);
	    } 
	    return s;
	  }  
}
