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

package org.tsaap.notes

import org.tsaap.directory.MailCheckingService

class MailCheckingJob {

  MailCheckingService mailCheckingService

  static triggers = {
    // every 30 seconds
    cron name: 'mailCheckingCronTrigger', startDelay: 10000, cronExpression: '0 0/2 * * * ?'
  }

  def execute() {
    log.info("Start email checking job...")
    mailCheckingService.sendCheckingEmailMessages()
    log.info("End email checking  job.")
  }
}
