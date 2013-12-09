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

/**
 * @author franck Silvestre
 */
public interface Answer {


    /**
     * Get the identifier of the answer relative to the question
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
     * @return
     */
    public String getFeedBack();
}
