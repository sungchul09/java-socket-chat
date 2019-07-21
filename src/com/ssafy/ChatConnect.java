package com.ssafy;
//ä���Ҷ� ���� ����� ���õ� �۾��� �����ϴ� Ŭ����

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatConnect {
	private Socket s;
	private BufferedReader br, br1;
	private PrintWriter pw;;
	private String name; // �� or ���� or ��ȭ�� ��� �߰�
	String str;
	private ChatClient cl; // ���� ���� �����͸� �����ų� ������ �޽����� �����ϴ� GUI ������

	private String ip;
	private int port;

	public ChatConnect(ChatClient cf, String ip, int port, String name) {
		this.cl = cf;
		this.ip = ip;
		this.port = port;
		this.name = name;

	}

	public void go() {
		try {
			s = new Socket(ip, port);// ������������

			ClientThread ct = new ClientThread(s);
			ct.start();

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void sendName(String msg) {
		try {
			pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);

			pw.println(msg);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void send(String msg) {
		try {
			pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);

			pw.println("[" + name + "] " + msg);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class ClientThread extends Thread {
		Socket s;
		BufferedReader br1;
		String str;

		public ClientThread(Socket s) throws IOException {
			this.s = s;
			br1 = new BufferedReader(new InputStreamReader(s.getInputStream()));
		}

		public void run() {
			try {
				while ((str = br1.readLine()) != null) {
					// System.out.println(str);
					cl.show(str);
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

	}
}
