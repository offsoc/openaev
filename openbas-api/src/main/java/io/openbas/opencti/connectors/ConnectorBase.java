package io.openbas.opencti.connectors;

import java.util.List;
import lombok.Data;

@Data
public abstract class ConnectorBase {
  private String url;
  private String authToken;
  private String id;
  private List<String> scope;
  private boolean auto = false;
  private boolean onlyContextual = false;
  private boolean playbookCompatible = false;
  private String listenCallbackURI;

  public abstract String getName();

  public abstract ConnectorType getType();
}
