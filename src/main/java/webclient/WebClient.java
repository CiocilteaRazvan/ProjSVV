package webclient;

import utils.Commands;

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

    public WebClient(String address, int portNumber) {
        try {
            socket = getSocket(address, portNumber);
            userIn = getInStreamUser();
            userOut = getOutStreamUser();
            socketIn = getInStreamSocket();
            socketOut = getOutStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        readFromUser();
        close();
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

    protected void askForAvailableHtmlPages() throws IOException{
        socketOut.println(Commands.REQUEST_AVAILABLE_HTML_FILES);
        userOut.println(readFromSocket());
    }

    protected void disconnect() {
        socketOut.println(Commands.END_CONNECTION);
    }

    protected String readFromSocket() throws IOException {
        String response = "";
        String line;
        if ((line = socketIn.readLine()) != null) {
            response += line;
        }

        return response;
    }

    protected void close() throws IOException{
        userIn.close();
        userOut.close();
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