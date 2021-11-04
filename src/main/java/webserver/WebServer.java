package webserver;

import java.net.*;
import java.io.*;

public class WebServer{
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;

	private WebServer(int portNumber) {
		try {
			ServerSocket serverSocket = new ServerSocket(10008);
			System.out.println("Connection Socket Created");
			System.out.println("Waiting for Connection");
			this.socket = serverSocket.accept();
			System.out.println("Connection accepted!");

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			readFromSocket();
			out.close();
			in.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readFromSocket() throws IOException {
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			System.out.println("Server: " + inputLine);
			out.println(inputLine);

			if (inputLine.trim().equals("quit"))
				break;
		}
	}

	public static void main(String args[]) {
		WebServer webServer = new WebServer(10008);
	}
}