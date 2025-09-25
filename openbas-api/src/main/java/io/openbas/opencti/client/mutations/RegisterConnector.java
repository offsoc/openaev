package io.openbas.opencti.client.mutations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.openbas.opencti.connectors.ConnectorBase;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegisterConnector implements Mutation {
  private final ConnectorBase connector;
  private final String queryText =
      """
    mutation RegisterConnector($input: RegisterConnectorInput) {
      registerConnector(input: $input) {
        id
        connector_state
        config {
          connection {
            host
            vhost
            use_ssl
            port
            user
            pass
          }
          listen
          listen_routing
          listen_exchange
          push
          push_routing
          push_exchange
        }
        connector_user_id
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
    node.set("input", mapper.valueToTree(toInput(connector)));
    return node;
  }

  @Data
  private static class Input {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private String type;

    @JsonProperty("scope")
    private List<String> scope;

    @JsonProperty("only_contextual")
    private boolean onlyContextual;

    @JsonProperty("playbook_compatible")
    private boolean playbookCompatible;

    @JsonProperty("listen_callback_uri")
    private String listenCallbackURI;
  }

  private Input toInput(ConnectorBase connector) {
    Input input = new Input();
    input.setId(connector.getId());
    input.setName(connector.getName());
    input.setType(connector.getType());
    input.setScope(connector.getScope());
    input.setOnlyContextual(connector.isOnlyContextual());
    input.setPlaybookCompatible(connector.isPlaybookCompatible());
    input.setListenCallbackURI(connector.getListenCallbackURI());
    return input;
  }
}
