package org.tsaap.directory

import grails.transaction.Transactional
import spock.lang.Specification

@Transactional
class MailCheckingServiceIntegrationTest extends Specification {

    MailCheckingService mailCheckingService
    UserAccountService userAccountService

    def "test FindAllNotifications"() {
        given:"one user from tsaap"
        User userTsaap = userAccountService.addUser(new User(firstName: "pauline", lastName: "aloga",
                username: "palog", password: "password", email: "aloga@nomail.com", language: 'fr'),
                RoleEnum.STUDENT_ROLE.role, false, 'fr', true,
                'tsaap')

        and: "one user from elaastic"
        User userElaastic = userAccountService.addUser(new User(firstName: "eric", lastName: "aloga",
                username: "ealog", password: "password", email: "ealoga@nomail.com", language: 'fr'),
                RoleEnum.STUDENT_ROLE.role, false, 'fr', true,
                'elaastic')

        when:"find all notifications for elaastic is called"
        def elaasticNotifs = mailCheckingService.findAllNotifications('elaastic')

        then:"only one entry is found"
        elaasticNotifs.keySet().size() == 1

        and: "the entry find corresponds to userElaastic"
        ActivationKey elaasticKey = ActivationKey.findByUser(userElaastic)
        elaasticNotifs.containsKey(elaasticKey.activationKey)

        when:"find all notifications for tsaap is called"
        def tsaapNotifs = mailCheckingService.findAllNotifications('tsaap')

        then:"only one enrty is found"
        tsaapNotifs.keySet().size() == 1

        and: "the entry find corresponds to userTsaap"
        ActivationKey tsaapKey = ActivationKey.findByUser(userTsaap)
        tsaapNotifs.containsKey(tsaapKey.activationKey)

        when:"find all notifications for 'truc' is called"
        def trucNotifs = mailCheckingService.findAllNotifications('truc')

        then:"no entry is found"
        trucNotifs.keySet().size() == 0

    }
}
