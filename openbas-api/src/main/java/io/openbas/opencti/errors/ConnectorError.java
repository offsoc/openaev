package io.openbas.opencti.errors;

public class ConnectorError extends Exception {
  public ConnectorError(String message) {
    super(message);
  }
}
