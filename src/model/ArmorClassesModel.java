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

public class ArmorClassesModel {
  private Connection connection;
  private ResultSet resultSet;
  private PreparedStatement prepStatement;

  public ArmorClassesModel() {
    this.connection = null;
    this.resultSet = null;
    this.prepStatement = null;
  }

  public ArrayList<Map<String, String>> getCatArmorByClass(String className) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.prepStatement = this.connection.prepareStatement(
        "SELECT ca.categoryArmor, ca.typeArmor "
        + "FROM chroniclesOfArtifacts.classes c "
        + "JOIN chroniclesOfArtifacts.armorClasses ac "
        + "ON c.id = ac.classId "
        + "JOIN chroniclesOfArtifacts.categoryArmors ca "
        + "ON ac.catArmorId = ca.id "
        + "WHERE c.nameClass = ?;",
        Statement.RETURN_GENERATED_KEYS
        );
 
      this.prepStatement.setString(1, className);
      this.resultSet = this.prepStatement.executeQuery();

      ArrayList<Map<String, String>> catArmors = new ArrayList<Map<String, String>>();

      while(this.resultSet.next()) {
        TreeMap<String, String> line = new TreeMap<String, String>();
        line.put("categoryArmor", this.resultSet.getString("categoryArmor"));
        line.put("typeArmor", this.resultSet.getString("typeArmor"));
        catArmors.add(line);
      }

      return catArmors;
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    }
  };

  public ArrayList<String> getArmorsByClass(String className) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.prepStatement = this.connection.prepareStatement(
        "SELECT a.armor "
        + "FROM chroniclesOfArtifacts.armors a "
        + "JOIN chroniclesOfArtifacts.categoryArmors ca "
        + "ON a.category = ca.id "
        + "JOIN chroniclesOfArtifacts.armorClasses ac "
        + "ON a.category = ac.catArmorId "
        + "JOIN chroniclesOfArtifacts.classes c "
        + "ON ac.classId = c.id "
        + "WHERE c.nameClass = ?;",
        Statement.RETURN_GENERATED_KEYS
      );

      this.prepStatement.setString(1, className);
      this.resultSet = this.prepStatement.executeQuery();

      ArrayList<String> armors = new ArrayList<String>();

      while(this.resultSet.next()) {
        armors.add(this.resultSet.getString("armor"));
      }
      
      return armors;
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    }
  }

  public ArrayList<String> getClassesByArmor(String armor) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.prepStatement = this.connection.prepareStatement(
        "SELECT c.nameClass "
        + "FROM classes c "
        + "INNER JOIN armorClasses ac "
        + "ON c.id = ac.classId "
        + "INNER JOIN categoryArmors cat "
        + "ON ac.catArmorId = cat.id "
        + "INNER JOIN armors a "
        + "ON cat.id = a.category "
        + "WHERE a.armor = ?;",
        Statement.RETURN_GENERATED_KEYS
      );

      this.prepStatement.setString(1, armor);
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

  public void insertArmorClass(ArrayList<String> armors, int classId) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      for (int i = 0; i < armors.size(); i += 1) {
        this.prepStatement = this.connection.prepareStatement(
          "SELECT id from chroniclesOfArtifacts.categoryArmors "
          + "WHERE typeArmor = ?;"
        );
        this.prepStatement.setString(1, armors.get(i));
        this.resultSet = this.prepStatement.executeQuery();
        int armorId = 0;
        if (this.resultSet.next()) {
          armorId = this.resultSet.getInt("id");
        };
        this.prepStatement = this.connection.prepareStatement(
          "SELECT * FROM chroniclesOfArtifacts.armorClasses "
          + "WHERE classId = ? AND catArmorId = ?",
          Statement.RETURN_GENERATED_KEYS
        );
        this.prepStatement.setInt(1, classId);
        this.prepStatement.setInt(2, armorId);
        ResultSet result = this.prepStatement.executeQuery();
        boolean resultBoolean = result.next();
        if (!resultBoolean) {
          if (armorId > 0) {
            System.out.println("aqui");
            this.prepStatement = this.connection.prepareStatement(
              "INSERT INTO chroniclesOfArtifacts.armorClasses "
              + "(classId, catArmorId) "
              + "VALUES(?, ?)",
              Statement.RETURN_GENERATED_KEYS
            );
            this.prepStatement.setInt(1, classId);
            this.prepStatement.setInt(2, armorId);
            this.prepStatement.executeUpdate();
          }
        }
      }
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    }
  }

  public void removeArmorClasses(int classId) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.prepStatement = this.connection.prepareStatement(
        "DELETE FROM chroniclesOfArtifacts.armorClasses "
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

