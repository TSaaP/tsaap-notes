package org.tsaap

import org.tsaap.lti.LtiContextInitialisationException
import org.tsaap.skin.SkinUtil

class IndexController {

  def index() {
    render(view: '/'+SkinUtil.getView(params, session, 'index'))
  }

  def notFound() {
    render(view: '/'+SkinUtil.getView(params, session, '404'))
  }

  def showTerms() {
    render(view: '/'+SkinUtil.getView(params, session, 'terms'))
  }

  def showLtiTerms() {
    render(view: '/lti/'+SkinUtil.getView(params, session, 'terms'))
  }

  def showErrorPage() {
    render(view: '/'+SkinUtil.getView(params, session, 'error'))
  }

  def showLtiErrorPage() {
    render(view: '/lti/'+SkinUtil.getView(params, session, 'error'))
  }

  def testError() {
    throw new IllegalStateException("This is a default demo error")
  }

  def testLtiError() {
    throw new LtiContextInitialisationException("This is a LTI demo error")
  }
}
