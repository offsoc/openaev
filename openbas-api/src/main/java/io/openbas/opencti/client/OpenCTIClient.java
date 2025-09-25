package io.openbas.opencti.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.openbas.authorisation.HttpClientFactory;
import io.openbas.opencti.client.mutations.Mutation;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenCTIClient {
  private final HttpClientFactory httpClientFactory;
  private final ObjectMapper mapper;

  public Response execute(String url, String authToken, Mutation mutation)
      throws ClientProtocolException, JsonProcessingException {
    return execute(url, authToken, mutation.getQueryText(), mutation.getVariables());
  }

  public Response execute(String url, String authToken, String mutationBody, JsonNode variables)
      throws ClientProtocolException, JsonProcessingException {
    HttpPost req = new HttpPost(url);
    req.addHeader("Authorization", "Bearer %s".formatted(authToken));
    req.addHeader("Content-Type", "application/json; charset=utf-8");
    req.addHeader("Accept", "application/json");
    Map<String, JsonNode> payload = new HashMap<>();
    payload.put("query", mapper.valueToTree(mutationBody));
    if (variables != null) {
      payload.put("variables", variables);
    }
    req.setEntity(new StringEntity(mapper.writeValueAsString(payload)));

    return execute(req);
  }

  private Response execute(ClassicHttpRequest request) throws ClientProtocolException {
    try (CloseableHttpClient client = httpClientFactory.httpClientCustom()) {
      return client.execute(
          request,
          response -> new Response(response.getCode(), EntityUtils.toString(response.getEntity())));
    } catch (IOException e) {
      throw new ClientProtocolException(
          "Unexpected response for request on: " + request.getRequestUri(), e);
    }
  }
}
