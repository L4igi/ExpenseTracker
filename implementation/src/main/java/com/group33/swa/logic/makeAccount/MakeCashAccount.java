package com.group33.swa.logic.makeAccount;

import com.group33.swa.model.account.Account;
import com.group33.swa.model.account.AccountType;
import com.group33.swa.model.account.CashAccount;
import com.group33.swa.model.transaction.PrivateTransaction;
import com.group33.swa.model.transaction.Transaction;
import com.group33.swa.model.transaction.TransactionCategory;
import com.group33.swa.model.transaction.TransactionType;
import com.group33.swa.model.transaction.transactionCategories.TransactionCategoryFactory;
import com.group33.swa.webServices.account.AccountService;
import com.group33.swa.webServices.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** */
@Component
public class MakeCashAccount extends MakeAccount {
  @Autowired private TransactionCategoryFactory transactionCategoryFactory;

  /**
   * @param userID {@link Integer} uses the userID to check if a user
   * @param accountName {@link String} name of the account
   * @param accountType {@link AccountType} Type of a Account, e.g AccountType.PRIVATE
   * @param studentid {@link String} value of the studentid
   * @param overdraftlimit {@link Float} the value the maxium overdrafting balance
   * @param startingamount {@link Float} the value on which the account balance is starting with
   * @return {@link Account}
   */
  @Override
  public Account setUpData(
      int userID,
      String accountName,
      AccountType accountType,
      String studentid,
      float overdraftlimit,
      float startingamount) {
    Account account = new CashAccount(userID, accountName, accountType, startingamount);
    return account;
  }

  @Autowired private AccountService accountService;
  @Autowired private TransactionService transactionService;

  /**
   * if Transaction Type is Cash starting amount is transfered from default account to Cash account
   * once at creation Current balance of accounts is always calculated by all saved transaction, so
   * transaction on create is necessary to keep everything cohesive
   *
   * @param account {@link Account} Account where the starting amount balance will be set
   * @param startingamount {@link Float} startingbalance value
   * @return
   */
  @Override
  public Account setStartingValue(Account account, float startingamount) {
    if (account != null) {
      long millis = System.currentTimeMillis();
      java.sql.Date date = new java.sql.Date(millis);
      Transaction startingAmountTrans =
          new PrivateTransaction(
              1,
              account.getAccountID(),
              1,
              startingamount,
              TransactionType.PRIVATE,
              date,
              "Cash StartAmount",
              TransactionCategory.SELF);
      startingAmountTrans.setTransactionCategoryIcon(
          transactionCategoryFactory.getCategory("Self").drawCategoryIcon());
      transactionService.newTransaction(startingAmountTrans);
    }
    return account;
  }
}
