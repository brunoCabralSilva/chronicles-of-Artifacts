package armors;

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
import model.CatArmorsModel;

public class ArmorsModel {
  private Connection connection;
  private ResultSet resultSet;
  private PreparedStatement prepStatement;

  public ArmorsModel() {
    this.connection = null;
    this.resultSet = null;
    this.prepStatement = null;
  }

  public ArrayList<Map<String, Object>> getArmors(Object data) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      if (data == "all") {
        this.prepStatement = this.connection.prepareStatement(
          "SELECT * FROM chroniclesOfArtifacts.armors"
        );
      } else if (!data.getClass().getSimpleName().equals("String")) {
        this.prepStatement = this.connection.prepareStatement(
        "SELECT * FROM chroniclesOfArtifacts.armors WHERE id = ?"
        );
        this.prepStatement.setInt(1, (Integer) data);
      } else {
        this.prepStatement = this.connection.prepareStatement(
        "SELECT * FROM chroniclesOfArtifacts.armors WHERE armor = ?"
        );
        this.prepStatement.setString(1, ((String) data).toLowerCase());
      }
      this.resultSet = this.prepStatement.executeQuery();
      ArrayList<Map<String, Object>> armorMap = new ArrayList<Map<String, Object>>();
      while (this.resultSet.next()) {
        TreeMap<String, Object> line = new TreeMap<String, Object>();
        this.prepStatement = this.connection.prepareStatement(
          "SELECT categoryArmor, typeArmor FROM chroniclesOfArtifacts.categoryArmors WHERE id = ?"
        );
        this.prepStatement.setInt(1, this.resultSet.getInt(6));
        ResultSet result = this.prepStatement.executeQuery();
        result.next();
        line.put("id", this.resultSet.getInt(1));
        line.put("armor", this.resultSet.getString(2));
        line.put("ca", this.resultSet.getInt(3));
        line.put("penalty", this.resultSet.getInt(4));
        line.put("displacement", this.resultSet.getInt(5));
        line.put("type", result.getString(2));
        line.put("category", result.getString(1));
        armorMap.add(line);
      }
      return armorMap;
    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
  };

  public boolean insertArmor(
    String armor,
    int ca,
    int penalty,
    int displacement,
    String type
  ) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.connection.setAutoCommit(false);
      CatArmorsModel catArmorsModel = new CatArmorsModel();
      ArrayList<Map<String, Object>> idCategoryArmor = catArmorsModel.getCatArmors(type);

      if (catArmorsModel.getCatArmors(type).size() == 0) {
        throw new DBException("Categoria de Armadura não foi encontrada");
      }
      this.prepStatement = this.connection.prepareStatement(
        "INSERT INTO chroniclesOfArtifacts.armors "
        + "(armor, ca, penalty, displacement, category) "
        + "VALUES (?, ?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, armor.toLowerCase());
      this.prepStatement.setInt(2, ca);
      this.prepStatement.setInt(3, penalty);
      this.prepStatement.setInt(4, displacement);
      this.prepStatement.setInt(5, (int) idCategoryArmor.get(0).get("id"));
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

  public boolean updateArmor(
    int id,
    String armor,
    int ca,
    int penalty,
    int displacement,
    String type
  ) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.connection.setAutoCommit(false); 
      CatArmorsModel catArmorsModel = new CatArmorsModel();
      ArrayList<Map<String, Object>> idCategoryArmor = catArmorsModel.getCatArmors(type);

      if (catArmorsModel.getCatArmors(type).size() == 0) {
        throw new DBException("Categoria de Armadura não foi encontrada");
      }
      this.prepStatement = this.connection.prepareStatement(
        "UPDATE chroniclesOfArtifacts.armors "
        + "SET armor = ?, ca = ?, penalty = ?, displacement = ?, category = ?"
        + " WHERE id = ?",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, armor.toLowerCase());
      this.prepStatement.setInt(2, ca);
      this.prepStatement.setInt(3, penalty);
      this.prepStatement.setInt(4, displacement);
      this.prepStatement.setInt(5, (int) idCategoryArmor.get(0).get("id"));
      this.prepStatement.setInt(6, id);
      this.prepStatement.executeUpdate();
      this.connection.commit();
      return true;
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    } 
  };

  public boolean removeArmor(String armor) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false);
      this.prepStatement = this.connection.prepareStatement("SET SQL_SAFE_UPDATES = 0");
      this.prepStatement.executeUpdate();
      
      this.prepStatement = this.connection.prepareStatement(
        "DELETE FROM chroniclesOfArtifacts.armors "
        + "WHERE armor = ? ",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, armor.toLowerCase());
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
