package org.tsaap.assignments.statement

import grails.validation.Validateable
import org.tsaap.assignments.JsonDefaultSpecification

/**
 * Created by qsaieb on 01/03/2017.
 */
@Validateable
class ChoiceItemSpecification extends JsonDefaultSpecification {

    private static final String INDEX = "index"
    private static final String SCORE = "score"

    ChoiceItemSpecification() {}

    /**
     * Create an interaction choice object
     * @param index the index of the choice in the list of choice starting from 1
     * @param score the asscociated to this choice (max = 100)
     */
    ChoiceItemSpecification(Integer index, Float score) {
        this.index = index
        this.score = score
    }

    Integer getIndex() {
        return getSpecificationProperty(INDEX)
    }

    void setIndex(Integer index) {
        setSpecificationProperty(INDEX, index)
    }

    Float getScore() {
        return getSpecificationProperty(SCORE)
    }

    void setScore(Float score) {
        setSpecificationProperty(SCORE, score)
    }


    static constraints = {
        index nullable: false
        score nullable: false, max: 100 as Float
    }
}
