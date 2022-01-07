package webserver;

import utils.Commands;
import utils.Constants.*;

import java.net.*;
import java.io.*;

public class WebServer{
	private ServerSocket serverSocket;
	private Socket socket;
	private BufferedReader socketIn;
	private PrintWriter socketOut;
	private PrintWriter logOut;
	private WEBSERVER_STATUS status = WEBSERVER_STATUS.STOPPED;

	public WebServer(int portNumber) {
		try {
			this.serverSocket = getServerSocket(portNumber);
			this.socket = getSocket(serverSocket);
			this.socketIn = getInStream();
			this.socketOut = getOutStreamSocket();
			this.logOut = getOutStreamLog();

			status = WEBSERVER_STATUS.RUNNING;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public WebServer() {

	}

	public void start(){
		try {
			readFromSocket();
			close();

		} catch (IOException e) {
			System.out.println("Exception caught at start() in WebServer.java");
			e.printStackTrace();
		}
	}

	public boolean open(int portNumber) {
		try {
			System.out.println("Opening server 1");
			this.serverSocket = getServerSocket(portNumber);
			System.out.println("Opening server 2");
			this.socket = getSocket(serverSocket);
			System.out.println("Opening server 3");
			this.socketIn = getInStream();
			System.out.println("Opening server 4");
			this.socketOut = getOutStreamSocket();
			System.out.println("Opening server 5");
			this.logOut = getOutStreamLog();
			System.out.println("Opening server 6");

			status = WEBSERVER_STATUS.RUNNING;
			System.out.println("Opened server on port " + portNumber);
			return true;

		} catch (Exception e) {
			System.out.println("Exception caught at open() in WebServer.java");
			return false;
		}
	}

	public void close() {
		try {
			System.out.println("Closing the socket");
			socketOut.close();
			socketIn.close();
			logOut.flush();
			socket.close();
			serverSocket.close();
			System.out.println("Closed the socket");

			status = WEBSERVER_STATUS.STOPPED;
		} catch (IOException e) {
			System.out.println("Exception caught at close() in WebServer.java");
		}
	}

	protected void readFromSocket() throws IOException {
		String inputLine;
		while ((inputLine = socketIn.readLine()) != null && status == WEBSERVER_STATUS.RUNNING) {
			boolean endConnection = false;
			logOut.println("Input: " + inputLine);

			switch(inputLine){
				case Commands.REQUEST_AVAILABLE_HTML_FILES:
					socketOut.println(getAvailableHtmlFiles());
					break;

				case Commands.END_CONNECTION:
					endConnection = true;
					break;

				default:
					socketOut.println(inputLine);
			}

			if(endConnection)
				break;
		}
	}

	private String getAvailableHtmlFiles() {
		return "a.html;\nb.html;\n";
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
		return new PrintWriter(System.out, true);
	}

	protected ServerSocket getServerSocket(int portNumber) throws IOException {
		return new ServerSocket(portNumber);
	}
}