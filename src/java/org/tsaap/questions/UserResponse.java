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

import java.util.List;

/**
 * @author franck Silvestre
 */
public interface UserResponse {

    /**
     * Get the user identifier
     *
     * @return the user identifier
     */
    public String getUserIdentifier();

    /**
     * Get the question
     *
     * @return the question the response is for
     */
    public Question getQuestion();

    /**
     * Get the user answer block list
     *
     * @return the user answer block list
     */
    public List<UserAnswerBlock> getUserAnswerBlockList();

    /**
     * Get the percent credit the user receive for his answers on the question
     *
     * @return the percent credit
     */
    public Float evaluatePercentCredit();


}
