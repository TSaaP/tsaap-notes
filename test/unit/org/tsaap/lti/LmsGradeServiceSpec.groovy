package org.tsaap.lti

import grails.test.mixin.TestFor
import org.tsaap.lti.tp.ResourceLink
import org.tsaap.lti.tp.User
import spock.lang.Specification

@TestFor(LmsGradeService)
class LmsGradeServiceSpec extends Specification {

    LmsGradeService lmsGradeService
    ResourceLink resourceLink
    User user

    def setup() {
        lmsGradeService = new LmsGradeService()
    }

    void "test send users grades for context"() {

        given: "the collaborators"
        resourceLink = Mock(ResourceLink)
        user = Mock(User)

        when: "I want to send users grade for a given context"
        lmsGradeService.sendUserGradeToLms(resourceLink,user,66)

        then: "the grades are correctly sent"
        1*resourceLink.doOutcomesService(resourceLink.EXT_WRITE,_,user) >> true

    }
}
