package webclient;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class ClientRunnerTest {
    ClientRunner clientRunner;

    @DisplayName("Test that the client runner calls start() method of the web client")
    @Test
    void testRunnerStartsWebClient() throws Exception {
        WebClient mockWebClient = mock(WebClient.class);
        clientRunner = new ClientRunner() {
            @Override
            protected WebClient getWebClient() {
                return mockWebClient;
            }
        };

        clientRunner.run();

        verify(mockWebClient, times(1)).start();
    }
}
