grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {
  // inherit Grails' default dependencies
  inherits("global") {
    // uncomment to disable ehcache
    // excludes 'ehcache'
  }
  log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
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
    compile ":taggable:1.0.1"


    // plugins for the runtime only
    runtime ":hibernate:3.6.10.M3"

    runtime ":jquery:1.9.1"
    runtime ":resources:1.2"
  }
}
