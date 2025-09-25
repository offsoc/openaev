package io.openbas.opencti.connectors.impl;

import io.openbas.opencti.connectors.ConnectorBase;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "openbas.xtm.opencti.connector.security-coverage")
public class SecurityCoverageConnector extends ConnectorBase {}
