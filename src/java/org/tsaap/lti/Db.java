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
