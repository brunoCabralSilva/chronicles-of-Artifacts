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
        query = "SELECT * FROM chroniclesOfArtifacts.classes WHERE class = ?";
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

  public boolean insertClass(
    String nameClass,
    String functionClass,
    ArrayList<String> categoryWeapons,
    ArrayList<String> categoryArmors
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
        this.weaponClassesModel.insertWeaponClass(categoryWeapons, generatedId);
        this.armorClassesModel.insertArmorClass(categoryArmors, generatedId);
      }
      this.connection.commit();
      return true;
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    }
  }
}

//   public boolean updateWeapon(
//     int id,
//     String weapon,
//     String categoryWeapon,
//     int proficiency,
//     String damage,
//     String rangeWeapon,
//     int numberOfHands,
//     ArrayList<String> properties,
//     boolean override
//     ) throws FileNotFoundException, IOException {
//     this.connection = ConnectionDB.getConnection();
//     try {
//       this.connection.setAutoCommit(false);
//       this.prepStatement = this.connection.prepareStatement(
//         "UPDATE chroniclesOfArtifacts.weapons "
//         + "SET weapon = ?, categoryWeapon = ?, proficiency = ?, damage = ?, rangeWeapon = ?, numberOfHands = ?"
//         + " WHERE id = ?",
//         Statement.RETURN_GENERATED_KEYS
//       );
//       this.prepStatement.setString(1, weapon.toLowerCase());
//       this.prepStatement.setString(2, categoryWeapon.toLowerCase());
//       this.prepStatement.setInt(3, proficiency);
//       this.prepStatement.setString(4, damage.toLowerCase());
//       this.prepStatement.setString(5, rangeWeapon.toLowerCase());
//       this.prepStatement.setInt(6, numberOfHands);
//       this.prepStatement.setInt(7, id);
//       this.prepStatement.executeUpdate();
//       if (properties.size() != 0) {
//         if (override) {
//           this.weaponsPropertiesModel.removeWeaponProperties("weaponId", id);
//           this.weaponsPropertiesModel.insertWeaponProperties(id, properties);
//         } else {
//           this.weaponsPropertiesModel.insertWeaponProperties(id, properties);
//         }
//       }
//       this.connection.commit();
//       return true;
//     } catch (SQLException e) {
//       ConnectionDB.rollbackFunction(this.connection);
//       throw new DBException(e.getMessage());
//     } 
//   };

//   public boolean removeWeapon(String weapon, ArrayList<Map<String,Object>> item) throws FileNotFoundException, IOException {
//     this.connection = ConnectionDB.getConnection();
//     try {
//       this.connection.setAutoCommit(false);
//       this.prepStatement = this.connection.prepareStatement("SET SQL_SAFE_UPDATES = 0");
//       this.weaponsPropertiesModel.removeWeaponProperties("weaponId", (int) item.get(0).get("id"));
//       this.prepStatement = this.connection.prepareStatement(
//         "DELETE FROM chroniclesOfArtifacts.weapons "
//         + "WHERE weapon = ? ",
//         Statement.RETURN_GENERATED_KEYS
//       );
//       this.prepStatement.setString(1, weapon);
//       this.prepStatement.executeUpdate();
//       this.resultSet = this.prepStatement.getGeneratedKeys();
//       this.resultSet.next();
//       this.connection.commit();
//       return true;
//     } catch (SQLException e) {
//       ConnectionDB.rollbackFunction(this.connection);
//       throw new DBException(e.getMessage());
//     }
//   }
// }

// ====================================


//   public boolean insertClass(String nameClass, String functionClass) throws FileNotFoundException, IOException {
//     try {
//       this.connection = ConnectionDB.getConnection();
//       this.connection.setAutoCommit(false);
//       this.prepStatement = this.connection.prepareStatement(
//         "INSERT INTO chroniclesOfArtifacts.classes "
//         + "(nameClass, functionClass) "
//         + "VALUES (?, ?)",
//         Statement.RETURN_GENERATED_KEYS
//       );
//       this.prepStatement.setString(1, nameClass.toLowerCase());
//       this.prepStatement.setString(2, functionClass.toLowerCase());
//       this.prepStatement.executeUpdate();
//       this.resultSet = this.prepStatement.getGeneratedKeys();
//       this.connection.commit();
//       return true;
//     }
//     catch (SQLException e) {
//       ConnectionDB.rollbackFunction(this.connection);
//       throw new DBException(e.getMessage());
//     }
//   };

//   public boolean updateClass(
//     int id,
//     String nameClass,
//     String functionClass
//     ) throws FileNotFoundException, IOException {
//     this.connection = ConnectionDB.getConnection();
//     try {
//       this.connection.setAutoCommit(false); 
//       this.prepStatement = this.connection.prepareStatement(
//         "UPDATE chroniclesOfArtifacts.classes "
//         + "SET nameClass = ?, functionClass = ?"
//         + " WHERE id = ?",
//         Statement.RETURN_GENERATED_KEYS
//       );
//       this.prepStatement.setString(1, nameClass.toLowerCase());
//       this.prepStatement.setString(2, functionClass.toLowerCase());
//       this.prepStatement.setInt(3, id);
//       this.prepStatement.executeUpdate();
//       this.connection.commit();
//       return true;
//     } catch (SQLException e) {
//       ConnectionDB.rollbackFunction(this.connection);
//       throw new DBException(e.getMessage());
//     } 
//   };

//   public boolean removeClass(String nameClass) throws FileNotFoundException, IOException {
//     this.connection = ConnectionDB.getConnection();
//     try {
//       this.connection.setAutoCommit(false);
//       this.prepStatement = this.connection.prepareStatement("SET SQL_SAFE_UPDATES = 0");
//       this.prepStatement.executeUpdate();
      
//       this.prepStatement = this.connection.prepareStatement(
//         "DELETE FROM chroniclesOfArtifacts.classes "
//         + "WHERE nameClass = ? ",
//         Statement.RETURN_GENERATED_KEYS
//       );
//       this.prepStatement.setString(1, nameClass.toLowerCase());
//       this.prepStatement.executeUpdate();
//       this.resultSet = this.prepStatement.getGeneratedKeys();
//       this.resultSet.next();
//       this.connection.commit();
//       return true;
//     } catch (SQLException e) {
//       ConnectionDB.rollbackFunction(this.connection);
//       throw new DBException(e.getMessage());
//     }
//   }
// }
