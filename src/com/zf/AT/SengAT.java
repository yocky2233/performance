package com.zf.AT;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Enumeration;

import gnu.io.*;

public class SengAT {

	public Port connectCOM(String comName) {
		Port port = new Port();
		// 连接端口
		System.out.println("正在连接" + comName + "通讯端口...");
		if (port.open(comName)) {
			System.out.println(comName + "通讯端口已经连接!");
		} else {
			System.out.println(comName + "通讯端口连接失败!");
		}
		return port;
	}

	public static void main(String[] args) throws Exception {
		SengAT test = new SengAT();
		// 连接端口
		Port port = test.connectCOM("COM8");

		// String feedback = port.sendAT("AT+ON");
		String feedback = port.sendAT("AT+OFF");

		System.out.println(feedback);

		port.close();
	}

}
