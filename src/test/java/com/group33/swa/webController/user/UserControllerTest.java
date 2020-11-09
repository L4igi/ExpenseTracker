package com.group33.swa.webController.user;

import com.group33.swa.model.user.User;
import com.group33.swa.webServices.user.UserService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class UserControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private UserService userService;

  @Test
  /**
   * Tests login tries to login if user does not exist --> throws exception creates user and checks
   * if login returns data equal to user created in test
   */
  void login() throws Exception {
    try {
      mockMvc.perform(MockMvcRequestBuilders.get("/login").param("username", "testUser"));
    } catch (Exception e) {
      String expectedMessage = "Login-Error: Cannot login to Useraccount";
      String actualMessage = e.getMessage();
      assertTrue(actualMessage.contains(expectedMessage));
    }
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/createUser")
                .param("username", "testUser")
                .param("imageIcon", "Icon"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    mockMvc
        .perform(MockMvcRequestBuilders.get("/login").param("username", "testUser"))
        .andExpect(MockMvcResultMatchers.status().isOk());

    User testUser = userService.login("testUser");

    assertEquals("testUser", testUser.getUsername());
    assertEquals("Icon", testUser.getImageIcon());
  }

  /**
   * Tests createUser creates user and checks if data in database is equal to user created in test
   */
  @Test
  void createUser() throws Exception {
    // mockMvc.perform(MockMvcRequestBuilders.get("/createUser").requestAttr("testUser",
    // "Icon")).andExpect(MockMvcResultMatchers.status().is(400));
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/createUser")
                .param("username", "testUser")
                .param("imageIcon", "Icon"))
        .andExpect(MockMvcResultMatchers.status().isOk());

    User testUser = userService.login("testUser");

    assertEquals("testUser", testUser.getUsername());
    assertEquals("Icon", testUser.getImageIcon());
  }

  /**
   * creates multiple Useres checks if functions getAllUSers returns all Users created
   *
   * @throws Exception
   */
  @Test
  void getAllUsers() throws Exception {
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

    mockMvc
        .perform(MockMvcRequestBuilders.get("/getAllUsers"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    List<User> testUserList = userService.getAllUsers();

    assertEquals(4, testUserList.size());

    assertEquals("testUser1", testUserList.get(1).getUsername());
    assertEquals("testUser2", testUserList.get(2).getUsername());
    assertEquals("testUser3", testUserList.get(3).getUsername());
  }
}
