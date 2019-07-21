package com.ssafy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ChatServer {

	ServerSocket ss;
	Socket s;
	ServerThread st;

	Vector<ServerThread> v;

	public ChatServer() {
		v = new Vector<>(10, 10); // 10개의 공간이 생성되고, 가득차면 10개씩 공간을 늘린다.
	}

	public synchronized void addThread(ServerThread st) { // Vector에 넣는것이기 때문에
															// 굳이 synchronized
															// 안해도 됨.
		v.add(st);
	}

	public synchronized void removeThread(ServerThread st) { // 제거한다
		v.remove(st);
	}

	public synchronized void broadcast(String str) {
		for (int i = 0; i < v.size(); i++) {
			ServerThread st1 = v.get(i);
			st1.sendMessage(str);
		}
	}

	public static void main(String[] args) {
		ChatServer server = new ChatServer();
		server.go();
	}

	public static int PORT = 5432; // 9000 이상 60000이하의 번호를 사용하자!

	public void go() {
		try {
			ss = new ServerSocket(PORT);
			ss.setReuseAddress(true); // 속도 향상을 위해
			System.out.println("1. ready~~~");
			while (true) {
				s = ss.accept();

				ServerThread st = new ServerThread(this, s); // 소켓을 스레드에 넣는다

				this.addThread(st); // 소켓이 담긴 스레드를 Vector에 넣는다

				st.start();

				System.out.println("2. contact ! new Client");
				System.out.println(s.getPort()); // 연결된 클라이언트의 포트번호를 얻는다
				System.out.println(s.getLocalAddress()); // 연결된 클라이언트의 로컬 주소 반환
				System.out.println(s.getInetAddress()); // 연결한 서버의 주소 반환

			}
		} catch (IOException e) {
			try {
				s.close();
				ss.close();
			} catch (IOException ie) {
			}
			System.out.println(e.getMessage());
		}
	}

}

class ServerThread extends Thread {
	Socket s;
	BufferedReader br;
	PrintWriter pw;
	String str;
	ChatServer server;

	public ServerThread(ChatServer server, Socket s) throws IOException {
		this.server = server; // 서버에 있는 방(vector)을 위해
		this.s = s;
		br = new BufferedReader(new InputStreamReader(s.getInputStream())); // 읽은
																			// 것
																			// br에
																			// 넣기
		pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true); // 쓸거는
																					// pw에
		System.out.println(s.getInetAddress() + "가 붙음");
	}

	public void sendMessage(String string) {
		pw.println(string);
	}

	public void run() {
		try {
			while ((str = br.readLine()) != null) {
				server.broadcast(str); // 방에 접근하여 메시지를 보낸다!
			}
		} catch (IOException e) {
			System.out.println(s.getInetAddress() + "가 나감");
			server.removeThread(this); // 나가면 방(vector)에서 자기 스레드를 지운다
			try {
				s.close();
			} catch (IOException ie) {
			}
		}
	}
}
