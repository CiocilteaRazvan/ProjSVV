package webclient;

import utils.Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WebClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader inSocket;
    private BufferedReader inUser;

    public WebClient(String address, int portNumber) {
        try {
            socket = getSocket(address, portNumber);
            inUser = getInStreamUser();
            inSocket = getInStreamSocket();
            out = getOutStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        writeToSocket();
        close();
    }

    private void writeToSocket() throws IOException {
        String line;
        while ((line = inUser.readLine()) != null) {
            out.println(line);

            if (line.equals(Commands.END_MESSAGE))
                break;
        }
    }

    private String readFromSocket() throws IOException {
        String response = "";
        String line;
        while ((line = inSocket.readLine()) != null) {
            response += line + "\n";

            if (line.equals(Commands.END_MESSAGE))
                break;
        }

        return response;
    }

    private void askForHtmlPages() {
        out.println("get html options");
    }

    private void close() throws IOException{
        inUser.close();
        out.close();
        socket.close();
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