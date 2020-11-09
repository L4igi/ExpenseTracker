package com.group33.swa.webController.account;

import com.group33.swa.exceptions.AccountException;
import com.group33.swa.logic.accountIterator.AccountContainerRepository;
import com.group33.swa.logic.iterator.Iterator;
import com.group33.swa.logic.makeAccount.MakeCashAccount;
import com.group33.swa.logic.makeAccount.MakeGiroAccount;
import com.group33.swa.logic.makeAccount.MakeStudentAccount;
import com.group33.swa.logic.messageDecorator.Message;
import com.group33.swa.logic.messageDecorator.WebsocketMessageDecorator;
import com.group33.swa.logic.observer.Observable;
import com.group33.swa.logic.transactionIterator.TransactionContainerRepository;
import com.group33.swa.model.account.Account;
import com.group33.swa.model.account.AccountType;
import com.group33.swa.model.account.GiroAccount;
import com.group33.swa.model.communication.ErrorMessage;
import com.group33.swa.model.transaction.Transaction;
import com.group33.swa.model.transaction.TransactionType;
import com.group33.swa.webController.websocket.WebSocketHandler;
import com.group33.swa.webServices.account.AccountService;
import com.group33.swa.webServices.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/** AccountController Class */
@Controller
public class AccountController extends Observable {

  @Autowired private AccountService accountService;
  @Autowired private TransactionService transactionService;
  @Autowired private MakeGiroAccount makeGiroAccount;
  @Autowired private MakeCashAccount makeCashAccount;
  @Autowired private MakeStudentAccount makeStudentAccount;
  @Autowired private TransactionContainerRepository transactionContainerRepository;
  @Autowired private AccountContainerRepository accountContainerRepository;
  @Autowired public WebSocketHandler webSocketHandler;

  /**
   * Initial Method to setup the Account-Environment
   *
   * @throws AccountException throws exception if Account Exception occurs
   */
  @PostConstruct
  public void init() throws AccountException {

    System.out.println("Init AccountController");
    this.addObserver(webSocketHandler);
    // Creating default transaction account on creation of first account
    if (accountService.getAllAccounts().isEmpty()) {
      Account defaultaccount =
          new GiroAccount(1, "defaultAccount", AccountType.GIRO, Float.POSITIVE_INFINITY);

      accountService.createAccount(defaultaccount);
    }
  }

  /**
   * Creates new account, saved it to the databse
   *
   * @param userID unique identifier of the user
   * @param accountName the name of the account
   * @param accountType type of the account
   * @param studentNumber student number
   * @param overdraftlimit limit
   * @param startAmount amount which the accounts start from
   * @param request REQ-Client informations
   * @param response RES-Client informations
   * @return
   * @throws AccountException
   */
  @RequestMapping(value = "/newAccount", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public Account newAccount(
      @RequestParam(value = "userID", required = true) int userID,
      @RequestParam(value = "accountName", required = true) String accountName,
      @RequestParam(value = "accountType", required = true) String accountType,
      @RequestParam(value = "studentNumber", required = false) String studentNumber,
      @RequestParam(value = "overdraftlimit", required = false, defaultValue = "0")
          float overdraftlimit,
      @RequestParam(value = "startAmount", required = false, defaultValue = "0") float startAmount,
      HttpServletRequest request,
      HttpServletRequest response)
      throws AccountException {
    System.out.println("Calling newAccount");

    Account account = null;
    List<Account> allAccounts = accountService.getAllAccounts();
    Message wsMessage;
    for(Account tempAcc: allAccounts){
      if(tempAcc.getAccountName().equals(accountName)){
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(
                "Account with the same name already exists");
        wsMessage = new WebsocketMessageDecorator(errorMessage);
        this.notifyObservers(wsMessage);
      }
    }
    AccountType type = AccountType.valueOf(accountType);
    switch (accountType) {
      case "GIRO":
        makeGiroAccount.makeAccount(
            userID, accountName, type, studentNumber, overdraftlimit, startAmount);
        break;
      case "STUDENT":
        makeStudentAccount.makeAccount(
            userID, accountName, type, studentNumber, overdraftlimit, startAmount);
        break;
      case "CASH":
        makeCashAccount.makeAccount(
            userID, accountName, type, studentNumber, overdraftlimit, startAmount);
        break;
      default:
        break;
    }
    return account;
  }

  /**
   * Returns all accounts
   *
   * @param userID unique identifier of the user
   * @param request REQ-Client informations
   * @param response RES-Client informations
   * @return {@link List}&lt;{@link Account}&gt;
   */
  @RequestMapping(
      value = "/getAllAccounts",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseBody
  public List<Account> requestAllAccounts(
      @RequestParam(value = "userID", required = true) int userID,
      HttpServletRequest request,
      HttpServletRequest response) {
    /** calculates current balance of all existing accounts based on transactions */
    /*
    for (Account account : accounts) {
      List<Transaction> transactions =
              transactionService.getAllTransactionsByAccountID(account.getAccountID());
      float balance = 0.0f;
      for (Transaction transaction : transactions) {
        if (transaction.getFromAccountID() == account.getAccountID()) {
          balance -= transaction.getAmount();
        }
        if (transaction.getToAccountID() == account.getAccountID()) {
          balance += transaction.getAmount();
        }
      }
      account.setBalance(balance);
      accountService.updateAccount(account);
    }
     */
    /**
     * Used Iterator Pattern to Iterate through accounts and transactions Calculates current balance
     * for all accounts
     */
    accountContainerRepository.setUserID(userID);
    for (Iterator iteracc = accountContainerRepository.getIterator(); iteracc.hasNext(); ) {
      float balance = 0;
      Account tempacc = (Account) iteracc.next();
      transactionContainerRepository.setAccountID(tempacc.getAccountID());
      for (Iterator itertrans = transactionContainerRepository.getIterator();
          itertrans.hasNext(); ) {
        Transaction temptrans = (Transaction) itertrans.next();
        if (temptrans.getFromAccountID() == tempacc.getAccountID()) {
          balance -= temptrans.getAmount();
        }
        if (temptrans.getToAccountID() == tempacc.getAccountID()) {
          float toAddBalance = temptrans.getAmount();
          if (temptrans.getTransactionType() == TransactionType.BUSINESS) {
            toAddBalance = temptrans.getAmount() / (float) (1 + 0.015);
          }
          if (temptrans.getTransactionType() == TransactionType.CRYPTO) {
            toAddBalance = temptrans.getAmount() / (float) (1 + 0.0025);
          }
          balance += toAddBalance;
        }
      }
      tempacc.setBalance(balance);
      accountService.updateAccount(tempacc);
    }

    return accountService.getAllAccountsbyUserID(userID);
  }
}

/** *************************testing it******** */
