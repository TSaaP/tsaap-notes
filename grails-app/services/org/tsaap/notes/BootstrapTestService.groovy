package org.tsaap.notes

import org.tsaap.directory.User

class BootstrapTestService {

  User learnerPaul
  User teacherJeanne

  def initializeTests() {
    initializeUsers()
  }

  def initializeUsers() {
    learnerPaul = User.findByUsername("learner_paul")
    if (!learnerPaul) {
      learnerPaul = new User(username: "learner_paul", password: "password").save()
    }
    teacherJeanne = User.findByUsername("teacher_jeanne")
    if (!teacherJeanne) {
      teacherJeanne = new User(username: "teacher_jeanne", password: "password").save()
    }

  }
}
