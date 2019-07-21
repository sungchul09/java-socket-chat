package com.ssafy;
//채팅할때 소켓 연결과 관련된 작업을 수행하는 클래스

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
	private String name; // 나 or 상대방 or 대화명 기능 추가
	String str;
	private ChatClient cl; // 내가 보낼 데이터를 얻어오거나 수신한 메시지를 전달하는 GUI 프레임

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
			s = new Socket(ip, port);// 길준이형서버

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
