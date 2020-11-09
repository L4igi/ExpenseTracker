package com.group33.swa.webController.testing;

import com.group33.swa.exceptions.AccountException;
import com.group33.swa.exceptions.UserException;
import com.group33.swa.logic.makeAccount.MakeCashAccount;
import com.group33.swa.logic.makeAccount.MakeGiroAccount;
import com.group33.swa.logic.makeAccount.MakeStudentAccount;
import com.group33.swa.model.account.Account;
import com.group33.swa.model.account.AccountType;
import com.group33.swa.model.transaction.TransactionCategory;
import com.group33.swa.model.transaction.TransactionType;
import com.group33.swa.model.user.User;
import com.group33.swa.webServices.account.AccountService;
import com.group33.swa.webServices.transaction.TransactionMaker;
import com.group33.swa.webServices.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;

@Controller
public class TestingController {

  @Autowired private UserService userService;
  @Autowired private TransactionMaker transactionServiceFacade;
  @Autowired private AccountService accountService;

  /**
   * Creates 2 Users, 3 Accounts and up to 1000 transactions, filled with random data Used for
   * testing different function that require much data
   */
  @Autowired private MakeGiroAccount makeGiroAccount;

  @Autowired private MakeCashAccount makeCashAccount;
  @Autowired private MakeStudentAccount makeStudentAccount;

  /**
   * For Testing, creates many Users, Accounts and Transactions for accounts
   *
   * @return {@link String}
   * @throws UserException
   * @throws AccountException
   */
  @RequestMapping(
      value = "/createManyTransactions",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseBody
  public String createManyTransactions() throws UserException, AccountException, ParseException {
    User testUser1 = new User();
    testUser1.setUsername("TestUser1");
    testUser1.setImageIcon(
        "data:image/png;base64,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	");
    userService.createUser(testUser1);
    User testUser2 = new User();
    testUser2.setUsername("TestUser2");
    testUser2.setImageIcon(
        "data:image/png;base64,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	");
    userService.createUser(testUser2);

    // create many accounts
    makeGiroAccount.makeAccount(
        testUser1.getId(),
        "TestGiroAccount1",
        (AccountType) AccountType.valueOf("GIRO"),
        "0",
        100000,
        0);
    makeStudentAccount.makeAccount(
        testUser1.getId(),
        "TestStudentAccount1",
        (AccountType) AccountType.valueOf("STUDENT"),
        "01447910",
        100000,
        0);
    makeCashAccount.makeAccount(
        testUser1.getId(),
        "TestCashAccount1",
        (AccountType) AccountType.valueOf("CASH"),
        "0",
        0,
        1000);

    makeGiroAccount.makeAccount(
        testUser2.getId(),
        "TestGiroAccount2",
        (AccountType) AccountType.valueOf("GIRO"),
        "0",
        100000,
        0);
    makeStudentAccount.makeAccount(
        testUser2.getId(),
        "TestStudentAccount2",
        (AccountType) AccountType.valueOf("STUDENT"),
        "01447911",
        100000,
        0);
    makeCashAccount.makeAccount(
        testUser2.getId(),
        "TestCashAccount2",
        (AccountType) AccountType.valueOf("CASH"),
        "0",
        0,
        2000);

    // create many transactions

    int testTransAmount = 0;

    while (testTransAmount < 420) {
      int spendingUser = (int) (Math.random() * ((2 - 1) + 1)) + 1;
      if (spendingUser == 1) {
        spendingUser = testUser1.getId();
      } else {
        spendingUser = testUser2.getId();
      }
      int spendingAccount = 0;

      if (spendingUser == 1) {
        spendingAccount = 1;
      } else if (spendingUser == testUser1.getId()) {
        spendingAccount = getSpendingAccountID(testUser1);
      } else if (spendingUser == testUser2.getId()) {
        spendingAccount = getSpendingAccountID(testUser2);
      }

      int toAccountID = (int) (Math.random() * ((7 - 1) + 1)) + 1;
      while (toAccountID == spendingAccount) {
        toAccountID =
            (int) (Math.random() * ((accountService.getAllAccounts().size() - 1) + 1)) + 1;
      }

      String transactionType = TransactionType.PRIVATE.toString();
      int randomType = (int) (Math.random() * ((3 - 1) + 1)) + 1;

      switch (randomType) {
        case 1:
          transactionType = TransactionType.PRIVATE.toString();
          break;
        case 2:
          transactionType = TransactionType.CRYPTO.toString();
          break;
        case 3:
          transactionType = TransactionType.BUSINESS.toString();
          break;
        default:
          break;
      }

      String transactionCategory = TransactionCategory.DIVIDEND.toString();

      randomType = (int) (Math.random() * ((6 - 1) + 1)) + 1;

      switch (randomType) {
        case 1:
          transactionCategory = TransactionCategory.DIVIDEND.toString();
          break;
        case 2:
          transactionCategory = TransactionCategory.EDUCATION.toString();
          break;
        case 3:
          transactionCategory = TransactionCategory.FOOD.toString();
          break;
        case 4:
          transactionCategory = TransactionCategory.SALARY.toString();
          break;
        case 5:
          transactionCategory = TransactionCategory.SELF.toString();
          break;
        case 6:
          transactionCategory = TransactionCategory.TRANSPORTATION.toString();
          break;
        default:
          break;
      }

      float randomTransactionAmount = (float) (Math.random() * ((5000 - 1) + 1)) + 1;

      int randomyear = (int) (Math.random() * 5) + 1995;
      int randommonth = (int) (Math.random() * ((12 - 1) + 1)) + 1;
      int randomDay = (int) (Math.random() * ((31 - 1) + 1)) + 1;

      String date = randomyear + "-" + randommonth + "-" + randomDay;

      Account tempAcc = accountService.getAccount(spendingUser, spendingAccount);

      if (((tempAcc.getBalance() - randomTransactionAmount)
              > tempAcc.getOverdraftlimit() * (-1))) {

        transactionServiceFacade.processTransaction(
            tempAcc.getAccountID(),
            toAccountID,
            tempAcc.getUserID(),
            randomTransactionAmount,
            transactionType,
            date,
            "TestTransaction " + testTransAmount,
            transactionCategory);
      }
      float balance = tempAcc.getBalance() - randomTransactionAmount;
      tempAcc.setBalance(balance);

      testTransAmount++;
    }
    return ("Created Many Transactions");
  }

  private int getSpendingAccountID(User testUser) {
    int spendingAccount;
    spendingAccount = (int) (Math.random() * ((3 - 1) + 1)) + 1;
    if (spendingAccount == 1) {
      spendingAccount =
          accountService.getAllAccountsbyUserID(testUser.getId()).get(0).getAccountID();
    } else if (spendingAccount == 2) {
      spendingAccount =
          accountService.getAllAccountsbyUserID(testUser.getId()).get(1).getAccountID();
    } else if (spendingAccount == 3) {
      spendingAccount =
          accountService.getAllAccountsbyUserID(testUser.getId()).get(2).getAccountID();
    }
    return spendingAccount;
  }
}
