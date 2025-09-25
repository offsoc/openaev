package io.openbas.opencti.client.mutations;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.io.entity.StringEntity;

public abstract class MutationBase {
  public abstract String getQueryText();
}
