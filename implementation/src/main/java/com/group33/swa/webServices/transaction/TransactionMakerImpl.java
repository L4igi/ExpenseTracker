package com.group33.swa.webServices.transaction;

import com.group33.swa.logic.feeCalculator.*;
import com.group33.swa.model.transaction.*;
import com.group33.swa.model.transaction.transactionCategories.TransactionCategoryFactory;
import com.group33.swa.webServices.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
/** Implementation of the Facade Pattern for managing Transaction procedures */
public class TransactionMakerImpl implements TransactionMaker {
  @Autowired private TransactionMakerImpl transactionServiceFacade;
  @Autowired private AccountService accountService;
  @Autowired private TransactionService transactionService;
  @Autowired private TransactionCategoryFactory transactionCategoryFactory;

  public TransactionMakerImpl() {}

  /**
   * processes Transaction, checks if Accounts exist and create new Transaction
   *
   * @param fromAccountID unique identifier of account
   * @param toAccountID unique identifier of account
   * @param fromUserID unique identifier of the user
   * @param amount amount of the transaction
   * @param transactionType type of the transaction
   * @param transactionDate date of the transaction
   * @param transactionDescription description of the transaction
   * @param transactionCategory category of the transaction
   * @return
   */
  public Transaction processTransaction(
      int fromAccountID,
      int toAccountID,
      int fromUserID,
      float amount,
      String transactionType,
      String transactionDate,
      String transactionDescription,
      String transactionCategory) {

    /** checks if accounts exists, if not chooses return null */
    if (accountService.accountExist(fromAccountID) && accountService.accountExist(toAccountID)) {
      TransactionCategory category = TransactionCategory.valueOf(transactionCategory);

      TransactionType type = TransactionType.valueOf(transactionType);

      java.sql.Date stringToDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());

      System.out.println("Transactiondate " + transactionDate);
      if (transactionDate != null && !transactionDate.equals("")) {
        stringToDate = Date.valueOf(transactionDate);
      }

      Transaction transaction = null;
      switch (transactionType) {
        case "PRIVATE":
          transaction =
              new PrivateTransaction(
                  fromAccountID,
                  toAccountID,
                  fromUserID,
                  amount,
                  type,
                  stringToDate,
                  transactionDescription,
                  category);
          TransactionFeeCalculatorStrategy privateStrategy = new PrivateFeesStrategy();
          TransactionFeeCalculator privateCalculator =
              new TransactionFeeCalculator(privateStrategy);
          privateCalculator.setTransaction(transaction);
          transaction = privateCalculator.executeStrategy();
          break;
        case "BUSINESS":
          transaction =
              new BusinessTransaction(
                  fromAccountID,
                  toAccountID,
                  fromUserID,
                  amount,
                  type,
                  stringToDate,
                  transactionDescription,
                  category);
          TransactionFeeCalculatorStrategy businessStrategy = new BusinessFeesStrategy();
          TransactionFeeCalculator businessCalculator =
              new TransactionFeeCalculator(businessStrategy);
          businessCalculator.setTransaction(transaction);
          transaction = businessCalculator.executeStrategy();
          break;
        case "CRYPTO":
          transaction =
              new CryptoTransaction(
                  fromAccountID,
                  toAccountID,
                  fromUserID,
                  amount,
                  type,
                  stringToDate,
                  transactionDescription,
                  category);
          TransactionFeeCalculatorStrategy cryptoStrategy = new CryptoFeesStrategy();
          TransactionFeeCalculator cryptoCalculator = new TransactionFeeCalculator(cryptoStrategy);
          cryptoCalculator.setTransaction(transaction);
          transaction = cryptoCalculator.executeStrategy();
          break;
        default:
          break;
      }

      if (transaction != null) {
        // set category icon using factory mathod pattern
        String icon =
            transactionCategoryFactory.getCategory(transactionCategory).drawCategoryIcon();
        if (icon != null) transaction.setTransactionCategoryIcon(icon);
        else transaction.setTransactionCategoryIcon("");

        Transaction temp_tran = transactionService.newTransaction(transaction);
        System.out.println("Save transaction");
        System.out.println(temp_tran);

        if (temp_tran != null) {
          System.out.println("returntype transaction not null " + temp_tran);
          return temp_tran;
          /** if account could not be created, todo: should throw TransactionException */
        } else {
          System.out.println("transaction not successfull returned null");
        }
      } else {
        return null;
      }
    }
    return null;
  }
}
