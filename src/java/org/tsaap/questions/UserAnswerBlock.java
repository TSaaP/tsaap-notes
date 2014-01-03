/*
 * Copyright 2013 Tsaap Development Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tsaap.questions;

import java.util.List;

/**
 * @author franck Silvestre
 */
public interface UserAnswerBlock {

    /**
     * Get the list of answers given y the user for the current answer block
     * @return
     */
    public List<Answer> getAnswerList();

    /**
     * Get the percent credit the user receive for his answers on the current block
     * @return the percent credit
     */
    public Float evaluatePercentCredit();


}
