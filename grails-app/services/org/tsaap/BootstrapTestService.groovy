package org.tsaap

import org.tsaap.directory.User

class BootstrapTestService {

  User learnerPaul
  User teacherJeanne
  User learnerMary

  def initializeTests() {
    initializeUsers()
  }

  def initializeUsers() {
    learnerPaul = User.findByUsername("learner_paul")
    if (!learnerPaul) {
      learnerPaul = new User(firstName: "Paul",lastName: "D", username: "learner_paul", password: "password",email: "paul@nomail.com").save()
    }
    teacherJeanne = User.findByUsername("teacher_jeanne")
    if (!teacherJeanne) {
      teacherJeanne = new User(firstName: "Jeanne",lastName: "L", username: "teacher_jeanne", password: "password", email: "jeanne@nomail.com").save()
    }
    learnerMary = User.findByUsername("learner_Mary")
    if (!learnerMary) {
      learnerMary = new User(firstName: "Mary",lastName: "S", username: "learner_Mary", password: "password", email: "mary@nomail.com").save()
    }

  }
}
