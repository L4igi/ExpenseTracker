package com.group33.swa.model.transaction;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;
/** Transaction Class */
@Entity
public abstract class Transaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int transactionID;

  float amount;
  int fromAccountID;
  int toAccountID;
  int fromUserID;
  TransactionType transactionType;
  java.sql.Date transactionDate;
  String transactionDescription;
  TransactionCategory transactionCategory;
  String transactionCategoryIcon;
  /*
  @Transient
  Account fromAccount;
  @Transient
  Account toAccount;
   */

  public Transaction() {}

  /**
   * * creates new transaction * Takes from to which account money should be transfered if from or
   * toAccount does not exist in database it is seen as external transfer and money goes or comes
   * from default account
   *
   * @param fromAccountID defines account from which transaction is outgoing
   * @param toAccountID defines account from which transaction is going to
   * @param fromUserID defines user from which transaction is outgoing
   * @param amount amount of money to be transfered
   * @param transactionType type of transaction
   * @param transactionDate date of transaction
   * @param transactionDescription description of transaction
   * @param transactionCategory category of transaction
   */
  public Transaction(
      int fromAccountID,
      int toAccountID,
      int fromUserID,
      float amount,
      TransactionType transactionType,
      java.sql.Date transactionDate,
      String transactionDescription,
      TransactionCategory transactionCategory) {
    this.fromAccountID = fromAccountID;
    this.toAccountID = toAccountID;
    this.fromUserID = fromUserID;
    this.amount = amount;
    this.transactionType = transactionType;
    this.transactionDate = new Date(transactionDate.getTime());
    this.transactionDescription = transactionDescription;
    this.transactionCategory = transactionCategory;
  }

  public int getTransactionID() {
    return transactionID;
  }

  public void setTransactionID(int transactionID) {
    this.transactionID = transactionID;
  }

  public float getAmount() {
    return amount;
  }

  public void setAmount(float amount) {
    this.amount = amount;
  }

  public int getFromAccountID() {
    return fromAccountID;
  }

  public void setFromAccountID(int fromAccountID) {
    this.fromAccountID = fromAccountID;
  }

  public int getToAccountID() {
    return toAccountID;
  }

  public void setToAccountID(int toAccountID) {
    this.toAccountID = toAccountID;
  }

  public int getFromUserID() {
    return fromUserID;
  }

  public void setFromUserID(int fromUserID) {
    this.fromUserID = fromUserID;
  }

  public TransactionType getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(TransactionType transactionType) {
    this.transactionType = transactionType;
  }

  public java.sql.Date getTransactionDate() {
    return new Date(transactionDate.getTime());
  }

  public void setTransactionDate(java.sql.Date transactionDate) {
    this.transactionDate = new Date(transactionDate.getTime());
  }

  public String getTransactionDescription() {
    return transactionDescription;
  }

  public void setTransactionDescription(String transactionDescription) {
    this.transactionDescription = transactionDescription;
  }

  public TransactionCategory getTransactionCategory() {
    return transactionCategory;
  }

  public void setTransactionCategory(TransactionCategory transactionCategory) {
    this.transactionCategory = transactionCategory;
  }

  public String getTransactionCategoryIcon() {
    return transactionCategoryIcon;
  }

  public void setTransactionCategoryIcon(String transactionCategoryIcon) {
    this.transactionCategoryIcon = transactionCategoryIcon;
  }

  @Override
  public String toString() {
    return "Transaction{"
        + "transactionID="
        + transactionID
        + ", amount="
        + amount
        + ", fromAccountID="
        + fromAccountID
        + ", toAccountID="
        + toAccountID
        + ", fromUserID="
        + fromUserID
        + ", transactionType="
        + transactionType
        + ", transactionDate="
        + transactionDate
        + ", transactionDescription='"
        + transactionDescription
        + '\''
        + ", transactionCategory="
        + transactionCategory
        + '}';
  }
}
