package org.tsaap.directory



import spock.lang.*

/**
 *
 */
class UserRoleSpec extends Specification {

    UserAccountService userAccountService
    User user
    UserRole userRole = new UserRole()
    def setup() {
        user = userAccountService.addUser(new User(
                firstName: "moghite", lastName: "kacimi", username: "akac",
                password: "password", email: "akac@nomail.com", language: 'fr'), RoleEnum.STUDENT_ROLE.role)
    }

    def cleanup() {
        if (user)
            user.delete()
    }

    void "test of removeAll function"() {
        when:"count number of userRole in database"
        def nbUserRole = UserRole.count()

        and:"call function removeAll"
        userRole.removeAll(RoleEnum.STUDENT_ROLE.role)

        then:"number of userRomle must decrease"
        UserRole.count() == nbUserRole - 1
    }

    void "test of remove function"() {
        when: "instance of userRole exist in database"
        def res
        res = userRole.remove(user, RoleEnum.STUDENT_ROLE.role)

        then:"res must equal to true"
        res == true

        when:"instance of userRole does not exit, return false"
        res = userRole.remove(user, RoleEnum.ADMIN_ROLE.role)

        then:"res must equal to false"
        res == false
    }

}
