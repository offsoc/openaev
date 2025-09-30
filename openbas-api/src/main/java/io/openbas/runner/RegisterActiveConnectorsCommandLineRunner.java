package io.openbas.runner;

import io.openbas.opencti.connectors.service.OpenCTIConnectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RegisterActiveConnectorsCommandLineRunner implements CommandLineRunner {
  private final OpenCTIConnectorService openCTIConnectorService;

  @Override
  public void run(String... args) throws Exception {
    openCTIConnectorService.registerAllConnectors();
    log.info("Finished registering connectors.");
  }
}
