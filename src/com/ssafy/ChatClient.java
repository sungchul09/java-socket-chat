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
		System.out.print("�������̵� �Է����ּ���:");
		String name = sc.next();
		ChatClient cct = new ChatClient("192.168.29.198", 5432, name);

	}

	public void createGUI() {
		ta = new JTextArea(40, 50);
		tf = new JTextField(50);
		bSend = new JButton("SEND");

		bExit = new JButton("EXIT");
		panel = new JPanel();
		// textarea ���� Ű���� �Է� ����
		ta.setEditable(false);
		ta.setBackground(new Color(255, 0, 0, 50));

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(bSend);
		panel.add(bExit);

		// tf�� send ��ư�� �츮�� �ۼ��� �̺�Ʈ ó�� ��ü �����ϱ�
		tf.addActionListener(new ChattingListener());
		bSend.addActionListener(new ChattingListener());
		bExit.addActionListener(new ExitListener());
		ta.setEditable(false);

		ta.setBackground(Color.pink);
		// �гΰ� ta�� tf�� �����ӿ� �߰�
		add(panel, BorderLayout.LINE_END);
		add(new JScrollPane(ta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),
				BorderLayout.CENTER);
		// add(ta, BorderLayout.CENTER);
		add(tf, BorderLayout.SOUTH);

		setTitle("ä�� ���α׷� �Դϴ�.");
		setSize(400, 500);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void show(String msg) {
		ta.append(msg + "\n");
	}

	// frame�� ����� ta�� tf ���� �ֵ����� �����Ϸ��� ���� Ŭ������ �ۼ��ϴ°� ������.
	class ExitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// tf���� ����Ű�� �����ų� send ��ư�� Ŭ���ϴ� �̺�Ʈ�� �߻����� �� �� �޼ҵ尡 ����ǰ� �մϴ�.
			System.exit(0);
		}

	}

	class ChattingListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// tf���� ����Ű�� �����ų� send ��ư�� Ŭ���ϴ� �̺�Ʈ�� �߻����� �� �� �޼ҵ尡 ����ǰ� �մϴ�.
			String input_msg = tf.getText();// textField�� �Էµ� �޼����� �����ͼ�

			cc.send(input_msg); // textArea�� �ٿ�����
			tf.setText(""); // textField���� ���ο� ������ ���� ����ֱ�
		}

	}
}
