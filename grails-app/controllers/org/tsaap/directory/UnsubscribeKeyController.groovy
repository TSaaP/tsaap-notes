package org.tsaap.directory

import grails.transaction.Transactional

@Transactional
class UnsubscribeKeyController {


    def doUnsubscribeDaily() {
        def key = params.unsubKey
        def id = params.id
        User user = User.get(id)
        UnsubscribeKey = UnsubscribeKey.findByUserAndUnsubscribeKey(user, key)

        // TODO Call service method

        redirect (uri:'/directory/dailyNotifUnsubscribe')
    }

    def doUnsubscribeMention() {
        def key = params.unsubKey
        def id = params.id
        User user = User.get(id)
        UnsubscribeKey = UnsubscribeKey.findByUserAndUnsubscribeKey(user, key)

        // TODO Call service method

        redirect (uri:'/directory/mentionNotifUnsubscribe')
    }

}
