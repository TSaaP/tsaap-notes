
/*
 * Copyright (C) 2013-2016 Université Toulouse 3 Paul Sabatier
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

// Fichier de configuration externe par défaut
grails.config.locations = ["file:${userHome}/.grails/elaastic-questions-config.groovy"]

// Fichier de configuration externe spécifique (si la propriété système est définie)
def appConfigLocation = System.properties["${appName}.config.location"]
if (appConfigLocation) {
    grails.config.locations = ["file:$appConfigLocation"]
}


grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [all          : '*/*',
                     atom         : 'application/atom+xml',
                     css          : 'text/css',
                     csv          : 'text/csv',
                     form         : 'application/x-www-form-urlencoded',
                     html         : ['text/html', 'application/xhtml+xml'],
                     js           : 'text/javascript',
                     json         : ['application/json', 'text/json'],
                     multipartForm: 'multipart/form-data',
                     rss          : 'application/rss+xml',
                     text         : 'text/plain',
                     hal          : ['application/hal+json', 'application/hal+xml'],
                     xml          : ['text/xml', 'application/xml'],
                     pdf          : 'application/pdf',
                     rtf          : 'application/rtf',
                     excel        : 'application/vnd.ms-excel',
                     ods          : 'application/vnd.oasis.opendocument.spreadsheet',
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*', '/fonts/*', '/semantic/*']
grails.resources.adhoc.includes = ['/images/**', '/css/**', '/js/**', '/plugins/**', '/fonts/**', 'semantic/**']

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

//GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside ${}
                scriptlet = 'html' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        filteringCodecForContentType {
            //'text/html' = 'html'
        }
    }
}

grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart = false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

environments {
    development {
        grails.logging.jul.usebridge = true
        grails.resources.debug=true
        grails.serverURL = "http://localhost:8080/elaastic-questions"
    }
    production {
        grails.logging.jul.usebridge = false
    }
    elaasticQuestionsDemo {
        grails.logging.jul.usebridge = false
    }
}

environments {
    development {
        log4j = {

            error 'org.codehaus.groovy.grails.web.servlet',  //  controllers
                    'org.codehaus.groovy.grails.web.pages', //  GSP
                    'org.codehaus.groovy.grails.web.sitemesh', //  layouts
                    'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
                    'org.codehaus.groovy.grails.web.mapping', // URL mapping
                    'org.codehaus.groovy.grails.commons', // core / classloading
                    'org.codehaus.groovy.grails.plugins', // plugins
                    'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
                    'org.springframework',
                    'org.hibernate',
                    'net.sf.ehcache.hibernate'

        }
    }
    test {
        log4j = {

            error 'org.codehaus.groovy.grails.web.servlet',  //  controllers
                    'org.codehaus.groovy.grails.web.pages', //  GSP
                    'org.codehaus.groovy.grails.web.sitemesh', //  layouts
                    'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
                    'org.codehaus.groovy.grails.web.mapping', // URL mapping
                    'org.codehaus.groovy.grails.commons', // core / classloading
                    'org.codehaus.groovy.grails.plugins', // plugins
                    'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
                    'org.springframework',
                    'org.hibernate',
                    'net.sf.ehcache.hibernate'
        }
    }

}

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'org.tsaap.directory.User'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'org.tsaap.directory.UserRole'
grails.plugins.springsecurity.authority.className = 'org.tsaap.directory.Role'

// security config
grails.plugins.springsecurity.password.algorithm = 'bcrypt'
grails.plugins.springsecurity.successHandler.defaultTargetUrl = '/home/index'

environments {
    development {
        grails.plugins.springsecurity.useSwitchUserFilter = true
    }
    test {
        // hack : in test environment, the bcrypt is not considered
        grails.plugins.springsecurity.password.algorithm = 'SHA-1'
    }
    travis_ci {
        // hack : in test environment, the bcrypt is not considered
        grails.plugins.springsecurity.password.algorithm = 'SHA-1'
    }
}

// email checking on subscription

environments {
    development {
        tsaap.auth.check_user_email = true
        tsaap.datastore.path = '/opt/shared/tsaap-repo'
    }
    test {
        tsaap.auth.check_user_email = false
        tsaap.datastore.path = '/opt/shared/tsaap-repo'
    }
    travis_ci {
        tsaap.auth.check_user_email = false
        tsaap.datastore.path = './tsaap-repo'
    }
    production {
        tsaap.auth.check_user_email = true
    }
    elaasticQuestionsDemo {
        tsaap.auth.check_user_email = true
    }
}

ckeditor.config = '/ckeditor/config.js'