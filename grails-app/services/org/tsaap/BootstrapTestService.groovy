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

import org.tsaap.assignments.*
import org.tsaap.assignments.interactions.EvaluationSpecification
import org.tsaap.assignments.interactions.ResponseSubmissionSpecification
import org.tsaap.assignments.statement.ChoiceItemSpecification
import org.tsaap.assignments.statement.ChoiceSpecification
import org.tsaap.directory.SettingsService
import org.tsaap.directory.User


class BootstrapTestService {


    SettingsService settingsService
    SequenceService sequenceService
    AssignmentService assignmentService

    User learnerPaul
    User teacherJeanne
    User learnerMary
    List<User> learners = []


    Statement statement1
    Statement statement2
    Statement statement3
    Statement statement4
    Statement statement5

    Assignment assignment1
    Assignment assignment2With2Sequences
    Assignment assignment3WithInteractions
    Assignment assignment4WithOpenInteractions

    Interaction responseSubmissionInteraction
    Interaction responseSubmissionOpenInteraction
    Interaction evaluationInteraction
    Interaction evaluationOpenInteraction
    Interaction readInteraction
    Interaction readOpenInteraction

    ChoiceSpecification multipleChoiceSpec
    ChoiceSpecification exclusiveChoiceSpec
    ChoiceSpecification openEndedChoiceSpec

    def initializeTests() {
        initializeUsers()
        initializeChoiceSpecification()
        initializeStatements()
        initializeInteractions()
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
                currentUser = new User(firstName: "learner_$i", lastName: "learner_$i", username: "learner_$i", password: "learner_$i", email: "learner_$i@nomail.com").save(failOnError: true)
                currentUser.settings = settingsService.initializeSettingsForUser(currentUser, 'en')
            }
            learners[i] = currentUser
        }
    }



    def initializeChoiceSpecification() {
        initializeMultipleChoiceSpec()
        initializeExclusiveChoiceSpec()
        initializeOpenEndedChoiceSpec()
    }

    def initializeAssignments() {
        if (!Assignment.findByTitle("Assignment 1")) {
            assignment1 = assignmentService.saveAssignment(new Assignment(title: "Assignment 1", owner: teacherJeanne))
        }
        if (!Assignment.findByTitle("Assignment 2")) {
            assignment2With2Sequences = assignmentService.saveAssignment(new Assignment(title: "Assignment 2", owner: teacherJeanne))
            sequenceService.createAndAddSequenceToAssignment(assignment2With2Sequences, teacherJeanne, statement1)
            sequenceService.createAndAddSequenceToAssignment(assignment2With2Sequences, teacherJeanne, statement2)
        }
        if (!Assignment.findByTitle("Assignment 3")) {
            assignment3WithInteractions = assignmentService.saveAssignment(new Assignment(title: "Assignment 3", owner: teacherJeanne))
            Sequence s = sequenceService.createAndAddSequenceToAssignment(assignment3WithInteractions, teacherJeanne, statement3)
            sequenceService.addSequenceInteractions(s, teacherJeanne, [responseSubmissionInteraction, evaluationInteraction, readInteraction])
        }
        if (!Assignment.findByTitle("Assignment 4")) {
            assignment4WithOpenInteractions = assignmentService.saveAssignment(new Assignment(title: "Assignment 4", owner: teacherJeanne))
            Sequence s = sequenceService.createAndAddSequenceToAssignment(assignment4WithOpenInteractions, teacherJeanne, statement4)
            sequenceService.addSequenceInteractions(s, teacherJeanne, [responseSubmissionOpenInteraction, evaluationOpenInteraction, readOpenInteraction])
        }
    }

    def initializeStatements() {
        if (!Statement.findByTitle("Statement 1")) {
            statement1 = sequenceService.saveStatement(
                new Statement(
                    title: "Statement 1",
                    content:"Content of statement 1",
                    questionType: QuestionType.MultipleChoice,
                    choiceSpecification: multipleChoiceSpec.jsonString
                ),
                teacherJeanne
            )
        }
        if (!Statement.findByTitle("Statement 2")) {
            statement2 = sequenceService.saveStatement(
                new Statement(
                    title: "Statement 2",
                    content:"Content of statement 2",
                    questionType: QuestionType.ExclusiveChoice,
                    choiceSpecification: exclusiveChoiceSpec.jsonString
                ),
                teacherJeanne
            )
        }
        if (!Statement.findByTitle("Statement 3")) {
            statement3 = sequenceService.saveStatement(
                new Statement(
                    title: "Statement 3",
                    content:"Content of statement 3",
                    questionType: QuestionType.MultipleChoice,
                    choiceSpecification: multipleChoiceSpec.jsonString
                ),
                teacherJeanne
            )
        }
        if (!Statement.findByTitle("Statement 4")) {
            statement4 = sequenceService.saveStatement(
                new Statement(
                    title: "Statement 4",
                    content:"Content of statement 4",
                    questionType: QuestionType.OpenEnded,
                    choiceSpecification: openEndedChoiceSpec.jsonString
                ),
                teacherJeanne
            )
        }
        if (!Statement.findByTitle("Statement 5")) {
            statement5 = sequenceService.saveStatement(
                new Statement(
                    title: "Statement 5",
                    content:"Content of statement 5",
                    questionType: QuestionType.MultipleChoice,
                    choiceSpecification: multipleChoiceSpec.jsonString
                ),
                teacherJeanne
            )
        }
    }

    def initializeInteractions() {
        initializeResponseSubmissionInteraction()
        initializeResponseSubmissionOpenInteraction()
        initialiseEvaluationInteraction()
        initializeReadInteraction()
        initialiseEvaluationOpenInteraction()
        initializeReadOpenInteraction()
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

    private initializeMultipleChoiceSpec() {
        multipleChoiceSpec = new ChoiceSpecification()
        multipleChoiceSpec.choiceInteractionType = 'MULTIPLE'
        multipleChoiceSpec.itemCount = 5
        multipleChoiceSpec.expectedChoiceList = [
            new ChoiceItemSpecification(2, 100f/3f as Float),
            new ChoiceItemSpecification(3, 100f/3f as Float),
            new ChoiceItemSpecification(5, 100f/3f as Float)
        ]
    }
    private initializeExclusiveChoiceSpec() {
        exclusiveChoiceSpec = new ChoiceSpecification()
        exclusiveChoiceSpec.choiceInteractionType = 'EXCLUSIVE'
        exclusiveChoiceSpec.itemCount = 2
        exclusiveChoiceSpec.expectedChoiceList = [
            new ChoiceItemSpecification(2, 100f),
        ]
    }
    private initializeOpenEndedChoiceSpec() {
        openEndedChoiceSpec = new ChoiceSpecification()
    }


}
