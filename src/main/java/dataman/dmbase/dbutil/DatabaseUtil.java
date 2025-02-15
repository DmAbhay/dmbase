package dataman.dmbase.dbutil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class DatabaseUtil {
    private String hostName;
    private String portName;
    private String databaseName;
    private String userName;
    private String password;
    private DatabaseConnectionManager connectionManager; 

    // Constructor initializes its own connection
    public DatabaseUtil(String hostName, String portName, String databaseName, String userName, String password) {
        this.hostName = hostName;
        this.portName = portName;
        this.databaseName = databaseName;
        this.userName = userName;
        this.password = password;
        this.connectionManager = new DatabaseConnectionManager(hostName, portName, databaseName, userName, password);
    }

    public Map<String, Object> getRowAsMap(String sqlQuery) {
        Map<String, Object> rowMap = new HashMap<>();

        try (PreparedStatement stmt = connectionManager.getConnection().prepareStatement(sqlQuery);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    rowMap.put(columnName, columnValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowMap.isEmpty() ? null : rowMap;
    }
    
    public String fetchSpecificField(String sqlQuery, String columnName) {
        Map<String, Object> rowMap = new HashMap<>();

        try (PreparedStatement stmt = connectionManager.getConnection().prepareStatement(sqlQuery);
             ResultSet rs = stmt.executeQuery()) {

        	if (rs.next()) {
                return rs.getString(columnName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Map<String, Object>> getRowsAsList(String sqlQuery) {
        List<Map<String, Object>> resultList = new ArrayList<>();

        try (PreparedStatement stmt = connectionManager.getConnection().prepareStatement(sqlQuery);
             ResultSet rs = stmt.executeQuery()) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> rowMap = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    rowMap.put(columnName, columnValue);
                }
                resultList.add(rowMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
}




//package dataman.dmbase.dbutil;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class DatabaseUtil {
//	
//	
//	private String hostName;
//    private String portName;
//    private String databaseName;
//    private String userName;
//    private String password;
//    
//    
//    // Initialize connection once
//    public void initializeConnection() {
//        DatabaseConnectionManager.initialize(hostName, portName, databaseName, userName, password);
//    }
//    
//
//    public String getDatabaseName(String sqlQuery, String columnName) {
//        try (PreparedStatement stmt = DatabaseConnectionManager.getConnection().prepareStatement(sqlQuery);
//             ResultSet rs = stmt.executeQuery()) {
//        	
//            if (rs.next()) {
//                return rs.getString(columnName);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//    
//
//    // Fetch whole row as a Map<String, Object>
//    public Map<String, Object> getRowAsMap(String sqlQuery) {
//        Map<String, Object> rowMap = new HashMap<>();
//
//        try (PreparedStatement stmt = DatabaseConnectionManager.getConnection().prepareStatement(sqlQuery);
//             ResultSet rs = stmt.executeQuery()) {
//
//            if (rs.next()) {
//                ResultSetMetaData metaData = rs.getMetaData();
//                int columnCount = metaData.getColumnCount();
//
//                for (int i = 1; i <= columnCount; i++) {
//                    String columnName = metaData.getColumnName(i);
//                    Object columnValue = rs.getObject(i);
//                    rowMap.put(columnName, columnValue);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return rowMap.isEmpty() ? null : rowMap;
//    }
//    
//    public List<Map<String, Object>> getRowsAsList(String sqlQuery) {
//        List<Map<String, Object>> resultList = new ArrayList<>();
//
//        try (PreparedStatement stmt = DatabaseConnectionManager.getConnection().prepareStatement(sqlQuery);
//             ResultSet rs = stmt.executeQuery()) {
//
//            ResultSetMetaData metaData = rs.getMetaData();
//            int columnCount = metaData.getColumnCount();
//
//            while (rs.next()) {
//                Map<String, Object> rowMap = new HashMap<>();
//                for (int i = 1; i <= columnCount; i++) {
//                    String columnName = metaData.getColumnName(i);
//                    Object columnValue = rs.getObject(i);
//                    rowMap.put(columnName, columnValue);
//                }
//                resultList.add(rowMap);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return resultList;
//    }
//	
//	
//
//}
