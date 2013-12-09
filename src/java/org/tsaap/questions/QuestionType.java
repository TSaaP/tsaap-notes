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
public enum QuestionType {

    Undefined(0),
    ExclusiveChoice(1),
    MultipleChoice(2),
    TrueFalse(3),
    FillInTheBlank(4);

    private int code;

    QuestionType(int code) {
        this.code = code;
    }

    /**
     * Get the code of que question type
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }
}


