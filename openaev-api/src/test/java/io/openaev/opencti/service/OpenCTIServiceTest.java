package io.openaev.opencti.service;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.openaev.IntegrationTest;
import io.openaev.opencti.client.OpenCTIClient;
import io.openaev.opencti.client.mutations.Ping;
import io.openaev.opencti.client.mutations.RegisterConnector;
import io.openaev.opencti.client.response.Response;
import io.openaev.opencti.connectors.ConnectorBase;
import io.openaev.opencti.errors.ConnectorError;
import io.openaev.utils.fixtures.opencti.ConnectorFixture;
import io.openaev.utils.fixtures.opencti.ResponseFixture;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("OpenCTI Service tests")
public class OpenCTIServiceTest extends IntegrationTest {
  @MockBean private OpenCTIClient mockOpenCTIClient;
  @Autowired private OpenCTIService openCTIService;
  @Autowired private ObjectMapper mapper;

  @Nested
  @DisplayName("Response parsing tests")
  public class ResponseParsingTests {
    @Nested
    @DisplayName("For registering connectors")
    public class ForRegisteringConnectors {
      @Test
      @DisplayName("When request crashes, throw exception")
      public void whenRequestCrashes_throwException() throws IOException, ConnectorError {
        ConnectorBase testConnector = ConnectorFixture.getDefaultConnector();
        when(mockOpenCTIClient.execute(any(), any(), any(RegisterConnector.class)))
            .thenThrow(IOException.class);

        assertThatThrownBy(() -> openCTIService.registerConnector(testConnector))
            .isInstanceOf(IOException.class);
      }

      @Test
      @DisplayName("When response has errors in it, throw exception")
      public void whenResponseHasErrorsInIt_throwException() throws IOException, ConnectorError {
        ConnectorBase testConnector = ConnectorFixture.getDefaultConnector();
        Response errorResponse = ResponseFixture.getErrorResponse();
        when(mockOpenCTIClient.execute(any(), any(), any(RegisterConnector.class)))
            .thenReturn(errorResponse);

        assertThatThrownBy(() -> openCTIService.registerConnector(testConnector))
            .isInstanceOf(ConnectorError.class)
            .hasMessageContaining(errorResponse.getErrors().get(0).getMessage())
            .hasMessageContaining(
                "Failed to register connector %s with OpenCTI at %s"
                    .formatted(testConnector.getName(), testConnector.getUrl()));
      }

      @Test
      @DisplayName("When response is valid, return correct payload")
      public void whenResponseIsValid_returnCorrectPayload() throws IOException, ConnectorError {
        ConnectorBase testConnector = ConnectorFixture.getDefaultConnector();
        Response okResponse = ResponseFixture.getOkResponse();
        String payloadText =
            """
          {
            "registerConnector": {
              "id": "%s",
              "connector_state": null,
              "config": {
                "connection": {
                  "host": "some host",
                  "vhost": "some vhost",
                  "use_ssl": true,
                  "port": 1234,
                  "user": "some user",
                  "pass": "some pass"
                },
                "listen": "some listen",
                "listen_routing": "some listen routing",
                "listen_exchange": "some listen exchange",
                "push": "some push",
                "push_routing": "some push routing",
                "push_exchange": "some push exchange"
              },
              "connector_user_id": "some user id"
            }
          }
          """
                .formatted(testConnector.getId());
        okResponse.setData((ObjectNode) mapper.readTree(payloadText));

        when(mockOpenCTIClient.execute(any(), any(), any(RegisterConnector.class)))
            .thenReturn(okResponse);

        RegisterConnector.ResponsePayload payload = openCTIService.registerConnector(testConnector);

        assertThatJson(mapper.valueToTree(payload)).isEqualTo(payloadText);
        assertThat(testConnector.isRegistered()).isTrue();
      }
    }

    @Nested
    @DisplayName("For pinging connectors")
    public class ForPingingConnectors {
      @Test
      @DisplayName("When response has errors in it, throw exception")
      public void whenResponseHasErrorsInIt_throwException() throws IOException, ConnectorError {
        ConnectorBase testConnector = ConnectorFixture.getDefaultConnector();
        testConnector.setRegistered(true); // so it can ping!
        Response errorResponse = ResponseFixture.getErrorResponse();
        when(mockOpenCTIClient.execute(any(), any(), any(Ping.class))).thenReturn(errorResponse);

        assertThatThrownBy(() -> openCTIService.pingConnector(testConnector))
            .isInstanceOf(ConnectorError.class)
            .hasMessageContaining(errorResponse.getErrors().get(0).getMessage())
            .hasMessageContaining(
                "Failed to ping connector %s with OpenCTI at %s"
                    .formatted(testConnector.getName(), testConnector.getUrl()));
      }

      @Test
      @DisplayName("When request crashes, throw exception")
      public void whenRequestCrashes_throwException() throws IOException, ConnectorError {
        ConnectorBase testConnector = ConnectorFixture.getDefaultConnector();
        testConnector.setRegistered(true); // so it can ping!
        when(mockOpenCTIClient.execute(any(), any(), any(Ping.class))).thenThrow(IOException.class);

        assertThatThrownBy(() -> openCTIService.pingConnector(testConnector))
            .isInstanceOf(IOException.class);
      }

      @Test
      @DisplayName("When response is valid, return correct payload")
      public void whenResponseIsValid_returnCorrectPayload() throws IOException, ConnectorError {
        ConnectorBase testConnector = ConnectorFixture.getDefaultConnector();
        testConnector.setRegistered(true); // so it can ping !
        Response okResponse = ResponseFixture.getOkResponse();
        String payloadText =
            """
          {
            "pingConnector": {
              "id": "%s",
              "connector_state": null,
              "connector_info": {
                "run_and_terminate": false,
                "buffering": false,
                "queue_threshold": 0.0,
                "queue_messages_size": 0.0,
                "next_run_datetime": null,
                "last_run_datetime": null
              }
            }
          }
          """
                .formatted(testConnector.getId());
        okResponse.setData((ObjectNode) mapper.readTree(payloadText));

        when(mockOpenCTIClient.execute(any(), any(), any(Ping.class))).thenReturn(okResponse);

        Ping.ResponsePayload payload = openCTIService.pingConnector(testConnector);

        assertThatJson(mapper.valueToTree(payload)).isEqualTo(payloadText);
      }

      @Test
      @DisplayName("When connector not yet registered, throw exception")
      public void wheConnectorNotYetRegistered_throwException() throws IOException, ConnectorError {
        ConnectorBase testConnector = ConnectorFixture.getDefaultConnector();
        when(mockOpenCTIClient.execute(any(), any(), any(Ping.class)))
            .thenReturn(ResponseFixture.getOkResponse());

        assertThatThrownBy(() -> openCTIService.pingConnector(testConnector))
            .isInstanceOf(ConnectorError.class)
            .hasMessage(
                "Cannot ping connector %s with OpenCTI at %s: connector hasn't registered yet. Try again later."
                    .formatted(testConnector.getName(), testConnector.getUrl()));
      }
    }
  }
}
