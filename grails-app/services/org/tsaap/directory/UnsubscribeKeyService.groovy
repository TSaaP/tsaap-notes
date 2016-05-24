package org.tsaap.directory

import grails.transaction.Transactional

@Transactional
class UnsubscribeKeyService {



    UnsubscribeKey createKeyForUser (User user){
        UnsubscribeKey key = new UnsubscribeKey(unsubscribeKey: UUID.randomUUID().toString() ,user:user)
        key.save(flush: true)
        key
    }
}
