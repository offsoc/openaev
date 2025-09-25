package io.openbas.opencti.client.mutations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.methods.HttpPost;

import java.time.Instant;

@RequiredArgsConstructor
public class CreateReport extends MutationBase {
  private final String reportTitle;
  private final String reportDescription;
  private final Instant reportPublishedAt;

  private final String queryText =
    """
    mutation {
      reportAdd(
        input: {
          name: "%s",
          description: "%s",
          published: "%s"
        }
      )
      { id }
    }
    """;

  @Override
  public String getQueryText() {
    return this.queryText.formatted(this.reportTitle, this.reportDescription, this.reportPublishedAt.toString());
  }
}
