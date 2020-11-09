package com.group33.swa.webController.user;

import com.group33.swa.exceptions.UserException;
import com.group33.swa.model.user.User;
import com.group33.swa.webServices.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
/** UserController Class */
@Controller
public class UserController {

  @Autowired private UserService userService;

  /**
   * Initial Method to setup the User-Environment
   *
   * @throws UserException
   */
  @PostConstruct
  public void init() throws UserException {
    System.out.println("Init UserController");
    if (userService.getAllUsers().isEmpty()) {
      User defaultUser = new User();
      defaultUser.setUsername("TransactionUser");
      defaultUser.setId(0);
      userService.createUser(defaultUser);
    }
  }

  /**
   * Method to log a user into the system.
   *
   * @param username name of the user
   * @return {@link User}
   * @throws UserException
   */
  @RequestMapping(value = "/login", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public User login(@RequestParam(value = "username", required = true) String username)
      throws UserException {

    User user = userService.login(username);
    /** if user not exists, throw excpetion Should not be the case */
    if (user == null) {
      throw new UserException("Login-Error: Cannot login to Useraccount");
    } else {
      return user;
    }
  }

  /**
   * Method to create a new user for the system. It checks of the user already exist. If not, a new
   * user will be created.
   *
   * @param username name of the user
   * @param imageIcon icon of the user-account
   * @return {@link User}
   * @throws UserException
   */
  @RequestMapping(value = "/createUser", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public User createUser(
      @RequestParam(value = "username", required = true) String username,
      @RequestParam(value = "imageIcon", required = true) String imageIcon)
      throws UserException {

    // Creating first User creates User 0 for external transactions
    User current_user = userService.login(username);
    /** if user already exists, logs user in */
    if (current_user != null) {
      return current_user;
      //      throw new UserException("CreateUser-Error: User already exist!");
    } else {
      User newUser = new User();
      newUser.setUsername(username);
      newUser.setImageIcon(imageIcon);

      User savedUser = userService.createUser(newUser);

      return savedUser;
    }
  }

  /** @return {@link List}&lt;{@link User}&gt; */
  @RequestMapping(value = "/getAllUsers", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<User> getAllUsers() {

    List<User> userList = userService.getAllUsers();
    if (userList.size() != 0) {
      return userList;
    } else {
      return new ArrayList<>();
    }
  }
}
