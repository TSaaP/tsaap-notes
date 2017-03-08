package migrations.Schema

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * Created by qsaieb on 08/03/2017.
 */
import groovy.sql.Sql
import org.tsaap.assignments.QuestionType

QuestionType getQuestionType(String choiceInteractionType) {
  QuestionType questionType
  if (choiceInteractionType == 'MULTIPLE') {
    questionType = QuestionType.MultipleChoice
  } else if (choiceInteractionType == 'EXCLUSIVE') {
    questionType = QuestionType.ExclusiveChoice
  } else {
    questionType = QuestionType.OpenEnded
  }

  questionType
}

Map getJsonRepresentation (String stringRepresentation)  {
  JsonSlurper jsonSlurper = new JsonSlurper()
  Map specificationProperties = jsonSlurper.parseText(stringRepresentation)

  specificationProperties
}

Map extractSpecification (Map sequenceSpec) {
  Map choiceSpecification = [:]

  if (sequenceSpec['choiceInteractionType'] != null) {
    choiceSpecification['choiceInteractionType'] =  sequenceSpec['choiceInteractionType']
    choiceSpecification['itemCount'] = sequenceSpec['itemCount']
    choiceSpecification['expectedChoiceList'] = sequenceSpec['expectedChoiceList']
  } else {
    choiceSpecification = null
  }

  choiceSpecification
}

// TODO Grails Environnement
def sql = Sql.newInstance('jdbc:mysql://localhost/tsaap-notes', 'tsaap', 'tsaap');

// Get all interactions
sql.eachRow('SELECT sequence_id, specification FROM interaction WHERE rank = 1') { row ->
  String interactionSpec = row.specification
  int sequenceId = row.sequence_id

  // Get statement correspond with interaction
  def result = sql.rows('SELECT statement_id FROM sequence WHERE id = :sequenceId',[sequenceId: sequenceId] )
  int statementId = (int) result[0]['statement_id']

  // Create a new state for statement
  Map JsonInteractionSpec = getJsonRepresentation(interactionSpec)
  Map choiceSpecification = extractSpecification(JsonInteractionSpec)
  QuestionType questionType = getQuestionType(JsonInteractionSpec['choiceInteractionType'])

  // Update statement
  sql.executeUpdate([
      choiceSpec: choiceSpecification == null ? null : JsonOutput.toJson(choiceSpecification),
      questionType: questionType.name(),
      id: statementId
  ],
      'UPDATE statement SET choice_specification= :choiceSpec, question_type= :questionType WHERE id= :id'
  )
}

