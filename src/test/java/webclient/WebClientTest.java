package webclient;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import utils.ClientMocksContainer;
import utils.Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class WebClientTest {
    private WebClient webClient;

    @DisplayName("Test that the web client reads two lines then the 'end' command and prints to output then closes itself")
    @Test
    void testWebClientReadWriteClose() throws Exception {
        ClientMocksContainer mocksContainer = getNewClientMocksContainer();

        webClient = getStubbedWebClient(mocksContainer);

        webClient.start();

        Socket mockSocket = mocksContainer.getMockSocket();
        PrintWriter mockPrintWriter = mocksContainer.getMockPrintWriter();
        BufferedReader mockUserBufferedReader = mocksContainer.getMockUserBufferedReader();
        BufferedReader mockSocketBufferedReader = mocksContainer.getMockSocketBufferedReader();

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

    @DisplayName("Tests if close() method closes all required resources")
    @Test
    void testClose() throws Exception{
        ClientMocksContainer mocksContainer = getNewClientMocksContainer();

        webClient = getStubbedWebClient(mocksContainer);
        webClient.close();

        Socket mockSocket = mocksContainer.getMockSocket();
        PrintWriter mockPrintWriter = mocksContainer.getMockPrintWriter();
        BufferedReader mockUserBufferedReader = mocksContainer.getMockUserBufferedReader();
        BufferedReader mockSocketBufferedReader = mocksContainer.getMockSocketBufferedReader();

        verify(mockSocket).close();
        verify(mockPrintWriter).close();
        verify(mockUserBufferedReader).close();
        verify(mockSocketBufferedReader).close();
    }


    //=============================== UTILS ===================================//


    private ClientMocksContainer getNewClientMocksContainer() throws IOException {
        Socket mockSocket = getMockSocket();
        BufferedReader mockUserBufferedReader = getMockUserBufferedReader();
        BufferedReader mockSocketBufferedReader = getMockSocketBufferedReader();
        PrintWriter mockPrintWriter = getMockPrintWriter();

        return new ClientMocksContainer(mockSocket,
                mockUserBufferedReader,
                mockSocketBufferedReader,
                mockPrintWriter);
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

    private WebClient getStubbedWebClient(ClientMocksContainer mocksContainer) {
        return new WebClient("127.0.0.1", 10008) {
            @Override
            protected Socket getSocket(String address, int portNumber) throws IOException {
                return mocksContainer.getMockSocket();
            }

            @Override
            protected BufferedReader getInStreamUser() {
                return mocksContainer.getMockUserBufferedReader();
            }

            @Override
            protected BufferedReader getInStreamSocket() {
                return mocksContainer.getMockSocketBufferedReader();
            }

            @Override
            protected PrintWriter getOutStream() throws IOException {
                return mocksContainer.getMockPrintWriter();
            }
        };
    }
}
