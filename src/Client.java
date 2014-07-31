import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client{
	public static void main(String[] args) throws UnknownHostException, IOException{

		Socket client = new Socket("localhost",9876);

		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		PrintWriter out = new PrintWriter(client.getOutputStream(),true);
		Scanner sc = new Scanner(System.in);
		Scanner strsc = new Scanner(System.in);
		int tmp;
		
//		out.println("INIT");
//		
//		System.out.println(in.readLine());
//		System.out.println(in.readLine());
//		System.out.println(in.readLine());
//		
//		tmp = intsc.nextInt();
//		
//		out.println(tmp);
		
		
		while(true){
			String str = in.readLine();

			if(str.equalsIgnoreCase("menu")){
				while(true){
					str = in.readLine();
					if(str.equals("end")) break;
					System.out.println(str);
				}
			}else{
				System.out.println(str);

			}
			String reply=sc.nextLine();
			out.println(reply);
			out.flush();
		}
	}
}