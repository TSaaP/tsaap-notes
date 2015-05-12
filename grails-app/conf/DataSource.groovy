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

hibernate {
  cache.use_second_level_cache = true
  cache.use_query_cache = false
  cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
  //cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory'
  // Hibernate 4
}

// environment specific settings
environments {
  development {
    dataSource {
      driverClassName = "com.mysql.jdbc.Driver"
      dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
      url = "jdbc:mysql://localhost/tsaap-notes"
      //url = "jdbc:mysql://localhost/tsaap-notes-dump"
      username = "tsaap"
      password = "tsaap"
      logSql = true
    }
  }
  test {
    dataSource {
      driverClassName = "com.mysql.jdbc.Driver"
      dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
      url = "jdbc:mysql://localhost/tsaap-notes-test"
      username = "tsaap"
      password = "tsaap"
      logSql = true
    }
  }

  production {
    dataSource {
      driverClassName = "com.mysql.jdbc.Driver"
      dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
      url = "jdbc:mysql://notes.tsaap.eu/tsaap-notes"
      pooled = true
      properties {
        maxActive = -1
        minEvictableIdleTimeMillis = 1800000
        timeBetweenEvictionRunsMillis = 1800000
        numTestsPerEvictionRun = 3
        testOnBorrow = true
        testWhileIdle = true
        testOnReturn = false
        validationQuery = "SELECT 1"
        jdbcInterceptors = "ConnectionState"
      }
    }
  }
}
