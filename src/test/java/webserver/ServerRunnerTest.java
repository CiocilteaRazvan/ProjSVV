package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class ServerRunnerTest {
    ServerRunner serverRunner;

    @DisplayName("Test that the server runner calls start() method of the web server")
    @Test
    void testRunnerStartsWebServer() throws Exception {
        WebServer mockWebServer = mock(WebServer.class);
        serverRunner = new ServerRunner() {
            @Override
            protected WebServer getWebServer() {
                return mockWebServer;
            }
        };

        serverRunner.run();

        verify(mockWebServer, times(1)).start();
    }
}
