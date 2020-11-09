package com.group33.swa.model.user;

import org.springframework.lang.Nullable;

import javax.persistence.*;

/**
 * This Class represents a User-Entity and is used as a DTO between Java and the Database.
 *
 * @author Alexander Garber
 */
@Entity
public class User {

  @Id
  @Column(name = "user_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  @Column(unique = true)
  String username;

  @Nullable
  @Column(columnDefinition = "CLOB")
  String imageIcon;

  public User() {}

  /** @param username unique identifier of user, userid auto increment in background */
  public User(String username) {
    this.username = username;
  }
  /**
   * @param id unique identifier
   * @param username unique identifier of user
   */
  public User(int id, String username) {
    this.id = id;
    this.username = username;
  }

  /**
   * @param id unique identifier
   * @param username unique identifier of user
   * @param imageIcon imageData of icon of the user
   */
  public User(int id, String username, String imageIcon) {
    this.id = id;
    this.username = username;
    this.imageIcon = imageIcon;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getImageIcon() {
    return imageIcon;
  }

  public void setImageIcon(String imageIcon) {
    this.imageIcon = imageIcon;
  }

  //  public List<User> getColleagues() {
  //    return colleagues;
  //  }
  //
  //  public void setColleagues(List<User> colleagues) {
  //    this.colleagues = colleagues;
  //  }
  //
  //  public List<User> getUsermates() {
  //    return usermates;
  //  }
  //
  //  public void setUsermates(List<User> usermates) {
  //    this.usermates = usermates;
  //  }

  @Override
  public String toString() {
    return "User{" + "id=" + id + ", username='" + username + '\'' + '}';
  }
}
