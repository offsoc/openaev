package io.openbas.opencti.connectors.service;

import io.openbas.opencti.client.response.Response;
import io.openbas.opencti.connectors.ConnectorBase;
import io.openbas.opencti.service.OpenCTIService;
import java.io.IOException;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenCTIConnectorService {
  @Getter private final List<ConnectorBase> connectors;
  private final OpenCTIService openCTIService;

  public List<Response> registerAllConnectors() throws IOException {
    List<Response> responses =
        connectors.stream()
            .map(
                c -> {
                  try {
                    return openCTIService.registerConnector(c);
                  } catch (IOException e) {
                    return null;
                  }
                })
            .toList();
    return responses;
  }
}
