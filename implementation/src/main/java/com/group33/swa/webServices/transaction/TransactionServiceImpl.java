package com.group33.swa.webServices.transaction;

import com.group33.swa.logic.accountIterator.AccountContainerRepository;
import com.group33.swa.logic.iterator.Iterator;
import com.group33.swa.logic.transactionIterator.TransactionContainerRepository;
import com.group33.swa.model.account.Account;
import com.group33.swa.model.transaction.Transaction;
import com.group33.swa.model.transaction.TransactionCategory;
import com.group33.swa.repository.account.AccountRepository;
import com.group33.swa.repository.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/** TransactionServiceImpl Class */
@Service
public class TransactionServiceImpl implements TransactionService {

  @Autowired TransactionRepository transactionRepository;
  @Autowired AccountRepository accountRepository;
  @Autowired private AccountContainerRepository accountContainerRepository;
  @Autowired private TransactionContainerRepository transactionContainerRepository;

  /**
   * Creates a new transaction and stores it in the database
   *
   * @param transaction represents a TransationDTO
   * @return {@link Transaction}
   */
  @Override
  public Transaction newTransaction(Transaction transaction) {
    return transactionRepository.save(transaction);
  }

  /**
   * Receives all Transactions from the Database for specific user with userID
   *
   * @param userID unique identifier of the user
   * @return {@link List}&lt;{@link Transaction}&gt;
   */
  @Override
  public List<Transaction> getAllTransactionsByUserID(int userID) {
    return transactionRepository.getTransactionsByFromUserID(userID);
  }

  /**
   * Receives all Transactions from the Database for specific account with accountID
   *
   * @param accountID unique identifier of the Account
   * @return {@link List}&lt;{@link Transaction}&gt;
   */
  @Override
  public List<Transaction> getAllTransactionsByAccountID(int accountID) {

    List<Transaction> transactionList = new ArrayList<>();

    // First List - fromAccountID
    List<Transaction> transactionList_fromAccount =
        transactionRepository.findAllByFromAccountID(accountID);
    // Second List - toAccountID
    List<Transaction> transactionList_toAccount =
        transactionRepository.findAllByToAccountID(accountID);
    transactionList.addAll(transactionList_fromAccount);
    transactionList.addAll(transactionList_toAccount);
    return transactionList;
  }

  /**
   * returns a list of transactions from one account and one transaction type
   *
   * @param accountID unique identifier of the Account
   * @param transactionCategory category of transaction
   * @return {@link List}&lt;{@link Transaction}&gt;
   */
  @Override
  public List<Transaction> getTransactionsByFromAccountIDAndTransactionCategory(
      int accountID, TransactionCategory transactionCategory) {
    return transactionRepository.getTransactionsByFromAccountIDAndTransactionCategory(
        accountID, transactionCategory);
  }

  /**
   * returns Transaction by Transaction ID
   *
   * @param transactionID unique identifier of the Transaction
   * @return {@link Transaction}&gt;
   */
  @Override
  public Transaction getTransactionByTransactionID(int transactionID) {
    return transactionRepository.getTransactionsByTransactionID(transactionID);
  }

  /**
   * gets years transactions happend by user id
   *
   * @param userID unique identifier of the user
   * @return {@link List}&lt;{@link Integer}&gt;
   */
  @Override
  public List<Integer> getYearsByUserID(int userID) {
    List<Integer> accountYearList = new ArrayList<>();

    accountContainerRepository.setUserID(userID);
    for (Iterator iteracc = accountContainerRepository.getIterator(); iteracc.hasNext(); ) {
      Account tempacc = (Account) iteracc.next();
      transactionContainerRepository.setAccountID(tempacc.getAccountID());
      yearTransactionIteration(accountYearList);
    }
    return accountYearList;
  }

  private void yearTransactionIteration(List<Integer> accountYearList) {
    for (Iterator itertrans = transactionContainerRepository.getIterator(); itertrans.hasNext(); ) {
      Transaction temptrans = (Transaction) itertrans.next();
      Calendar cal = Calendar.getInstance();
      cal.setTime(temptrans.getTransactionDate());
      boolean alreadyInList = false;

      for (int temp : accountYearList) {
        if (temp == cal.get(Calendar.YEAR)) {
          alreadyInList = true;
        }
      }
      if (!alreadyInList) {
        accountYearList.add(cal.get(Calendar.YEAR));
      }
    }
  }

  /**
   * gets the month of years transactions happend by user id
   *
   * @param userID unique identifier of the user
   * @param year
   * @return {@link List}&lt;{@link Integer}&gt;
   */
  @Override
  public List<Integer> getMonthsByYearByUserID(int userID, int year) {
    List<Integer> accountMonthList = new ArrayList<>();

    accountContainerRepository.setUserID(userID);
    for (Iterator iteracc = accountContainerRepository.getIterator(); iteracc.hasNext(); ) {
      Account tempacc = (Account) iteracc.next();
      transactionContainerRepository.setAccountID(tempacc.getAccountID());
      monthTransactionIteration(year, accountMonthList);
    }

    return accountMonthList;
  }

  /**
   * iterates months transaction happend in a year and puts it in a list
   *
   * @param year
   * @param accountMonthList
   */
  private void monthTransactionIteration(int year, List<Integer> accountMonthList) {
    for (Iterator itertrans = transactionContainerRepository.getIterator(); itertrans.hasNext(); ) {
      Transaction temptrans = (Transaction) itertrans.next();
      Calendar cal = Calendar.getInstance();
      cal.setTime(temptrans.getTransactionDate());
      boolean alreadyInList = false;

      if (cal.get(Calendar.YEAR) != year) {
        alreadyInList = true;
      } else {
        for (int temp : accountMonthList) {
          if (temp == cal.get(Calendar.MONTH)) {
            alreadyInList = true;
          }
        }
      }
      if (!alreadyInList) {
        accountMonthList.add((cal.get(Calendar.MONTH)));
      }
    }
  }

