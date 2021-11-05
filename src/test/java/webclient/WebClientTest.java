package webclient;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class WebClientTest {
    private WebClient webClient;

    @Test
    void testWebClientReadWriteClose() throws Exception {
        Socket mockSocket = getMockSocket();
        BufferedReader mockBufferedReader = getMockBufferedReader();
        PrintWriter mockPrintWriter = getMockPrintWriter();

        webClient = new WebClient("127.0.0.1", 10008) {
            @Override
            protected Socket getSocket(String address, int portNumber) throws IOException {
                return mockSocket;
            }

            @Override
            protected BufferedReader getInStream() {
                return mockBufferedReader;
            }

            @Override
            protected PrintWriter getOutStream() throws IOException {
                return mockPrintWriter;
            }
        };

        webClient.start();

        verify(mockBufferedReader, times(3)).readLine();
        verify(mockPrintWriter).println("First Message");
        verify(mockPrintWriter).println("Second Message");
        verify(mockPrintWriter).println("end");
        verify(mockSocket).close();
        verify(mockBufferedReader).close();
        verify(mockPrintWriter).close();
    }

    private PrintWriter getMockPrintWriter() {
        return mock(PrintWriter.class);
    }

    private BufferedReader getMockBufferedReader() throws IOException{
        BufferedReader mockBufferedReader = mock(BufferedReader.class);
        when(mockBufferedReader.readLine())
                .thenReturn("First Message")
                .thenReturn("Second Message")
                .thenReturn("end");

        return mockBufferedReader;
    }

    private Socket getMockSocket() {
        return mock(Socket.class);
    }
}
