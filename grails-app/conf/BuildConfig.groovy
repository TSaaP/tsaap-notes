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

grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.war.file = "target/${appName}.war"


grails.project.dependency.resolver = "maven"  // or ivy
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false
    // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        inherits true // Whether to inherit repository definitions from plugins
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenLocal()
        mavenCentral()
        mavenRepo "http://repo.grails.org/grails/core"
        mavenRepo "https://jitpack.io"
        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        runtime 'mysql:mysql-connector-java:5.1.24'
        compile 'org.gcontracts:gcontracts-grails:1.2.12'
        compile 'org.liquibase:liquibase-core:3.2.2'
        compile 'commons-beanutils:commons-beanutils:1.8.3'

        compile 'org.springframework.security:spring-security-crypto:3.2.3.RELEASE'

        compile 'com.github.TSaaP:tsaap-questions:1.0'
        compile 'com.github.TSaaP:tsaap-lti:0.2'

        // Latest httpcore and httpmime for Coveralls plugin
        build 'org.apache.httpcomponents:httpcore:4.3.2'
        build 'org.apache.httpcomponents:httpclient:4.3.2'
        build 'org.apache.httpcomponents:httpmime:4.3.3'
    }

    plugins {

        build ":tomcat:7.0.47"

        // plugins for the compile step
        compile ":scaffolding:2.0.1"
        compile ':cache:1.1.1'
        compile ":spring-security-core:1.2.7.3"
        compile ":codenarc:0.19"

        // plugins for the runtime only
        runtime ":hibernate:3.6.10.6" //or ":hibernate4:4.1.11.6"

        runtime ":jquery:1.10.2.2"
        runtime ":resources:1.2.1"

        compile ":mail:1.0.1"
        compile ":quartz:1.0"

        compile ":export:1.6"

        // Coveralls plugin
        build(':coveralls:0.1.3', ':rest-client-builder:1.0.3') {
            export = false
        }
        test(':code-coverage:1.2.7') {
            export = false
        }

    }
}

coverage {
    exclusions = ["CustomConfig*"]
}

codenarc.properties = {
    // Each property definition is of the form:  RULE.PROPERTY-NAME = PROPERTY-VALUE
    GrailsPublicControllerMethod.enabled = false

}

codenarc.reports = {
    // Each report definition is of the form:
    //    REPORT-NAME(REPORT-TYPE) {
    //        PROPERTY-NAME = PROPERTY-VALUE
    //        PROPERTY-NAME = PROPERTY-VALUE
    //    }

    XmlReport('xml') {                    // The report name "MyXmlReport" is user-defined; Report type is 'xml'
        outputFile = 'target/CodeNarc-Report.xml'  // Set the 'outputFile' property of the (XML) Report
        title = 'XML Report'             // Set the 'title' property of the (XML) Report
    }
    HtmlReport('html') {                  // Report type is 'html'
        outputFile = 'target/CodeNarc-Report.html'
        title = 'HTML Report'
    }
}