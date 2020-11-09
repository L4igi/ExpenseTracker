package com.group33.swa.model.transaction.transactionCategories;

import org.springframework.stereotype.Component;

@Component
public class Dividend implements TransactionCategoryInterface {
  @Override
  public String drawCategoryIcon() {
    return "<i class=\"fas fa-hand-holding-usd\"></i>";
  }
}
