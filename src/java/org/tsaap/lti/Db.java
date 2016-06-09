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

package org.tsaap.lti;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by dorian on 15/06/15.
 */
public class Db {
    private Connection connection = null;

    /**
     * Class constructor.
     * <p>
     * Throws an SQLException if a connection error occurs.
     */
    public Db() throws SQLException {

        StringBuilder connectionString = new StringBuilder(Config.DB_NAME);
        if ((Config.DB_USERNAME.length() > 0) || (Config.DB_PASSWORD.length() > 0)) {
            connectionString.append("?");
            if (Config.DB_USERNAME.length() > 0) {
                connectionString.append("user=").append(Config.DB_USERNAME);
                if (Config.DB_PASSWORD.length() > 0) {
                    connectionString.append("&");
                }
            }
            if (Config.DB_PASSWORD.length() > 0) {
                connectionString.append("password=").append(Config.DB_PASSWORD);
            }
        }
        try {
            // Load driver
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // Obtain connection
            this.connection = DriverManager.getConnection(connectionString.toString());
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }

    }

    /**
     * Returns the database connection.
     *
     * @return database connection instance
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * Close datatabase connection
     *
     * @throws SQLException
     */
    public void closeConnection() throws SQLException {
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
