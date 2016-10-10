package org.tsaap.lti

/**
 * Class corresponding to an LMS activity (LTI activity) that is an assignment
 */
class LmsAssignment {

    Long assignmentId

    String ltiActivityId
    String ltiCourseId
    String ltiConsumerKey
    String ltiConsumerName
    String title

    LmsUser owner
    boolean hasError
    String errorCause
    String globalId

    LmsAssignment() {}

    LmsAssignment(String ltiActivityId, String ltiCourseId, String ltiConsumerKey, String ltiConsumerName,String title,
                  LmsUser owner) {
        this.ltiActivityId = ltiActivityId
        this.ltiCourseId = ltiCourseId
        this.ltiConsumerKey = ltiConsumerKey
        this.ltiConsumerName = ltiConsumerName
        this.owner = owner
        this.title = title
    }


}
