package io.openbas.opencti.client.mutations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.openbas.opencti.connectors.ConnectorBase;
import java.time.Instant;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Ping implements Mutation {
  private final ConnectorBase connector;
  private final String queryText =
      """
      mutation PingConnector($id: ID!, $state: String, $connectorInfo: ConnectorInfoInput ) {
        pingConnector(id: $id, state: $state, connectorInfo: $connectorInfo) {
          id
          connector_state
          connector_info {
              run_and_terminate
              buffering
              queue_threshold
              queue_messages_size
              next_run_datetime
              last_run_datetime
          }
        }
      }
    """;

  @Override
  public String getQueryText() {
    return this.queryText;
  }

  @Override
  public JsonNode getVariables() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode node = mapper.createObjectNode();
    node.set("id", mapper.valueToTree(connector.getId()));
    node.set("state", null);
    node.set("connectorInfo", mapper.valueToTree(new ConnectorInfo()));
    return node;
  }

  @Data
  public static class ResponsePayload {
    @JsonProperty("pingConnector")
    private PingConnectorContent pingConnectorContent;

    @Data
    public static class PingConnectorContent {
      @JsonProperty("id")
      private String id;

      @JsonProperty("connector_state")
      private ObjectNode connectorState;

      @JsonProperty("connector_info")
      private ConnectorInfo connectorInfo;

      @Data
      public static class ConnectorInfo {
        @JsonProperty("run_and_terminate")
        private Boolean runAndTerminate;

        @JsonProperty("buffering")
        private Boolean buffering;

        @JsonProperty("queue_threshold")
        private Double queueThreshold;

        @JsonProperty("queue_messages_size")
        private Double queueMessagesSize;

        @JsonProperty("next_run_datetime")
        private Instant nextRunDatetime;

        @JsonProperty("last_run_datetime")
        private Instant lastRunDatetime;
      }
    }
  }

  @Getter
  private static class ConnectorInfo {
    @JsonProperty("run_and_terminate")
    private boolean runAndTerminate = false;

    @JsonProperty("buffering")
    private boolean buffering = false;

    @JsonProperty("queue_threshold")
    private double queueThreshold = 500.0;

    @JsonProperty("queue_messages_size")
    private double queueMessagesSize = 500.0;

    @JsonProperty("next_run_datetime")
    private Instant nextRunDatetime = null;

    @JsonProperty("last_run_datetime")
    private Instant lastRunDatetime = null;
  }
}
