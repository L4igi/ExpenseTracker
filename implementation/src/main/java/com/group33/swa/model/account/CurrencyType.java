package com.group33.swa.model.account;
/** CurrencyType Enum Class */
public enum CurrencyType {
  EUR("/u20AC"),
  USD("/u0024"),
  YEN("/u00A5"),
  GBP("/u00A3"),
  BTC("/u20BF");

  private String unicode;

  CurrencyType(String unicode) {
    this.unicode = unicode;
  }
}
