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

package org.tsaap.directory

class ActivationKeyGarbageCollectorJob {

    ActivationKeyService activationKeyService

    static triggers = {
        cron name: 'garbageActKeyCronTrigger', startDelay: 10000, cronExpression: '0 0 0/1 * * ?'
    }

    def execute() {
        log.debug("Start Activation Key garbage collector job ...")
        activationKeyService.removeOldActivationKeys()
        log.debug("End Activation Key garbage collector job.")
    }
}
