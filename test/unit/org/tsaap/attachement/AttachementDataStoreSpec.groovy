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
