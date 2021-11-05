package webserver;

import utils.Commands;

import java.net.*;
import java.io.*;

public class WebServer{
	private Socket socket;
	private BufferedReader socketIn;
	private PrintWriter socketOut;

	public WebServer(int portNumber) {
		try {
			ServerSocket serverSocket = getServerSocket(portNumber);
			this.socket = getSocket(serverSocket);

			socketIn = getInStream();
			socketOut = getOutStream();
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
		while ((inputLine = socketIn.readLine()) != null) {
			System.out.println("Server: " + inputLine);
			socketOut.println(inputLine);

			if (inputLine.trim().equals(Commands.END_MESSAGE))
				break;
		}
	}

	private void close() throws IOException{
		socketOut.close();
		socketIn.close();
		socket.close();
	}

	protected Socket getSocket(ServerSocket serverSocket) throws IOException {
		return serverSocket.accept();
	}

	protected BufferedReader getInStream() throws IOException {
		return new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	protected PrintWriter getOutStream() throws IOException {
		return new PrintWriter(socket.getOutputStream(), true);
	}

	protected ServerSocket getServerSocket(int portNumber) throws IOException {
		return new ServerSocket(portNumber);
	}
}