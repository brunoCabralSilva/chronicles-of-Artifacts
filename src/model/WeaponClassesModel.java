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

  public void insertWeaponClass(ArrayList<String> categoryWeapons, int classId) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      for (int i = 0; i < categoryWeapons.size(); i += 1) {
        System.out.println(categoryWeapons.get(i));
        this.prepStatement = this.connection.prepareStatement(
          "SELECT id "
          + "FROM chroniclesOfArtifacts.categoryWeapons "
          + "WHERE categoryWeapon = ?",
          Statement.RETURN_GENERATED_KEYS
        );

        this.prepStatement.setString(1, categoryWeapons.get(i));
        this.resultSet = this.prepStatement.executeQuery();

        if (this.resultSet.next()) {
          this.prepStatement = this.connection.prepareStatement(
            "INSERT INTO chroniclesOfArtifacts.weaponClasses "
            + "(classId, catWeaponId) "
            + "VALUES(?, ?)",
            Statement.RETURN_GENERATED_KEYS
          );
          this.prepStatement.setInt(1, classId);
          this.prepStatement.setInt(2, this.resultSet.getInt(1));
          this.prepStatement.executeUpdate();
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
