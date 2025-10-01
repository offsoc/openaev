package io.openbas.opencti.connectors.service;

import io.openbas.opencti.connectors.ConnectorBase;
import io.openbas.opencti.errors.ConnectorError;
import io.openbas.opencti.service.OpenCTIService;
import java.io.IOException;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenCTIConnectorService {
  @Getter private final List<ConnectorBase> connectors;
  private final OpenCTIService openCTIService;

  /**
   * Register or pings all loaded connectors. Does not crash if registering or pinging a connector
   * raises an exception, but logs a warning.
   */
  public void registerOrPingAllConnectors() {
    for (ConnectorBase c : connectors.stream().filter(ConnectorBase::shouldRegister).toList()) {
      try {
        if (!c.isRegistered()) {
          openCTIService.registerConnector(c);
        } else {
          openCTIService.pingConnector(c);
        }
      } catch (ConnectorError e) {
        log.warn("An error occurred in the backend.", e);
      } catch (IOException e) {
        log.warn(
            "A technical error occurred while registering connector {} with OpenCTI at {}",
            c.getName(),
            c.getUrl(),
            e);
      }
    }
  }
}
