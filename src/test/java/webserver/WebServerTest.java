package webserver;

import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void init() throws Exception{
        mockContainer = getNewServerMockContainer();
        webServer = getStubbedWebServer(mockContainer);
    }

    @DisplayName("Test that the start() method closes all required resources when done")
    @Test
    void testStart() throws Exception {
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
    void testReadFromSocket() throws Exception {
        webServer.readFromSocket();

        BufferedReader mockSocketIn = mockContainer.getMockSocketIn();
        PrintWriter mockLogOut = mockContainer.getMockLogOut();

        InOrder inOrder = inOrder(mockSocketIn, mockLogOut);
        inOrder.verify(mockSocketIn).readLine();
        inOrder.verify(mockLogOut).println("Input: First message");
        inOrder.verify(mockSocketIn).readLine();
        inOrder.verify(mockLogOut).println("Input: Second message");
        inOrder.verify(mockSocketIn).readLine();
        inOrder.verify(mockLogOut).println("Input: " + Commands.END_MESSAGE);
    }



    //=================================== UTILS ===================================//



    private ServerSocket getMockServerSocket() {
        return mock(ServerSocket.class);
    }

    private Socket getMockSocket() {
        return mock(Socket.class);
    }

    private PrintWriter getMockOutStream() {
        return mock(PrintWriter.class);
    }

    private BufferedReader getMockSocketIn() throws IOException {
        BufferedReader mockBufferedReader = mock(BufferedReader.class);
        when(mockBufferedReader.readLine())
                .thenReturn("First message")
                .thenReturn("Second message")
                .thenReturn(Commands.END_MESSAGE);

        return mockBufferedReader;
    }

    private ServerMockContainer getNewServerMockContainer() throws IOException {
        ServerSocket mockServerSocket = getMockServerSocket();
        Socket mockSocket = getMockSocket();
        BufferedReader mockSocketIn = getMockSocketIn();
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
