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

grails.project.dependency.resolver = "maven"  // or ivy
grails.project.dependency.resolution = {
  // inherit Grails' default dependencies
  inherits("global") {
    // uncomment to disable ehcache
    // excludes 'ehcache'
  }
  log "warn"
  // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
  repositories {
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
    // runtime 'mysql:mysql-connector-java:5.1.24'

    // dependencies for tests
    test "org.spockframework:spock-grails-support:0.7-groovy-2.0"
  }

  plugins {
    // plugins for the build system only
    build(":release:3.0.0.BUILD-SNAPSHOT",
          ":rest-client-builder:1.0.3") {
      export = false
    }

    build ":tomcat:7.0.39"

    // plugins for the compile step
    compile ":scaffolding:1.0.0"
    compile ':cache:1.0.1'
    compile ":spring-security-core:1.2.7.3"

    // plugins for test step
    test(":spock:0.7") {
      exclude "spock-grails-support"
    }

    // plugins for the runtime only
    runtime ":hibernate:3.6.10.M3"

    runtime ":jquery:1.10.2"
    runtime ":resources:1.2"
  }
}
