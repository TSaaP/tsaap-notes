package org.tsaap.directory

import org.apache.commons.lang.time.DateUtils
import spock.lang.*

/**
 *
 */
class ActivationKeyServiceSpec extends Specification {

    ActivationKey activationKey
    ActivationKeyService activationKeyService
    User user1
    User user2
    def setup() {
        user1 = new User(firstName: "moghite", lastName: "kacimi", username: "akac", enabled: false,password: "password", email: "akac@nomail.com", language: 'fr').save()
        user2 = new User(firstName: "moghite2", lastName: "kacimi", username: "akac2", enabled: false,password: "password", email: "akac2@nomail.com", language: 'fr').save()
        ActivationKey.deleteAll()
    }

    def cleanup() {
        user1.delete()
        user2.delete()
    }

    void "test removeOldActivationKeys"() {

        given:"Create activation key for user1"
        activationKey = new ActivationKey(activationKey: "Key", activationEmailSent: false, dateCreated: new Date()  , user : user1)
        activationKey.save()

        expect:""
        ActivationKey.count() == 1

        when:"Start Activation Key garbage collector job "
        activationKeyService.removeOldActivationKeys()

        then:"Count number of ActivationKey"
        ActivationKey.count() == 1


        when:"Create activation key for user2"
        ActivationKey activationKey2 = new ActivationKey(activationKey: "Key", activationEmailSent: false, user : user2)
        activationKey2.save()
        activationKey2.dateCreated = DateUtils.addHours(new Date(), -5)
        activationKey2.save()


        and:"Start Activation Key garbage collector job "
        activationKeyService.removeOldActivationKeys()

        then:"Count number of ActivationKey"
        ActivationKey.count() == 1


    }
}
