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

import grails.util.Environment
import grails.util.Holders
import groovy.sql.Sql
import migration.DataMigrationService
import org.tsaap.BootstrapService
import org.tsaap.lti.Config

import java.util.logging.Level

class BootStrap {

    BootstrapService bootstrapService
    DataMigrationService dataMigrationService

    def init = { servletContext ->
        dataMigrationService.migrateStatement()
        bootstrapService.initializeReferenceData()
        Environment.executeForCurrentEnvironment {
            development {
                Sql.LOG.level = Level.FINE
                bootstrapService.inializeDevUsers()
            }
            demo {
                bootstrapService.inializeDevUsers()
            }
        }
        // inti config db connection for LTI
        def conf = Holders.config
        Config.DB_NAME = conf.dataSource.url
        Config.DB_USERNAME = conf.dataSource.username
        Config.DB_PASSWORD = conf.dataSource.password
    }

    def destroy = {}
}
