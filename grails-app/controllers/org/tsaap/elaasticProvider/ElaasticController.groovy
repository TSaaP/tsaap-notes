package org.tsaap.elaasticProvider

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import net.sf.cglib.core.Local
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.tsaap.assignments.Assignment
import org.tsaap.directory.Role
import org.tsaap.directory.RoleEnum
import org.tsaap.directory.User


class ElaasticController {

  SpringSecurityService springSecurityService
    private final static teacherName = 'demo-elaastic-teacher'
    private final static learnerName = 'demo-elaastic-learner'

    def index() { println("index")}

  def assignment (String id, String username) {

    if (springSecurityService.currentUser == null) {
      User demoUser = User.findByUsername(username);
      if (!demoUser || !(demoUser.firstName == teacherName || demoUser.firstName == learnerName)) {
        render(status: 401, text:'401 - Unauthorized')
      }
      // for demo user credential is username and password == username
      springSecurityService.reauthenticate(demoUser.username, demoUser.username)
      String n =  springSecurityService.currentUser.username
      redirect(uri: '/elaastic/assignment/' + id)
      return
    }

    Assignment assignment = Assignment.findById(Long.parseLong(id))
    if (!assignment) {
      render(status: 404, text:'404 - Not found - Assignment id is invalid')
    }

    render(view: "show_assignment", model: [assignmentInstance: assignment, user: springSecurityService.currentUser])
  }

}