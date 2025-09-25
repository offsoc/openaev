package io.openbas.opencti.connectors.service;

import io.openbas.IntegrationTest;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class OpenCTIConnectorServiceTest extends IntegrationTest {
  @Autowired OpenCTIConnectorService openCTIConnectorService;

  @Test
  public void test() throws IOException {
    openCTIConnectorService.registerAllConnectors();
  }
}
