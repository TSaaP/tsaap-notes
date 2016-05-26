package org.tsaap.directory

import org.apache.commons.lang.time.DateUtils

class ActivationKeyService {

    //def grailsApplication

    def removeOldActivationKeys() {
        def keys = ActivationKey.findAllByDateCreatedLessThan(DateUtils.addHours(new Date(), -3))
        ActivationKey.deleteAll(keys)
    }
}
