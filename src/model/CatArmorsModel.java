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

public class CatArmorsModel {
  private Connection connection;
  private ResultSet resultSet;
  private PreparedStatement prepStatement;

  public CatArmorsModel() {
    this.connection = null;
    this.resultSet = null;
    this.prepStatement = null;
  }

  private ArrayList<String> getArmorsById(int id) throws FileNotFoundException, IOException {
    try {
    ArrayList<String> list = new ArrayList<String>();
    this.connection = ConnectionDB.getConnection();
    this.prepStatement = this.connection.prepareStatement(
      "SELECT armor from chroniclesOfArtifacts.armors "
      + "WHERE category = ?",
      Statement.RETURN_GENERATED_KEYS
    );
    this.prepStatement.setInt(1, id);
    ResultSet result = this.prepStatement.executeQuery();
    while(result.next()) {
      list.add(result.getString("armor"));
    }
    return list;
    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
  };

  public ArrayList<Map<String, Object>> getCatArmors(Object data) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      if (data == "all") {
        this.prepStatement = this.connection.prepareStatement(
        "SELECT * FROM chroniclesOfArtifacts.categoryArmors"
        );
      }
      else if (!data.getClass().getSimpleName().equals("String")) {
        this.prepStatement = this.connection.prepareStatement(
        "SELECT * FROM chroniclesOfArtifacts.categoryArmors WHERE id = ?"
        );
        this.prepStatement.setInt(1, (Integer) data);
      } else {
        this.prepStatement = this.connection.prepareStatement(
        "SELECT * FROM chroniclesOfArtifacts.categoryArmors WHERE typeArmor = ?"
        );
        this.prepStatement.setString(1, ((String) data).toLowerCase());
      }
      this.resultSet = this.prepStatement.executeQuery();
      ArrayList<Map<String, Object>> catArmorMap = new ArrayList<Map<String, Object>>();
      while (this.resultSet.next()) {
        TreeMap<String, Object> line = new TreeMap<String, Object>();
        line.put("id", this.resultSet.getInt(1));
        line.put("typeArmor", this.resultSet.getString(2));
        line.put("categoryArmor", this.resultSet.getString(3));
        line.put("armors", this.getArmorsById(this.resultSet.getInt(1)));
        catArmorMap.add(line);
      }
      return catArmorMap;
    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
  };
  
  public boolean insertCatArmor(
    String typeArmor,
    String categoryArmor
  ) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.connection.setAutoCommit(false);
      this.prepStatement = this.connection.prepareStatement(
        "INSERT INTO chroniclesOfArtifacts.categoryArmors "
        + "(typeArmor, categoryArmor) "
        + "VALUES (?, ?)",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, typeArmor.toLowerCase());
      this.prepStatement.setString(2, categoryArmor.toLowerCase());
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

  public boolean updateCatArmor(
    int id,
    String typeArmor,
    String categoryArmor
    ) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false); 
      this.prepStatement = this.connection.prepareStatement(
        "UPDATE chroniclesOfArtifacts.categoryArmors "
        + "SET typearmor = ?, categoryArmor = ?"
        + " WHERE id = ?",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, typeArmor.toLowerCase());
      this.prepStatement.setString(2, categoryArmor.toLowerCase());
      this.prepStatement.setInt(3, id);
      this.prepStatement.executeUpdate();
      this.connection.commit();
      return true;
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    } 
  };

  public boolean removeCatArmor(String typeArmor) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false);
      this.prepStatement = this.connection.prepareStatement("SET SQL_SAFE_UPDATES = 0");
      this.prepStatement.executeUpdate();
      
      this.prepStatement = this.connection.prepareStatement(
        "DELETE FROM chroniclesOfArtifacts.categoryArmors "
        + "WHERE typeArmor = ? ",
        Statement.RETURN_GENERATED_KEYS
      );
      this.prepStatement.setString(1, typeArmor.toLowerCase());
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
