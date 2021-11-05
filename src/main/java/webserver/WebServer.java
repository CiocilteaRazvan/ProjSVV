package webserver;

import java.net.*;
import java.io.*;

public class WebServer{
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	public WebServer(int portNumber) {
		try {
			ServerSocket serverSocket = getServerSocket(portNumber);
			this.socket = getSocket(serverSocket);

			in = getInStream();
			out = getOutStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() throws IOException{
		readFromSocket();
		close();
	}

	private void readFromSocket() throws IOException {
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			System.out.println("Server: " + inputLine);
			out.println(inputLine);

			if (inputLine.trim().equals("end"))
				break;
		}
	}

	private void close() throws IOException{
		out.close();
		in.close();
		socket.close();
	}

	protected Socket getSocket(ServerSocket serverSocket) throws IOException {
		return serverSocket.accept();
	}

	protected PrintWriter getOutStream() throws IOException {
		return new PrintWriter(socket.getOutputStream(), true);
	}

	protected BufferedReader getInStream() throws IOException {
		return new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	protected ServerSocket getServerSocket(int portNumber) throws IOException {
		return new ServerSocket(portNumber);
	}
}