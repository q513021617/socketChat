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
		

		// 设置文本域只读
		
		
		try {
			// 创建一个套接字
			socket = new Socket("127.0.0.1", 28888);
			// 创建一个往套接字中写数据的管道，即输出流，给服务器发送信息
			pWriter = new PrintWriter(socket.getOutputStream());
			// 创建一个从套接字读数据的管道，即输入流，读服务器的返回信息
			bReader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 注册监听
		
		new sendToServer().start();
		// 启动线程
		new GetMsgFromServer().start();
	}
	
	public static void sendMsg(String username,String strMsg) {
		// 获取用户输入的文本
		
		
		if (!strMsg.equals("")) {
			// 通过输出流将数据发送给服务器
			pWriter.println(username+"："+strMsg);
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
	// 接收服务器的返回信息的线程
		class GetMsgFromServer extends Thread {
			public void run() {
				while (this.isAlive()) {
					try {
						String strMsg = bReader.readLine();
						if (strMsg != null) {
							// 在文本域中显示聊天信息
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
