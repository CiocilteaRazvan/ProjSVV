package webclient;

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

    @DisplayName("Test that the start method closes all required resources when finished")
    @Test
    void testStart() throws Exception {
        webClient = getClientWithInputAndResponse(disconnect(), noResponse());
        webClient.start();

        Socket mockSocket = mockContainer.getMockSocket();
        PrintWriter mockSocketOut = mockContainer.getMockSocketOut();
        PrintWriter mockUserOut = mockContainer.getMockUserOut();
        BufferedReader mockSocketIn = mockContainer.getMockSocketIn();

        verify(mockSocket).close();
        verify(mockSocketOut).close();
        verify(mockSocketIn).close();
        verify(mockUserOut).flush();
    }

    @DisplayName("Tests if close() method closes all required resources")
    @Test
    void testClose() throws Exception{
        webClient = getClientWithInputAndResponse(disconnect(), noResponse());
        webClient.close();

        Socket mockSocket = mockContainer.getMockSocket();
        PrintWriter mockSocketOut = mockContainer.getMockSocketOut();
        PrintWriter mockUserOut = mockContainer.getMockUserOut();
        BufferedReader mockSocketIn = mockContainer.getMockSocketIn();

        verify(mockSocket).close();
        verify(mockSocketOut).close();
        verify(mockUserOut).flush();
        verify(mockSocketIn).close();
    }

    @DisplayName("Test if writeUserToSocket() prints 'Command not known' when unknown command in inputted")
    @Test
    void testReadFromUserRandomMessages() throws Exception {
        webClient = getClientWithInputAndResponse(twoRandomMessages(), noResponse());
        webClient.readFromUser();

        PrintWriter mockUserOut = mockContainer.getMockUserOut();

        verify(mockUserOut, times(2)).println("Command not known");
    }

    @DisplayName("Test if correct response is printed to userOut when userIn is REQUEST_AVAILABLE_HTML_FILES command")
    @Test
    void testReadFromUserGetAvailableHtmlFiles() throws Exception {
        webClient = getClientWithInputAndResponse(getAvailableHtmlFiles(), availableHtmlFiles());
        webClient.readFromUser();

        PrintWriter mockSocketOut = mockContainer.getMockSocketOut();
        PrintWriter mockUserOut = mockContainer.getMockUserOut();

        InOrder inOrder = inOrder(mockSocketOut, mockUserOut);
        inOrder.verify(mockSocketOut).println(Commands.REQUEST_AVAILABLE_HTML_FILES);
        inOrder.verify(mockUserOut).println("a.html;\nb.html;\n");
    }

    @DisplayName("Test that when user inputs disconnect command, the command is delivered to the server")
    @Test
    void testReadFromUserDisconnect() throws Exception {
        webClient = getClientWithInputAndResponse(disconnect(), noResponse());
        webClient.readFromUser();

        PrintWriter mockSocketOut = mockContainer.getMockSocketOut();
        verify(mockSocketOut).println(Commands.END_CONNECTION);
    }

    @DisplayName("Test if readFromSocket() returns response from socketIn")
    @Test
    void testReadFromSocket() throws Exception {
        webClient = getClientWithInputAndResponse(getAvailableHtmlFiles(), availableHtmlFiles());
        String response = webClient.readFromSocket();

        String expected = "a.html;\nb.html;\n";
        assertEquals(expected, response);
    }

    @DisplayName("Test if askForHtmlPages() sends correct command to socket and prints response to userOut")
    @Test
    void testAskForAvailableHtmlFiles() throws Exception{
        webClient = getClientWithInputAndResponse(getAvailableHtmlFiles(), availableHtmlFiles());
        webClient.askForAvailableHtmlPages();

        PrintWriter socketOut = mockContainer.getMockSocketOut();
        PrintWriter mockUserOut = mockContainer.getMockUserOut();
        InOrder inOrder = inOrder(socketOut, mockUserOut);
        inOrder.verify(socketOut).println(Commands.REQUEST_AVAILABLE_HTML_FILES);
        inOrder.verify(mockUserOut).println("a.html;\nb.html;\n");
    }

    @DisplayName("Test if disconnect() method sends END_CONNECTION command to socketOut")
    @Test
    void testDisconnect() throws Exception{
        webClient = getClientWithInputAndResponse(disconnect(), noResponse());
        webClient.disconnect();

        PrintWriter mockSocketOut = mockContainer.getMockSocketOut();
        verify(mockSocketOut).println(Commands.END_CONNECTION);
    }



    //==================================== UTILS ====================================//



    private WebClient getClientWithInputAndResponse(BufferedReader mockUserIn, BufferedReader mockSocketIn) throws Exception{
        mockContainer = getNewClientMockContainer(mockUserIn, mockSocketIn);
        return getStubbedWebClient(mockContainer);
    }

    private PrintWriter getMockOutStream() {
        return mock(PrintWriter.class);
    }

    private BufferedReader disconnect() throws IOException {
        BufferedReader mockBufferedReader = mock(BufferedReader.class);
        when(mockBufferedReader.readLine())
                .thenReturn(Commands.END_CONNECTION);

        return mockBufferedReader;
    }

    private BufferedReader getAvailableHtmlFiles() throws IOException {
        BufferedReader mockBufferedReader = mock(BufferedReader.class);
        when(mockBufferedReader.readLine())
                .thenReturn(Commands.REQUEST_AVAILABLE_HTML_FILES)
                .thenReturn(Commands.END_CONNECTION);

        return mockBufferedReader;
    }

    private BufferedReader twoRandomMessages() throws IOException {
        BufferedReader mockBufferedReader = mock(BufferedReader.class);
        when(mockBufferedReader.readLine())
                .thenReturn("First random message")
                .thenReturn("Second random message")
                .thenReturn(Commands.END_CONNECTION);

        return mockBufferedReader;
    }

    private BufferedReader availableHtmlFiles() throws IOException {
        BufferedReader mockBufferedReader = mock(BufferedReader.class);
        when(mockBufferedReader.readLine())
                .thenReturn("a.html;")
                .thenReturn("b.html;")
                .thenReturn("");

        return mockBufferedReader;
    }

    private BufferedReader noResponse() {
        return mock(BufferedReader.class);
    }

    private Socket getMockSocket() {
        return mock(Socket.class);
    }

    private ClientMockContainer getNewClientMockContainer(BufferedReader mockUserIn, BufferedReader mockSocketIn) {
        Socket mockSocket = getMockSocket();
        PrintWriter mockUserOut = getMockOutStream();
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
