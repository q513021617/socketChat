package socketChat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
public class ChatServer {

	// 声明服务器端套接字ServerSocket
		ServerSocket serverSocket;
		// 输入流列表集合
		ArrayList<BufferedReader> bReaders = new ArrayList<BufferedReader>();
		// 输出流列表集合
		ArrayList<PrintWriter> pWriters = new ArrayList<PrintWriter>();
		// 聊天信息链表集合
		LinkedList<String> msgList = new LinkedList<String>();

		public ChatServer() {
			try {
				// 创建服务器端套接字ServerSocket，在28888端口监听
				serverSocket = new ServerSocket(28888);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 创建接收客户端Socket的线程实例，并启动
			new AcceptSocketThread().start();
			// 创建给客户端发送信息的线程实例，并启动
			new SendMsgToClient().start();
			System.out.println("服务器已启动...");
		}

		// 接收客户端Socket套接字线程
		class AcceptSocketThread extends Thread {
			public void run() {
				while (this.isAlive()) {
					try {
						// 接收一个客户端Socket对象
						Socket socket = serverSocket.accept();
						// 建立该客户端的通信管道
						if (socket != null) {
							// 获取Socket对象的输入流
							BufferedReader bReader = new BufferedReader(
									new InputStreamReader(socket.getInputStream()));
							// 将输入流添加到输入流列表集合中
							bReaders.add(bReader);
							// 开启一个线程接收该客户端的聊天信息
							new GetMsgFromClient(bReader).start();

							// 获取Socket对象的输出流，并添加到输入出流列表集合中
							pWriters.add(new PrintWriter(socket.getOutputStream()));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		}

		// 接收客户端的聊天信息的线程
		class GetMsgFromClient extends Thread {
			BufferedReader bReader;

			public GetMsgFromClient(BufferedReader bReader) {
				this.bReader = bReader;
			}

			public void run() {
				while (this.isAlive()) {
					try {
						// 从输入流中读一行信息
						String strMsg = bReader.readLine();
						if (strMsg != null) {
							
							msgList.addFirst(strMsg);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		// 给所有客户发送聊天信息的线程
		class SendMsgToClient extends Thread {
			public void run() {
				while (this.isAlive()) {
					try {
						// 如果信息链表集合不空（还有聊天信息未发送）
						if (!msgList.isEmpty()) {
							// 取信息链表集合中的最后一条,并移除
							String msg = msgList.removeLast();
							// 对输出流列表集合进行遍历，循环发送信息给所有客户端
							for (int i = 0; i < pWriters.size(); i++) {
								pWriters.get(i).println(msg);
								pWriters.get(i).flush();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChatServer();
	}

}
