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

public class ArmorsModel {
  private Connection connection;
  private Statement statement;
  private ResultSet resultSet;
  private PreparedStatement prepStatement;

  public ArmorsModel() {
    this.connection = null;
    this.statement = null;
    this.resultSet = null;
    this.prepStatement = null;
  }

  public ArrayList<Map<String, Object>> getArmors(Object data) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.statement = this.connection.createStatement();
      if (data == "all") {
        this.resultSet = this.statement.executeQuery(
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
        line.put("id", this.resultSet.getString(1));
        line.put("armor", this.resultSet.getString(2));
        line.put("ca", this.resultSet.getString(3));
        line.put("penalty", this.resultSet.getString(4));
        line.put("displacement", this.resultSet.getString(5));
        line.put("category", this.resultSet.getString(6));
        return armorMap;
      }
      return null;
    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
  };

  public boolean insertArmor(
    String armor,
    int ca,
    int penalty,
    int displacement,
    int category
  ) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.connection.setAutoCommit(false);
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
      this.prepStatement.setInt(5, category);
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
    int category
  ) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false); 
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
      this.prepStatement.setInt(5, category);
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
