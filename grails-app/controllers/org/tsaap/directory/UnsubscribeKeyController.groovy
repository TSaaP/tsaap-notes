package org.tsaap.directory

import grails.transaction.Transactional

@Transactional
class UnsubscribeKeyController {

    UnsubscribeKeyService unsubscribeKeyService

    def doUnsubscribeDaily() {
        def key = params.key
        def idUser = UnsubscribeKey.executeQuery("SELECT user from UnsubscribeKey where unsubscribe_key = :var" ,[var : key] )

        Settings.executeUpdate("""
                UPDATE Settings
                SET dailyNotifications = 0
                where user = :idUser
                """, [idUser: idUser[0]])

        render (view:'/directory/dailyNotifUnsubscribe')
    }

    def doUnsubscribeMention() {
        def key = params.key
        def idUser = UnsubscribeKey.executeQuery("SELECT user from UnsubscribeKey where unsubscribe_key = :var" ,[var : key] )

        Settings.executeUpdate("""
                UPDATE Settings
                SET mentionNotifications = 0
                where user = :idUser
                """, [idUser: idUser[0]])



        render (view:'/directory/mentionNotifUnsubscribe')
    }

}
