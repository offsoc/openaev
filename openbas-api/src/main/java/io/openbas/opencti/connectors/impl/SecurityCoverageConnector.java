package io.openbas.opencti.connectors.impl;

import io.openbas.opencti.connectors.ConnectorBase;
import io.openbas.opencti.connectors.ConnectorType;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@ConfigurationProperties(prefix = "openbas.xtm.opencti.connector.security-coverage")
public class SecurityCoverageConnector extends ConnectorBase {
  private final ConnectorType type = ConnectorType.INTERNAL_ENRICHMENT;
  private final String name = "OpenAEV Coverage";

  public SecurityCoverageConnector() {
    this.setScope(new ArrayList<>(List.of("application/stix+json;version=2.1", "indicator")));
  }
}
