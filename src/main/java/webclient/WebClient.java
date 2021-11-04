package webclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WebClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public WebClient(String address, int portNumber) {
        try {
            socket = getSocket(address, portNumber);
            in = getInStream();
            out = getOutStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BufferedReader getInStream() {
        return new BufferedReader(new InputStreamReader(System.in));
    }

    private PrintWriter getOutStream() throws IOException {
        return new PrintWriter(socket.getOutputStream(), true);
    }

    private Socket getSocket(String address, int portNumber) throws IOException {
        return new Socket(address, portNumber);
    }

    public void writeToSocket() throws IOException {
        String line;
        while ((line = in.readLine()) != null) {
            out.println(line);

            if (line.equals("quit"))
                break;
        }
    }

    public void close() throws IOException{
        in.close();
        out.close();
        socket.close();
    }
}