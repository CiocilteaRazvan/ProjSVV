package webclient;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import utils.Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class WebClientTest {
    private WebClient webClient;

    @DisplayName("Test that the web client reads two lines then the 'end' command and prints to output then closes itself")
    @Test
    void testWebClientReadWriteClose() throws Exception {
        Socket mockSocket = getMockSocket();
        BufferedReader mockUserBufferedReader = getMockUserBufferedReader();
        BufferedReader mockSocketBufferedReader = getMockSocketBufferedReader();
        PrintWriter mockPrintWriter = getMockPrintWriter();

        webClient = new WebClient("127.0.0.1", 10008) {
            @Override
            protected Socket getSocket(String address, int portNumber) throws IOException {
                return mockSocket;
            }

            @Override
            protected BufferedReader getInStreamUser() {
                return mockUserBufferedReader;
            }

            @Override
            protected BufferedReader getInStreamSocket() {
                return mockSocketBufferedReader;
            }

            @Override
            protected PrintWriter getOutStream() throws IOException {
                return mockPrintWriter;
            }
        };

        webClient.start();

        InOrder inOrder = inOrder(mockSocket,
                mockUserBufferedReader,
                mockSocketBufferedReader,
                mockPrintWriter);

        inOrder.verify(mockPrintWriter).println(Commands.GET_HTML_FILES);
        inOrder.verify(mockPrintWriter).println(Commands.END_MESSAGE);
        inOrder.verify(mockSocketBufferedReader, times(2)).readLine();
        inOrder.verify(mockUserBufferedReader).readLine();
        inOrder.verify(mockPrintWriter).println("First Message");
        inOrder.verify(mockUserBufferedReader).readLine();
        inOrder.verify(mockPrintWriter).println("Second Message");
        inOrder.verify(mockUserBufferedReader).readLine();
        inOrder.verify(mockPrintWriter).println(Commands.END_MESSAGE);
        inOrder.verify(mockUserBufferedReader).close();
        inOrder.verify(mockSocketBufferedReader).close();
        inOrder.verify(mockPrintWriter).close();
        inOrder.verify(mockSocket).close();
    }

    private PrintWriter getMockPrintWriter() {
        return mock(PrintWriter.class);
    }

    private BufferedReader getMockUserBufferedReader() throws IOException {
        BufferedReader mockBufferedReader = mock(BufferedReader.class);
        when(mockBufferedReader.readLine())
                .thenReturn("First Message")
                .thenReturn("Second Message")
                .thenReturn(Commands.END_MESSAGE);

        return mockBufferedReader;
    }

    private BufferedReader getMockSocketBufferedReader() throws IOException {
        BufferedReader mockBufferedReader = mock(BufferedReader.class);
        when(mockBufferedReader.readLine())
                .thenReturn("src/main/java/resources/TestSite/a.html")
                .thenReturn(Commands.END_MESSAGE);

        return mockBufferedReader;
    }

    private Socket getMockSocket() {
        return mock(Socket.class);
    }
}
