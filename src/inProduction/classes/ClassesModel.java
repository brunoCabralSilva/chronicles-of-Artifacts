package inProduction.classes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import connection.ConnectionDB;
import connection.DBException;
import model.ArmorClassesModel;
import model.WeaponClassesModel;

public class ClassesModel {
  private Connection connection;
  private ResultSet resultSet;
  private PreparedStatement prepStatement;
  private ArmorClassesModel armorClassesModel;
  private WeaponClassesModel weaponClassesModel;

  public ClassesModel() {
    this.connection = null;
    this.resultSet = null;
    this.prepStatement = null;
    this.weaponClassesModel = new WeaponClassesModel();
    this.armorClassesModel = new ArmorClassesModel();
  }

  public ArrayList<Map<String, Object>> getClasses(Object data) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      String query;
      if (data == "all") {
        query = "SELECT * FROM chroniclesOfArtifacts.classes";
      } else if (data.getClass().getSimpleName().equals("String")) {
        query = "SELECT * FROM chroniclesOfArtifacts.classes WHERE nameClass = ?";
      } else {
        query = "SELECT * FROM chroniclesOfArtifacts.classes WHERE id = ?";
      }
      this.prepStatement = this.connection.prepareStatement(query);
      if (data instanceof Integer) {
        this.prepStatement.setInt(1, (Integer) data);
      } else if (data instanceof String && data != "all") {
        this.prepStatement.setString(1, ((String) data).toLowerCase());
      }
      this.resultSet = this.prepStatement.executeQuery();
      ArrayList<Map<String, Object>> classes = new ArrayList<>();
      
      while (this.resultSet.next()) {
        ArrayList<String> weapons = this.weaponClassesModel.getWeaponsByClass((String) this.resultSet.getString("nameClass"));
        ArrayList<String> categoryWeapons = this.weaponClassesModel.getCatWeaponByClass((String) this.resultSet.getString("nameClass"));

        ArrayList<String> armors = this.armorClassesModel.getArmorsByClass((String) this.resultSet.getString("nameClass"));
        ArrayList<Map<String, String>> categoryArmors = this.armorClassesModel.getCatArmorByClass((String) this.resultSet.getString("nameClass"));

        TreeMap<String, Object> classMap = new TreeMap<>();
        classMap.put("id", this.resultSet.getInt("id"));
        classMap.put("nameClass", this.resultSet.getString("nameClass"));
        classMap.put("functionClass", this.resultSet.getString("functionClass"));
        classMap.put("armors", armors);
        classMap.put("weapons", weapons);
        classMap.put("categoryArmors", categoryArmors);
        classMap.put("categoryWeapons", categoryWeapons);
        classes.add(classMap);
      }
      return classes;
    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
  }

  public int insertClass(
    String nameClass,
    String functionClass,
    ArrayList<String> weapons,
    ArrayList<String> armors
  ) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.connection.setAutoCommit(false);
      this.prepStatement = this.connection.prepareStatement(
        "INSERT INTO chroniclesOfArtifacts.classes "
        + "(nameClass, functionClass) "
        + "VALUES (?, ?)",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, nameClass.toLowerCase());
      this.prepStatement.setString(2, functionClass.toLowerCase());
      this.prepStatement.executeUpdate();
      this.resultSet = this.prepStatement.getGeneratedKeys();
      int generatedId = -1;
      if (this.resultSet.next()) {
        generatedId = this.resultSet.getInt(1);
        this.weaponClassesModel.insertWeaponClass(weapons, generatedId);
        this.armorClassesModel.insertArmorClass(armors, generatedId);
      }
      this.connection.commit();
      return generatedId;
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    }
  }

  public boolean updateClass(
    int id,
    String nameClass,
    String functionClass,
    ArrayList<String> weapons,
    ArrayList<String> armors,
    boolean overrideWeapons,
    boolean overrideArmors
    ) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false);
      this.prepStatement = this.connection.prepareStatement(
        "UPDATE chroniclesOfArtifacts.classes "
        + "SET nameClass = ?, functionClass = ? "
        + " WHERE id = ?",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, nameClass.toLowerCase());
      this.prepStatement.setString(2, functionClass.toLowerCase());
      this.prepStatement.setInt(3, id);
      this.prepStatement.executeUpdate();
      if (weapons.size() != 0) {
        if (overrideWeapons) {
          this.weaponClassesModel.removeWeaponClasses(id);
          this.weaponClassesModel.insertWeaponClass(weapons, id);
        } else {
          this.weaponClassesModel.insertWeaponClass(weapons, id);
        }
      } else {
        this.weaponClassesModel.removeWeaponClasses(id);
      }
      if (armors.size() != 0) {
        if (overrideArmors) {
          this.armorClassesModel.removeArmorClasses(id);
          this.armorClassesModel.insertArmorClass(armors, id);
        } else {
          this.armorClassesModel.insertArmorClass(armors, id);
        }
      } else {
        this.armorClassesModel.removeArmorClasses(id);
      }
      this.connection.commit();
      return true;
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    } 
  };

  public boolean removeClass(int id, String nameClass) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false);
      this.prepStatement = this.connection.prepareStatement("SET SQL_SAFE_UPDATES = 0");
      this.weaponClassesModel.removeWeaponClasses(id);
      this.armorClassesModel.removeArmorClasses(id);
      this.prepStatement = this.connection.prepareStatement(
        "DELETE FROM chroniclesOfArtifacts.classes "
        + "WHERE nameClass = ? ",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, nameClass);
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