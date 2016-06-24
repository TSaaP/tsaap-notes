package org.tsaap.directory



import spock.lang.*

/**
 *
 */
class UserIntegrationSpec extends Specification {

    UserAccountService userAccountService
    def setup() {
    }

    def cleanup() {
    }

    def "test about user role"() {
        when: "create new user with a student role"
        User studentUser = userAccountService.addUser(new User(firstName: "moghite", lastName: "kacimi", username: "akac", password: "password", email: "akac@nomail.com", language: 'fr'), RoleEnum.STUDENT_ROLE.role)

        then:"test if studentUser is student"
        def res = studentUser.isLearner()
        res == true

        when: "create new user with a student role"
        User teacherUser = userAccountService.addUser(new User(firstName: "moghite", lastName: "kacimi", username: "akac2", password: "password", email: "akac2@nomail.com", language: 'fr'), RoleEnum.TEACHER_ROLE.role)

        then:"test if studentUser is student"
        def res2 = teacherUser.isTeacher()
        res2 == true

    }
}
