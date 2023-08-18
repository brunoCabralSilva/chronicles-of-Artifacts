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

  public void insertArmorClass(ArrayList<String> categoryWeapons, int classId) {
    
  }
}
