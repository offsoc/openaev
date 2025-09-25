package io.openbas.opencti.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.openbas.IntegrationTest;
import io.openbas.authorisation.HttpClientFactory;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
public class OpenCTIClientTest extends IntegrationTest {

  @MockBean private HttpClientFactory mockHttpClientFactory;
  @Mock private CloseableHttpClient mockHttpClient;
  @Autowired private OpenCTIClient client;

  // to set
  private final String baseUrl = "base_url";
  private final String authToken = "authToken";

  @BeforeEach
  public void setup() throws JsonProcessingException {
    when(mockHttpClientFactory.httpClientCustom()).thenReturn(mockHttpClient);
  }

  private ClassicHttpResponse getMockResponse(int statusCode, String responseBody) {
    ClassicHttpResponse mock = Mockito.mock(ClassicHttpResponse.class);
    when(mock.getEntity()).thenReturn(new StringEntity(responseBody));
    when(mock.getCode()).thenReturn(statusCode);
    return mock;
  }

  @Nested
  @DisplayName("When calling execute")
  public class WhenCallingRegisterConnector {
    @Nested
    @DisplayName("When endpoint has a communication error")
    public class WhenEndpointHasACommunicationError {
      @BeforeEach
      public void setup() throws IOException {
        when(mockHttpClient.execute((ClassicHttpRequest) any(), (HttpClientResponseHandler<?>) any())).thenThrow(IOException.class);
      }

      @Test
      @DisplayName("It throws an exception")
      public void itThrowsAnException() {
        assertThatThrownBy(() -> client.execute(baseUrl, authToken, "fake mutation"))
          .isInstanceOf(ClientProtocolException.class)
          .hasMessageContaining("Unexpected response for request on: %s".formatted(baseUrl))
          .hasCauseInstanceOf(IOException.class);
      }
    }

    @Nested
    @DisplayName("When endpoint returns NOK status")
    public class WhenEndpointReturnsNOKStatus {
      @BeforeEach
      public void setup() throws IOException {
        ClassicHttpResponse mockResponse = getMockResponse(HttpStatus.SC_BAD_REQUEST, "");
        when(mockHttpClient.execute((ClassicHttpRequest) any(), (HttpClientResponseHandler) any()))
                .thenReturn(mockResponse);
      }

      @Test
      @DisplayName("It returns the response as-is")
      public void itReturnsResponseAsIs() throws ClientProtocolException, JsonProcessingException {
        Response response = client.execute(baseUrl, authToken, "fake mutation");
        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
      }
    }

    @Nested
    @DisplayName("When endpoint returns OK status")
    public class WhenEndpointReturnsOKStatus {
      @BeforeEach
      public void setup() throws IOException {
        ClassicHttpResponse mockResponse = getMockResponse(HttpStatus.SC_OK, "good");
        when(mockHttpClient.execute((ClassicHttpRequest) any(), (HttpClientResponseHandler) any()))
                .thenReturn(mockResponse);
      }

      @Test
      @DisplayName("It returns expected structure")
      public void itReturnsExpectedStructure() throws IOException {
        Response response = client.execute(baseUrl, authToken, "fake mutation");
        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);
      }
    }
  }
}
