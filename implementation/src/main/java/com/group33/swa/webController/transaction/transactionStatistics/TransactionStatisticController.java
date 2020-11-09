package com.group33.swa.webController.transaction.transactionStatistics;

import com.group33.swa.logic.accountIterator.AccountContainerRepository;
import com.group33.swa.logic.iterator.Iterator;
import com.group33.swa.model.account.Account;
import com.group33.swa.model.transaction.Transaction;
import com.group33.swa.model.transaction.TransactionCategory;
import com.group33.swa.webServices.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/** Controller to handle the statistical communication of the transaction controller */
@Controller
public class TransactionStatisticController {

  @Autowired private TransactionService transactionService;
  @Autowired private AccountContainerRepository accountContainerRepository; // Iterator-Pattern

  /**
   * gets years transactions happend by user id
   *
   * @param userID unique identifier of the user
   * @param accountID unique identifier of the account
   * @param request
   * @param response
   * @return {@link List}&lt;{@link java.util.Map}&lt;{@link String},{@link Integer}&gt;&gt;
   */
  @RequestMapping(
      value = "/getYearsByUserID",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseBody
  public Map requestYears(
      @RequestParam(value = "userID", required = true) int userID,
      @RequestParam(value = "accountID", required = false) Integer accountID,
      HttpServletRequest request,
      HttpServletRequest response) {

    Map<String, List<Integer>> returnMap = new HashMap<String, List<Integer>>();
    if (accountID == null) {
      returnMap.put("year", transactionService.getYearsByUserID(userID));
    } else {
      try {
        // check if account is linked to user
        accountContainerRepository.setUserID(userID);
        boolean userContainsAccount = false;
        for (Iterator iteracc = accountContainerRepository.getIterator(); iteracc.hasNext(); ) {
          Account tempacc = (Account) iteracc.next();
          if (tempacc.getAccountID() == accountID) {
            userContainsAccount = true;
          }
        }
        if (userContainsAccount) {
          returnMap.put("year", transactionService.getYearsByAccountID(accountID));
        } else {
          throw new Exception("AccountAccessException: Account doesn't belong to User ");
        }
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

    return returnMap;
  }

  /**
   * gets the month of years transactions happend by user id
   *
   * @param userID unique identifier of the account
   * @param year
   * @param accountID unique identifier of the account
   * @param request
   * @param response
   * @return {@link List}&lt;{@link java.util.Map}&lt;{@link String}@com;{@link Integer}&gt;&gt;
   */
  @RequestMapping(
      value = "/getMonthsByYearByUserID",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseBody
  public Map requestMonthsByYear(
      @RequestParam(value = "userID", required = true) int userID,
      @RequestParam(value = "year", required = true) int year,
      @RequestParam(value = "accountID", required = false) Integer accountID,
      HttpServletRequest request,
      HttpServletRequest response) {

    Map<String, List<Integer>> returnMap = new HashMap<String, List<Integer>>();
    if (accountID == null) {
      List<Integer> returnList = transactionService.getMonthsByYearByUserID(userID, year);

      ListIterator<Integer> iterator = returnList.listIterator();
      while (iterator.hasNext()) {
        Integer next = iterator.next();
        iterator.set(next + 1);
      }
      returnMap.put("month", returnList);
    } else {
      try {
        // check if account is linked to user
        accountContainerRepository.setUserID(userID);
        boolean userContainsAccount = false;
        for (Iterator iteracc = accountContainerRepository.getIterator(); iteracc.hasNext(); ) {
          Account tempacc = (Account) iteracc.next();
          if (tempacc.getAccountID() == accountID) {
            userContainsAccount = true;
          }
        }
        if (userContainsAccount) {
          List<Integer> returnList = transactionService.getMonthsByYearByAccountID(accountID, year);

          ListIterator<Integer> iterator = returnList.listIterator();
          while (iterator.hasNext()) {
            Integer next = iterator.next();
            iterator.set(next + 1);
          }
          returnMap.put("month", returnList);
        } else {
          throw new Exception("AccountAccessException: Account doesn't belong to User ");
        }
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

    return returnMap;
  }

  /**
   * returns days in month in year that transactions happends by userID
   *
   * @param userID unique identifier of the user
   * @param year
   * @param month
   * @param accountID unique identifier of the account
   * @param request
   * @param response
   * @return {@link List}&lt;{@link java.util.Map}&lt;{@link String}@com;{@link Integer}&gt;&gt;
   */
  @RequestMapping(
      value = "/getDaysByMonthsByYearByUserID",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseBody
  public Map requestDaysByMonthByYear(
      @RequestParam(value = "userID", required = true) int userID,
      @RequestParam(value = "year", required = true) int year,
      @RequestParam(value = "month", required = true) int month,
      @RequestParam(value = "accountID", required = false) Integer accountID,
      HttpServletRequest request,
      HttpServletRequest response) {

    Map<String, List<Integer>> returnMap = new HashMap<String, List<Integer>>();
    if (accountID == null) {
      returnMap.put(
          "day", transactionService.getDaysByMonthsByYearByUserID(userID, year, month - 1));
    } else {
      try {
        // check if account is linked to user
        accountContainerRepository.setUserID(userID);
        boolean userContainsAccount = false;
        for (Iterator iteracc = accountContainerRepository.getIterator(); iteracc.hasNext(); ) {
          Account tempacc = (Account) iteracc.next();
          if (tempacc.getAccountID() == accountID) {
            userContainsAccount = true;
          }
        }
        if (userContainsAccount) {
          returnMap.put(
              "day",
              transactionService.getDaysByMonthsByYearByAccountID(accountID, year, month - 1));
        } else {
          throw new Exception("AccountAccessException: Account doesn't belong to User ");
        }
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

    return returnMap;
  }

  /**
   * gets the balance summed up to a certain date
   *
   * @param userID unique identifier of the user
   * @param year
   * @param month
   * @param accountID unique identifier of the account
   * @param request
   * @param response
   * @return {@link List}&lt;{@link java.util.Map}&lt;{@link Integer}@com;{@link float}&gt;&gt;
   */
  @RequestMapping(
      value = "/getBalanceByMonthByYearByAccountID",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseBody
  public Map getBalanceByMonthByYearByAccountID(
      @RequestParam(value = "userID", required = true) int userID,
      @RequestParam(value = "year", required = true) int year,
      @RequestParam(value = "month", required = true) int month,
      @RequestParam(value = "accountID", required = true) Integer accountID,
      HttpServletRequest request,
      HttpServletRequest response) {

    Map<Integer, Float> returnMap = new TreeMap<Integer, Float>();

    int daysInMonth = 31;
    switch (month % 2) {
      case 0:
        daysInMonth = 30;
        break;
      case 1:
        daysInMonth = 31;
        break;
      default:
        break;
    }
    if (month == 2) {
      daysInMonth = 29;
    }

    for (int i = 1; i <= daysInMonth; i++) {
      returnMap.put(i, transactionService.getBalanceUptoDate(userID, accountID, year, month, i));
    }
    return returnMap;
  }

  /**
   * return summed up income and expenses for an account for an account category over a certain
   * timeframe
   *
   * @param fromDateAsString
   * @param toDateAsString
   * @param accountID unique identifier of the Account
   * @param request
   * @param response
   * @return {@link List}&lt;{@link java.util.Map}&lt;{@link String}@com;{@link Object}&gt;&gt;
   */
  @RequestMapping(
      value = "/getSumsByCategoriesOverPeriodByAccountID",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseBody
  public List<Map<String, Object>> getSumsByCategoriesOverPeriodByAccountID(
      @RequestParam(value = "fromDate", required = true) String fromDateAsString,
      @RequestParam(value = "toDate", required = true) String toDateAsString,
      @RequestParam(value = "accountID", required = true) Integer accountID,
      @RequestParam(value = "userID", required = true) Integer userID,
      HttpServletRequest request,
      HttpServletRequest response) {

    Map<String, Object> categoryMap;
    List<Map<String, Object>> categoryMapList = new ArrayList<>();

    // ENUM-List
    for (TransactionCategory transactionCategory : TransactionCategory.values()) {

      categoryMap = new HashMap<>();

      AtomicReference<Float> income = new AtomicReference<>((float) 0);
      AtomicReference<Float> expense = new AtomicReference<>((float) 0);

      List<Transaction> transactions =
          transactionService.getAllTransactionsByAccountIDAndTransactionCategory(
              accountID, transactionCategory);

      transactions.forEach(
          transaction -> {
            Date fromDate = null;
            Date toDate = null;

            try {
              fromDate = new SimpleDateFormat("yyyyMMdd").parse(fromDateAsString);
              toDate = new SimpleDateFormat("yyyyMMdd").parse(toDateAsString);

              boolean isExpense = transactionService.isExpense(userID, transaction);

              if (transaction.getTransactionDate().getTime() >= fromDate.getTime()
                  && transaction.getTransactionDate().getTime() <= toDate.getTime()) {
                // Income
                if (!isExpense) {
                  income.set(income.get() + transaction.getAmount());
                }
                // Expense
                else {
                  expense.set(expense.get() + transaction.getAmount());
                }
              }

            } catch (ParseException e) {
              e.printStackTrace();
            }
          });

      categoryMap.put("category", transactionCategory.name());
      categoryMap.put("income", income.get());
      categoryMap.put("expense", expense.get());
      categoryMapList.add(categoryMap);
    }

    return categoryMapList;
  }
}
