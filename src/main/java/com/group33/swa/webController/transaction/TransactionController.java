package com.group33.swa.webController.transaction;

import com.group33.swa.logic.messageDecorator.Message;
import com.group33.swa.logic.messageDecorator.WebsocketMessageDecorator;
import com.group33.swa.logic.observer.Observable;
import com.group33.swa.logic.transactionIterator.TransactionContainerRepository;
import com.group33.swa.model.account.Account;
import com.group33.swa.model.communication.ErrorMessage;
import com.group33.swa.model.communication.SuccessMessage;
import com.group33.swa.model.transaction.Transaction;
import com.group33.swa.model.transaction.TransactionCategory;
import com.group33.swa.model.transaction.TransactionType;
import com.group33.swa.webServices.account.AccountService;
import com.group33.swa.webServices.transaction.TransactionMaker;
import com.group33.swa.webServices.transaction.TransactionService;
import com.group33.swa.webServices.user.UserService;
import com.group33.swa.webController.websocket.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/** TransactionController Class */
@Controller
public class TransactionController extends Observable {

  @Autowired private TransactionService transactionService;
  @Autowired private UserService userSerivce;
  @Autowired private AccountService accountService;
  @Autowired private TransactionMaker transactionServiceFacade;

  @Autowired public WebSocketHandler webSocketHandler;

  @PostConstruct
  public void init() {

    System.out.println("Trying to add WebSocketHandler");
    this.addObserver(webSocketHandler);
    System.out.println(webSocketHandler);
    System.out.println("Added WebSocketHandler");
  }

