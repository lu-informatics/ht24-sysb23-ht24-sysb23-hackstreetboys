package se.lu.ics.data;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MetadataDao {


   private ConnectionHandler connectionHandler;


   public MetadataDao() throws IOException {
       this.connectionHandler = new ConnectionHandler();
   }


   // METHOD: fetchAllColumnNames
   public List<String> fetchAllColumnNames() {
       String query = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS";
       List<String> columnNames = new ArrayList<>();


       try (Connection connection = connectionHandler.getConnection();
               PreparedStatement statement = connection.prepareStatement(query);
               ResultSet resultSet = statement.executeQuery()) {


           while (resultSet.next()) {
               String columnName = resultSet.getString("COLUMN_NAME");
               columnNames.add(columnName);
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }


       return columnNames;
   }


   // METHOD: Fetch all primary key constraints
   public List<String> fetchAllPrimaryKeyConstraints() {
       String query = "SELECT CONSTRAINT_NAME FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS " +
               "WHERE CONSTRAINT_TYPE = 'PRIMARY KEY'";
       List<String> primaryKeyConstraints = new ArrayList<>();


       try (Connection connection = connectionHandler.getConnection();
               PreparedStatement statement = connection.prepareStatement(query);
               ResultSet resultSet = statement.executeQuery()) {


           while (resultSet.next()) {
               String constraintName = resultSet.getString("CONSTRAINT_NAME");
               primaryKeyConstraints.add(constraintName);
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }


       return primaryKeyConstraints;
   }


   // METHOD: Fetch all check constraints
   public List<String> fetchAllCheckConstraints() {
       String query = "SELECT CONSTRAINT_NAME FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS " +
               "WHERE CONSTRAINT_TYPE = 'CHECK'";
       List<String> checkConstraints = new ArrayList<>();


       try (Connection connection = connectionHandler.getConnection();
               PreparedStatement statement = connection.prepareStatement(query);
               ResultSet resultSet = statement.executeQuery()) {


           while (resultSet.next()) {
               String constraintName = resultSet.getString("CONSTRAINT_NAME");
               checkConstraints.add(constraintName);
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }


       return checkConstraints;
   }


   // METHOD: Fetch all column names from consultant table, no integers
   public List<String> fetchNonIntegerColumns() throws DaoException {
       String query = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS " +
               "WHERE TABLE_NAME = 'Consultant' " +
               "AND DATA_TYPE != 'int'";
       List<String> nonIntegerColumns = new ArrayList<>();


       try (Connection connection = connectionHandler.getConnection();
               PreparedStatement statement = connection.prepareStatement(query);
               ResultSet resultSet = statement.executeQuery()) {


           while (resultSet.next()) {
               String columnName = resultSet.getString("COLUMN_NAME");
               nonIntegerColumns.add(columnName);
           }


       } catch (SQLException e) {
           throw new DaoException("Error fetching non-INTEGER columns from Consultant table.", e);
       }


       return nonIntegerColumns;
   }
   // METHOD: Fetch table with most rows
   public String[] fetchTableWithMostRows() throws DaoException {
       String query = "SELECT TOP 1 TABLE_NAME, ROW_COUNT " +
               "FROM (" +
               "    SELECT TABLE_NAME, SUM(p.rows) AS ROW_COUNT " +
               "    FROM INFORMATION_SCHEMA.TABLES t " +
               "    INNER JOIN sys.partitions p ON t.TABLE_NAME = OBJECT_NAME(p.object_id) " +
               "    WHERE t.TABLE_TYPE = 'BASE TABLE' AND p.index_id IN (0, 1) " +
               "    GROUP BY TABLE_NAME" +
               ") AS TableRowCounts " +
               "ORDER BY ROW_COUNT DESC;";


       String[] result = new String[2]; // Array to hold table name and row count


       try (Connection connection = connectionHandler.getConnection();
               PreparedStatement statement = connection.prepareStatement(query);
               ResultSet resultSet = statement.executeQuery()) {


           if (resultSet.next()) {
               result[0] = resultSet.getString("TABLE_NAME"); // Table name
               result[1] = String.valueOf(resultSet.getLong("ROW_COUNT")); // Row count as long
           }
       } catch (SQLException e) {
           throw new DaoException("Error fetching table with most rows from database.", e);
       }


       return result; // Return an array containing the table name and row count
   }


}
