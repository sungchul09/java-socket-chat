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
		v = new Vector<>(10, 10); // 10���� ������ �����ǰ�, �������� 10���� ������ �ø���.
	}

	public synchronized void addThread(ServerThread st) { // Vector�� �ִ°��̱� ������
															// ���� synchronized
															// ���ص� ��.
		v.add(st);
	}

	public synchronized void removeThread(ServerThread st) { // �����Ѵ�
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

	public static int PORT = 5432; // 9000 �̻� 60000������ ��ȣ�� �������!

	public void go() {
		try {
			ss = new ServerSocket(PORT);
			ss.setReuseAddress(true); // �ӵ� ����� ����
			System.out.println("1. ready~~~");
			while (true) {
				s = ss.accept();

				ServerThread st = new ServerThread(this, s); // ������ �����忡 �ִ´�

				this.addThread(st); // ������ ��� �����带 Vector�� �ִ´�

				st.start();

				System.out.println("2. contact ! new Client");
				System.out.println(s.getPort()); // ����� Ŭ���̾�Ʈ�� ��Ʈ��ȣ�� ��´�
				System.out.println(s.getLocalAddress()); // ����� Ŭ���̾�Ʈ�� ���� �ּ� ��ȯ
				System.out.println(s.getInetAddress()); // ������ ������ �ּ� ��ȯ

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
		this.server = server; // ������ �ִ� ��(vector)�� ����
		this.s = s;
		br = new BufferedReader(new InputStreamReader(s.getInputStream())); // ����
																			// ��
																			// br��
																			// �ֱ�
		pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true); // ���Ŵ�
																					// pw��
		System.out.println(s.getInetAddress() + "�� ����");
	}

	public void sendMessage(String string) {
		pw.println(string);
	}

	public void run() {
		try {
			while ((str = br.readLine()) != null) {
				server.broadcast(str); // �濡 �����Ͽ� �޽����� ������!
			}
		} catch (IOException e) {
			System.out.println(s.getInetAddress() + "�� ����");
			server.removeThread(this); // ������ ��(vector)���� �ڱ� �����带 �����
			try {
				s.close();
			} catch (IOException ie) {
			}
		}
	}
}
