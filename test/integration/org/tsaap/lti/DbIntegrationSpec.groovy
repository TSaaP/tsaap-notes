package org.tsaap.lti

import spock.lang.Specification

/**
 * Created by dorian on 26/06/15.
 */
class DbIntegrationSpec extends Specification {

    def "test Db initilisation"() {

        when: "I want to initialise a Db"
        Db db = new Db()

        then: "I get a connection"
        db.getConnection() != null

        and: "I can close the connection"
        db.closeConnection()

        then: "The connection is closed"
        db.connection.closed

    }
}
