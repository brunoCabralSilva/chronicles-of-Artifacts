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

public class ArtWeaponsModel {
  private Connection connection;
  private ResultSet resultSet;
  private PreparedStatement prepStatement;
  private WeaponsModel weaponsModel;

  public ArtWeaponsModel() {
    this.connection = null;
    this.resultSet = null;
    this.prepStatement = null;
    this.weaponsModel = new WeaponsModel();
  }

  public ArrayList<Map<String, Object>> getArtifact(Object data) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      String query;
      if (data == "all") {
        query = "SELECT * FROM chroniclesOfArtifacts.artifactWeapons";
      } else if (data.getClass().getSimpleName().equals("String")) {
        query = "SELECT * FROM chroniclesOfArtifacts.artifactWeapons WHERE artifact = ?";
      } else {
        query = "SELECT * FROM chroniclesOfArtifacts.artifactWeapons WHERE id = ?";
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
        ArrayList<Map<String, Object>> weapon = this.weaponsModel.getWeapons(this.resultSet.getInt("typeWeapon"));
        TreeMap<String, Object> line = new TreeMap<String, Object>();
        line.put("id", this.resultSet.getInt("id"));
        line.put("artifact", this.resultSet.getString("artifact"));
        line.put("descWeapon", this.resultSet.getString("descWeapon"));
        line.put("skill", this.resultSet.getString("skill"));
        line.put("bonusAtk", this.resultSet.getString("bonusAtk"));
        line.put("bonusDamage", this.resultSet.getString("bonusDamage"));
        line.put("price", this.resultSet.getDouble("price"));
        line.put("weightWeapon", this.resultSet.getDouble("weightWeapon"));
        line.put("typeWeapon", weapon.get(0).get("weapon"));
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
    String descWeapon,
    String skill,
    String bonusAtk,
    String bonusDamage,
    double price,
    double weightWeapon,
    String typeWeapon,
    Date registerDate
  ) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.connection.setAutoCommit(false);
      ArrayList<Map<String, Object>> weapon = this.weaponsModel.getWeapons(typeWeapon);

      if (weapon.size() == 0) {
        throw new DBException("A Arma de referência não foi encontrada na base de dados.");
      }

      this.prepStatement = this.connection.prepareStatement(
        "INSERT INTO chroniclesOfArtifacts.artifactWeapons "
        + "(artifact, descWeapon, skill, bonusAtk, bonusDamage, price, weightWeapon, typeWeapon, registerDate) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, artifact.toLowerCase());
      this.prepStatement.setString(2, descWeapon);
      this.prepStatement.setString(3, skill);
      this.prepStatement.setString(4, bonusAtk.toLowerCase());
      this.prepStatement.setString(5, bonusDamage.toLowerCase());
      this.prepStatement.setDouble(6, price);
      this.prepStatement.setDouble(7, weightWeapon);
      this.prepStatement.setInt(8, (int) weapon.get(0).get("id"));
      this.prepStatement.setDate(9, registerDate);
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
    String descWeapon,
    String skill,
    String bonusAtk,
    String bonusDamage,
    double price,
    double weightWeapon,
    String typeWeapon,
    Date registerDate
  ) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false);
      ArrayList<Map<String, Object>> weapon = this.weaponsModel.getWeapons(typeWeapon);

      if (weapon.size() == 0) {
        throw new DBException("A Arma de referência não foi encontrada na base de dados.");
      }

      this.prepStatement = this.connection.prepareStatement(
        "UPDATE chroniclesOfArtifacts.artifactWeapons "
        + "SET artifact = ?, descWeapon = ?, skill = ?, bonusAtk = ?, bonusDamage = ?, price = ?, weightWeapon = ?, typeWeapon = ?, registerDate = ?"
        + " WHERE id = ?",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, artifact.toLowerCase());
      this.prepStatement.setString(2, descWeapon);
      this.prepStatement.setString(3, skill);
      this.prepStatement.setString(4, bonusAtk.toLowerCase());
      this.prepStatement.setString(5, bonusDamage.toLowerCase());
      this.prepStatement.setDouble(6, price);
      this.prepStatement.setDouble(7, weightWeapon);
      this.prepStatement.setInt(8, (int) weapon.get(0).get("id"));
      this.prepStatement.setDate(9, registerDate);
      this.prepStatement.setInt(10, id);
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
        "DELETE FROM chroniclesOfArtifacts.artifactWeapons "
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
