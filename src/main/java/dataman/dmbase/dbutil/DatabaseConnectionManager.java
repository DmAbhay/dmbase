package dataman.dmbase.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {
    private Connection connection;

    // Constructor initializes a new connection for each instance
    public DatabaseConnectionManager(String hostName, String portName, String databaseName, String userName, String password) {
        String url = String.format("jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true;", 
                                   hostName, portName, databaseName);
        try {
            this.connection = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get the instance-specific connection
    public Connection getConnection() {
        return connection;
    }

    // Close the connection when the application stops
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}






//package dataman.dmbase.dbutil;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class DatabaseConnectionManager {
//    private static Connection connection;
//
//    // Private constructor to prevent instantiation
//    private DatabaseConnectionManager() {}
//
//    // Method to initialize connection
//    public static void initialize(String hostName, String portName, String databaseName, String userName, String password) {
//        if (connection == null) {
//            String url = String.format("jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true;", 
//                                        hostName, portName, databaseName);
//            try {
//                connection = DriverManager.getConnection(url, userName, password);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    // Method to get connection
//    public static Connection getConnection() {
//        return connection;
//    }
//
//    // Close connection when the application stops
//    public static void closeConnection() {
//        try {
//            if (connection != null && !connection.isClosed()) {
//                connection.close();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
