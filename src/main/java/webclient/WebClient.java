package webclient;

import utils.Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WebClient {
    private Socket socket;
    private PrintWriter socketOut;
    private BufferedReader socketIn;
    private BufferedReader userIn;

    private String response;

    public WebClient(String address, int portNumber) {
        try {
            socket = getSocket(address, portNumber);
            userIn = getInStreamUser();
            socketIn = getInStreamSocket();
            socketOut = getOutStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        askForHtmlPages();
        readFromSocket();
        writeUserToSocket();
        close();
    }

    protected void writeUserToSocket() throws IOException {
        String line;
        while ((line = userIn.readLine()) != null) {
            socketOut.println(line);

            if (line.equals(Commands.END_MESSAGE))
                break;
        }
    }

    protected void readFromSocket() throws IOException {
        response = "";
        String line;
        while ((line = socketIn.readLine()) != null) {
            response += line + "\n";

            if (line.equals(Commands.END_MESSAGE))
                break;
        }
    }

    protected void askForHtmlPages() {
        socketOut.println(Commands.GET_HTML_FILES);
        socketOut.println(Commands.END_MESSAGE);
    }

    protected void close() throws IOException{
        userIn.close();
        socketIn.close();
        socketOut.close();
        socket.close();
    }

    public String getResponse() {
        return response;
    }

    protected BufferedReader getInStreamUser() {
        return new BufferedReader(new InputStreamReader(System.in));
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