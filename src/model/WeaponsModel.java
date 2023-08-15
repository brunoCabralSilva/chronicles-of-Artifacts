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

public class WeaponsModel {
  private Connection connection;
  private ResultSet resultSet;
  private PreparedStatement prepStatement;
  WeaponPropertiesModel weaponsPropertiesModel;

  public WeaponsModel() {
    this.connection = null;
    this.resultSet = null;
    this.prepStatement = null;
    this.weaponsPropertiesModel = new WeaponPropertiesModel();
  }

  public ArrayList<Map<String, Object>> getWeapons(Object data) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      String query;
      if (data == "all") {
        query = "SELECT * FROM chroniclesOfArtifacts.weapons";
      } else if (data.getClass().getSimpleName().equals("String")) {
        query = "SELECT * FROM chroniclesOfArtifacts.weapons WHERE weapon = ?";
      } else {
        query = "SELECT * FROM chroniclesOfArtifacts.weapons WHERE id = ?";
      }
      this.prepStatement = this.connection.prepareStatement(query);
      if (data instanceof Integer) {
        this.prepStatement.setInt(1, (Integer) data);
      } else if (data instanceof String && data != "all") {
        this.prepStatement.setString(1, ((String) data).toLowerCase());
      }
      this.resultSet = this.prepStatement.executeQuery();
      ArrayList<Map<String, Object>> weapons = new ArrayList<>();
      
      while (this.resultSet.next()) {
        ArrayList<String> property = this.weaponsPropertiesModel.weaponPropertiesByWeapon((String) this.resultSet.getString("weapon"));
        
        TreeMap<String, Object> weaponMap = new TreeMap<>();
        weaponMap.put("id", this.resultSet.getInt("id"));
        weaponMap.put("weapon", this.resultSet.getString("weapon"));
        weaponMap.put("categoryWeapon", this.resultSet.getString("categoryWeapon"));
        weaponMap.put("proficiency", this.resultSet.getInt("proficiency"));
        weaponMap.put("damage", this.resultSet.getString("damage"));
        weaponMap.put("rangeWeapon", this.resultSet.getString("rangeWeapon"));
        weaponMap.put("numberOfHands", this.resultSet.getInt("numberOfHands"));
        weaponMap.put("properties", property);
        weapons.add(weaponMap);
      }
      return weapons;
    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
  }

  public int insertWeapon(
    String weapon,
    String categoryWeapon,
    int proficiency,
    String damage,
    String rangeWeapon,
    int numberOfHands,
    ArrayList<String> properties
  ) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.connection.setAutoCommit(false);
      this.prepStatement = this.connection.prepareStatement(
        "INSERT INTO chroniclesOfArtifacts.weapons "
        + "(weapon, categoryWeapon, proficiency, damage, rangeWeapon, numberOfHands) "
        + "VALUES (?, ?, ?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, weapon.toLowerCase());
      this.prepStatement.setString(2, categoryWeapon.toLowerCase());
      this.prepStatement.setInt(3, proficiency);
      this.prepStatement.setString(4, damage.toLowerCase());
      this.prepStatement.setString(5, rangeWeapon.toLowerCase());
      this.prepStatement.setInt(6, numberOfHands);
      this.prepStatement.executeUpdate();
      this.resultSet = this.prepStatement.getGeneratedKeys();
      int generatedId = -1;
      if (this.resultSet.next()) {
        generatedId = this.resultSet.getInt(1);
      }
      if (properties.size() > 0 && generatedId > 0) {
        this.weaponsPropertiesModel.insertWeaponProperties(generatedId, properties);
      }
      this.connection.commit();
      return generatedId;
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    }
  }

  public boolean updateWeapon(
    int id,
    String weapon,
    String categoryWeapon,
    int proficiency,
    String damage,
    String rangeWeapon,
    int numberOfHands,
    ArrayList<String> properties,
    boolean override
    ) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false);
      this.prepStatement = this.connection.prepareStatement(
        "UPDATE chroniclesOfArtifacts.weapons "
        + "SET weapon = ?, categoryWeapon = ?, proficiency = ?, damage = ?, rangeWeapon = ?, numberOfHands = ?"
        + " WHERE id = ?",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, weapon.toLowerCase());
      this.prepStatement.setString(2, categoryWeapon.toLowerCase());
      this.prepStatement.setInt(3, proficiency);
      this.prepStatement.setString(4, damage.toLowerCase());
      this.prepStatement.setString(5, rangeWeapon.toLowerCase());
      this.prepStatement.setInt(6, numberOfHands);
      this.prepStatement.setInt(7, id);
      this.prepStatement.executeUpdate();
      if (properties.size() != 0) {
        if (override) {
          this.weaponsPropertiesModel.removeWeaponProperty(id);
          this.weaponsPropertiesModel.insertWeaponProperties(id, properties);
        } else {
          this.weaponsPropertiesModel.insertWeaponProperties(id, properties);
        }
      }
      this.connection.commit();
      return true;
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    } 
  };

  public boolean removeWeapon(String weapon, ArrayList<Map<String,Object>> item) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false);
      this.prepStatement = this.connection.prepareStatement("SET SQL_SAFE_UPDATES = 0");
      this.weaponsPropertiesModel.removeWeaponProperty((int) item.get(0).get("id"));
      this.prepStatement = this.connection.prepareStatement(
        "DELETE FROM chroniclesOfArtifacts.weapons "
        + "WHERE weapon = ? ",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, weapon);
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