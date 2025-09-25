package io.openbas.opencti.client.mutations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.util.List;

@NoArgsConstructor
public class RegisterConnector extends MutationBase {
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

  public class Input {
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
}
