package org.tsaap.directory

class UserAccountService {

  /**
   * Add a new user, the user is not enabled
   * @param firstName the first name
   * @param  lastName the last name
   * @param username the username
   * @param email the email
   * @param password the password
   * @return the created user
   */
  User addUser(User user, Role mainRole, Boolean enabled = false) {
    user.enabled = enabled
    user.save()
    if (!user.hasErrors()) {
      UserRole.create(user, mainRole)
    }
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
