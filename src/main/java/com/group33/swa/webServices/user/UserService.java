package com.group33.swa.webServices.user;

import com.group33.swa.exceptions.UserException;
import com.group33.swa.model.user.User;

import java.util.List;
/** UserService Interface */
public interface UserService {
  /**
   * Login function that a USer is able to login in to the system
   *
   * @param username represents the login username of the User
   * @return {@link User}
   */
  User login(String username);

  /**
   * Function to create a User and store it in the Database
   *
   * @param user UserDTO to store in the Database
   * @return {@link User} returns a User if successfully stored
   * @throws UserException
   */
  User createUser(User user) throws UserException;

  /**
   * Recevies all Users stored in the Database
   *
   * @return {@link User}
   */
  List<User> getAllUsers();

  /**
   * Checks if a user exists in the database
   *
   * @param userID uses the userID to check if a user
   * @return {@link boolean}
   */
  boolean userExistByUserID(int userID);

  /**
   * returns User by Username
   *
   * @param userName
   * @return {@link User}
   */
  User findUserByUserName(String userName);

  /**
   * Updates the User-Model in the Database
   *
   * @param user
   * @return {@link User}
   */
  User update(User user);
}
