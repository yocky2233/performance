package com.zf.AT;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Enumeration;

import gnu.io.*;

public class SengAT {

	public Port connectCOM(String comName) {
		Port port = new Port();
		// ���Ӷ˿�
		System.out.println("��������" + comName + "ͨѶ�˿�...");
		if (port.open(comName)) {
			System.out.println(comName + "ͨѶ�˿��Ѿ�����!");
		} else {
			System.out.println(comName + "ͨѶ�˿�����ʧ��!");
		}
		return port;
	}

	public static void main(String[] args) throws Exception {
		SengAT test = new SengAT();
		// ���Ӷ˿�
		Port port = test.connectCOM("COM8");

		// String feedback = port.sendAT("AT+ON");
		String feedback = port.sendAT("AT+OFF");

		System.out.println(feedback);

		port.close();
	}

}
