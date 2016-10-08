package org.tsaap

import grails.plugins.springsecurity.SpringSecurityService
import org.tsaap.directory.User

class HomeController {

    SpringSecurityService springSecurityService

    def index() {
        User user = springSecurityService.currentUser
        if (user.isLearner()) {
            chain(controller: 'player', action: 'index')
        } else {
            chain(controller: 'assignment', action: 'index')
        }
    }
}
