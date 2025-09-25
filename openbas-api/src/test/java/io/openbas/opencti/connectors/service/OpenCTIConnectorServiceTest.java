package io.openbas.opencti.connectors.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.openbas.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class OpenCTIConnectorServiceTest extends IntegrationTest {
  @Autowired OpenCTIConnectorService openCTIConnectorService;

  @Test
  public void test() {
    assertThat(openCTIConnectorService.getConnectors()).isNotNull();
  }
}
