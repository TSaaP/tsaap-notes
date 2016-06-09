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

package org.tsaap.questions;

/**
 * @author franck Silvestre
 */
public interface Answer {


    /**
     * Get the identifier of the answer relative to the question
     *
     * @return the identifier
     */
    public String getIdentifier();

    /**
     * Get the text value of the answer
     *
     * @return the text value of the answer
     */
    public String getTextValue();

    /**
     * Get the percent credit the answer represents in the answer set
     *
     * @return the percent credit
     */
    public Float getPercentCredit();

    /**
     * Get the feedback to present to a student for this answer
     *
     * @return
     */
    public String getFeedBack();
}
