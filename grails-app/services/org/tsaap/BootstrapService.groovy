/*
 * Copyright (C) 2013-2016 Université Toulouse 3 Paul Sabatier
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.tsaap

import groovy.sql.Sql
import org.tsaap.assignments.Assignment
import org.tsaap.assignments.AssignmentService
import org.tsaap.assignments.Interaction
import org.tsaap.assignments.InteractionType
import org.tsaap.assignments.Schedule
import org.tsaap.assignments.SequenceService
import org.tsaap.assignments.Statement
import org.tsaap.assignments.interactions.ChoiceInteractionType
import org.tsaap.assignments.interactions.EvaluationSpecification
import org.tsaap.assignments.interactions.InteractionChoice
import org.tsaap.assignments.interactions.ResponseSubmissionSpecification
import org.tsaap.directory.Role
import org.tsaap.directory.RoleEnum
import org.tsaap.directory.User
import org.tsaap.directory.UserAccountService
import org.tsaap.notes.*

class BootstrapService {

    def dataSource
    UserAccountService userAccountService
    ContextService contextService
    NoteService noteService
    AssignmentService assignmentService
    SequenceService sequenceService


    Role studentRole
    Role teacherRole
    Role adminRole

    User fsil
    User mary
    User thom
    User john
    User erik
    User admin

    User elaasticTeacher
    User elaasticTeacherLearner
    User elaasticLearnerDemo1
    User elaasticLearnerDemo2
    User elaasticLearnerDemo3


    Assignment assignment1
    Assignment assignment2

    Statement statement1
    Statement statement2
    Statement statement3

    Interaction responseSubmissionInteraction
    Interaction responseSubmissionOpenInteraction
    Interaction evaluationInteraction
    Interaction evaluationOpenInteraction
    Interaction readInteraction
    Interaction readOpenInteraction
    Interaction exclusifResponseSubmissionInteraction
    Interaction exclusifEvaluationInteraction
    Interaction exclusifReadInteraction

    Context science
    Context football

    Tag goal

    Note note


  def initializeReferenceData() {
        initializeRoles()
        initializeAdmin()
    }

    def initializeRoles() {
        Sql sql = new Sql(dataSource)
        studentRole = Role.findByRoleName(RoleEnum.STUDENT_ROLE.name())
        if (!studentRole) {
            sql.executeInsert("insert into role (id,authority) values (2,${RoleEnum.STUDENT_ROLE.name()})")
            studentRole = RoleEnum.STUDENT_ROLE.role
        }
        teacherRole = Role.findByRoleName(RoleEnum.TEACHER_ROLE.name())
        if (!teacherRole) {
            sql.executeInsert("insert into role (id,authority) values (3,${RoleEnum.TEACHER_ROLE.name()})")
            teacherRole = RoleEnum.TEACHER_ROLE.role
        }
        adminRole = Role.findByRoleName(RoleEnum.ADMIN_ROLE.name())
        if (!adminRole) {
            sql.executeInsert("insert into role (id,authority) values (1,${RoleEnum.ADMIN_ROLE.name()})")
            adminRole = RoleEnum.ADMIN_ROLE.role
        }
    }

    def initializeAdmin() {
        admin = User.findByUsername("admin")
        if (!admin) {
            def user = new User(firstName: "Admin", lastName: "Tsaap", username: "admin", password: "1234", email: 'admin@tsaap.org')
            admin = userAccountService.addUser(user, adminRole, true, 'en')
        }
    }

    def inializeDevUsers() {
        fsil = User.findByUsername("fsil")
        if (!fsil) {
            def user = new User(firstName: "Franck", lastName: "Sil", username: "fsil", password: "1234", email: 'fsil@fsil.com')
            fsil = userAccountService.addUser(user, teacherRole, true, 'fr')

        }
        mary = User.findByUsername("mary")
        if (!mary) {
            def user = new User(firstName: "Mary", lastName: "Sil", username: "mary", password: "1234", email: 'mary@mary.com')
            mary = userAccountService.addUser(user, studentRole, true, 'fr')
        }
        thom = User.findByUsername("thom")
        if (!thom) {
            def user = new User(firstName: "Thom", lastName: "Thom", username: "thom", password: "1234", email: 'thom@thom.com')
            thom = userAccountService.addUser(user, studentRole, true, 'fr')
        }
        john = User.findByUsername("john")
        if (!john) {
            def user = new User(firstName: "John", lastName: "John", username: "john", password: "1234", email: 'john@john.com')
            john = userAccountService.addUser(user, studentRole, true, 'fr')
        }
        erik = User.findByUsername("erik")
        if (!erik) {
            def user = new User(firstName: "Erik", lastName: "Erik", username: "erik", password: "1234", email: 'erik@erik.com')
            erik = userAccountService.addUser(user, studentRole, true, 'fr')
        }
    }


    def initializeDevContext() {
        science = Context.findByContextName('science')
        if (!science) {
            science = contextService.saveContext(new Context(owner: mary, contextName: 'science', descriptionAsNote: "everything about #science, but sur mainly on #computer", url: 'http://fr.wikipedia.org/wiki/Science', source: null))
        }

    }

    def initializeDevContextWithFragment() {
        football = Context.findByContextName('football')
        if (!football) {
            football = contextService.saveContext(new Context(owner: thom, contextName: 'football', descriptionAsNote: 'everything about #football', url: 'http://fr.wikipedia.org/wiki/Football', source: null))
        }
        goal = Tag.findOrSaveWhere(name: 'goal')
        for (int i = 0; i < 10; i++) {
            note = Note.findByContent("goal$i")
            if (!note) {
                note = noteService.addStandardNote(thom, "goal$i", football, goal, null)
            }
        }
        note = Note.findByContent("goal11")
        if (!note) {
            note = noteService.addStandardNote(mary, "goal11", football, goal, null)
        }
        note = Note.findByContent("goal12")
        if (!note) {
            note = noteService.addStandardNote(fsil, "goal12", football, goal, null)
        }
    }

  def initializeElaasticDemo () {
    initializeElaasticUsers()
    initializeElaasticStatement()
    initializeInteractions()
    initializeElaasticAssignment()
  }


  private void initializeElaasticUsers() {
      elaasticTeacher = User.findByUsername("teacherdemo1")
      if (!elaasticTeacher) {
          def user = new User(firstName: "teacher-demo-1", lastName: "teacherdemo1", username: "teacherdemo1", password: "teacherdemo1", email: 'elaT@elaT.com')
          elaasticTeacher = userAccountService.addUser(user, teacherRole, true, 'fr')
      }

      elaasticTeacherLearner = User.findByUsername("teacherlearner1")
      if (!elaasticTeacherLearner) {
        def user = new User(firstName: "teacher-learner-1", lastName: "teacherlearner1", username: "teacherlearner1", password: "teacherlearner1", email: 'elaTL@delaTL.com')
        elaasticTeacherLearner = userAccountService.addUser(user, teacherRole, true, 'fr')
      }

      elaasticLearnerDemo1 = User.findByUsername("learnerdemo1")
      if (!elaasticLearnerDemo1) {
          def user = new User(firstName: "learner-demo-1", lastName: "learnerdemo1", username: "learnerdemo1", password: "learnerdemo1", email: 'elaL-1@gmail.com')
          elaasticLearnerDemo1 = userAccountService.addUser(user, studentRole, true, 'fr')
      }

      elaasticLearnerDemo2 = User.findByUsername("learnerdemo2")
      if (!elaasticLearnerDemo2) {
        def user = new User(firstName: "learner-demo-2", lastName: "learnerdemo2", username: "learnerdemo2", password: "learnerdemo2", email: 'elaL-2@gmail.com')
        elaasticLearnerDemo2 = userAccountService.addUser(user, studentRole, true, 'fr')
      }

      elaasticLearnerDemo3 = User.findByUsername("learnerdemo3")
        if (!elaasticLearnerDemo3) {
          def user = new User(firstName: "learner-demo-3", lastName: "learnerdemo3", username: "learnerdemo3", password: "learnerdemo3", email: 'elaL-3@gmail.com')
          elaasticLearnerDemo3 = userAccountService.addUser(user, studentRole, true, 'fr')
        }
    }


  private void initializeElaasticAssignment () {
    assignment1 = Assignment.findByTitle("Assignment 1 de démonstration")
    if (!assignment1) {
      assignment1 = new Assignment(
          owner: elaasticTeacher,
          title: "Assignment 1 de démonstration",
      );
      assignmentService.saveAssignment(assignment1)
      sequenceService.addSequenceToAssignment(assignment1, elaasticTeacher, statement1, [responseSubmissionInteraction,evaluationInteraction, readInteraction])
      sequenceService.addSequenceToAssignment(assignment1, elaasticTeacher, statement2, [responseSubmissionOpenInteraction,evaluationOpenInteraction, readOpenInteraction])
      sequenceService.addSequenceToAssignment(assignment1, elaasticTeacher, statement3, [exclusifResponseSubmissionInteraction,exclusifEvaluationInteraction, exclusifReadInteraction])
    }

    assignment2 = Assignment.findByTitle("Assignment 2 de démonstration")
    if (!assignment2) {
      assignment2 = new Assignment(
          owner: elaasticTeacher,
          title: "Assignment 2 de démonstration"
      );
      assignmentService.saveAssignment(assignment2)
    }
  }

  private void initializeElaasticStatement() {
    statement1 = Statement.findByTitle("Première question")
    if (!statement1) {
      statement1 = sequenceService.saveStatement(new Statement(title: "Première question", content:"Choisir les bonnes responses"),elaasticTeacher)
    }

    statement2 = Statement.findByTitle("Seconde question")
    if (!statement2) {
      statement2 = sequenceService.saveStatement(new Statement(title: "Seconde question", content:"Question Ouverte"),elaasticTeacher)
    }

    statement3 = Statement.findByTitle("Troisième question")
    if (!statement3) {
      statement3 = sequenceService.saveStatement(new Statement(title: "Troisième question", content:"Choisir la bonne réponse"),elaasticTeacher)
    }
  }

  def initializeInteractions() {
    initializeResponseSubmissionInteraction()
    initializeResponseSubmissionOpenInteraction()
    initialiseEvaluationInteraction()
    initializeReadInteraction()
    initialiseEvaluationOpenInteraction()
    initializeReadOpenInteraction()
    initializeExclusifResponseSubmissionInteraction()
    initialiseExclusifEvaluationInteraction()
    initializeExclusifReadInteraction()
  }

  private void initialiseEvaluationInteraction() {
    EvaluationSpecification evalSpec = new EvaluationSpecification()
    evalSpec.responseToEvaluateCount = 3
    evaluationInteraction = new Interaction(rank: 2, specification: evalSpec.jsonString,
        interactionType: InteractionType.Evaluation.name(),
        schedule: new Schedule(startDate: new Date()))
  }

  private void initialiseEvaluationOpenInteraction() {
    EvaluationSpecification evalSpec = new EvaluationSpecification()
    evalSpec.responseToEvaluateCount = 2
    evaluationOpenInteraction = new Interaction(rank: 2, specification: evalSpec.jsonString,
        interactionType: InteractionType.Evaluation.name(),
        schedule: new Schedule(startDate: new Date()))
  }

  private void initializeResponseSubmissionInteraction() {
    ResponseSubmissionSpecification respSpec = new ResponseSubmissionSpecification()
    respSpec.choiceInteractionType = ChoiceInteractionType.MULTIPLE.name()
    respSpec.itemCount = 5
    respSpec.expectedChoiceList = [new InteractionChoice(2,100f/3f as Float),
                                   new InteractionChoice(3,100f/3f as Float),
                                   new InteractionChoice(5,100f/3f as Float)]
    respSpec.studentsProvideExplanation = true
    respSpec.studentsProvideConfidenceDegree = true
    responseSubmissionInteraction = new Interaction(rank: 1, specification: respSpec.jsonString,
        interactionType: InteractionType.ResponseSubmission.name(),
        schedule: new Schedule(startDate: new Date()))
  }

  private void initializeResponseSubmissionOpenInteraction() {
    ResponseSubmissionSpecification respSpec = new ResponseSubmissionSpecification()
    respSpec.studentsProvideExplanation = true
    respSpec.studentsProvideConfidenceDegree = true
    respSpec.expectedChoiceList = null
    respSpec.choiceInteractionType = null
    responseSubmissionOpenInteraction = new Interaction(rank: 1, specification: respSpec.jsonString,
        interactionType: InteractionType.ResponseSubmission.name(),
        schedule: new Schedule(startDate: new Date()))
  }

  private void initializeReadInteraction() {
    readInteraction = new Interaction(rank: 3, specification: Interaction.EMPTY_SPECIFICATION,
        interactionType: InteractionType.Read.name(),
        schedule: new Schedule(startDate: new Date()))
  }
  private void initializeReadOpenInteraction() {
    readOpenInteraction = new Interaction(rank: 3, specification: Interaction.EMPTY_SPECIFICATION,
        interactionType: InteractionType.Read.name(),
        schedule: new Schedule(startDate: new Date()))
  }
  private void initializeExclusifResponseSubmissionInteraction() {
    ResponseSubmissionSpecification respSpec = new ResponseSubmissionSpecification()
    respSpec.choiceInteractionType = ChoiceInteractionType.EXCLUSIVE.name()
    respSpec.itemCount = 4
    respSpec.expectedChoiceList = [new InteractionChoice(3, 100f as Float)]
    respSpec.studentsProvideExplanation = true
    respSpec.studentsProvideConfidenceDegree = true
    exclusifResponseSubmissionInteraction = new Interaction(rank: 1, specification: respSpec.jsonString,
        interactionType: InteractionType.ResponseSubmission.name(),
        schedule: new Schedule(startDate: new Date()))
  }

  private void initialiseExclusifEvaluationInteraction() {
    EvaluationSpecification evalSpec = new EvaluationSpecification()
    evalSpec.responseToEvaluateCount = 3
    exclusifEvaluationInteraction = new Interaction(rank: 2, specification: evalSpec.jsonString,
        interactionType: InteractionType.Evaluation.name(),
        schedule: new Schedule(startDate: new Date()))
  }



  private void initializeExclusifReadInteraction() {
    exclusifReadInteraction = new Interaction(rank: 3, specification: Interaction.EMPTY_SPECIFICATION,
        interactionType: InteractionType.Read.name(),
        schedule: new Schedule(startDate: new Date()))
  }




}
