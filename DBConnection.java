package com.library.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Central place for obtaining a JDBC connection to the database.
 * Configured below for ORACLE. Update DB_URL / DB_USER / DB_PASSWORD to
 * match your local Oracle setup (see database/library_management_oracle.sql).
 *
 * --- If you ever switch back to MySQL, use this block instead ---
 * private static final String DB_URL = "jdbc:mysql://localhost:3306/library_management?useSSL=false&serverTimezone=UTC";
 * private static final String DB_USER = "root";
 * private static final String DB_PASSWORD = "YOUR_MYSQL_PASSWORD";
 * Class.forName("com.mysql.cj.jdbc.Driver");
 * -------------------------------------------------------------------
 */
public class DBConnection {

    // For Oracle XE with a pluggable database (18c/19c/21c), the service name
    // is usually XEPDB1. For older Oracle XE (11g) with a plain SID, use:
    //   jdbc:oracle:thin:@localhost:1521:XE
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    private static final String DB_USER = "library_admin";
    private static final String DB_PASSWORD = "library123"; // <-- change this to match your Oracle user

    static {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Oracle JDBC Driver not found. Add ojdbc jar (e.g. ojdbc11.jar) to WEB-INF/lib.", e);
        }
    }

    private DBConnection() {
        // utility class - prevent instantiation
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
