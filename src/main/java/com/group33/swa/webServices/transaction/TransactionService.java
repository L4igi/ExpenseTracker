package com.group33.swa.webServices.transaction;

import com.group33.swa.model.transaction.Transaction;
import com.group33.swa.model.transaction.TransactionCategory;

import java.util.List;
/** TransactionService Interface */
public interface TransactionService {
  /**
   * Creates a new transaction and stores it in the database
   *
   * @param transaction represents a TransationDTO
   * @return {@link Transaction}
   */
  Transaction newTransaction(Transaction transaction);

  /**
   * Receives all Transactions from the Database for specific user with userID
   *
   * @param userID unique identifier of the user
   * @return {@link List}&lt;{@link Transaction}&gt;
   */
  List<Transaction> getAllTransactionsByUserID(int userID);

  /**
   * Receives all Transactions from the Database for specific account with accountID
   *
   * @param accountID unique identifier of the Account
   * @return {@link List}&lt;{@link Transaction}&gt;
   */
  List<Transaction> getAllTransactionsByAccountID(int accountID);

  /**
   * returns a list of transactions from one account and one transaction type
   *
   * @param accountID unique identifier of the Account
   * @param transactionCategory category of transaction
   * @return {@link List}&lt;{@link Transaction}&gt;
   */
  List<Transaction> getTransactionsByFromAccountIDAndTransactionCategory(
      int accountID, TransactionCategory transactionCategory);

  /**
   * returns Transaction by Transaction ID
   *
   * @param transactionID unique identifier of the Transaction
   * @return {@link Transaction}&gt;
   */
  Transaction getTransactionByTransactionID(int transactionID);

  /**
   * gets years transactions happend by user id
   *
   * @param userID unique identifier of the user
   * @return {@link List}&lt;{@link Integer}&gt;
   */
  List<Integer> getYearsByUserID(int userID);

  /**
   * gets the month of years transactions happend by user id
   *
   * @param userID unique identifier of the user
   * @param year
   * @return {@link List}&lt;{@link Integer}&gt;
   */
  List<Integer> getMonthsByYearByUserID(int userID, int year);

  /**
   * returns days in month in year that transactions happends by userID
   *
   * @param userID unique identifier of the user
   * @param year
   * @param month
   * @return {@link List}&lt;{@link Integer}&gt;
   */
  List<Integer> getDaysByMonthsByYearByUserID(int userID, int year, int month);

  /**
   * gets years transactions happend by accountID
   *
   * @param accountID unique identifier of the account
   * @return {@link List}&lt;{@link Integer}&gt;
   */
  List<Integer> getYearsByAccountID(int accountID);

  /**
   * gets the month of years transactions happend by accountID
   *
   * @param accountID unique identifier of the account
   * @param year
   * @return {@link List}&lt;{@link Integer}&gt;
   */
  List<Integer> getMonthsByYearByAccountID(int accountID, int year);

  /**
   * * returns days in month in year that transactions happends by AccountID
   *
   * @param accountID unique identifier of the account
   * @param year
   * @param month
   * @return {@link List}&lt;{@link Integer}&gt;
   */
  List<Integer> getDaysByMonthsByYearByAccountID(int accountID, int year, int month);

  /**
   * gets the summed up balance up to a certain date by an accountID
   *
   * @param userID unique identifier of the user
   * @param accountID unique identifier of the account
   * @param year
   * @param month
   * @param day
   * @return {@link List}&lt;{@link Transaction}&gt;
   */
  float getBalanceUptoDate(int userID, int accountID, int year, int month, int day);

  /**
   * help function for verification if transaction was expense or income for user
   *
   * @param userID unique identifier of the user
   * @param transaction represents a transaction
   * @return {@link boolean}
   */
  boolean isExpense(int userID, Transaction transaction);

  /**
   * returns all transactions by an accountID and a transaction category
   *
   * @param accountID unique identifier of the account
   * @param transactionCategory category of the transactionCategory
   * @return {@link List}&lt;{@link Transaction}&gt;
   */
  List<Transaction> getAllTransactionsByAccountIDAndTransactionCategory(
      int accountID, TransactionCategory transactionCategory);
}
