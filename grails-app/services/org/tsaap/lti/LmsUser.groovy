package org.tsaap.lti

/**
 * Class representing a user coming from an LMS
 */
class LmsUser {

    String ltiUserId
    String ltiConsumerKey

    Long userId

    String firstname
    String lastname
    String username
    String password

    String email
    boolean isLearner
    boolean isEnabled

    LmsUser() {}

    LmsUser(String ltiUserId, String ltiConsumerKey, String firstname, String lastname, String email, boolean isLearner) {
        this.ltiUserId = ltiUserId
        this.ltiConsumerKey = ltiConsumerKey
        this.firstname = firstname
        this.lastname = lastname
        this.email = email
        this.isLearner = isLearner
    }
}
