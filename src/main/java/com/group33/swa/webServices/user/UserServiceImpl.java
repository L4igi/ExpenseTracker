package com.group33.swa.webServices.user;

import com.group33.swa.exceptions.UserException;
import com.group33.swa.model.user.User;
import com.group33.swa.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** UserService Class */
@Service
public class UserServiceImpl implements UserService {

  @Autowired UserRepository userRepository;

  /**
   * @param username represents the login username of the User
   * @return {@link User}
   */
  @Override
  public User login(String username) {
    return userRepository.findUserByUsername(username);
  }

  /**
   * @param user UserDTO to store in the Database
   * @return {@link User}
   * @throws UserException
   */
  @Override
  public User createUser(User user) throws UserException {
    User returnUser = userRepository.save(user);
    if (returnUser == null) {
      throw new UserException("User Create Error");
    }
    return returnUser;
  }

  /** @return {@link List}&lt;{@link User}&gt; */
  @Override
  public List<User> getAllUsers() {
    return (List<User>) userRepository.findAll();
  }

  /**
   * @param userID uses the userID to check if a user
   * @return {@link boolean}
   */
  @Override
  public boolean userExistByUserID(int userID) {
    return (boolean) userRepository.existsById(userID);
  }

  /**
   * @param userName
   * @return {@link User}
   */
  @Override
  public User findUserByUserName(String userName) {
    return userRepository.findUserByUsername(userName);
  }

  /**
   * Updates the User-Model in the Database
   *
   * @param user
   * @return {@link User}
   */
  @Override
  public User update(User user) {
    return userRepository.save(user);
  }
}
