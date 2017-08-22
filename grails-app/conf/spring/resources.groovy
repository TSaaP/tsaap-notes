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

import liquibase.integration.spring.SpringLiquibase
import org.tsaap.assignments.ia.DefaultResponseRecommendationService
import org.tsaap.attachement.AttachementDataStore


beans = {

    //springSecurityService(SpringSecurityService)
    springConfig.addAlias "springSecurityService", "springSecurityCoreSpringSecurityService"

    // beans pour la migration des données
    liquibase(SpringLiquibase) {
        dataSource = ref("dataSource")
        changeLog = "classpath:migrations/changelog-tsaap-notes-incremental.xml"
    }

    // Configuration of datastore

    dataStore(AttachementDataStore) { bean ->
        path = application.config.tsaap.datastore.path ?: null
        bean.initMethod = 'initFileDataStore'
    }

    // configuration of algorithm for peer learning
    responseRecommendationService(DefaultResponseRecommendationService) { bean ->
        //bean.scope = 'request'
    }
}

