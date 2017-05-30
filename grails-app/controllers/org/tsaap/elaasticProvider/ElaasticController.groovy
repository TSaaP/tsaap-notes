package org.tsaap.elaasticProvider

import grails.plugins.springsecurity.SpringSecurityService
import org.tsaap.BootstrapDemoService
import org.tsaap.assignments.Assignment
import org.tsaap.directory.User

class ElaasticController {

  SpringSecurityService springSecurityService
  BootstrapDemoService bootstrapDemoService

    private final teacherName = BootstrapDemoService.firstNameTeacherDemo
    private final learnerDemoName = BootstrapDemoService.firstNameLearnerDemo

    def index() { println("index")}

  def assignment (String id, String username) {

    Assignment assignment = Assignment.findById(Long.parseLong(id))
    if (!assignment) {
      render(status: 404, text:'404 - Not found - Assignment id is invalid')
      return
    }

    if (springSecurityService.currentUser == null) {
      User demoUser = User.findByUsername(username);
      if (!demoUser || !(demoUser.firstName == teacherName || demoUser.firstName == learnerDemoName)) {
        User n =  (User) springSecurityService.currentUser
        render(view: "show_assignment", model: [assignmentInstance: assignment, user: null])
        return
      }
      // for demo user credential is username and password == username
      springSecurityService.reauthenticate(demoUser.username, demoUser.username)
      User n =  (User) springSecurityService.currentUser
      redirect(uri: '/elaastic/assignment/' + id)
      return
    }

    render(view: "show_assignment", model: [assignmentInstance: assignment, user: springSecurityService.currentUser])
  }

  def reset (String id) {
    bootstrapDemoService.restartElaasticDemo()
    render('Demo reset with success')
  }
}