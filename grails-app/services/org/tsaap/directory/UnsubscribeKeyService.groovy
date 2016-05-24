package org.tsaap.directory

import grails.transaction.Transactional
import groovy.sql.Sql

import javax.sql.DataSource

@Transactional
class UnsubscribeKeyService {


    DataSource dataSource
    UnsubscribeKey createKeyForUser (User user){
        UnsubscribeKey key = new UnsubscribeKey(unsubscribeKey: UUID.randomUUID().toString() ,user:user)
        key.save(flush: true)
        key
    }

}
