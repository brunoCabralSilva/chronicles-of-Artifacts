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

public class CatWeaponsModel {
  private Connection connection;
  private ResultSet resultSet;
  private PreparedStatement prepStatement;

  public CatWeaponsModel() {
    this.connection = null;
    this.resultSet = null;
    this.prepStatement = null;
  }

  private ArrayList<String> getweaponsById(int id) throws FileNotFoundException, IOException {
    try {
    ArrayList<String> list = new ArrayList<String>();
    this.connection = ConnectionDB.getConnection();
    this.prepStatement = this.connection.prepareStatement(
      "SELECT weapon from chroniclesOfArtifacts.weapons "
      + "WHERE category = ?",
      Statement.RETURN_GENERATED_KEYS
    );
    this.prepStatement.setInt(1, id);
    ResultSet result = this.prepStatement.executeQuery();
    while(result.next()) {
      list.add(result.getString("weapon"));
    }
    return list;
    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
  };

  public ArrayList<Map<String, Object>> getCatWeapons(Object data) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      if (data == "all") {
        this.prepStatement = this.connection.prepareStatement(
        "SELECT * FROM chroniclesOfArtifacts.categoryWeapons"
        );
      }
      else if (!data.getClass().getSimpleName().equals("String")) {
        this.prepStatement = this.connection.prepareStatement(
        "SELECT * FROM chroniclesOfArtifacts.categoryWeapons WHERE id = ?"
        );
        this.prepStatement.setInt(1, (Integer) data);
      } else {
        this.prepStatement = this.connection.prepareStatement(
        "SELECT * FROM chroniclesOfArtifacts.categoryWeapons WHERE categoryWeapon = ?"
        );
        this.prepStatement.setString(1, ((String) data).toLowerCase());
      }
      this.resultSet = this.prepStatement.executeQuery();
      ArrayList<Map<String, Object>> catWeaponMap = new ArrayList<Map<String, Object>>();
      while (this.resultSet.next()) {
        TreeMap<String, Object> line = new TreeMap<String, Object>();
        line.put("id", this.resultSet.getInt(1));
        line.put("categoryWeapon", this.resultSet.getString(2));
        line.put("weapons", this.getweaponsById(this.resultSet.getInt(1)));
        catWeaponMap.add(line);
      }
      return catWeaponMap;
    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
  };

  public boolean insertCatWeapon(String categoryWeapon) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.connection.setAutoCommit(false);
      this.prepStatement = this.connection.prepareStatement(
        "INSERT INTO chroniclesOfArtifacts.categoryWeapons "
        + "(categoryWeapon) "
        + "VALUES (?)",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, categoryWeapon.toLowerCase());
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

  public boolean updateCatWeapon(
    int id,
    String categoryWeapon
    ) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false); 
      this.prepStatement = this.connection.prepareStatement(
        "UPDATE chroniclesOfArtifacts.categoryWeapons "
        + "SET categoryWeapon = ? "
        + "WHERE id = ?",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, categoryWeapon.toLowerCase());
      this.prepStatement.setInt(2, id);
      this.prepStatement.executeUpdate();
      this.connection.commit();
      return true;
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    } 
  };

  public boolean removeCatWeapon(String categoryWeapon) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false);
      this.prepStatement = this.connection.prepareStatement("SET SQL_SAFE_UPDATES = 0");
      this.prepStatement.executeUpdate();
      
      this.prepStatement = this.connection.prepareStatement(
        "DELETE FROM chroniclesOfArtifacts.categoryWeapons "
        + "WHERE categoryWeapon = ? ",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, categoryWeapon.toLowerCase());
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
