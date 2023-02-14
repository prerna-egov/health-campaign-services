package org.egov.stock.web.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets transactionType
 */
public enum TransactionType {
  
  RECEIVED("RECEIVED"),
  
  DISPATCHED("DISPATCHED");

  private String value;

  TransactionType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TransactionType fromValue(String text) {
    for (TransactionType b : TransactionType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

