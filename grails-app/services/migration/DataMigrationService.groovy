package migration

import grails.transaction.Transactional
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.sql.Sql
import org.tsaap.assignments.Interaction
import org.tsaap.assignments.QuestionType
import org.tsaap.assignments.Sequence
import org.tsaap.assignments.Statement

@Transactional
class DataMigrationService {


    def migrateStatement() {
        List<Statement> statements = Statement.findAllByQuestionTypeIsNull();
        log.info('***** Number of statements have to migrate : ' + statements.size() + ' ****')
        statements.each { statement ->
            QuestionType questionType = QuestionType.Undefined
            Sequence sequence = Sequence.findByStatement(statement)
            Interaction interaction = Interaction.findBySequenceAndRank(sequence, 1)
            Map choiceSpecification = null
            if (interaction) {
                String interactionSpec = interaction.specification
                // Create a new state for statement
                Map JsonInteractionSpec = getJsonRepresentation(interactionSpec)
                choiceSpecification = extractSpecification(JsonInteractionSpec)
                questionType = getQuestionType(JsonInteractionSpec['choiceInteractionType'])
            }
            // Update statement
            statement.questionType = questionType
            statement.choiceSpecification = choiceSpecification == null ? null : JsonOutput.toJson(choiceSpecification)
            statement.save(flush: true)
            if (statement.hasErrors()) {
                log.error(statement.errors.allErrors)
            }
        }
    }


    private QuestionType getQuestionType(String choiceInteractionType) {
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

    private Map getJsonRepresentation(String stringRepresentation) {
        JsonSlurper jsonSlurper = new JsonSlurper()
        Map specificationProperties = jsonSlurper.parseText(stringRepresentation)

        specificationProperties
    }

    private Map extractSpecification(Map sequenceSpec) {
        Map choiceSpecification = [:]

        if (sequenceSpec['choiceInteractionType'] != null) {
            choiceSpecification['choiceInteractionType'] = sequenceSpec['choiceInteractionType']
            choiceSpecification['itemCount'] = sequenceSpec['itemCount']
            choiceSpecification['expectedChoiceList'] = sequenceSpec['expectedChoiceList']
        } else {
            choiceSpecification = null
        }

        choiceSpecification
    }
}
