package com.group33.swa.model.transaction.transactionCategories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Factory which is used to return a Object which implements the {@link
 * TransactionCategoryInterface}
 */
@Component
public class TransactionCategoryFactory {
  @Autowired private Dividend dividend;
  @Autowired private Education education;
  @Autowired private Food food;
  @Autowired private Salary salary;
  @Autowired private Self self;
  @Autowired private Transportation transportation;

  public TransactionCategoryInterface getCategory(String categoryType) {
    if (categoryType == null) {
      return null;
    }
    if (categoryType.equalsIgnoreCase("DIVIDEND")) {
      return dividend;
    } else if (categoryType.equalsIgnoreCase("EDUCATION")) {
      return education;
    } else if (categoryType.equalsIgnoreCase("FOOD")) {
      return food;
    } else if (categoryType.equalsIgnoreCase("SALARY")) {
      return salary;
    } else if (categoryType.equalsIgnoreCase("SELF")) {
      return self;
    } else if (categoryType.equalsIgnoreCase("TRANSPORTATION")) {
      return transportation;
    }
    return null;
  }
}
