package webclient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import utils.ClientMockContainer;
import utils.Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class WebClientTest {
    private ClientMockContainer mockContainer;
    private WebClient webClient;

    @BeforeEach
    void init() throws Exception{
        mockContainer = getNewClientMockContainer();
        webClient = getStubbedWebClient(mockContainer);
    }

    @DisplayName("Test that the start method closes all required resources when finished")
    @Test
    void testStart() throws Exception {
        webClient.start();

        Socket mockSocket = mockContainer.getMockSocket();
        PrintWriter mockSocketOut = mockContainer.getMockSocketOut();
        PrintWriter mockUserOut = mockContainer.getMockUserOut();
        BufferedReader mockUserIn = mockContainer.getMockUserIn();
        BufferedReader mockSocketIn = mockContainer.getMockSocketIn();

        verify(mockSocket).close();
        verify(mockSocketOut).close();
        verify(mockSocketIn).close();
        verify(mockUserOut).close();
        verify(mockUserIn).close();
    }

    @DisplayName("Tests if close() method closes all required resources")
    @Test
    void testClose() throws Exception{
        webClient.close();

        Socket mockSocket = mockContainer.getMockSocket();
        PrintWriter mockSocketOut = mockContainer.getMockSocketOut();
        PrintWriter mockUserOut = mockContainer.getMockUserOut();
        BufferedReader mockUserIn = mockContainer.getMockUserIn();
        BufferedReader mockSocketIn = mockContainer.getMockSocketIn();

        verify(mockSocket).close();
        verify(mockSocketOut).close();
        verify(mockUserIn).close();
        verify(mockUserOut).close();
        verify(mockSocketIn).close();
    }

    @DisplayName("Test if writeUserToSocket() prints from user input to socket output")
    @Test
    void testWriteUserToSocket() throws Exception {
        webClient.writeUserToSocket();

        BufferedReader mockUserIn = mockContainer.getMockUserIn();
        PrintWriter mockSocketOut = mockContainer.getMockSocketOut();

        InOrder inOrder = inOrder(mockUserIn, mockSocketOut);
        inOrder.verify(mockUserIn).readLine();
        inOrder.verify(mockSocketOut).println("First Message");
        inOrder.verify(mockUserIn).readLine();
        inOrder.verify(mockSocketOut).println("Second Message");
        inOrder.verify(mockUserIn).readLine();
        inOrder.verify(mockSocketOut).println(Commands.END_CONNECTION);
    }

    @DisplayName("Test if readFromSocket() prints from socket input to user output and returns correct String")
    @Test
    void testWriteSocketToUser() throws Exception {
        String response = webClient.writeSocketToUser();

        String expected = "a.html\n";
        assertEquals(expected, response);

        BufferedReader socketIn = mockContainer.getMockSocketIn();
        PrintWriter userOut = mockContainer.getMockUserOut();
        InOrder inOrder = inOrder(socketIn, userOut);
        inOrder.verify(socketIn).readLine();
        inOrder.verify(userOut).println("a.html");
        inOrder.verify(socketIn).readLine();
        inOrder.verify(userOut).println(Commands.END_CONNECTION);
    }

    @DisplayName("Test if askForHtmlPages() sends correct commands to socket")
    @Test
    void testAskForAvailableHtmlPages() {
        webClient.askForAvailableHtmlPages();

        PrintWriter socketOut = mockContainer.getMockSocketOut();
        InOrder inOrder = inOrder(socketOut);
        inOrder.verify(socketOut).println(Commands.REQUEST_AVAILABLE_HTML_FILES);
        inOrder.verify(socketOut).println(Commands.END_CONNECTION);
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
                .thenReturn(Commands.END_CONNECTION);

        return mockBufferedReader;
    }

    private BufferedReader getMockSocketIn() throws IOException {
        BufferedReader mockBufferedReader = mock(BufferedReader.class);
        when(mockBufferedReader.readLine())
                .thenReturn("a.html")
                .thenReturn(Commands.END_CONNECTION);

        return mockBufferedReader;
    }

    private Socket getMockSocket() {
        return mock(Socket.class);
    }

    private ClientMockContainer getNewClientMockContainer() throws IOException {
        Socket mockSocket = getMockSocket();
        BufferedReader mockUserIn = getMockUserIn();
        PrintWriter mockUserOut = getMockOutStream();
        BufferedReader mockSocketIn = getMockSocketIn();
        PrintWriter mockSocketOut = getMockOutStream();

        return new ClientMockContainer(
                mockSocket,
                mockUserIn,
                mockUserOut,
                mockSocketIn,
                mockSocketOut);
    }

    private WebClient getStubbedWebClient(ClientMockContainer mockContainer) {
        return new WebClient("127.0.0.1", 10008) {
            @Override
            protected Socket getSocket(String address, int portNumber) {
                return mockContainer.getMockSocket();
            }

            @Override
            protected BufferedReader getInStreamUser() {
                return mockContainer.getMockUserIn();
            }

            @Override
            protected PrintWriter getOutStreamUser() {
                return mockContainer.getMockUserOut();
            }

            @Override
            protected BufferedReader getInStreamSocket() {
                return mockContainer.getMockSocketIn();
            }

            @Override
            protected PrintWriter getOutStream() {
                return mockContainer.getMockSocketOut();
            }
        };
    }
}
