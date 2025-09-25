package io.openbas.opencti.service;

import static io.openbas.database.model.ExecutionTrace.getNewErrorTrace;
import static io.openbas.database.model.ExecutionTrace.getNewSuccessTrace;

import io.openbas.database.model.DataAttachment;
import io.openbas.database.model.Execution;
import io.openbas.database.model.ExecutionTraceAction;
import io.openbas.opencti.client.Response;
import io.openbas.opencti.client.mutations.CreateCase;
import io.openbas.opencti.client.mutations.CreateReport;
import io.openbas.opencti.client.mutations.MutationBase;
import io.openbas.opencti.client.mutations.RegisterConnector;
import io.openbas.opencti.config.OpenCTIConfig;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import io.openbas.opencti.client.OpenCTIClient;
import io.openbas.opencti.connectors.ConnectorBase;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OpenCTIService {
  private OpenCTIConfig classicOpenCTIConfig;
  private OpenCTIClient openCTIClient;

  @Autowired
  public void setConfig(OpenCTIConfig config) {
    this.classicOpenCTIConfig = config;
  }

  @Autowired
  public void setOpenCTIClient(OpenCTIClient openCTIClient) {
    this.openCTIClient = openCTIClient;
  }

  public Response registerConnector(ConnectorBase connector) throws IOException {
    MutationBase mut = new RegisterConnector();
    return openCTIClient.execute(connector.getRemoteUrl(), connector.getAuthToken(), mut);
  }


  public void createCase(
      Execution execution, String name, String description, List<DataAttachment> attachments)
      throws Exception {
    MutationBase mut = new CreateCase(name, description);
    Response response = openCTIClient.execute(classicOpenCTIConfig.getApiUrl(), classicOpenCTIConfig.getToken(), mut);
    if (response.getStatus() == HttpStatus.SC_OK) {
      execution.addTrace(
              getNewSuccessTrace("Case created (" + response.getResponseBody() + ")", ExecutionTraceAction.COMPLETE));
    } else {
      execution.addTrace(getNewErrorTrace("Fail to POST", ExecutionTraceAction.COMPLETE));
    }
  }

  public void createReport(Execution execution, String name, String description, List<DataAttachment> attachments) throws IOException, ParseException {
    MutationBase mut = new CreateReport(name, description, Instant.now());
    Response response = openCTIClient.execute(classicOpenCTIConfig.getApiUrl(), classicOpenCTIConfig.getToken(), mut);
    if (response.getStatus() == HttpStatus.SC_OK) {
      execution.addTrace(
              getNewSuccessTrace(
                      "Report created (" + response.getResponseBody() + ")", ExecutionTraceAction.COMPLETE));
    } else {
      execution.addTrace(getNewErrorTrace("Fail to POST", ExecutionTraceAction.COMPLETE));
    }
  }
}
