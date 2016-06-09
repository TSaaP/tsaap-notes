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

package org.tsaap.questions.impl;

import org.tsaap.questions.Answer;

/**
 * @author franck Silvestre
 */
public class DefaultAnswer implements Answer {

    private String textValue;
    private Float percentCredit;
    private String identifier;
    private String feedback;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultAnswer that = (DefaultAnswer) o;

        if (!identifier.equals(that.identifier)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    /**
     * Get the text value of the answer
     *
     * @return the text value of the answer
     */
    public String getTextValue() {
        return textValue;
    }

    /**
     * Set te text value of the answer
     *
     * @param textValue the new text value
     */
    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    /**
     * Get the percent credit the answer represents in the answer set
     *
     * @return the percent credit
     */
    public Float getPercentCredit() {
        return percentCredit;
    }

    /**
     * Set the percent credit of the answer
     *
     * @param percentCredit the percent credit
     */
    public void setPercentCredit(Float percentCredit) {
        this.percentCredit = percentCredit;
    }

    /**
     * Get the identifier of the answer relative to the question
     *
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Set the identifier
     *
     * @param identifier the new identifier
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Set the feedback
     *
     * @param feedback
     */
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    /**
     * Get the feedback to present to a student for this answer
     *
     * @return
     */
    public String getFeedBack() {
        return feedback;
    }
}
