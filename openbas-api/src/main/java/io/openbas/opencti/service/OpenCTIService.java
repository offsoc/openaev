package io.openbas.opencti.service;

import static io.openbas.database.model.ExecutionTrace.getNewErrorTrace;
import static io.openbas.database.model.ExecutionTrace.getNewSuccessTrace;

import io.openbas.database.model.DataAttachment;
import io.openbas.database.model.Execution;
import io.openbas.database.model.ExecutionTraceAction;
import io.openbas.opencti.client.OpenCTIClient;
import io.openbas.opencti.client.mutations.*;
import io.openbas.opencti.client.response.Response;
import io.openbas.opencti.config.OpenCTIConfig;
import io.openbas.opencti.connectors.ConnectorBase;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
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
    Mutation mut = new RegisterConnector(connector);
    return openCTIClient.execute(connector.getUrl(), connector.getAuthToken(), mut);
  }

  public Response pingConnector(ConnectorBase connector) throws IOException {
    Mutation mut = new Ping(connector);
    return openCTIClient.execute(connector.getUrl(), connector.getAuthToken(), mut);
  }

  public void createCase(
      Execution execution, String name, String description, List<DataAttachment> attachments)
      throws Exception {
    Mutation mut = new CreateCase(name, description);
    Response response =
        openCTIClient.execute(
            classicOpenCTIConfig.getApiUrl(), classicOpenCTIConfig.getToken(), mut);
    if (response.getStatus() == HttpStatus.SC_OK) {
      execution.addTrace(
          getNewSuccessTrace(
              "Case created (" + response.getData() + ")", ExecutionTraceAction.COMPLETE));
    } else {
      execution.addTrace(getNewErrorTrace("Fail to POST", ExecutionTraceAction.COMPLETE));
    }
  }

  public void createReport(
      Execution execution, String name, String description, List<DataAttachment> attachments)
      throws IOException, ParseException {
    Mutation mut = new CreateReport(name, description, Instant.now());
    Response response =
        openCTIClient.execute(
            classicOpenCTIConfig.getApiUrl(), classicOpenCTIConfig.getToken(), mut);
    if (response.getStatus() == HttpStatus.SC_OK) {
      execution.addTrace(
          getNewSuccessTrace(
              "Report created (" + response.getData() + ")", ExecutionTraceAction.COMPLETE));
    } else {
      execution.addTrace(getNewErrorTrace("Fail to POST", ExecutionTraceAction.COMPLETE));
    }
  }
}
