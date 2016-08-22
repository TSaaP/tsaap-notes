package org.tsaap.lti

/**
 * Class corresponding an LMS activity (LTI activity)
 */
class LmsContext {

    Long contextId

    String ltiActivityId
    String ltiCourseId
    String ltiConsumerKey
    String ltiConsumerName

    String contextTitle
    LmsUser owner

    boolean noteTakingEnabled = false

    LmsContext() {}

    LmsContext(String ltiActivityId, String ltiCourseId, String ltiConsumerKey, String ltiConsumerName,
               String contextTitle, LmsUser owner) {
        this.ltiActivityId = ltiActivityId
        this.ltiCourseId = ltiCourseId
        this.ltiConsumerKey = ltiConsumerKey
        this.ltiConsumerName = ltiConsumerName
        this.contextTitle = contextTitle
        this.owner = owner
    }
}
