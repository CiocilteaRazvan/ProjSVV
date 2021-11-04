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
            System.out.println("Trying to connect");
            socket = new Socket(address, portNumber);
            System.out.println("Connection Success");
            in = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(socket.getOutputStream(), true);

            writeToSocket();

            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeToSocket() throws IOException {
        String line;
        while ((line = in.readLine()) != null) {
            out.println(line);

            if(line.equals("quit"))
                break;
        }
    }

    public static void main(String args[]) {
        WebClient client = new WebClient("127.0.0.1", 10008);
    }
}