  /**
   * returns days in month in year that transactions happends by userID
   *
   * @param userID unique identifier of the user
   * @param year
   * @param month
   * @return {@link List}&lt;{@link Integer}&gt;
   */
  public List<Integer> getDaysByMonthsByYearByUserID(int userID, int year, int month) {
    List<Integer> accountDaysList = new ArrayList<>();

    accountContainerRepository.setUserID(userID);
    for (Iterator iteracc = accountContainerRepository.getIterator(); iteracc.hasNext(); ) {
      Account tempacc = (Account) iteracc.next();
      transactionContainerRepository.setAccountID(tempacc.getAccountID());
      daysTransactionIteration(year, month, accountDaysList);
    }
    return accountDaysList;
  }

  /**
   * iterates days in months transaction happend in a year and puts it in a list
   *
   * @param year
   * @param month
   * @param accountDaysList
   */
  private void daysTransactionIteration(int year, int month, List<Integer> accountDaysList) {
    for (Iterator itertrans = transactionContainerRepository.getIterator(); itertrans.hasNext(); ) {
      Transaction temptrans = (Transaction) itertrans.next();
      Calendar cal = Calendar.getInstance();
      cal.setTime(temptrans.getTransactionDate());
      boolean alreadyInList = false;

      if (cal.get(Calendar.YEAR) != year || cal.get(Calendar.MONTH) != (month)) {
        alreadyInList = true;
      } else {
        for (int temp : accountDaysList) {
          if (temp == cal.get(Calendar.DAY_OF_MONTH)) {
            alreadyInList = true;
          }
        }
      }
      if (!alreadyInList) {
        accountDaysList.add(cal.get(Calendar.DAY_OF_MONTH));
      }
    }
  }

  /**
   * gets years transactions happend by accountID
   *
   * @param accountID unique identifier of the account
   * @return {@link List}&lt;{@link Integer}&gt;
   */
  @Override
  public List<Integer> getYearsByAccountID(int accountID) {
    List<Integer> accountYearList = new ArrayList<>();

    transactionContainerRepository.setAccountID(accountID);
    yearTransactionIteration(accountYearList);
    return accountYearList;
  }

  /**
   * gets the month of years transactions happend by accountID
   *
   * @param accountID unique identifier of the account
   * @param year
   * @return {@link List}&lt;{@link Integer}&gt;
   */
  @Override
  public List<Integer> getMonthsByYearByAccountID(int accountID, int year) {
    List<Integer> accountMonthList = new ArrayList<>();

    transactionContainerRepository.setAccountID(accountID);
    monthTransactionIteration(year, accountMonthList);
    return accountMonthList;
  }

  /**
   * * returns days in month in year that transactions happends by AccountID
   *
   * @param accountID unique identifier of the account
   * @param year
   * @param month
   * @return {@link List}&lt;{@link Integer}&gt;
   */
  @Override
  public List<Integer> getDaysByMonthsByYearByAccountID(int accountID, int year, int month) {
    List<Integer> accountDaysList = new ArrayList<>();

    transactionContainerRepository.setAccountID(accountID);
    daysTransactionIteration(year, month, accountDaysList);
    return accountDaysList;
  }

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
  @Override
  public float getBalanceUptoDate(int userID, int accountID, int year, int month, int day) {
    List<Transaction> allTransactions = getAllTransactionsByAccountID(accountID);

    float balanceUptoDate = 0;

    for (Transaction tempTran : allTransactions) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(tempTran.getTransactionDate());
      int tempYear = cal.get(Calendar.YEAR);
      int tempMonth = cal.get(Calendar.MONTH);
      int tempDay = cal.get(Calendar.DAY_OF_MONTH);

      boolean isExpense = isExpense(userID, tempTran);

      if (tempYear < year) {
        if (isExpense) {
          balanceUptoDate -= tempTran.getAmount();
        } else {
          balanceUptoDate += tempTran.getAmount();
        }
      } else if (tempYear == year && (tempMonth + 1) < month) {
        if (isExpense) {
          balanceUptoDate -= tempTran.getAmount();
        } else {
          balanceUptoDate += tempTran.getAmount();
        }
      } else if (tempYear == year && (tempMonth + 1) == month && tempDay <= day) {
        if (isExpense) {
          balanceUptoDate -= tempTran.getAmount();
        } else {
          balanceUptoDate += tempTran.getAmount();
        }
      } else {
        // todo exception or assertin
        // should never reach here
      }
    }
    return balanceUptoDate;
  }

  /**
   * help function for verification if transaction was expense or income for user
   *
   * @param userID unique identifier of the user
   * @param transaction represents a transaction
   * @return {@link boolean}
   */
  @Override
  public boolean isExpense(int userID, Transaction transaction) {
    return (accountRepository.getAccountByUserIDAndAccountID(userID, transaction.getFromAccountID())
        != null);
  }

  /**
   * returns all transactions by an accountID and a transaction category
   *
   * @param accountID unique identifier of the account
   * @param transactionCategory category of the transactionCategory
   * @return {@link List}&lt;{@link Transaction}&gt;
   */
  @Override
  public List<Transaction> getAllTransactionsByAccountIDAndTransactionCategory(
      int accountID, TransactionCategory transactionCategory) {
    List<Transaction> transactionList = getAllTransactionsByAccountID(accountID);
    List<Transaction> temp_transactionList = new ArrayList<>(transactionList);

    transactionList.forEach(
        transaction -> {
          if (transaction.getTransactionCategory() == transactionCategory)
            temp_transactionList.add(transaction);
        });

    return temp_transactionList;
  }
}
