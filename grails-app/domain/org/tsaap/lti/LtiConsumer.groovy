package org.tsaap.lti

import groovy.transform.ToString

@ToString(includeFields = true)
class LtiConsumer {

    String id
    String consumerName
    String secret
    String ltiVersion = '1.0'
    String productName
    String productVersion
    String productGuid
    String cssPath
    Integer isProtected = 0
    Integer isEnabled = 1
    Date enableFrom
    Date enableUntil
    Date lastAccess
    Date dateCreated
    Date lastUpdated

    static constraints = {
        id maxSize: 255
        consumerName maxSize: 45
        secret maxSize: 32
        ltiVersion nullable: true
        productName nullable: true
        productVersion nullable: true
        productGuid nullable: true
        cssPath nullable: true
        enableFrom nullable: true
        enableUntil nullable: true
        lastAccess nullable: true
    }

    static mapping = {
        version false
        id column:'consumer_key', generator:'assigned', type:'string', name:'id'
        consumerName column: 'name'
        productName column: 'consumer_name'
        productVersion column: 'consumer_version'
        productGuid column:'consumer_guid'
        isProtected column:'protected'
        isEnabled column:'enabled'
        dateCreated column:'created'
        lastUpdated column:'updated'
    }
}
