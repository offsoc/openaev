package io.openbas.opencti.connectors.service;

import io.openbas.opencti.connectors.ConnectorBase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenCTIConnectorService {
  @Getter
  private final List<ConnectorBase> connectors;
}
