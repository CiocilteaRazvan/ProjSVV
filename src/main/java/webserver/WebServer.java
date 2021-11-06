package webserver;

import utils.Commands;

import java.net.*;
import java.io.*;

public class WebServer{
	private ServerSocket serverSocket;
	private Socket socket;
	private BufferedReader socketIn;
	private PrintWriter socketOut;
	private PrintWriter logOut;

	public WebServer(int portNumber) {
		try {
			this.serverSocket = getServerSocket(portNumber);
			this.socket = getSocket(serverSocket);
			this.socketIn = getInStream();
			this.socketOut = getOutStreamSocket();
			this.logOut = getOutStreamLog();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() throws IOException{
		readFromSocket();
		close();
	}

	protected void readFromSocket() throws IOException {
		String inputLine;
		while ((inputLine = socketIn.readLine()) != null) {
			logOut.println("Input: " + inputLine);
			socketOut.println(inputLine);

			if (inputLine.trim().equals(Commands.END_CONNECTION))
				break;
		}
	}

	protected void close() throws IOException{
		socketOut.close();
		socketIn.close();
		logOut.close();
		socket.close();
		serverSocket.close();
	}

	protected Socket getSocket(ServerSocket serverSocket) throws IOException {
		return serverSocket.accept();
	}

	protected BufferedReader getInStream() throws IOException {
		return new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	protected PrintWriter getOutStreamSocket() throws IOException {
		return new PrintWriter(socket.getOutputStream(), true);
	}

	protected PrintWriter getOutStreamLog() {
		return new PrintWriter(System.out);
	}

	protected ServerSocket getServerSocket(int portNumber) throws IOException {
		return new ServerSocket(portNumber);
	}
}