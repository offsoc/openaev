package io.openbas.opencti.client.mutations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.openbas.opencti.connectors.ConnectorBase;
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
    node.set("connectorInfo", mapper.createObjectNode());
    return node;
  }
}
