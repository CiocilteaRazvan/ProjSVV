package webserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import utils.Commands;
import utils.MockContainer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.mockito.Mockito.*;

public class WebServerTest {
    private MockContainer mockContainer;
    private WebServer webServer;

    @BeforeEach
    void init() throws Exception{
        mockContainer = getNewServerMockContainer();
        webServer = getStubbedWebServer(mockContainer);
    }

//    @DisplayName("The web server receives two lines then the 'end' command which are printed. After this, the web server closes itself")
//    @Test
//    void testWebServerReadWriteClose() throws Exception {
//        webServer.start();
//
//        Socket mockSocket = mockContainer.getMockSocket();
//        BufferedReader mockSocketIn = mockContainer.getMockSocketIn();
//        PrintWriter mockSocketOut = mockContainer.getMockSocketOut();
//
//        verify(mockSocketIn, times(3)).readLine();
//        verify(mockSocketOut).println("First message");
//        verify(mockSocketOut).println("Second message");
//        verify(mockSocketOut).println(Commands.END_MESSAGE);
//    }

    @DisplayName("Test that the close() closes all required resources")
    @Test
    void testClose() throws Exception {
        webServer.close();

        Socket mockSocket = mockContainer.getMockSocket();
        BufferedReader mockSocketIn = mockContainer.getMockSocketIn();
        PrintWriter mockSocketOut = mockContainer.getMockSocketOut();
        PrintWriter mockLogOut = mockContainer.getMockLogOut();

        verify(mockSocketIn).close();
        verify(mockSocketOut).close();
        verify(mockSocket).close();
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

    private MockContainer getNewServerMockContainer() throws IOException {
        Socket mockSocket = getMockSocket();
        BufferedReader mockSocketIn = getMockSocketIn();
        PrintWriter mockSocketOut = getMockOutStream();
        PrintWriter mockLogOut = getMockOutStream();

        return new MockContainer(
                mockSocket,
                mockSocketIn,
                mockSocketOut,
                mockLogOut);
    }

    private WebServer getStubbedWebServer(MockContainer mockContainer) {
        return new WebServer(10008) {
            @Override
            protected Socket getSocket (ServerSocket serverSocket) {
                return mockContainer.getMockSocket();
            }

            @Override
            protected BufferedReader getInStream() {
                return mockContainer.getMockSocketIn();
            }

            @Override
            protected PrintWriter getOutStreamSocket() {
                return mockContainer.getMockSocketOut();
            }

            @Override
            protected PrintWriter getOutStreamLog() {
                return mockContainer.getMockLogOut();
            }
        };
    }
}
