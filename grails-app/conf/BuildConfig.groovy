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

        //test ":code-coverage:1.2.7"

        compile ":mail:1.0.1"
        compile ":quartz:1.0"

    }
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