package io.openbas.opencti.client.mutations;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateCase extends MutationBase {
  private final String caseTitle;
  private final String caseDescription;

  private final String queryText =
    """
    mutation {
      caseIncidentAdd(
        input: {
          name: "%s",
          description: "%s"
         }
      )
      { id }
    }
    """;

  @Override
  public String getQueryText() {
    return this.queryText.formatted(this.caseTitle, this.caseDescription);
  }
}
