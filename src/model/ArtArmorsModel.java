package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import connection.ConnectionDB;
import connection.DBException;

public class ArtArmorsModel {
  private Connection connection;
  private ResultSet resultSet;
  private PreparedStatement prepStatement;
  private ArmorsModel armorsModel;
  private ArmorClassesModel armorClassesModel;

  public ArtArmorsModel() {
    this.connection = null;
    this.resultSet = null;
    this.prepStatement = null;
    this.armorsModel = new ArmorsModel();
    this.armorClassesModel = new ArmorClassesModel();
  }

  public ArrayList<Map<String, Object>> getArtifact(Object data) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      String query;
      if (data == "all") {
        query = "SELECT * FROM chroniclesOfArtifacts.artifactArmors";
      } else if (data.getClass().getSimpleName().equals("String")) {
        query = "SELECT * FROM chroniclesOfArtifacts.artifactArmors WHERE artifact = ?";
      } else {
        query = "SELECT * FROM chroniclesOfArtifacts.artifactArmors WHERE id = ?";
      }
      this.prepStatement = this.connection.prepareStatement(query);
      if (data instanceof Integer) {
        this.prepStatement.setInt(1, (Integer) data);
      } else if (data instanceof String && data != "all") {
        this.prepStatement.setString(1, ((String) data).toLowerCase());
      }
      this.resultSet = this.prepStatement.executeQuery();
      
      ArrayList<Map<String, Object>> listArtifacts = new ArrayList<Map<String, Object>>();

      while (this.resultSet.next()) {
        ArrayList<Map<String, Object>> armor = this.armorsModel.getArmors(this.resultSet.getInt("typearmor"));
        ArrayList<String> classes = this.armorClassesModel.getClassesByArmor((String) armor.get(0).get("armor"));
        TreeMap<String, Object> line = new TreeMap<String, Object>();
        line.put("id", this.resultSet.getInt("id"));
        line.put("artifact", this.resultSet.getString("artifact"));
        line.put("descArmor", this.resultSet.getString("descArmor"));
        line.put("skill", this.resultSet.getString("skill"));
        line.put("bonusDefense", this.resultSet.getString("bonusDefense"));
        line.put("price", this.resultSet.getDouble("price"));
        line.put("weightArmor", this.resultSet.getDouble("weightArmor"));
        line.put("typeArmor", armor.get(0).get("armor"));
        line.put("classes", classes);
        line.put("registerDate", this.resultSet.getDate("registerDate"));
        listArtifacts.add(line);
      }
      return listArtifacts;
    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
  }

  public int insertArtifact(
    String artifact,
    String descArmor,
    String skill,
    String bonusDefense,
    double price,
    double weightArmor,
    String typeArmor,
    Date registerDate
  ) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.connection.setAutoCommit(false);
      ArrayList<Map<String, Object>> armor = this.armorsModel.getArmors(typeArmor);

      if (armor.size() == 0) {
        throw new DBException("A Armadura de referência não foi encontrada na base de dados.");
      }

      this.prepStatement = this.connection.prepareStatement(
        "INSERT INTO chroniclesOfArtifacts.artifactArmors "
        + "(artifact, descArmor, skill, bonusDefense, price, weightArmor, typeArmor, registerDate) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, artifact.toLowerCase());
      this.prepStatement.setString(2, descArmor);
      this.prepStatement.setString(3, skill);
      this.prepStatement.setString(4, bonusDefense.toLowerCase());
      this.prepStatement.setDouble(5, price);
      this.prepStatement.setDouble(6, weightArmor);
      this.prepStatement.setInt(7, (int) armor.get(0).get("id"));
      this.prepStatement.setDate(8, registerDate);
      this.prepStatement.executeUpdate();
      this.resultSet = this.prepStatement.getGeneratedKeys();
      int generatedId = -1;
      if (this.resultSet.next()) {
          generatedId = this.resultSet.getInt(1);
      }
      this.connection.commit();
      return generatedId;
    } catch (SQLException e) {
        ConnectionDB.rollbackFunction(this.connection);
        throw new DBException(e.getMessage());
    }
  };

  public boolean updateArtifact(
    int id,
    String artifact,
    String descArmor,
    String skill,
    String bonusDefense,
    double price,
    double weightArmor,
    String typeArmor,
    Date registerDate
  ) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false);
      ArrayList<Map<String, Object>> armor = this.armorsModel.getArmors(typeArmor);

      if (armor.size() == 0) {
        throw new DBException("A Arma de referência não foi encontrada na base de dados.");
      }

      this.prepStatement = this.connection.prepareStatement(
        "UPDATE chroniclesOfArtifacts.artifactArmors "
        + "SET artifact = ?, descArmor = ?, skill = ?, bonusDefense = ?, price = ?, weightArmor = ?, typeArmor = ?, registerDate = ?"
        + " WHERE id = ?",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, artifact.toLowerCase());
      this.prepStatement.setString(2, descArmor);
      this.prepStatement.setString(3, skill);
      this.prepStatement.setString(4, bonusDefense.toLowerCase());
      this.prepStatement.setDouble(5, price);
      this.prepStatement.setDouble(6, weightArmor);
      this.prepStatement.setInt(7, (int) armor.get(0).get("id"));
      this.prepStatement.setDate(8, registerDate);
      this.prepStatement.setInt(9, id);
      this.prepStatement.executeUpdate();
      this.connection.commit();
      return true;
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    } 
  };

  public boolean removeArtifact(String artifact, int id) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false);
      this.prepStatement = this.connection.prepareStatement("SET SQL_SAFE_UPDATES = 0");
      this.prepStatement.executeUpdate();
      this.prepStatement = this.connection.prepareStatement(
        "DELETE FROM chroniclesOfArtifacts.artifactArmors "
        + "WHERE artifact = ? ",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, artifact.toLowerCase());
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
