package inProduction.classes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import connection.ConnectionDB;
import connection.DBException;

public class ClassesModel {
  private Connection connection;
  private Statement statement;
  private ResultSet resultSet;
  private PreparedStatement prepStatement;

  public ClassesModel() {
    this.connection = null;
    this.statement = null;
    this.resultSet = null;
    this.prepStatement = null;
  }

  public TreeMap<String, String> getOneClass(Object data) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.statement = this.connection.createStatement();
      if (!data.getClass().getSimpleName().equals("String")) {
        this.prepStatement = this.connection.prepareStatement(
        "SELECT * FROM chroniclesOfArtifacts.classes WHERE id = ?"
        );
        this.prepStatement.setInt(1, (Integer) data);
      } else {
        this.prepStatement = this.connection.prepareStatement(
        "SELECT * FROM chroniclesOfArtifacts.classes WHERE nameClass = ?"
        );
        this.prepStatement.setString(1, ((String) data).toLowerCase());
      }
      this.resultSet = this.prepStatement.executeQuery();
      while (this.resultSet.next()) {
        TreeMap<String, String> classMap = new TreeMap<String, String>();
        classMap.put("id", this.resultSet.getString(1));
        classMap.put("nameClass", this.resultSet.getString(2));
        classMap.put("functionClass", this.resultSet.getString(3));
        return classMap;
      }
      return null;
    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
  };

  public ArrayList<Map<String, String>> getAllClasses() throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.statement = this.connection.createStatement();
      this.resultSet = this.statement.executeQuery(
        "SELECT * FROM chroniclesOfArtifacts.classes"
      );
      ArrayList<Map<String, String>> listClasses = new ArrayList<Map<String, String>>();
      while(this.resultSet.next()) {
        TreeMap<String, String> line = new TreeMap<String, String>();
        line.put("id", this.resultSet.getString("id"));
        line.put("nameClass", this.resultSet.getString("nameClass"));
        line.put("functionClass", this.resultSet.getString("functionClass"));
        listClasses.add(line);
      }
      return listClasses;
    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
  }

  public boolean insertClass(String nameClass, String functionClass) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.connection.setAutoCommit(false);
      this.prepStatement = this.connection.prepareStatement(
        "INSERT INTO chroniclesOfArtifacts.classes "
        + "(nameClass, functionClass) "
        + "VALUES (?, ?)",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, nameClass.toLowerCase());
      this.prepStatement.setString(2, functionClass.toLowerCase());
      this.prepStatement.executeUpdate();
      this.resultSet = this.prepStatement.getGeneratedKeys();
      this.connection.commit();
      return true;
    }
    catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    }
  };

  public boolean updateClass(
    int id,
    String nameClass,
    String functionClass
    ) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false); 
      this.prepStatement = this.connection.prepareStatement(
        "UPDATE chroniclesOfArtifacts.classes "
        + "SET nameClass = ?, functionClass = ?"
        + " WHERE id = ?",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, nameClass.toLowerCase());
      this.prepStatement.setString(2, functionClass.toLowerCase());
      this.prepStatement.setInt(3, id);
      this.prepStatement.executeUpdate();
      this.connection.commit();
      return true;
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    } 
  };

  public boolean removeClass(String nameClass) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false);
      this.prepStatement = this.connection.prepareStatement("SET SQL_SAFE_UPDATES = 0");
      this.prepStatement.executeUpdate();
      
      this.prepStatement = this.connection.prepareStatement(
        "DELETE FROM chroniclesOfArtifacts.classes "
        + "WHERE nameClass = ? ",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, nameClass.toLowerCase());
      this.prepStatement.executeUpdate();
      this.resultSet = this.prepStatement.getGeneratedKeys();
      this.resultSet.next();
      this.connection.commit();
      return true;
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    }
  }
}
