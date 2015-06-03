/*
 * Copyright 2013 Tsaap Development Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Fichier de configuration externe propre à l'application
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
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

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
        grails.serverURL = "http://localhost:8080/tsaap-notes"
    }
    production {
        grails.logging.jul.usebridge = false
        // TODO: grails.serverURL = "http://www.changeme.com"
    }
}


log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

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

    //debug 'org.springframework.security'
    //debug 'grails.app.jobs'
    //debug 'grails.app.services.org.tsaap.directory'
    //debug 'groovy.sql.Sql'

}

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'org.tsaap.directory.User'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'org.tsaap.directory.UserRole'
grails.plugins.springsecurity.authority.className = 'org.tsaap.directory.Role'

// security config
grails.plugins.springsecurity.password.algorithm = 'bcrypt'
grails.plugins.springsecurity.successHandler.defaultTargetUrl = '/notes/index?displaysMyNotes=on'

environments {
    development {
        grails.plugins.springsecurity.useSwitchUserFilter = true
    }
    test {
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
    prod {
        tsaap.auth.check_user_email = true
    }
}
