package com.group33.swa.model.transaction.transactionCategories;

import org.springframework.stereotype.Component;

@Component
public class Education implements TransactionCategoryInterface {
  @Override
  public String drawCategoryIcon() {
    return "<i class=\"fas fa-graduation-cap\"></i>";
  }
}
