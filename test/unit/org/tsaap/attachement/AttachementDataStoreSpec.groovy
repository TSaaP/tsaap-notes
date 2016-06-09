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

package org.tsaap.attachement

import org.tsaap.uploadImage.DataStoreException
import spock.lang.Specification

/**
 * Created by dorian on 29/06/15.
 */
class AttachementDataStoreSpec extends Specification {

    def "test init file data store errors"() {

        given: "an attachement data store"
        AttachementDataStore attachementDataStore = new AttachementDataStore()

        when: "I want to init the Attachement data store with a null path"
        attachementDataStore.path = null
        attachementDataStore.initFileDataStore()

        then: "I get an exception"
        thrown(DataStoreException)

        when: "I want to init Attachement data store with a path who's not exist"
        attachementDataStore.path = "a"
        attachementDataStore.initFileDataStore()

        then: "I get an exception"
        thrown(DataStoreException)

        when: "I want to init Attachement data store with a given path who's not a directory"
        attachementDataStore.path = "/opt/shared/tsaap-repo/grails_logo.png"
        attachementDataStore.initFileDataStore()

        then: "I get an exception"
        thrown(DataStoreException)

        when: "I want to init Attachement data store with a given path who's a directory but without write right"
        attachementDataStore.path = "/opt/shared/tsaap-repo/test"
        println new File(attachementDataStore.path).canWrite()
        attachementDataStore.initFileDataStore()

        then: "I get an exception"
        thrown(DataStoreException)
    }
}
