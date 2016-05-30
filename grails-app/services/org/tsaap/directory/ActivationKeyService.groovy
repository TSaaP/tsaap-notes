package org.tsaap.directory

import grails.transaction.Transactional
import org.apache.commons.lang.time.DateUtils

@Transactional
class ActivationKeyService {

    def grailsApplication

    /**
     * Remove all activationKeys older than 3 (or custom value) hours and corresponding user who didn't activate their
     * accounts
     * The maximum lifetime is expressed in the parameter tsaap.auth.activation_key.lifetime_in_hours
     * @return
     */
    def removeOldActivationKeys() {
        def lifetime = grailsApplication.config.tsaap.auth.activation_key.lifetime_in_hours ?: 3
        def keys = ActivationKey.findAllByDateCreatedLessThan(DateUtils.addHours(new Date(), -lifetime))
        ActivationKey.deleteAll(keys)
        def users = User.findAllByIdInListAndEnabled(keys.user.id, false)
        def settings = Settings.findAllByUserInList(users)
        def roles = UserRole.findAllByUserInList(users)
        def unsubKeys = UnsubscribeKey.findAllByUserInList(users)

        UnsubscribeKey.deleteAll(unsubKeys)
        UserRole.deleteAll(roles)
        Settings.deleteAll(settings)
        User.deleteAll(users)
    }
}
