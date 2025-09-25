package io.openbas.opencti.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Response {
  private final int status;
  private final String responseBody;
}
