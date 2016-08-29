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

import org.tsaap.assignments.Assignment
import org.tsaap.assignments.AssignmentService
import org.tsaap.assignments.SequenceService
import org.tsaap.assignments.Statement
import org.tsaap.directory.SettingsService
import org.tsaap.directory.User
import org.tsaap.notes.Context
import org.tsaap.notes.ContextService
import org.tsaap.notes.Note
import org.tsaap.notes.NoteService

class BootstrapTestService {

    NoteService noteService
    ContextService contextService
    SettingsService settingsService
    SequenceService sequenceService
    AssignmentService assignmentService

    User learnerPaul
    User teacherJeanne
    User learnerMary
    List<User> learners = []
    Note note1
    Note note2
    Context context1
    Context context2

    Statement statement1
    Statement statement2

    Assignment assignment1
    Assignment assignment2With2Sequences



    def initializeTests() {
        initializeUsers()
        initializeNotes()
        initializeContexts()
        initializeStatements()
        initializeAssignments()
    }

    def initializeUsers() {
        learnerPaul = User.findByUsername("learner_paul")
        if (!learnerPaul) {
            learnerPaul = new User(firstName: "Paul", lastName: "D", username: "learner_paul", password: "password", email: "paul@nomail.com").save()
            learnerPaul.settings = settingsService.initializeSettingsForUser(learnerPaul, 'fr')
        }
        teacherJeanne = User.findByUsername("teacher_jeanne")
        if (!teacherJeanne) {
            teacherJeanne = new User(firstName: "Jeanne", lastName: "L", username: "teacher_jeanne", password: "password", email: "jeanne@nomail.com").save()
            teacherJeanne.settings = settingsService.initializeSettingsForUser(teacherJeanne, 'fr')
        }
        learnerMary = User.findByUsername("learner_Mary")
        if (!learnerMary) {
            learnerMary = new User(firstName: "Mary", lastName: "S", username: "learner_Mary", password: "password", email: "mary@nomail.com").save()
            learnerMary.settings = settingsService.initializeSettingsForUser(learnerMary, 'fr')
        }
        for (int i = 0; i < 10; i++) {
            User currentUser = User.findByUsername("learner_$i")
            if (!currentUser) {
                currentUser = new User(firstName: "learner_$i", lastName: "learner_$i", username: "learner_$i", password: "learner_$i", email: "learner_$i@nomail.com").save()
                currentUser.settings = settingsService.initializeSettingsForUser(currentUser, 'en')
            }
            learners[i] = currentUser
        }
    }

    def initializeNotes() {
        if (!Note.findByContent("content note 1")) {
            note1 = noteService.addStandardNote(learnerPaul, "content note 1")
        }
        if (!Note.findByContent("content note 2")) {
            note2 = noteService.addStandardNote(learnerMary, "content note 2")
        }
    }

    def initializeContexts() {
        if (!Context.findByContextName("Context1")) {
            context1 = contextService.saveContext(new Context(owner: learnerPaul, contextName: "Context1", url: 'http://www.w3.org', descriptionAsNote: 'a description', source: null))
        }
        if (!Context.findByContextName("Context2")) {
            context2 = contextService.saveContext(new Context(owner: learnerPaul, contextName: "Context2", url: 'http://www.w3.org', descriptionAsNote: 'a description', source: null))
        }

    }


    def initializeAssignments() {
        if (!Assignment.findByTitle("Assignment 1")) {
            assignment1 = assignmentService.saveAssignment(new Assignment(title: "Assignment 1", owner: teacherJeanne))
        }
        if (!Assignment.findByTitle("Assignment 2")) {
            assignment2With2Sequences = assignmentService.saveAssignment(new Assignment(title: "Assignment 2", owner: teacherJeanne))
            assignmentService.addSequenceToAssignment(assignment2With2Sequences, teacherJeanne, statement1)
            assignmentService.addSequenceToAssignment(assignment2With2Sequences, teacherJeanne, statement2)
        }
    }

    def initializeStatements() {
        if (!Statement.findByTitle("Statement 1")) {
            statement1 = sequenceService.saveStatement(new Statement(title: "Statement 1", content:"Content of statement 1"),teacherJeanne)
        }
        if (!Statement.findByTitle("Statement 2")) {
            statement2 = sequenceService.saveStatement(new Statement(title: "Statement 2", content:"Content of statement 2"),teacherJeanne)
        }
    }



}
