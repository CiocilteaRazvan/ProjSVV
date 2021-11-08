package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import utils.Commands;
import utils.ServerMockContainer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.mockito.Mockito.*;

public class WebServerTest {
    private ServerMockContainer mockContainer;
    private WebServer webServer;

    @DisplayName("Test that the start() method closes all required resources when done")
    @Test
    void testStart() throws Exception {
        webServer = getWebServerWithInput(twoRandMessages());
        webServer.start();

        ServerSocket mockServerSocket = mockContainer.getMockServerSocket();
        Socket mockSocket = mockContainer.getMockSocket();
        BufferedReader mockSocketIn = mockContainer.getMockSocketIn();
        PrintWriter mockSocketOut = mockContainer.getMockSocketOut();
        PrintWriter mockLogOut = mockContainer.getMockLogOut();

        verify(mockServerSocket).close();
        verify(mockSocket).close();
        verify(mockSocketIn).close();
        verify(mockSocketOut).close();
        verify(mockLogOut).close();
    }

    @DisplayName("Test that the close() closes all required resources")
    @Test
    void testClose() throws Exception {
        webServer = getWebServerWithInput(twoRandMessages());
        webServer.close();

        ServerSocket mockServerSocket = mockContainer.getMockServerSocket();
        Socket mockSocket = mockContainer.getMockSocket();
        BufferedReader mockSocketIn = mockContainer.getMockSocketIn();
        PrintWriter mockSocketOut = mockContainer.getMockSocketOut();
        PrintWriter mockLogOut = mockContainer.getMockLogOut();

        verify(mockServerSocket).close();
        verify(mockSocket).close();
        verify(mockSocketIn).close();
        verify(mockSocketOut).close();
        verify(mockLogOut).close();
    }

    @DisplayName("Test that readFromSocket() prints all input into logger")
    @Test
    void testReadFromSocketPrintsToLog() throws Exception {
        webServer = getWebServerWithInput(allPossibleInputs());
        webServer.readFromSocket();

        PrintWriter mockLogOut = mockContainer.getMockLogOut();

        InOrder inOrder = inOrder(mockLogOut);
        inOrder.verify(mockLogOut).println("Input: First random message");
        inOrder.verify(mockLogOut).println("Input: " + Commands.REQUEST_AVAILABLE_HTML_FILES);
        inOrder.verify(mockLogOut).println("Input: Second random message");
        inOrder.verify(mockLogOut).println("Input: " + Commands.END_CONNECTION);
    }

    @DisplayName("Test that readFromSocket() returns html file options when REQUEST_AVAILABLE_HTML_FILES command is called")
    @Test
    void testReadFromSocketRequestAvailableHtmlFiles() throws Exception{
        webServer = getWebServerWithInput(requestAvailableHtmlFiles());
        webServer.readFromSocket();

        PrintWriter mockSocketOut = mockContainer.getMockSocketOut();

        verify(mockSocketOut).println("a.html;");
    }


    //=================================== UTILS ===================================//



    WebServer getWebServerWithInput(BufferedReader mockSocketIn) throws Exception{
        mockContainer = getServerMockContainerWithInput(mockSocketIn);
        return getStubbedWebServer(mockContainer);
    }

    private ServerSocket getMockServerSocket() {
        return mock(ServerSocket.class);
    }

    private Socket getMockSocket() {
        return mock(Socket.class);
    }

    private PrintWriter getMockOutStream() {
        return mock(PrintWriter.class);
    }

    private BufferedReader twoRandMessages() throws IOException {
        BufferedReader mockBufferedReader = mock(BufferedReader.class);
        when(mockBufferedReader.readLine())
                .thenReturn("First message")
                .thenReturn("Second message")
                .thenReturn(Commands.END_CONNECTION);

        return mockBufferedReader;
    }

    private BufferedReader requestAvailableHtmlFiles() throws IOException {
        BufferedReader mockBufferedReader = mock(BufferedReader.class);
        when(mockBufferedReader.readLine())
                .thenReturn(Commands.REQUEST_AVAILABLE_HTML_FILES)
                .thenReturn(Commands.END_CONNECTION);

        return mockBufferedReader;
    }

    private BufferedReader allPossibleInputs() throws IOException {
        BufferedReader mockBufferedReader = mock(BufferedReader.class);
        when(mockBufferedReader.readLine())
                .thenReturn("First random message")
                .thenReturn(Commands.REQUEST_AVAILABLE_HTML_FILES)
                .thenReturn("Second random message")
                .thenReturn(Commands.END_CONNECTION);

        return mockBufferedReader;
    }

    private ServerMockContainer getServerMockContainerWithInput(BufferedReader mockSocketIn) {
        ServerSocket mockServerSocket = getMockServerSocket();
        Socket mockSocket = getMockSocket();
        PrintWriter mockSocketOut = getMockOutStream();
        PrintWriter mockLogOut = getMockOutStream();

        return new ServerMockContainer(
                mockServerSocket,
                mockSocket,
                mockSocketIn,
                mockSocketOut,
                mockLogOut);
    }

    private WebServer getStubbedWebServer(ServerMockContainer serverMockContainer) {
        return new WebServer(10008) {
            @Override
            protected ServerSocket getServerSocket(int portNumber) {
                return serverMockContainer.getMockServerSocket();
            }

            @Override
            protected Socket getSocket (ServerSocket serverSocket) {
                return serverMockContainer.getMockSocket();
            }

            @Override
            protected BufferedReader getInStream() {
                return serverMockContainer.getMockSocketIn();
            }

            @Override
            protected PrintWriter getOutStreamSocket() {
                return serverMockContainer.getMockSocketOut();
            }

            @Override
            protected PrintWriter getOutStreamLog() {
                return serverMockContainer.getMockLogOut();
            }
        };
    }
}
