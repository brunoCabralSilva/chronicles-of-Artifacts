package model;

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

public class PropertiesModel {
  private Connection connection;
  private ResultSet resultSet;
  private PreparedStatement prepStatement;
  WeaponPropertiesModel weaponsPropertiesModel;

  public PropertiesModel() {
    this.connection = null;
    this.resultSet = null;
    this.prepStatement = null;
    this.weaponsPropertiesModel = new WeaponPropertiesModel();
  }

  public ArrayList<Map<String, Object>> getProperties(Object data) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      String query;
      if (data == "all") {
        query = "SELECT * FROM chroniclesOfArtifacts.properties";
      } else if (data.getClass().getSimpleName().equals("String")) {
        query = "SELECT * FROM chroniclesOfArtifacts.properties WHERE property = ?";
      } else {
        query = "SELECT * FROM chroniclesOfArtifacts.properties WHERE id = ?";
      }
      this.prepStatement = this.connection.prepareStatement(query);
      if (data instanceof Integer) {
        this.prepStatement.setInt(1, (Integer) data);
      } else if (data instanceof String && data != "all") {
        this.prepStatement.setString(1, ((String) data).toLowerCase());
      }
      this.resultSet = this.prepStatement.executeQuery();
      
      ArrayList<Map<String, Object>> listProperties = new ArrayList<Map<String, Object>>();

      
      while (this.resultSet.next()) {
        TreeMap<String, Object> line = new TreeMap<String, Object>();
        ArrayList<String> properties = this.weaponsPropertiesModel.weaponPropertiesByProp(Integer.parseInt(this.resultSet.getString("id")));
        line.put("id", this.resultSet.getInt("id"));
        line.put("property", this.resultSet.getString("property"));
        line.put("weapons", properties);
        listProperties.add(line);
      }
      if (listProperties.size() > 1) {
        TreeMap<String, Object> noOneProperties = this.weaponsPropertiesModel.weaponWithoutProp();
        listProperties.add(noOneProperties);
      }
      return listProperties;
    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
  }

  public int insertProperty(String property) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.connection.setAutoCommit(false);
      this.prepStatement = this.connection.prepareStatement(
        "INSERT INTO chroniclesOfArtifacts.properties "
        + "(property) "
        + "VALUES (?)",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, property.toLowerCase());
      this.prepStatement.executeUpdate();
      this.resultSet = this.prepStatement.getGeneratedKeys();
      int generatedId = -1;
      if (this.resultSet.next()) {
          generatedId = this.resultSet.getInt(1);
      }
      this.connection.commit();
      return generatedId;
    } catch (SQLException e) {
        ConnectionDB.rollbackFunction(this.connection);
        throw new DBException(e.getMessage());
    }
  };

  public boolean updateProperty(int id, String property) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false); 
      this.prepStatement = this.connection.prepareStatement(
        "UPDATE chroniclesOfArtifacts.properties "
        + "SET property = ?"
        + " WHERE id = ?",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, property.toLowerCase());
      this.prepStatement.setInt(2, id);
      this.prepStatement.executeUpdate();
      this.connection.commit();
      return true;
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    } 
  };

  public boolean removeProperty(String property, int id) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false);
      this.prepStatement = this.connection.prepareStatement("SET SQL_SAFE_UPDATES = 0");
      this.prepStatement.executeUpdate();
      this.weaponsPropertiesModel.removeWeaponProperties("propertyId", id);
      this.prepStatement = this.connection.prepareStatement(
        "DELETE FROM chroniclesOfArtifacts.properties "
        + "WHERE property = ? ",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, property.toLowerCase());
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
