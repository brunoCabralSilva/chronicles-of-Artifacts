package weapons;

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
  private Statement statement;
  private ResultSet resultSet;
  private PreparedStatement prepStatement;

  public WeaponsModel() {
    this.connection = null;
    this.statement = null;
    this.resultSet = null;
    this.prepStatement = null;
  }

  private void rollbackFunction() {
    try {
      this.connection.rollback();
    } catch(SQLException e2) {
      throw new DBException("Não foi possível realizar o Rollback");
    }
  }

  public TreeMap<String, String> getWeapon(Object data) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.statement = this.connection.createStatement();
      if (!data.getClass().getSimpleName().equals("String")) {
        this.prepStatement = this.connection.prepareStatement(
        "SELECT * FROM chroniclesOfArtifacts.weapons WHERE id = ?"
        );
        this.prepStatement.setInt(1, (Integer) data);
      } else {
        this.prepStatement = this.connection.prepareStatement(
        "SELECT * FROM chroniclesOfArtifacts.weapons WHERE weapon = ?"
        );
        this.prepStatement.setString(1, ((String) data).toLowerCase());
      }
      this.resultSet = this.prepStatement.executeQuery();
      while (this.resultSet.next()) {
        TreeMap<String, String> weaponMap = new TreeMap<String, String>();
        weaponMap.put("id", this.resultSet.getString(1));
        weaponMap.put("weapon", this.resultSet.getString(2));
        weaponMap.put("categoryWeapon", this.resultSet.getString(3));
        weaponMap.put("proficiency", this.resultSet.getString(4));
        weaponMap.put("damage", this.resultSet.getString(5));
        weaponMap.put("rangeWeapon", this.resultSet.getString(6));
        weaponMap.put("numberOfHands", this.resultSet.getString(7));
        return weaponMap;
      }
      return null;
    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
  };

  public ArrayList<Map<String, String>> getAllWeapons() throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.statement = this.connection.createStatement();
      this.resultSet = this.statement.executeQuery(
        "SELECT * FROM chroniclesOfArtifacts.weapons"
      );
      ArrayList<Map<String, String>> listWeapons = new ArrayList<Map<String, String>>();
      while(this.resultSet.next()) {
        TreeMap<String, String> line = new TreeMap<String, String>();
        line.put("id", this.resultSet.getString("id"));
        line.put("weapon", this.resultSet.getString("weapon"));
        line.put("categoryWeapon", this.resultSet.getString("categoryWeapon"));
        line.put("proficiency", this.resultSet.getString("proficiency"));
        line.put("damage", this.resultSet.getString("damage"));
        line.put("rangeWeapon", this.resultSet.getString("rangeWeapon"));
        line.put("numberOfHands", this.resultSet.getString("numberOfHands"));
        listWeapons.add(line);
      }
      return listWeapons;
    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
  }

  public boolean insertWeapon(
    String weapon,
    String categoryWeapon,
    int proficiency,
    String damage,
    String rangeWeapon,
    int numberOfHands
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
      this.connection.commit();
      return true;
    }
    catch (SQLException e) {
      this.rollbackFunction();
      throw new DBException(e.getMessage());
    }
  };

  public boolean updateWeapon(
    int id,
    String weapon,
    String categoryWeapon,
    int proficiency,
    String damage,
    String rangeWeapon,
    int numberOfHands
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
      this.connection.commit();
      return true;
    } catch (SQLException e) {
      this.rollbackFunction();
      throw new DBException(e.getMessage());
    } 
  };

  public boolean removeWeapon(String weapon) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false);
      this.prepStatement = this.connection.prepareStatement("SET SQL_SAFE_UPDATES = 0");
      this.prepStatement.executeUpdate();
      
      this.prepStatement = this.connection.prepareStatement(
        "DELETE FROM chroniclesOfArtifacts.weapons "
        + "WHERE Weapon = ? ",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, weapon.toLowerCase());
      this.prepStatement.executeUpdate();
      this.resultSet = this.prepStatement.getGeneratedKeys();
      this.resultSet.next();
      this.connection.commit();
      return true;
    } catch (SQLException e) {
      this.rollbackFunction();
      throw new DBException(e.getMessage());
    }
  }
}
