package socketChat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;



public class ChatClient {

	Socket socket;
	static PrintWriter pWriter;
	BufferedReader bReader;

	
	
	public ChatClient() {
		

		// �����ı���ֻ��
		
		
		try {
			// ����һ���׽���
			socket = new Socket("127.0.0.1", 28888);
			// ����һ�����׽�����д���ݵĹܵ��������������������������Ϣ
			pWriter = new PrintWriter(socket.getOutputStream());
			// ����һ�����׽��ֶ����ݵĹܵ����������������������ķ�����Ϣ
			bReader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ע�����
		
		new sendToServer().start();
		// �����߳�
		new GetMsgFromServer().start();
	}
	
	public static void sendMsg(String username,String strMsg) {
		// ��ȡ�û�������ı�
		
		
		if (!strMsg.equals("")) {
			// ͨ������������ݷ��͸�������
			pWriter.println(username+"��"+strMsg);
			pWriter.flush();
			
		}
	}
	class sendToServer extends Thread {
		Scanner sc=new Scanner(System.in);
		public void run() {
			while (this.isAlive()) {
				try {
					String msg=sc.nextLine();
					String username=sc.nextLine();
					sendMsg(username,msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	// ���շ������ķ�����Ϣ���߳�
		class GetMsgFromServer extends Thread {
			public void run() {
				while (this.isAlive()) {
					try {
						String strMsg = bReader.readLine();
						if (strMsg != null) {
							// ���ı�������ʾ������Ϣ
							System.out.println(strMsg);
						}
						Thread.sleep(50);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChatClient();
	}

}
