package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connection.ConnectionDB;
import connection.DBException;

public class WeaponClassesModel {
  private Connection connection;
  private ResultSet resultSet;
  private PreparedStatement prepStatement;

  public WeaponClassesModel() {
    this.connection = null;
    this.resultSet = null;
    this.prepStatement = null;
  }

  public ArrayList<String> getClassesByWeapon(String weapon) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.prepStatement = this.connection.prepareStatement(
        "SELECT c.nameClass "
        + "FROM classes c "
        + "INNER JOIN weaponClasses wc "
        + "ON c.id = wc.classId "
        + "INNER JOIN categoryWeapons cat "
        + "ON wc.catWeaponId = cat.id "
        + "INNER JOIN weapons w "
        + "ON cat.id = w.category "
        + "WHERE w.weapon = ?;",
        Statement.RETURN_GENERATED_KEYS
      );

      this.prepStatement.setString(1, weapon);
      this.resultSet = this.prepStatement.executeQuery();

      ArrayList<String> allClasses = new ArrayList<String>();

      while(this.resultSet.next()) {
        allClasses.add(this.resultSet.getString("nameClass"));
      }

      return allClasses;
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    }
  };

  public ArrayList<String> getCatWeaponByClass(String className) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.prepStatement = this.connection.prepareStatement(
        "SELECT cw.categoryWeapon "
        + "FROM chroniclesOfArtifacts.classes c "
        + "JOIN chroniclesOfArtifacts.weaponClasses wc "
        + "ON c.id = wc.classId "
        + "JOIN chroniclesOfArtifacts.categoryWeapons cw "
        + "ON wc.catWeaponId = cw.id "
        + "WHERE c.nameClass = ?;",
        Statement.RETURN_GENERATED_KEYS
      );

      this.prepStatement.setString(1, className);
      this.resultSet = this.prepStatement.executeQuery();

      ArrayList<String> catWeapons = new ArrayList<String>();

      while(this.resultSet.next()) {
        catWeapons.add(this.resultSet.getString("categoryWeapon"));
      }

      return catWeapons;
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    }
  };

  public ArrayList<String> getWeaponsByClass(String className) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.prepStatement = this.connection.prepareStatement(
        "SELECT w.weapon "
        + "FROM chroniclesOfArtifacts.weapons w "
        + "JOIN chroniclesOfArtifacts.categoryWeapons cw "
        + "ON w.category = cw.id "
        + "JOIN chroniclesOfArtifacts.weaponClasses wc "
        + "ON w.category = wc.catWeaponId "
        + "JOIN chroniclesOfArtifacts.classes cl "
        + "ON wc.classId = cl.id "
        + "WHERE cl.nameClass = ?;",
        Statement.RETURN_GENERATED_KEYS
      );

      this.prepStatement.setString(1, className);
      this.resultSet = this.prepStatement.executeQuery();

      ArrayList<String> weapons = new ArrayList<String>();

      while(this.resultSet.next()) {
        weapons.add(this.resultSet.getString("weapon"));
      }

      return weapons;
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    }
  };

  public void insertWeaponClass(ArrayList<String> weapons, int classId) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      for (int i = 0; i < weapons.size(); i += 1) {
        this.prepStatement = this.connection.prepareStatement(
          "SELECT id from chroniclesOfArtifacts.categoryWeapons "
          + "WHERE categoryWeapon = ?;"
        );
        this.prepStatement.setString(1, weapons.get(i));
        this.resultSet = this.prepStatement.executeQuery();
        int weaponId = 0;
        if (this.resultSet.next()) {
          weaponId = this.resultSet.getInt("id");
        };
        this.prepStatement = this.connection.prepareStatement(
          "SELECT * FROM chroniclesOfArtifacts.weaponClasses "
          + "WHERE classId = ? AND catWeaponId = ?",
          Statement.RETURN_GENERATED_KEYS
        );
        this.prepStatement.setInt(1, classId);
        this.prepStatement.setInt(2, weaponId);
        ResultSet result = this.prepStatement.executeQuery();
        boolean resultBoolean = result.next();
        if (!resultBoolean) {
          if (weaponId > 0) {
            System.out.println("aqui");
            this.prepStatement = this.connection.prepareStatement(
              "INSERT INTO chroniclesOfArtifacts.weaponClasses "
              + "(classId, catWeaponId) "
              + "VALUES(?, ?)",
              Statement.RETURN_GENERATED_KEYS
            );
            this.prepStatement.setInt(1, classId);
            this.prepStatement.setInt(2, weaponId);
            this.prepStatement.executeUpdate();
          }
        }
      }
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    }
  }

  public void removeWeaponClasses(int classId) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.prepStatement = this.connection.prepareStatement(
        "DELETE FROM chroniclesOfArtifacts.weaponClasses "
        + "WHERE classId = ?",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setInt(1, classId);
          this.prepStatement.executeUpdate();
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    }
  }
}
