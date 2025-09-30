package io.openbas.opencti.connectors.service;

import io.openbas.opencti.client.mutations.Ping;
import io.openbas.opencti.client.mutations.RegisterConnector;
import io.openbas.opencti.connectors.ConnectorBase;
import io.openbas.opencti.errors.ConnectorError;
import io.openbas.opencti.service.OpenCTIService;
import java.io.IOException;
import java.util.ArrayList;
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

  public List<RegisterConnector.ResponsePayload> registerAllConnectors() {
    List<RegisterConnector.ResponsePayload> payloads = new ArrayList<>();
    for (ConnectorBase c : connectors) {
      try {
        payloads.add(openCTIService.registerConnector(c));
      } catch (ConnectorError e) {
        log.warn("An error occurred in the backend.", e);
      } catch (IOException e) {
        log.warn(
            "An technical error occurred while registering connector {} with OpenCTI at {}",
            c.getName(),
            c.getUrl(),
            e);
      }
    }
    return payloads;
  }

  public List<Ping.ResponsePayload> pingAllConnectors() {
    List<Ping.ResponsePayload> payloads = new ArrayList<>();
    for (ConnectorBase c : connectors) {
      try {
        payloads.add(openCTIService.pingConnector(c));
      } catch (ConnectorError e) {
        log.warn("An error occurred in the backend.", e);
      } catch (IOException e) {
        log.warn(
            "An technical error occurred while registering connector {} with OpenCTI at {}",
            c.getName(),
            c.getUrl(),
            e);
      }
    }
    return payloads;
  }
}
