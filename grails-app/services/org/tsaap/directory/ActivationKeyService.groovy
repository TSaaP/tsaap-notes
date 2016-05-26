package org.tsaap.directory

import org.apache.commons.lang.time.DateUtils

class ActivationKeyService {

    def grailsApplication

    def removeOldActivationKeys() {
        def lifetime = grailsApplication.config.tsaap.auth.activation_key.lifetime_in_hours ?: 3
        def keys = ActivationKey.findAllByDateCreatedLessThan(DateUtils.addHours(new Date(), -lifetime))
        ActivationKey.deleteAll(keys)
        UserRole.executeUpdate('''
                delete from UserRole ur
                where ur.user in (
                    select u.id from User u
                    where u.enabled = false and u.id not in (
                        select ak.user.id from ActivationKey ak))
                ''')
        Settings.executeUpdate('''
                delete from Settings s
                where s.user in (
                    select u.id from User u
                    where u.enabled = false and u.id not in (
                        select ak.user.id from ActivationKey ak))
                ''')
        User.executeUpdate('''
                delete from User u
                where u.enabled = false and u.id not in (select ak.user from ActivationKey ak)
                ''')
    }
}
