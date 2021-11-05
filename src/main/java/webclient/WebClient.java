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
        askForAvailableHtmlPages();
        writeSocketToUser();
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

    protected String writeSocketToUser() throws IOException {
        String response = "";
        String line;
        while ((line = socketIn.readLine()) != null) {
            userOut.println(line);
            if (line.equals(Commands.END_MESSAGE))
                break;
            response += line + "\n";
        }

        return response;
    }

    protected void askForAvailableHtmlPages() {
        socketOut.println(Commands.GET_HTML_FILES);
        socketOut.println(Commands.END_MESSAGE);
    }

    protected void close() throws IOException{
        userIn.close();
        socketIn.close();
        socketOut.close();
        socket.close();
    }

    protected BufferedReader getInStreamUser() {
        return new BufferedReader(new InputStreamReader(System.in));
    }

    protected PrintWriter getOutStreamUser() {
        return new PrintWriter(System.out);
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