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

package org.tsaap.questions

import java.util.regex.Matcher

/**
 *
 * @author franck Silvestre
 */

class QuestionHelper {

  /**
   * Get answer set list from a gift formated question
   * @param inText the gift text
   * @return the list of answer set in gift format
   */
   List<String> getAnswerSetListForGiftText(String inText) {
     List<String> res = []
     Matcher matcher = inText.trim() =~ /(?<!\\)\{.*?(?<!\\)\}/ ;
     matcher.each {
        res << it.substring(1,it.size()-1)
     }
     res
   }

  /**
   * Get tittle from a gift formated question
   * @param inText
   * @return the title
   */
   String getTitleForGiftText(String inText) {
     Matcher matcher = inText.trim() =~ /^::.*::/ ;
     matcher ? matcher[0].substring(2,matcher[0].size()-2) : null
   }

}