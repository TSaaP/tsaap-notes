package org.tsaap.directory

import org.gcontracts.annotations.Requires

class UserAccountService {

  SettingsService settingsService

  private static final HashMap<String,String> LANGUAGES_SUPPORTED = [
          'English':'en',
          'Français':'fr'
  ]

  /**
   * Check if the language passed in parameter is in LANGUAGES_SUPPORTED list
   * @param language the language to check
   * @return if LANGUAGES_SUPPORTED contains language
   */
  boolean languageIsSupported(String language) {
    LANGUAGES_SUPPORTED.containsValue(language)
  }

  /**
   * Add a new user, the user is not enabled
   * @param enabled flag that indicate if the account is enabled
   * @param mainRole the main role for the user
   * @param user the user to be added
   * @return the created user
   */
  User addUser(User user, Role mainRole, Boolean enabled = false, boolean checkEmailAccount = false) {
    user.enabled = enabled
    user.save()
    if (!user.hasErrors()) {
      UserRole.create(user, mainRole)
      if (checkEmailAccount) {
        ActivationKey actKey = new ActivationKey(activationKey: UUID.randomUUID().toString() ,user:user)
        actKey.save()
      }
    }
    settingsService.initializeSettingsForUser(user)
    user
  }

  /**
   * Update the user
   * @param user the user
   * @param mainRole the main role
   * @return the updated user
   */
  User updateUser(User user, Role mainRole) {
    user.save()
    if (!user.hasErrors() && !user.isAdmin() &&!UserRole.get(user.id, mainRole.id)) {
      UserRole.removeAll(user)
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
   * Enable a user with an activation key
   * @param user
   * @param activationKey
   * @return
   */
  @Requires({
    user &&
    !user.enabled  &&
    user == activationKey?.user
  })
  User enableUserWithActivationKey(User user, ActivationKey activationKey) {
    activationKey.delete()
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
   * @param newPassword the new password
   * @param user the user
   * @return the processed user
   */
  User updatePasswordForUser(String newPassword, User user) {
    user.password = newPassword
    user.save()
    user
  }
}
