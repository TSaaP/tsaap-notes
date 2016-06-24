package org.tsaap.directory

import grails.plugins.springsecurity.SpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(UnsubscribeKeyController)
@Mock([SettingsService, Role, User, UnsubscribeKey, Settings, SpringSecurityService])

class UnsubscribeKeyControllerSpec extends Specification {

    User user
    SpringSecurityService springSecurityService = Mock(SpringSecurityService)

    def setup() {
        user= new User(firstName: "moghite", lastName: "kacimi", username: "akac", email: "akac@mail.com", password: "password")
        user.springSecurityService = springSecurityService
        springSecurityService.encodePassword(user.password) >> user.password
        user.save()
        UnsubscribeKey unsubKey = new UnsubscribeKey(unsubscribeKey: UUID.randomUUID().toString(), user: user)
        unsubKey.save()
        Settings settings = new Settings(dailyNotifications: true, mentionNotifications: true)
        settings.user = user
        settings.language = 'fr'
        settings.save()
    }

    def cleanup() {
        if (user)
            user.delete()
    }

    void "unsubscribe from daily notifications"() {
        when:"get unsubscribe key for user"
        params.key = UnsubscribeKey.findByUser(user).unsubscribeKey

        and:"call action for unsubscribe to daily notifications"
        controller.doUnsubscribeDaily()

        then:"rend dailyNotifUnsubscribe view "
        view == '/directory/dailyNotifUnsubscribe'

        and:"dailyNotifications attribute marked as false"
        user.settings.dailyNotifications == false
    }

    void "unsubscribe from mention notifications"() {
        when:"get unsubscribe key for user"
        params.key = UnsubscribeKey.findByUser(user).unsubscribeKey

        and:"call action for unsubscribe to daily notifications"
        controller.doUnsubscribeMention()

        then:"rend dailyNotifUnsubscribe view "
        view == '/directory/mentionNotifUnsubscribe'

        and:"mentionNotifications attribute marked as false"
        user.settings.mentionNotifications == false
    }
}
