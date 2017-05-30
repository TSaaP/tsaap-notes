package org.tsaap

import grails.transaction.Transactional
import org.tsaap.assignments.*
import org.tsaap.assignments.interactions.ChoiceInteractionType
import org.tsaap.assignments.interactions.EvaluationSpecification
import org.tsaap.assignments.interactions.InteractionChoice
import org.tsaap.assignments.interactions.ResponseSubmissionSpecification
import org.tsaap.directory.RoleEnum
import org.tsaap.directory.User
import org.tsaap.directory.UserAccountService

/**
 * run BootstrapDemoServiceIntegrationSpec to check demo context
 */
@Transactional
class BootstrapDemoService {

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

    AssignmentService assignmentService
    SequenceService sequenceService
    UserAccountService userAccountService

    public final static String firstNameTeacherDemo = "teacher-demo"
    public final static String firstNameLearnerDemo = "learner-demo"
    public final static String titleAssignmentDemo1 = "Assignment 1 de démonstration"
    public final static String titleAssignmentDemo2 = "Assignment 2 de démonstration"


    def initializeElaasticDemo () {
        initializeElaasticUsers()
        initializeElaasticStatement()
        initializeInteractions()
        initializeElaasticAssignment()
        assignLearnerToAssignment()
    }


    def restartElaasticDemo() {
        Assignment assignmentDemo1 = Assignment.findByTitle(titleAssignmentDemo1)
        if (assignmentDemo1) {
            assignmentService.deleteAssignment(assignmentDemo1, assignment1.owner, true)
        }
        Assignment assignmentDemo2 = Assignment.findByTitle(titleAssignmentDemo2)
        if (assignmentDemo2) {
            assignmentService.deleteAssignment(assignmentDemo2, assignmentDemo2.owner, true)
        }

        initializeElaasticDemo()
    }


    private void initializeElaasticUsers() {
        elaasticTeacher = User.findByUsername("teacherdemo1")
        if (!elaasticTeacher) {
            def user = new User(firstName: firstNameTeacherDemo, lastName: "teacherdemo1", username: "teacherdemo1", password: "teacherdemo1", email: 'elaT@elaT.com')
            elaasticTeacher = userAccountService.addUser(user, RoleEnum.TEACHER_ROLE.role, true, 'fr')
        }

        elaasticTeacherLearner = User.findByUsername("teacherlearner1")
        if (!elaasticTeacherLearner) {
            def user = new User(firstName: "firstNameLearnerDemo", lastName: "teacherlearner1", username: "teacherlearner1", password: "teacherlearner1", email: 'elaTL@delaTL.com')
            elaasticTeacherLearner = userAccountService.addUser(user, RoleEnum.TEACHER_ROLE.role, true, 'fr')
        }

        elaasticLearnerDemo1 = User.findByUsername("learnerdemo1")
        if (!elaasticLearnerDemo1) {
            def user = new User(firstName: firstNameLearnerDemo, lastName: "learnerdemo1", username: "learnerdemo1", password: "learnerdemo1", email: 'elaL-1@gmail.com')
            elaasticLearnerDemo1 = userAccountService.addUser(user, RoleEnum.STUDENT_ROLE.role, true, 'fr')
        }

        elaasticLearnerDemo2 = User.findByUsername("learnerdemo2")
        if (!elaasticLearnerDemo2) {
            def user = new User(firstName: firstNameLearnerDemo, lastName: "learnerdemo2", username: "learnerdemo2", password: "learnerdemo2", email: 'elaL-2@gmail.com')
            elaasticLearnerDemo2 = userAccountService.addUser(user, RoleEnum.STUDENT_ROLE.role, true, 'fr')
        }

        elaasticLearnerDemo3 = User.findByUsername("learnerdemo3")
        if (!elaasticLearnerDemo3) {
            def user = new User(firstName: firstNameLearnerDemo, lastName: "learnerdemo3", username: "learnerdemo3", password: "learnerdemo3", email: 'elaL-3@gmail.com')
            elaasticLearnerDemo3 = userAccountService.addUser(user, RoleEnum.STUDENT_ROLE.role, true, 'fr')
        }
    }

    private void initializeElaasticAssignment () {
        assignment1 = Assignment.findByTitle(titleAssignmentDemo1)
        if (!assignment1) {
            assignment1 = new Assignment(
                owner: elaasticTeacher,
                title: titleAssignmentDemo1,
            );
            assignmentService.saveAssignment(assignment1)
            sequenceService.addSequenceToAssignment(assignment1, elaasticTeacher, statement1, [responseSubmissionInteraction,evaluationInteraction, readInteraction])
            sequenceService.addSequenceToAssignment(assignment1, elaasticTeacher, statement2, [responseSubmissionOpenInteraction,evaluationOpenInteraction, readOpenInteraction])
            sequenceService.addSequenceToAssignment(assignment1, elaasticTeacher, statement3, [exclusifResponseSubmissionInteraction,exclusifEvaluationInteraction, exclusifReadInteraction])
        }

        assignment2 = Assignment.findByTitle(titleAssignmentDemo2)
        if (!assignment2) {
            assignment2 = new Assignment(
                owner: elaasticTeacher,
                title: titleAssignmentDemo2
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

  /**
   * Assign learner to assignment
   */
    private void assignLearnerToAssignment() {
        assignmentService.registerUserOnAssignment(elaasticLearnerDemo1, assignment1)
        assignmentService.registerUserOnAssignment(elaasticLearnerDemo2, assignment1)
        assignmentService.registerUserOnAssignment(elaasticLearnerDemo3, assignment1)
        assignmentService.registerUserOnAssignment(elaasticLearnerDemo1, assignment2)
        assignmentService.registerUserOnAssignment(elaasticLearnerDemo2, assignment2)
        assignmentService.registerUserOnAssignment(elaasticLearnerDemo3, assignment2)
    }

  /**
   * Initialize interactions
   */
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
