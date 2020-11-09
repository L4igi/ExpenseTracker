package com.group33.swa.webController.account;

import com.group33.swa.model.account.Account;
import com.group33.swa.model.user.User;
import com.group33.swa.webServices.account.AccountService;
import com.group33.swa.webServices.user.UserService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AccountControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private UserService userService;
  @Autowired private AccountService accountService;

  /**
   * Sets up User accounts before each test to make account testing easier
   *
   * @throws Exception
   */
  @BeforeEach
  public void setUp() throws Exception {
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
  }

  /** Tests if the default User is created on Program init */
  @Test
  void init() {
    List<User> testUserList = userService.getAllUsers();

    assertEquals("TransactionUser", testUserList.get(0).getUsername());
  }

  /**
   * Test Creation of multiple different new Accounts throws Exception if Account creation was not
   * successful (e.g. Account with same name already exists)
   *
   * @throws Exception
   */
  @Test
  void newAccount() throws Exception {
    // creating new Giro Account
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newAccount")
                .param("userID", "2")
                .param("accountName", "testGiroAcc")
                .param("accountType", "GIRO")
                .param("overdraftlimit", "100"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    Account testAccounts = accountService.getAccount(2, 2);

    assertEquals("testGiroAcc", testAccounts.getAccountName());
    assertEquals("GIRO", testAccounts.getAccountType().toString());
    //System.out.println(accountService.getAllAccountsbyUserID(2).size());

    // creating new Giro Account other user default overdraft limit
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newAccount")
                .param("userID", "3")
                .param("accountName", "testGiroAcc2")
                .param("accountType", "GIRO")
                .param("overdraftlimit", ""))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    testAccounts = accountService.getAccount(3, 3);

    assertEquals("testGiroAcc2", testAccounts.getAccountName());
    assertEquals("GIRO", testAccounts.getAccountType().toString());
    // creating new Student Account
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newAccount")
                .param("userID", "2")
                .param("accountName", "testStudentAcc")
                .param("accountType", "STUDENT")
                .param("overdraftlimit", "100")
                .param("studentNumber", "01447910"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    testAccounts = accountService.getAccount(2, 4);

    assertEquals("testStudentAcc", testAccounts.getAccountName());
    assertEquals("STUDENT", testAccounts.getAccountType().toString());
    // creating new Cash Account
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newAccount")
                .param("userID", "2")
                .param("accountName", "testCashAcc")
                .param("accountType", "CASH")
                .param("overdraftlimit", "100")
                .param("startAmount", "100"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
    testAccounts = accountService.getAccount(2, 5);

    assertEquals("testCashAcc", testAccounts.getAccountName());
    assertEquals("CASH", testAccounts.getAccountType().toString());

    // creating new Account with existing name
    try {
      System.out.println(" Message to be thrown");
      mockMvc.perform(
          MockMvcRequestBuilders.get("/newAccount")
              .param("userID", "2")
              .param("accountName", "testGiroAcc3")
              .param("accountType", "GIRO")
              .param("overdraftlimit", "100"));
      ;
    } catch (Exception e) {
      String expectedMessage = "Account Creation Error";
      String actualMessage = e.getMessage();
      System.out.println(e.getMessage() + " Message caught");
      // assertTrue(actualMessage.contains(expectedMessage));
    }
    try {
      System.out.println(" Message to be thrown");
      mockMvc.perform(
              MockMvcRequestBuilders.get("/newAccount")
                      .param("userID", "2")
                      .param("accountName", "testGiroAcc3"));
      ;
    } catch (Exception e) {
      String expectedMessage = "Account Creation Error";
      String actualMessage = e.getMessage();
      System.out.println(e.getMessage() + " Message caught");
      // assertTrue(actualMessage.contains(expectedMessage));
    }
  }

  /**
   * creates multiple accounts checks if requestallaccounts return right values saved in database
   *
   * @throws Exception
   */
  @Test
  void requestAllAccounts() throws Exception {
    // creating new Giro Account
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/newAccount")
                .param("userID", "2")
                .param("accountName", "testGiroAcc")
                .param("accountType", "GIRO")
                .param("overdraftlimit", "100"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    ;
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
    mockMvc
        .perform(MockMvcRequestBuilders.get("/getAllAccounts").param("userID", "2"))
        .andExpect(MockMvcResultMatchers.status().isOk());

    List<Account> testAccounts = accountService.getAllAccountsbyUserID(2);

    assertEquals(2, testAccounts.size());

    assertEquals("testGiroAcc", testAccounts.get(0).getAccountName());
    assertEquals("testGiroAcc2", testAccounts.get(1).getAccountName());
    assertEquals((float) 100, testAccounts.get(0).getOverdraftlimit());
    assertEquals((float) 0, testAccounts.get(1).getOverdraftlimit());
  }


}
