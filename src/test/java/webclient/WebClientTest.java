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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        PrintWriter mockSocketOut = mocksContainer.getMockSocketOut();
        BufferedReader mockUserIn = mocksContainer.getMockUserIn();
        BufferedReader MockSocketIn = mocksContainer.getMockSocketIn();

        InOrder inOrder = inOrder(mockSocket,
                mockUserIn,
                MockSocketIn,
                mockSocketOut);

        inOrder.verify(mockSocketOut).println(Commands.GET_HTML_FILES);
        inOrder.verify(mockSocketOut).println(Commands.END_MESSAGE);
        inOrder.verify(MockSocketIn, times(2)).readLine();
    }

    @DisplayName("Tests if close() method closes all required resources")
    @Test
    void testClose() throws Exception{
        ClientMocksContainer mocksContainer = getNewClientMocksContainer();

        webClient = getStubbedWebClient(mocksContainer);
        webClient.close();

        Socket mockSocket = mocksContainer.getMockSocket();
        PrintWriter mockSocketOut = mocksContainer.getMockSocketOut();
        BufferedReader mockUserIn = mocksContainer.getMockUserIn();
        BufferedReader mockSocketIn = mocksContainer.getMockSocketIn();

        verify(mockSocket).close();
        verify(mockSocketOut).close();
        verify(mockUserIn).close();
        verify(mockSocketIn).close();
    }

    @DisplayName("Test if writeUserToSocket() prints from user input to socket output")
    @Test
    void testWriteUserToSocket() throws Exception {
        ClientMocksContainer mocksContainer = getNewClientMocksContainer();

        webClient = getStubbedWebClient(mocksContainer);
        webClient.writeUserToSocket();

        BufferedReader mockUserIn = mocksContainer.getMockUserIn();
        PrintWriter mockSocketOut = mocksContainer.getMockSocketOut();

        InOrder inOrder = inOrder(mockUserIn, mockSocketOut);
        inOrder.verify(mockUserIn).readLine();
        inOrder.verify(mockSocketOut).println("First Message");
        inOrder.verify(mockUserIn).readLine();
        inOrder.verify(mockSocketOut).println("Second Message");
        inOrder.verify(mockUserIn).readLine();
        inOrder.verify(mockSocketOut).println(Commands.END_MESSAGE);
    }

    @DisplayName("Test if readFromSocket() prints from socket input to user output and returns correct String")
    @Test
    void testWriteSocketToUser() throws Exception {
        ClientMocksContainer mocksContainer = getNewClientMocksContainer();

        webClient = getStubbedWebClient(mocksContainer);
        String response = webClient.writeSocketToUser();

        String expected = "a.html\n";
        assertEquals(expected, response);

        //TODO change variable names sIn and uOut
        BufferedReader sIn = mocksContainer.getMockSocketIn();
        PrintWriter uOut = mocksContainer.getMockUserOut();
        InOrder inOrder = inOrder(sIn, uOut);
        inOrder.verify(sIn).readLine();
        inOrder.verify(uOut).println("a.html");
        inOrder.verify(sIn).readLine();
        inOrder.verify(uOut).println(Commands.END_MESSAGE);
    }

    @DisplayName("Test if askForHtmlPages() sends correct commands to socket")
    @Test
    void testAskForAvailableHtmlPages() throws Exception{
        ClientMocksContainer mocksContainer = getNewClientMocksContainer();

        webClient = getStubbedWebClient(mocksContainer);
        webClient.askForAvailableHtmlPages();

        PrintWriter sOut = mocksContainer.getMockSocketOut();
        InOrder inOrder = inOrder(sOut);
        inOrder.verify(sOut).println(Commands.GET_HTML_FILES);
        inOrder.verify(sOut).println(Commands.END_MESSAGE);
    }



    //==================================== UTILS ====================================//



    private PrintWriter getMockOutStream() {
        return mock(PrintWriter.class);
    }

    private BufferedReader getMockUserIn() throws IOException {
        BufferedReader mockBufferedReader = mock(BufferedReader.class);
        when(mockBufferedReader.readLine())
                .thenReturn("First Message")
                .thenReturn("Second Message")
                .thenReturn(Commands.END_MESSAGE);

        return mockBufferedReader;
    }

    private BufferedReader getMockSocketIn() throws IOException {
        BufferedReader mockBufferedReader = mock(BufferedReader.class);
        when(mockBufferedReader.readLine())
                .thenReturn("a.html")
                .thenReturn(Commands.END_MESSAGE);

        return mockBufferedReader;
    }

    private Socket getMockSocket() {
        return mock(Socket.class);
    }

    private ClientMocksContainer getNewClientMocksContainer() throws IOException {
        Socket mockSocket = getMockSocket();
        BufferedReader mockUserIn = getMockUserIn();
        PrintWriter mockUserOut = getMockOutStream();
        BufferedReader mockSocketIn = getMockSocketIn();
        PrintWriter mockSocketOut = getMockOutStream();

        return new ClientMocksContainer(
                mockSocket,
                mockUserIn,
                mockUserOut,
                mockSocketIn,
                mockSocketOut);
    }

    private WebClient getStubbedWebClient(ClientMocksContainer mocksContainer) {
        return new WebClient("127.0.0.1", 10008) {
            @Override
            protected Socket getSocket(String address, int portNumber) {
                return mocksContainer.getMockSocket();
            }

            @Override
            protected BufferedReader getInStreamUser() {
                return mocksContainer.getMockUserIn();
            }

            @Override
            protected PrintWriter getOutStreamUser() {
                return mocksContainer.getMockUserOut();
            }

            @Override
            protected BufferedReader getInStreamSocket() {
                return mocksContainer.getMockSocketIn();
            }

            @Override
            protected PrintWriter getOutStream() {
                return mocksContainer.getMockSocketOut();
            }
        };
    }
}
