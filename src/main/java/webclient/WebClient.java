package webclient;

import utils.Commands;
import utils.Constants.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WebClient {
    private Socket socket;
    private BufferedReader socketIn;
    private PrintWriter socketOut;
    private BufferedReader userIn;
    private PrintWriter userOut;

    public WebClient(String address, int portNumber){
        setUserIO();
        connectToSocket(address, portNumber);
    }

    public void connectToSocket(String address, int portNumber) {
        try {
            System.out.println("Client connecting to socket");
            socket = getSocket(address, portNumber);
            setSocketIO();
            System.out.println("Client connected to socket");
        } catch (IOException e) {
            System.out.println("Client could not connect to socket");
            e.printStackTrace();
        }
    }

    protected void setSocketIO() throws IOException {
        socketIn = getInStreamSocket();
        socketOut = getOutStream();
    }

    protected void setUserIO() {
        userIn = getInStreamUser();
        userOut = getOutStreamUser();
    }

    public void start() {
        try {
            readFromUser();
            close();
        } catch (IOException e) {
            System.out.println("IO error in client");
        }
    }

    protected void readFromUser() throws IOException {
        boolean endConnection = false;
        String inputLine;
        while ((inputLine = userIn.readLine()) != null) {

            switch(inputLine) {
                case Commands.REQUEST_AVAILABLE_HTML_FILES:
                    askForAvailableHtmlPages();
                    break;
                case Commands.END_CONNECTION:
                    disconnect();
                    endConnection = true;
                    break;
                default:
                    userOut.println("Command not known");
            }

            if (endConnection)
                break;
        }
    }

    public void sendCommand(String command) throws IOException{
        userOut.println("Command given: " + command);
        socketOut.println(command);
        socketOut.flush();
    }

    public String readFromSocket() throws IOException {
        String response = "";
        String line;
        System.out.println("Starting the read");
        while (responseNotEnded(line = socketIn.readLine())) {
            System.out.println("Reading line :" + line + ":");
            response += line + "\n";
        }

        return response;
    }

    private boolean responseNotEnded(String response) {
        System.out.println("Checking if response ended");
        if(!response.isEmpty()) {
            return true;
        }
        return false;
    }

    protected void askForAvailableHtmlPages() throws IOException{
        socketOut.println(Commands.REQUEST_AVAILABLE_HTML_FILES);
        System.out.println("Reading from socket");
        userOut.println(readFromSocket());
    }

    protected void disconnect() {
        socketOut.println(Commands.END_CONNECTION);
    }

    protected void close() throws IOException{
        userOut.flush();
        socketIn.close();
        socketOut.close();
        socket.close();
    }

    protected BufferedReader getInStreamUser() {
        return new BufferedReader(new InputStreamReader(System.in));
    }

    protected PrintWriter getOutStreamUser() {
        return new PrintWriter(System.out, true);
    }

    protected BufferedReader getInStreamSocket() throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    protected PrintWriter getOutStream() throws IOException {
        return new PrintWriter(socket.getOutputStream(), true);
    }

    protected Socket getSocket(String address, int portNumber) throws IOException {
        return new Socket(address, portNumber);
    }
}