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

	public void getCommand() {
		try {
			while (socket.isConnected()) {
				respondToCommand(socketIn.readLine());
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error when reading from socket");
		}
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					while (socket.isConnected()) {
//						respondToCommand(socketIn.readLine());
//					}
//
//				} catch (IOException e) {
//					e.printStackTrace();
//					System.out.println("Error when reading from socket");
//				}
//			}
//		}).start();
	}

	private void respondToCommand(String command) {
		switch(command){
			case Commands.REQUEST_AVAILABLE_HTML_FILES:
				socketOut.println(getAvailableHtmlFiles());
				break;

			default:
				socketOut.println(command + " is not a recognized command");
		}
	}

	public void open(int portNumber) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					serverSocket = getServerSocket(portNumber);
					socket = getSocket(serverSocket);
					socketIn = getInStream();
					socketOut = getOutStreamSocket();
					logOut = getOutStreamLog();

					status = WEBSERVER_STATUS.RUNNING;
					System.out.println("Opened server on port " + portNumber);

					getCommand();

				} catch (Exception e) {
					System.out.println("Exception caught at open() in WebServer.java");
				}
			}
		}).start();
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