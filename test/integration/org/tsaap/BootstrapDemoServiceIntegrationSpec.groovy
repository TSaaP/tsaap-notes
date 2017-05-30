package org.tsaap

import org.tsaap.assignments.InteractionType
import org.tsaap.assignments.StateType
import spock.lang.Specification

/**
 * Created by qsaieb on 30/05/2017.
 */
class BootstrapDemoServiceIntegrationSpec extends Specification {

  BootstrapDemoService bootstrapDemoService

  def "InitializeElaasticDemo"() {
    when: 'initialize elaastic demo'
    bootstrapDemoService.initializeElaasticDemo()

    then:
    bootstrapDemoService.assignment1 != null
    bootstrapDemoService.assignment1.title == BootstrapDemoService.titleAssignmentDemo1
    bootstrapDemoService.assignment1.owner == bootstrapDemoService.elaasticTeacher
    bootstrapDemoService.assignment1.registeredUserCount() == 3
    bootstrapDemoService.assignment1.sequences.size() == 3

    bootstrapDemoService.assignment2 != null
    bootstrapDemoService.assignment2.title == BootstrapDemoService.titleAssignmentDemo2
    bootstrapDemoService.assignment2.owner == bootstrapDemoService.elaasticTeacher
    bootstrapDemoService.assignment2.registeredUserCount() == 3
    bootstrapDemoService.assignment2.sequences.size() == 0

    bootstrapDemoService.elaasticTeacher != null
    bootstrapDemoService.elaasticTeacher.firstName == BootstrapDemoService.firstNameTeacherDemo

    bootstrapDemoService.elaasticLearnerDemo1 != null
    bootstrapDemoService.elaasticLearnerDemo1.firstName == BootstrapDemoService.firstNameLearnerDemo


    bootstrapDemoService.elaasticLearnerDemo2 != null
    bootstrapDemoService.elaasticLearnerDemo2.firstName == BootstrapDemoService.firstNameLearnerDemo

    bootstrapDemoService.elaasticLearnerDemo3 != null
    bootstrapDemoService.elaasticLearnerDemo3.firstName == BootstrapDemoService.firstNameLearnerDemo

  }

  def "RestartElaasticDemo"() {
    when: "restart elaastic demo"
    bootstrapDemoService.restartElaasticDemo()

    then:
    bootstrapDemoService.assignment1 != null
    bootstrapDemoService.assignment1.title == BootstrapDemoService.titleAssignmentDemo1
    bootstrapDemoService.assignment1.owner == bootstrapDemoService.elaasticTeacher
    bootstrapDemoService.assignment1.registeredUserCount() == 3
    bootstrapDemoService.assignment1.sequences.each {s ->
      s.activeInteraction.state == StateType.beforeStart.name()  &&
      s.activeInteraction.interactionType == InteractionType.ResponseSubmission.name()
    }

    bootstrapDemoService.assignment2 != null
    bootstrapDemoService.assignment2.title == BootstrapDemoService.titleAssignmentDemo2
    bootstrapDemoService.assignment2.owner == bootstrapDemoService.elaasticTeacher
    bootstrapDemoService.assignment2.registeredUserCount() == 3
    bootstrapDemoService.assignment2.sequences.size() == 0
    /*each {s ->
      s.activeInteraction.state == StateType.beforeStart.name()  &&
      s.activeInteraction.interactionType == InteractionType.ResponseSubmission.name()
    }*/
  }
}