  /**
   * If amount is negativ, fromUser und toUser, fromAccount, toAccount will switch. Changes:
   * 12.11.2019 @RequestParam fromUser, required = false @RequestParam toUser, required = false
   *
   * <p>This Method represents a REST-Interface to create a new Transaction
   *
   * @param fromAccountID accountID where transaction is comming from
   * @param toAccountID accountID which the transaction is for
   * @param fromUserID userID from which user the transaction is beeing created
   * @param amount represents the actual amount of the specified currency
   * @param transactionDate represents the date or timestamp when the transaction was created
   * @param transactionDescription Description for the transaction
   * @param request REQ-Client informations
   * @param response RES-Client informations
   * @return {@link Transaction}
   */
  @RequestMapping(
      value = "/newTransaction",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseBody
  public Transaction newTransaction(
      @RequestParam(value = "fromAccount", required = true) Integer fromAccountID,
      @RequestParam(value = "toAccount", required = true) Integer toAccountID,
      @RequestParam(value = "fromUser", required = true) Integer fromUserID,
      @RequestParam(value = "amount", required = true) Float amount,
      @RequestParam(value = "date", required = true) String transactionDate,
      @RequestParam(value = "description", required = true) String transactionDescription,
      @RequestParam(value = "transactionCategory", required = true) String transactionCategory,
      @RequestParam(value = "transactionType", required = true) String transactionType,
      HttpServletRequest request,
      HttpServletRequest response) {

    Transaction returnTransaction = null;

    ErrorMessage errorMessage = new ErrorMessage();
    SuccessMessage successMessage = new SuccessMessage();
    Message wsMessage;

    // hotfix
    if (fromAccountID == 1) {
      fromUserID = 1;
    }

    Account spendingAccount = accountService.getAccount(fromUserID, fromAccountID);
    if (
        spendingAccount == null ||
        fromAccountID == null ||
        toAccountID == null ||
        amount == null ||
        transactionDate == null || transactionDate.equals("") ||
        transactionDescription == null || transactionDescription.equals("") ||
        transactionCategory == null || transactionCategory.equals("") ||
        transactionType == null || transactionType.equals("")
    ){

      errorMessage.setMessage(
          "Wrong data input. Please check your input");
      wsMessage = new WebsocketMessageDecorator(errorMessage);
      this.notifyObservers(wsMessage);
      return null;
    }

    if(amount < 0){
      errorMessage.setMessage(
          "Amount is negativ. Transaction will not be executed");
      wsMessage = new WebsocketMessageDecorator(errorMessage);
      this.notifyObservers(wsMessage);
      return null;
    }

    System.out.println("Balance: " + spendingAccount.getBalance());
    System.out.println("Amount: " + amount);
    System.out.println(""+spendingAccount.getBalance()+" - " + amount + " > " + spendingAccount.getOverdraftlimit() * (-1));
    if ((spendingAccount.getBalance() - amount) > spendingAccount.getOverdraftlimit() * (-1)) {
      // Then its possible
      returnTransaction =
          transactionServiceFacade.processTransaction(
              fromAccountID,
              toAccountID,
              fromUserID,
              amount,
              transactionType,
              transactionDate,
              transactionDescription,
              transactionCategory);

      System.out.println(returnTransaction);

    } else {
      errorMessage.setMessage(
          "Transaction overdraftlimit exceeded. Transaction will not be executed");
      wsMessage = new WebsocketMessageDecorator(errorMessage);
      this.notifyObservers(wsMessage);
      return null;
    }

    if (returnTransaction != null) {
      successMessage.setMessage("Transaction completed");
      wsMessage = new WebsocketMessageDecorator(successMessage);
      this.notifyObservers(wsMessage);
      return returnTransaction;
    } else {
      errorMessage.setMessage("Transaction will not be executed, wrong accountID!");
      wsMessage = new WebsocketMessageDecorator(errorMessage);
      this.notifyObservers(wsMessage);
      return null;
    }
  }

  /**
   * @param userID unique identifier of the user
   * @param request REQ-Client informations
   * @param response RES-Client informations
   * @return {@link List}&lt;{@link Transaction}&gt;
   */
  @RequestMapping(
      value = "/getAllTransactionByUserID",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseBody
  public List<Transaction> getAllTransactionByUserID(
      @RequestParam(value = "userID", required = true) int userID,
      HttpServletRequest request,
      HttpServletRequest response) {
    // System.out.println("CALLED GETALLTRANSACTIONSBYUSERID");
    /**
     * For Displaying right amount of the balance despite only saving one transaction in the DB
     * further calculations had to be done
     */
    if (userSerivce.userExistByUserID(userID)) {
      List<Transaction> userTransactionList = new ArrayList<>();
      List<Account> accountList = accountService.getAllAccountsbyUserID(userID);
      for (Account tempacc : accountList) {
        List<Transaction> transactionList =
            transactionService.getAllTransactionsByAccountID(tempacc.getAccountID());
        for (Transaction temptrans : transactionList) {
          userTransactionList.add(temptrans);
        }
      }

      return userTransactionList;
    }
    return new ArrayList<>();
  }

  /**
   * @param accountID unique identifier of the account
   * @param request REQ-Client informations
   * @param response RES-Client informations
   * @return {@link List}&lt;{@link Transaction}&gt;
   */
  @RequestMapping(
      value = "/getAllTransactionsByAccountID",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseBody
  public List<Transaction> getAllTransactionByAccountID(
      @RequestParam(value = "accountID", required = true) int accountID,
      HttpServletRequest request,
      HttpServletRequest response) {

    List<Transaction> allUserTransactionList =
        transactionService.getAllTransactionsByAccountID(accountID);
    /**
     * For Displaying right amount of the balance despite only saving one transaction in the DB
     * further calculations had to be done
     */
    return calcDisplayAmountTransaction(accountID, allUserTransactionList);
  }

  /**
   * @param accountID unique identifier of the account
   * @param transactionCategory category of the transaction
   * @param request
   * @param response
   * @return {@link List}&lt;{@link Transaction}&gt;
   */
  @RequestMapping(
      value = "/getAllTransactionFromByAccountIDandTransactionCategory",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseBody
  public List<Transaction> getAllTransactionFromByAccountIDandTransactionCategory(
      @RequestParam(value = "accountID", required = true) int accountID,
      @RequestParam(value = "transactionCategory", required = true) String transactionCategory,
      HttpServletRequest request,
      HttpServletRequest response) {
    TransactionCategory type = TransactionCategory.valueOf(transactionCategory);

    List<Transaction> list =
        transactionService.getTransactionsByFromAccountIDAndTransactionCategory(accountID, type);
    /**
     * For Displaying right amount of the balance despite only saving one transaction in the DB
     * further calculations had to be done
     */
    return calcDisplayAmountTransaction(accountID, list);
  }

  /**
   * calculates the amount to display in transactions makes it possible to have only one transaction
   * and display it for both account correctly
   *
   * @param accountID unique identifier of the account
   * @param list
   * @return {@link List}&lt;{@link Transaction}&gt;
   */
  private List<Transaction> calcDisplayAmountTransaction(
      @RequestParam(value = "accountID", required = true) int accountID, List<Transaction> list) {
    for (Transaction trans : list) {
      if (trans.getToAccountID() == accountID) {
        if (trans.getTransactionType() == TransactionType.BUSINESS)
          trans.setAmount(trans.getAmount() / (float) (1 + 0.015));
      }
      if (trans.getTransactionType() == TransactionType.CRYPTO)
        trans.setAmount(trans.getAmount() / (float) (1 + 0.0025));
    }

    return list;
  }
}
