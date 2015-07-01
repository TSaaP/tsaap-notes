package org.tsaap.lti

import javassist.NotFoundException

/**
 * Created by dorian on 01/07/15.
 */
class LtiContextInitialisationException extends NotFoundException {

    LtiContextInitialisationException(String s) {
        super(s)
    }
}
