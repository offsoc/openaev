package io.openbas.opencti.connectors;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class ConnectorBase {
  @Getter
  @Setter(AccessLevel.PROTECTED)
  private String remoteUrl;
  @Getter
  @Setter(AccessLevel.PROTECTED)
  private String authToken;

  public void register() {

  }
}
