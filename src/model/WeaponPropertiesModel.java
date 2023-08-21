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

public class WeaponPropertiesModel {
  private Connection connection;
  private ResultSet resultSet;
  private PreparedStatement prepStatement;

  public WeaponPropertiesModel() {
    this.connection = null;
    this.resultSet = null;
    this.prepStatement = null;
  }

  public ArrayList<String> weaponPropertiesByWeapon(String weapon) throws SQLException, FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    this.prepStatement = this.connection.prepareStatement(
      "SELECT p.property FROM chroniclesOfArtifacts.properties p "
      + "INNER JOIN chroniclesOfArtifacts.weaponProperties wp ON p.id = wp.propertyId "
      + "INNER JOIN chroniclesOfArtifacts.weapons w ON wp.weaponId = w.id "
      + "WHERE w.weapon = ?;",
      Statement.RETURN_GENERATED_KEYS
    );
    this.prepStatement.setString(1, weapon);
    ResultSet propertyResultSet = this.prepStatement.executeQuery();
    ArrayList<String> properties = new ArrayList<String>();
    while (propertyResultSet.next()) {
      properties.add(propertyResultSet.getString("property"));
    }
    return properties;
  }

  public ArrayList<String> weaponPropertiesByProp(int id) throws SQLException, FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    this.prepStatement = this.connection.prepareStatement(
      "SELECT w.weapon "
      + "FROM chroniclesOfArtifacts.weapons w "
      + "INNER JOIN chroniclesOfArtifacts.weaponProperties wp ON w.id = wp.weaponId "
      + "WHERE wp.propertyId = ?;",
      Statement.RETURN_GENERATED_KEYS
    );
    this.prepStatement.setInt(1, id);
    ResultSet weaponsResultSet = this.prepStatement.executeQuery();
    ArrayList<String> weapons = new ArrayList<String>();
    while (weaponsResultSet.next()) {
      weapons.add(weaponsResultSet.getString("weapon"));
    }
    return weapons;
  }  

  public void insertWeaponProperties(int weaponId, ArrayList<String> listProperties) throws FileNotFoundException, IOException {
    try {
      PropertiesModel propertiesModel = new PropertiesModel();
      this.connection = ConnectionDB.getConnection();
      this.connection.setAutoCommit(false);
      for (String property : listProperties) {
        ArrayList<Map<String, Object>> propertyId = propertiesModel.getProperties(property);
        if (propertyId == null || propertyId.size() == 0) {
          TreeMap<String, Object> properties = new TreeMap<String, Object>();
          properties.put("id", propertiesModel.insertProperty(property));
          propertyId = new ArrayList<Map<String, Object>>();
          propertyId.add(properties);
        }

        this.prepStatement = this.connection.prepareStatement(
          "SELECT * FROM chroniclesOfArtifacts.weaponProperties "
          + "WHERE propertyId = ? AND weaponId = ?"
        );
        this.prepStatement.setInt(1, Integer.parseInt((String) propertyId.get(0).get("id")));
        this.prepStatement.setInt(2, weaponId);
        this.resultSet = this.prepStatement.executeQuery();

        if (!this.resultSet.next()) {
          this.prepStatement = this.connection.prepareStatement(
            "INSERT INTO chroniclesOfArtifacts.weaponProperties (propertyId, weaponId) VALUES (?, ?)"
          );

          this.prepStatement.setInt(1, Integer.parseInt((String) propertyId.get(0).get("id")));
          this.prepStatement.setInt(2, weaponId);
          this.prepStatement.executeUpdate();
        }
      }
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    }
  }

  public void removeWeaponProperties(String column, int id) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false);
      this.prepStatement = this.connection.prepareStatement("SET SQL_SAFE_UPDATES = 0");

      if (column == "weaponId") {
      this.prepStatement = this.connection.prepareStatement(
        "DELETE FROM chroniclesOfArtifacts.weaponProperties "
        + "WHERE weaponId = ?"
        );
      this.prepStatement.setInt(1, id);
      } else {
        this.prepStatement = this.connection.prepareStatement(
        "DELETE FROM chroniclesOfArtifacts.weaponProperties "
        + "WHERE propertyId = ?"
        );
      this.prepStatement.setInt(1, id);
      }
      this.prepStatement.executeUpdate();
      this.connection.commit();
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    }
  }

  public TreeMap<String, Object> weaponWithoutProp() throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.prepStatement = this.connection.prepareStatement(
        "SELECT w.weapon "
        + "FROM chroniclesOfArtifacts.weapons w "
        + "WHERE w.id NOT IN ("
        + "SELECT wp.weaponId "
        + "FROM chroniclesOfArtifacts.weaponProperties wp);"
      );
      this.resultSet = this.prepStatement.executeQuery();
      TreeMap<String, Object> noProperty = new TreeMap<String, Object>();
      ArrayList<String> listOfWeapons= new ArrayList<String>();
      while (this.resultSet.next()) {
        listOfWeapons.add(this.resultSet.getString("weapon"));
      }
      noProperty.put("id", 0);
      noProperty.put("property", "nenhuma");
      noProperty.put("weapons", listOfWeapons);
      return noProperty;
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    }
  }
}
