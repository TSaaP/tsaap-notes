package org.tsaap.directory

import org.apache.commons.lang.time.DateUtils
import org.codehaus.groovy.grails.commons.GrailsApplication
import spock.lang.*

/**
 *
 */
class ActivationKeyServiceIntegrationSpec extends Specification {

    ActivationKey activationKey
    ActivationKeyService activationKeyService
    UserAccountService userAccountService

    User user1
    User user2
    User user3
    def lifetime
    def grailsApplication

    def setup() {
        user1 = userAccountService.addUser(new User(
                firstName: "moghite", lastName: "kacimi", username: "akac",
                password: "password", email: "akac@nomail.com", language: 'fr'), RoleEnum.STUDENT_ROLE.role)
        user2 = userAccountService.addUser(new User(
                firstName: "moghite2", lastName: "kacimi", username: "akac2",
                password: "password", email: "akac2@nomail.com", language: 'fr'), RoleEnum.STUDENT_ROLE.role)
        user3 = userAccountService.addUser(new User(
                firstName: "moghite3", lastName: "kacimi", username: "akac3",
                password: "password", email: "akac3@nomail.com", language: 'fr'), RoleEnum.STUDENT_ROLE.role, true)
        ActivationKey.deleteAll()
        lifetime = grailsApplication.config.tsaap.auth.activation_key.lifetime_in_hours ?: 3
    }

    def cleanup() {
        if (user1)
            user1.delete()
        if (user2)
            user2.delete()
        if (user3)
            user3.delete()
    }

    void "test removeOldActivationKeys"() {

        given: "Create activation key for user1"
        activationKey = new ActivationKey(activationKey: "Key", activationEmailSent: false, dateCreated: new Date(), user: user1)
        activationKey.save()

        expect: "Users and activation keys are presents"
        ActivationKey.count() == 1
        User.count() == 4

        when: "Start Activation Key garbage collector job "
        activationKeyService.removeOldActivationKeys()

        then: "Count number of ActivationKey and number of user"
        ActivationKey.count() == 1
        User.count() == 4

        when: "Create activation key for user2"
        ActivationKey activationKey2 = new ActivationKey(activationKey: "Key", activationEmailSent: false, user: user2)
        activationKey2.save()
        activationKey2.dateCreated = DateUtils.addHours(new Date(), -lifetime - 1)
        activationKey2.save()
        
        and: "Start Activation Key garbage collector job "
        activationKeyService.removeOldActivationKeys()

        then: "Count number of ActivationKey and number of user"
        ActivationKey.count() == 1
        User.count() == 3

        when: "Create activation key for user3"
        ActivationKey activationKey3 = new ActivationKey(activationKey: "Key", activationEmailSent: true, user: user3)
        activationKey3.save()
        activationKey3.dateCreated = DateUtils.addHours(new Date(), -lifetime - 1)
        activationKey3.save()

        and: "Start Activation Key garbage collector job "
        activationKeyService.removeOldActivationKeys()

        then: "Count number of ActivationKey and number of user"
        ActivationKey.count() == 1
        User.count() == 3
    }
}
