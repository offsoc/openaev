package io.openbas.opencti.connectors;

import java.util.List;
import lombok.Data;

@Data
public class ConnectorBase {
  private String url;
  private String authToken;
  private String id;
  private String name;
  private String type;
  private List<String> scope;
  private boolean onlyContextual = false;
  private boolean playbookCompatible = false;
  private String listenCallbackURI;
}
