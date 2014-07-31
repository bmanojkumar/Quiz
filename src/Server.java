import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;

public class Server{
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException{
		ServerSocket server = new ServerSocket(9876);

		while(true)
		{
			Socket socket = server.accept();
			MyThread k = new MyThread(socket);
			k.start();
		}
	}
}
class MyThread extends Thread{

	Socket socket;

	MyThread(Socket sock){
		socket = sock;
	}

	public void run(){

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			Scanner sc = new Scanner(System.in);

			System.out.println("client connected to server");

			HomeMenu hm = new HomeMenu(socket,in,out);	
			hm.menu();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}


}
