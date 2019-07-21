package com.ssafy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient extends JFrame {
	private JTextArea ta;
	private JTextField tf;
	private JButton bSend, bExit;
	private JPanel panel;
	private ChatConnect cc;
	static Scanner sc = new Scanner(System.in);

	public ChatClient(String ip, int port, String name) {
		createGUI();
		cc = new ChatConnect(this, ip, port, name);
		cc.go();
		cc.sendName(name);
	}

	public static void main(String[] args) {
		System.out.print("유저아이디를 입력해주세요:");
		String name = sc.next();
		ChatClient cct = new ChatClient("192.168.29.198", 5432, name);

	}

	public void createGUI() {
		ta = new JTextArea(40, 50);
		tf = new JTextField(50);
		bSend = new JButton("SEND");

		bExit = new JButton("EXIT");
		panel = new JPanel();
		// textarea 직접 키보드 입력 막기
		ta.setEditable(false);
		ta.setBackground(new Color(255, 0, 0, 50));

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(bSend);
		panel.add(bExit);

		// tf랑 send 버튼에 우리가 작성한 이벤트 처리 객체 전달하기
		tf.addActionListener(new ChattingListener());
		bSend.addActionListener(new ChattingListener());
		bExit.addActionListener(new ExitListener());
		ta.setEditable(false);

		ta.setBackground(Color.pink);
		// 패널과 ta와 tf를 프레임에 추가
		add(panel, BorderLayout.LINE_END);
		add(new JScrollPane(ta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),
				BorderLayout.CENTER);
		// add(ta, BorderLayout.CENTER);
		add(tf, BorderLayout.SOUTH);

		setTitle("채팅 프로그램 입니다.");
		setSize(400, 500);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void show(String msg) {
		ta.append(msg + "\n");
	}

	// frame의 멤버인 ta나 tf 같은 애들한테 접근하려면 내부 클래스로 작성하는게 용이함.
	class ExitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// tf에서 엔터키를 누르거나 send 버튼을 클릭하는 이벤트가 발생했을 때 이 메소드가 실행되게 합니다.
			System.exit(0);
		}

	}

	class ChattingListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// tf에서 엔터키를 누르거나 send 버튼을 클릭하는 이벤트가 발생했을 때 이 메소드가 실행되게 합니다.
			String input_msg = tf.getText();// textField에 입력된 메세지를 가져와서

			cc.send(input_msg); // textArea에 붙여놓고
			tf.setText(""); // textField에는 새로운 내용을 위해 비워주기
		}

	}
}
