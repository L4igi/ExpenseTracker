package com.group33.swa.webController.transaction;

import com.group33.swa.model.transaction.Transaction;
import com.group33.swa.model.transaction.TransactionCategory;
import com.group33.swa.webServices.account.AccountService;
import com.group33.swa.webServices.transaction.TransactionService;
import com.group33.swa.webServices.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TransactionControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private UserService userService;
  @Autowired private AccountService accountService;
  @Autowired private TransactionService transactionService;
  /**
   * Sets up multiple Users and different Accounts for testing of the transactions
   *
   * @throws Exception
   */
  @BeforeEach
  public void setUp() throws Exception {
    // setup Users for Testing
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/createUser")
                .param("username", "testUser1")
                .param("imageIcon", "Icon"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/createUser")
                .param("username", "testUser2")
                .param("imageIcon", "Icon"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/createUser")
                .param("username", "testUser3")
                .param("imageIcon", "Icon"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    // setup Accounts for Testing

    // creating new Giro Account
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newAccount")
                .param("userID", "2")
                .param("accountName", "testGiroAcc")
                .param("accountType", "GIRO")
                .param("overdraftlimit", "1000"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    System.out.println(accountService.getAllAccountsbyUserID(2).size());

    // creating new Giro Account other user default overdraft limit
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newAccount")
                .param("userID", "2")
                .param("accountName", "testGiroAcc2")
                .param("accountType", "GIRO")
                .param("overdraftlimit", ""))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    // creating new Student Account
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newAccount")
                .param("userID", "3")
                .param("accountName", "testStudentAcc")
                .param("accountType", "STUDENT")
                .param("overdraftlimit", "100")
                .param("studentNumber", "01447910"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    // creating new Cash Account
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newAccount")
                .param("userID", "4")
                .param("accountName", "testCashAcc")
                .param("accountType", "CASH")
                .param("overdraftlimit", "100")
                .param("startAmount", "100"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
  }

  /**
   * Testing new Transaction and checking if data saved in database is correct checks depending on
   * fee calculation strategy if amount was calculated correct
   *
   * @throws Exception
   */
  @Test
  void newTransaction() throws Exception {
    // Private Transaction Test 2 accounts same user
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newTransaction")
                .param("fromAccount", "2")
                .param("toAccount", "3")
                .param("fromUser", "2")
                .param("amount", "100")
                .param("date", "2020-01-06")
                .param("description", "Test")
                .param("transactionCategory", "SALARY")
                .param("transactionType", "PRIVATE"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    // check transaction Category and Type
    Transaction checkTransaction = transactionService.getTransactionByTransactionID(2);
    assertEquals("SALARY", checkTransaction.getTransactionCategory().toString());
    assertEquals("PRIVATE", checkTransaction.getTransactionType().toString());
    // check transaction Date and Description
    assertEquals("2020-01-06", checkTransaction.getTransactionDate().toString());
    assertEquals("Test", checkTransaction.getTransactionDescription());
    // check transaction amount for private transaction
    assertEquals((float) 100, checkTransaction.getAmount());

    // Business Transaction Test 2 accounts different user
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newTransaction")
                .param("fromAccount", "2")
                .param("toAccount", "4")
                .param("fromUser", "2")
                .param("amount", "100")
                .param("date", "2016-02-06")
                .param("description", "TestBusiness")
                .param("transactionCategory", "DIVIDEND")
                .param("transactionType", "BUSINESS"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    // check transaction Category and Type
    checkTransaction = transactionService.getTransactionByTransactionID(3);
    assertEquals("DIVIDEND", checkTransaction.getTransactionCategory().toString());
    assertEquals("BUSINESS", checkTransaction.getTransactionType().toString());
    // check transaction Date and Description
    assertEquals("2016-02-06", checkTransaction.getTransactionDate().toString());
    assertEquals("TestBusiness", checkTransaction.getTransactionDescription());
    // check transaction amount for private transaction
    assertEquals((float) 100 * 1.015f, checkTransaction.getAmount());

    // Crypto Transaction Test from default Account to User Account
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newTransaction")
                .param("fromAccount", "1")
                .param("toAccount", "4")
                .param("fromUser", "1")
                .param("amount", "100")
                .param("date", "2016-02-06")
                .param("description", "TestCrypto")
                .param("transactionCategory", "TRANSPORTATION")
                .param("transactionType", "CRYPTO"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    // check transaction Category and Type
    checkTransaction = transactionService.getTransactionByTransactionID(4);
    assertEquals("TRANSPORTATION", checkTransaction.getTransactionCategory().toString());
    assertEquals("CRYPTO", checkTransaction.getTransactionType().toString());
    // check transaction Date and Description
    assertEquals("2016-02-06", checkTransaction.getTransactionDate().toString());
    assertEquals("TestCrypto", checkTransaction.getTransactionDescription());
    // check transaction amount for private transaction
    assertEquals(Math.round(100 * 100.0 * 1.0025f) / 100.0, checkTransaction.getAmount());
  }

  /**
   * Tests getting all transactions from database providing a User ID
   *
   * @throws Exception
   */
  @Test
  void getAllTransactionsByUserID() throws Exception {
    // Private Transaction Test 2 accounts same user
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newTransaction")
                .param("fromAccount", "2")
                .param("toAccount", "3")
                .param("fromUser", "2")
                .param("amount", "100")
                .param("date", "2020-01-06")
                .param("description", "Test1")
                .param("transactionCategory", "SALARY")
                .param("transactionType", "PRIVATE"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    // Crypto Transaction Test 2 accounts same user
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newTransaction")
                .param("fromAccount", "2")
                .param("toAccount", "3")
                .param("fromUser", "2")
                .param("amount", "50")
                .param("date", "2020-01-06")
                .param("description", "Test2")
                .param("transactionCategory", "SALARY")
                .param("transactionType", "CRYPTO"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    // Business Transaction Test from default Account
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newTransaction")
                .param("fromAccount", "1")
                .param("toAccount", "3")
                .param("fromUser", "1")
                .param("amount", "50")
                .param("date", "2020-01-06")
                .param("description", "Test3")
                .param("transactionCategory", "SALARY")
                .param("transactionType", "BUSINESS"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;

    mockMvc
        .perform(MockMvcRequestBuilders.get("/getAllTransactionByUserID").param("userID", "2"))
        .andExpect(MockMvcResultMatchers.status().isOk());

    List<Transaction> testTransactions = transactionService.getAllTransactionsByUserID(2);
    // to see if only fromAccount counts and number is correct
    assertEquals(2, testTransactions.size());

    assertEquals("Test1", testTransactions.get(0).getTransactionDescription());
    assertEquals("Test2", testTransactions.get(1).getTransactionDescription());
  }

  /**
   * test getting all transactions from databaae of one Account
   *
   * @throws Exception
   */
  @Test
  void getAllTransactionByAccountID() throws Exception {
    // Private Transaction Test 2 accounts same user
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newTransaction")
                .param("fromAccount", "2")
                .param("toAccount", "3")
                .param("fromUser", "2")
                .param("amount", "100")
                .param("date", "2020-01-06")
                .param("description", "Test1")
                .param("transactionCategory", "SALARY")
                .param("transactionType", "PRIVATE"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    // Crypto Transaction Test 2 accounts same user
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newTransaction")
                .param("fromAccount", "2")
                .param("toAccount", "3")
                .param("fromUser", "2")
                .param("amount", "50")
                .param("date", "2020-01-06")
                .param("description", "Test2")
                .param("transactionCategory", "SALARY")
                .param("transactionType", "CRYPTO"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    // Business Transaction Test from default Account
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newTransaction")
                .param("fromAccount", "1")
                .param("toAccount", "3")
                .param("fromUser", "1")
                .param("amount", "50")
                .param("date", "2020-01-06")
                .param("description", "Test3")
                .param("transactionCategory", "SALARY")
                .param("transactionType", "BUSINESS"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/getAllTransactionsByAccountID").param("accountID", "2"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    List<Transaction> testTransactions = transactionService.getAllTransactionsByAccountID(2);

    assertEquals(2, testTransactions.size());
  }

  /**
   * test getting all transactions from databaae of one Account and a certain category
   *
   * @throws Exception
   */
  @Test
  void getAllTransactionFromByAccountIDandTransactionCategory() throws Exception {
    // Private Transaction Test 2 accounts same user
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newTransaction")
                .param("fromAccount", "2")
                .param("toAccount", "3")
                .param("fromUser", "2")
                .param("amount", "100")
                .param("date", "2020-01-06")
                .param("description", "Test1")
                .param("transactionCategory", "SALARY")
                .param("transactionType", "PRIVATE"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    // Crypto Transaction Test 2 accounts same user
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newTransaction")
                .param("fromAccount", "2")
                .param("toAccount", "3")
                .param("fromUser", "2")
                .param("amount", "50")
                .param("date", "2020-01-06")
                .param("description", "Test2")
                .param("transactionCategory", "SALARY")
                .param("transactionType", "CRYPTO"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    // Business Transaction Test from default Account
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newTransaction")
                .param("fromAccount", "1")
                .param("toAccount", "3")
                .param("fromUser", "1")
                .param("amount", "50")
                .param("date", "2020-01-06")
                .param("description", "Test3")
                .param("transactionCategory", "TRANSPORTATION")
                .param("transactionType", "BUSINESS"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    // checking all transactions account 2 category salary
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/getAllTransactionFromByAccountIDandTransactionCategory")
                .param("accountID", "2")
                .param("transactionCategory", "SALARY"))
        .andExpect(MockMvcResultMatchers.status().isOk());

    List<Transaction> testTransactions =
        transactionService.getTransactionsByFromAccountIDAndTransactionCategory(
            2, TransactionCategory.valueOf("SALARY"));
    ;

    assertEquals(2, testTransactions.size());
    assertEquals("SALARY", testTransactions.get(0).getTransactionCategory().toString());
    assertEquals("SALARY", testTransactions.get(1).getTransactionCategory().toString());

    // checking all transactions account 2 category transportation
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/getAllTransactionFromByAccountIDandTransactionCategory")
                .param("accountID", "1")
                .param("transactionCategory", "TRANSPORTATION"))
        .andExpect(MockMvcResultMatchers.status().isOk());

    testTransactions =
        transactionService.getTransactionsByFromAccountIDAndTransactionCategory(
            1, TransactionCategory.valueOf("TRANSPORTATION"));
    ;

    assertEquals(1, testTransactions.size());
    assertEquals("TRANSPORTATION", testTransactions.get(0).getTransactionCategory().toString());
  }

  @Test
  void requestYears() throws Exception {

    // Private Transaction Test 2 accounts same user
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newTransaction")
                .param("fromAccount", "2")
                .param("toAccount", "3")
                .param("fromUser", "2")
                .param("amount", "100")
                .param("date", "2020-01-06")
                .param("description", "Test1")
                .param("transactionCategory", "SALARY")
                .param("transactionType", "PRIVATE"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    // Crypto Transaction Test 2 accounts same user
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newTransaction")
                .param("fromAccount", "2")
                .param("toAccount", "3")
                .param("fromUser", "2")
                .param("amount", "50")
                .param("date", "2020-01-06")
                .param("description", "Test2")
                .param("transactionCategory", "SALARY")
                .param("transactionType", "CRYPTO"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    // Business Transaction Test from default Account
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newTransaction")
                .param("fromAccount", "1")
                .param("toAccount", "3")
                .param("fromUser", "1")
                .param("amount", "50")
                .param("date", "2010-01-06")
                .param("description", "Test3")
                .param("transactionCategory", "SALARY")
                .param("transactionType", "BUSINESS"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;

    mockMvc
        .perform(MockMvcRequestBuilders.get("/getYearsByUserID").param("userID", "2"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/getYearsByUserID")
                .param("userID", "2")
                .param("accountID", "2"))
        .andExpect(MockMvcResultMatchers.status().isOk());

    List<Integer> testYearList = new ArrayList<>();
    testYearList.add(2020);
    testYearList.add(2010);

    assertEquals(testYearList, transactionService.getYearsByUserID(2));

    testYearList = new ArrayList<>();

    assertEquals(testYearList, transactionService.getYearsByAccountID(4));
  }

  @Test
  void requestMonthsByYear() throws Exception {

    // Private Transaction Test 2 accounts same user
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newTransaction")
                .param("fromAccount", "2")
                .param("toAccount", "3")
                .param("fromUser", "2")
                .param("amount", "100")
                .param("date", "2020-01-06")
                .param("description", "Test1")
                .param("transactionCategory", "SALARY")
                .param("transactionType", "PRIVATE"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    // Crypto Transaction Test 2 accounts same user
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newTransaction")
                .param("fromAccount", "2")
                .param("toAccount", "3")
                .param("fromUser", "2")
                .param("amount", "50")
                .param("date", "2020-04-06")
                .param("description", "Test2")
                .param("transactionCategory", "SALARY")
                .param("transactionType", "CRYPTO"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    // Business Transaction Test from default Account
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newTransaction")
                .param("fromAccount", "1")
                .param("toAccount", "3")
                .param("fromUser", "1")
                .param("amount", "50")
                .param("date", "2010-01-06")
                .param("description", "Test3")
                .param("transactionCategory", "SALARY")
                .param("transactionType", "BUSINESS"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/getMonthsByYearByUserID")
                .param("userID", "2")
                .param("year", "2020"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/getMonthsByYearByUserID")
                .param("userID", "2")
                .param("year", "2020")
                .param("accountID", "2"))
        .andExpect(MockMvcResultMatchers.status().isOk());

    List<Integer> testMonthList = new ArrayList<>();
    testMonthList.add(0);
    testMonthList.add(3);

    assertEquals(testMonthList, transactionService.getMonthsByYearByUserID(2, 2020));

    testMonthList = new ArrayList<>();

    assertEquals(testMonthList, transactionService.getMonthsByYearByUserID(5, 2020));
  }

  @Test
  void requestDaysByMonthByYear() throws Exception {

    // Private Transaction Test 2 accounts same user
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newTransaction")
                .param("fromAccount", "2")
                .param("toAccount", "3")
                .param("fromUser", "2")
                .param("amount", "100")
                .param("date", "2020-01-06")
                .param("description", "Test1")
                .param("transactionCategory", "SALARY")
                .param("transactionType", "PRIVATE"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    // Crypto Transaction Test 2 accounts same user
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newTransaction")
                .param("fromAccount", "2")
                .param("toAccount", "3")
                .param("fromUser", "2")
                .param("amount", "50")
                .param("date", "2020-04-06")
                .param("description", "Test2")
                .param("transactionCategory", "SALARY")
                .param("transactionType", "CRYPTO"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    // Business Transaction Test from default Account
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newTransaction")
                .param("fromAccount", "1")
                .param("toAccount", "3")
                .param("fromUser", "1")
                .param("amount", "50")
                .param("date", "2010-01-06")
                .param("description", "Test3")
                .param("transactionCategory", "SALARY")
                .param("transactionType", "BUSINESS"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/getDaysByMonthsByYearByUserID")
                .param("userID", "2")
                .param("year", "2020")
                .param("month", "0"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/getDaysByMonthsByYearByUserID")
                .param("userID", "2")
                .param("year", "2020")
                .param("month", "0")
                .param("accountID", "2"))
        .andExpect(MockMvcResultMatchers.status().isOk());

    List<Integer> testDayList = new ArrayList<>();
    testDayList.add(6);

    // assertEquals(testDayList,transactionService.getDaysByMonthsByYearByUserID(2, 2020, 0));

    testDayList = new ArrayList<>();

    assertEquals(testDayList, transactionService.getDaysByMonthsByYearByAccountID(6, 2020, 0));
  }
}
