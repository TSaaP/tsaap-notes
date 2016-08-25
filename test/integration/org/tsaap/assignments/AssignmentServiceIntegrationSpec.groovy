package org.tsaap.assignments

import org.tsaap.BootstrapTestService
import org.tsaap.directory.User
import spock.lang.*


class AssignmentServiceIntegrationSpec extends Specification {

    BootstrapTestService bootstrapTestService
    AssignmentService assignmentService

    User teacher


    def setup() {
        bootstrapTestService.initializeUsers()
        teacher = bootstrapTestService.teacherJeanne
    }


    void "test save valid assignments"() {
        given: "A valid assignment"
        Assignment assignment = new Assignment(title: "an assignment", owner: teacher)

        when: "saving the assignment"
        Assignment savedAssignment = assignmentService.saveAssignment(assignment)

        then: "the save assignement has no errors"
        !savedAssignment.hasErrors()

        and: "its properties are correctly set"
        savedAssignment.title == assignment.title
        savedAssignment.owner == teacher
        savedAssignment.id != null
        savedAssignment.dateCreated != null
    }

    void "test save fail with invalid assignments"() {
        given: "An assignment without owner"
        Assignment assignment = new Assignment(title: "an assignment", owner: null)

        when: "saving the assignment"
        Assignment savedAssignment = assignmentService.saveAssignment(assignment)

        then: "the  assignement has  errors and no id"
        savedAssignment.hasErrors()
        savedAssignment.id == null

        when: "assignment has owner but blank title and trying to save"
        assignment = new Assignment(title: "", owner: teacher)
        savedAssignment = assignmentService.saveAssignment(assignment)

        then: "the  assignement has  errors and no id"
        savedAssignment.hasErrors()
        savedAssignment.id == null
    }

    void "test save assignment with schedule"() {
        given: "an assignment and  a schedule"
        Assignment assignment = new Assignment(title: "an assignment", owner: teacher)
        Schedule schedule = new Schedule(startDate: new Date())

        when: "saving the assignment"
        Assignment savedAssignment = assignmentService.saveAssignment(assignment,schedule)
        Schedule savedSchedule = savedAssignment.schedule

        then: "schedule and assignment are saved without errors"
        !savedAssignment.hasErrors()
        savedAssignment.id
        !savedSchedule.hasErrors()
        savedSchedule.id

    }

}
