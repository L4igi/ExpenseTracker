package com.group33.swa.model.transaction;
/** Transaction Category Enum Class */
public enum TransactionCategory {
  SALARY("SALARY"),
  DIVIDEND("DIVIDEND"),
  FOOD("FOOD"),
  TRANSPORTATION("TRANSPORTATION"),
  EDUCATION("EDUCATION"),
  SELF("SELF");

  String categoryName;

  TransactionCategory(String categoryName) {
    this.categoryName = categoryName;
  }
}
