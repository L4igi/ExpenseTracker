package com.group33.swa.repository.user;

import com.group33.swa.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/** UserRepository Class */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
  /**
   * returns all users by username
   *
   * @param username unique identifier of user
   * @return {@link User}
   */
  User findUserByUsername(String username);

  /**
   * returns all users
   *
   * @return {@link List}&lt;{@link User}&gt;
   */
  List<User> findAll();

  /**
   * returns true if user is in database
   *
   * @param userID unique identifier of user
   * @return boolean
   */
  boolean existsById(int userID);
}
