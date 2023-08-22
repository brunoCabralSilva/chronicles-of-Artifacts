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

public class ArtItemsModel {
  private Connection connection;
  private ResultSet resultSet;
  private PreparedStatement prepStatement;

  public ArtItemsModel() {
    this.connection = null;
    this.resultSet = null;
    this.prepStatement = null;
  }

  public ArrayList<Map<String, Object>> getArtifact(Object data) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      String query;
      if (data == "all") {
        query = "SELECT * FROM chroniclesOfArtifacts.artifactItems";
      } else if (data.getClass().getSimpleName().equals("String")) {
        query = "SELECT * FROM chroniclesOfArtifacts.artifactItems WHERE artifact = ?";
      } else {
        query = "SELECT * FROM chroniclesOfArtifacts.artifactItems WHERE id = ?";
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
        TreeMap<String, Object> line = new TreeMap<String, Object>();
        line.put("id", this.resultSet.getInt("id"));
        line.put("artifact", this.resultSet.getString("artifact"));
        line.put("descItem", this.resultSet.getString("descItem"));
        line.put("skill", this.resultSet.getString("skill"));
        line.put("price", this.resultSet.getDouble("price"));
        line.put("weightItem", this.resultSet.getDouble("weightItem"));
        line.put("bodySection", this.resultSet.getString("bodySection"));
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
    String descItem,
    String skill,
    double price,
    double weightItem,
    String bodySection,
    Date registerDate
  ) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.connection.setAutoCommit(false);
      this.prepStatement = this.connection.prepareStatement(
        "INSERT INTO chroniclesOfArtifacts.artifactItems "
        + "(artifact, descItem, skill, price, weightItem, bodySection, registerDate) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, artifact.toLowerCase());
      this.prepStatement.setString(2, descItem);
      this.prepStatement.setString(3, skill);
      this.prepStatement.setDouble(4, price);
      this.prepStatement.setDouble(5, weightItem);
      this.prepStatement.setString(6, bodySection.toLowerCase());
      this.prepStatement.setDate(7, registerDate);
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
    String descItem,
    String skill,
    double price,
    double weightItem,
    String bodySection,
    Date registerDate
  ) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false); 
      this.prepStatement = this.connection.prepareStatement(
        "UPDATE chroniclesOfArtifacts.artifactItems "
        + "SET artifact = ?, descItem = ?, skill = ?, price = ?, weightItem = ?, bodySection = ?, registerDate = ?"
        + " WHERE id = ?",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, artifact.toLowerCase());
      this.prepStatement.setString(2, descItem);
      this.prepStatement.setString(3, skill);
      this.prepStatement.setDouble(4, price);
      this.prepStatement.setDouble(5, weightItem);
      this.prepStatement.setString(6, bodySection.toLowerCase());
      this.prepStatement.setDate(7, registerDate);
      this.prepStatement.setInt(8, id);
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
        "DELETE FROM chroniclesOfArtifacts.artifactItems "
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
