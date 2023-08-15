package inProduction.categoryArmors;

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

public class CatArmorsModel {
  private Connection connection;
  private Statement statement;
  private ResultSet resultSet;
  private PreparedStatement prepStatement;

  public CatArmorsModel() {
    this.connection = null;
    this.statement = null;
    this.resultSet = null;
    this.prepStatement = null;
  }

  public TreeMap<String, String> getByIdOrType(Object data) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.statement = this.connection.createStatement();
      if (!data.getClass().getSimpleName().equals("String")) {
        this.prepStatement = this.connection.prepareStatement(
        "SELECT * FROM chroniclesOfArtifacts.categoryArmors WHERE id = ?"
        );
        this.prepStatement.setInt(1, (Integer) data);
      } else {
        this.prepStatement = this.connection.prepareStatement(
        "SELECT * FROM chroniclesOfArtifacts.categoryArmors WHERE typeArmor = ?"
        );
        this.prepStatement.setString(1, ((String) data).toLowerCase());
      }
      this.resultSet = this.prepStatement.executeQuery();
      while (this.resultSet.next()) {
        TreeMap<String, String> catArmorMap = new TreeMap<String, String>();
        catArmorMap.put("id", this.resultSet.getString(1));
        catArmorMap.put("typeArmor", this.resultSet.getString(2));
        catArmorMap.put("categoryArmor", this.resultSet.getString(3));
        return catArmorMap;
      }
      return null;
    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
  };

  public TreeMap<String, String> getByCatArmor(String data) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.statement = this.connection.createStatement();
      this.prepStatement = this.connection.prepareStatement(
      "SELECT * FROM chroniclesOfArtifacts.categoryArmors WHERE categoryArmor = ?"
      );
      this.prepStatement.setString(1, ((String) data).toLowerCase());
      this.resultSet = this.prepStatement.executeQuery();
      while (this.resultSet.next()) {
        TreeMap<String, String> catArmorMap = new TreeMap<String, String>();
        catArmorMap.put("id", this.resultSet.getString(1));
        catArmorMap.put("typeArmor", this.resultSet.getString(2));
        catArmorMap.put("categoryArmor", this.resultSet.getString(3));
        return catArmorMap;
      }
      return null;
    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
  };

  public ArrayList<Map<String, String>> getAllCatArmors() throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.statement = this.connection.createStatement();
      this.resultSet = this.statement.executeQuery(
        "SELECT * FROM chroniclesOfArtifacts.categoryArmors"
      );
      ArrayList<Map<String, String>> listCatArmors = new ArrayList<Map<String, String>>();
      while(this.resultSet.next()) {
        TreeMap<String, String> line = new TreeMap<String, String>();
        line.put("id", this.resultSet.getString("id"));
        line.put("typeArmor", this.resultSet.getString("typeArmor"));
        line.put("categoryArmor", this.resultSet.getString("categoryArmor"));
        listCatArmors.add(line);
      }
      return listCatArmors;
    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
  }

  public boolean insertCatArmor(
    String typeArmor,
    String categoryArmor
  ) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.connection.setAutoCommit(false);
      this.prepStatement = this.connection.prepareStatement(
        "INSERT INTO chroniclesOfArtifacts.categoryArmors "
        + "(typeArmor, categoryArmor) "
        + "VALUES (?, ?)",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, typeArmor.toLowerCase());
      this.prepStatement.setString(2, categoryArmor.toLowerCase());
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

  public boolean updateCatArmor(
    int id,
    String typeArmor,
    String categoryArmor
    ) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false); 
      this.prepStatement = this.connection.prepareStatement(
        "UPDATE chroniclesOfArtifacts.categoryArmors "
        + "SET typearmor = ?, categoryArmor = ?"
        + " WHERE id = ?",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, typeArmor.toLowerCase());
      this.prepStatement.setString(2, categoryArmor.toLowerCase());
      this.prepStatement.setInt(3, id);
      this.prepStatement.executeUpdate();
      this.connection.commit();
      return true;
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    } 
  };

  public boolean removeCatArmor(String typeArmor) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false);
      this.prepStatement = this.connection.prepareStatement("SET SQL_SAFE_UPDATES = 0");
      this.prepStatement.executeUpdate();
      
      this.prepStatement = this.connection.prepareStatement(
        "DELETE FROM chroniclesOfArtifacts.categoryArmors "
        + "WHERE typeArmor = ? ",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, typeArmor.toLowerCase());
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
