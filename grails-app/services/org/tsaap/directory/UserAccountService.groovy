package org.tsaap.directory

class UserAccountService {

  /**
   * Add a new user, the user is not enabled
   * @param username the username
   * @param email the email
   * @param password the password
   * @return the created user
   */
  User addUser(String username, String email, String password, Role mainRole) {
    User user = new User(username: username, email: email, password: password)
    user.save()
    UserRole.create(user, mainRole)
    user
  }

  /**
   * Enable a user
   * @param user the user to enabled
   * @return the processed user
   */
  User enableUser(User user) {
    user.enabled = true
    user.save()
    user
  }

  /**
   * Disable a user
   * @param user the user to disabled
   * @return the processed user
   */
  User disableUser(User user) {
    user.enabled = false
    user.save()
    user
  }

  /**
   *
   * @param newPassword  the new password
   * @param user the user
   * @return the processed user
   */
  User updatePasswordForUser(String newPassword, User user) {
    user.password = newPassword
    user.save()
    user
  }
}
