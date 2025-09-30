package io.openbas.scheduler.jobs;

import io.openbas.opencti.client.mutations.Ping;
import io.openbas.opencti.connectors.service.OpenCTIConnectorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@DisallowConcurrentExecution
public class ConnectorPingJob implements Job {
  private final OpenCTIConnectorService openCTIConnectorService;

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    List<Ping.ResponsePayload> payloads = openCTIConnectorService.pingAllConnectors();
  }
}
