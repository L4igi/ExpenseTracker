package com.group33.swa.repository.transaction;

import com.group33.swa.model.transaction.Transaction;
import com.group33.swa.model.transaction.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
/** TransactionRepository Class */
public interface TransactionRepository extends JpaRepository<Transaction, String> {
  /**
   * Returns List of all Transactions made from User
   *
   * @param UserID unique identifier of user
   * @return {@link Transaction}
   */
  List<Transaction> getTransactionsByFromUserID(int UserID);

  /**
   * Returns List of all Transactions made from Account
   *
   * @param accountID unique identifier of account
   * @return {@link List}&lt;{@link Transaction}&gt;
   */
  List<Transaction> findAllByFromAccountID(int accountID);

  /**
   * Returns List of all Transactions made to Account
   *
   * @param accountID unique identifier of account
   * @return {@link List}&lt;{@link Transaction}&gt;
   */
  List<Transaction> findAllByToAccountID(int accountID);

  /**
   * Returns List of all Transactions by Account and type
   *
   * @param accountID unique identifier of user
   * @param transactionCategory type of transaction
   * @return {@link List}&lt;{@link Transaction}&gt;
   */
  List<Transaction> getTransactionsByFromAccountIDAndTransactionCategory(
      int accountID, TransactionCategory transactionCategory);

  /**
   * Returns Transaction by Transaction ID
   *
   * @param transactionID
   * @return {@link Transaction}&gt;
   */
  Transaction getTransactionsByTransactionID(int transactionID);
}
