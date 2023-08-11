package weapons;

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

public class WeaponsModel {
  private Connection connection;
  private Statement statement;
  private ResultSet resultSet;
  private PreparedStatement prepStatement;

  public WeaponsModel() {
    this.connection = null;
    this.statement = null;
    this.resultSet = null;
    this.prepStatement = null;
  }

  private String getWeaponByName(String weapon) throws FileNotFoundException, IOException {
    try {
      this.statement = this.connection.createStatement();
      this.prepStatement = this.connection.prepareStatement(
        "SELECT * FROM chroniclesOfArtifacts.weapons "
        + "WHERE weapon = ?"
      );

      this.prepStatement.setString(1, weapon);
      this.resultSet = this.prepStatement.executeQuery();

      if (this.resultSet.next()) {
        return this.resultSet.getString(2);
      } return null;

    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
  }

  private String getWeaponById(int id) throws FileNotFoundException, IOException {
    try {
      this.statement = this.connection.createStatement();
      this.prepStatement = this.connection.prepareStatement(
        "SELECT * FROM chroniclesOfArtifacts.weapons "
        + "WHERE id = ?"
      );

      this.prepStatement.setInt(1, id);
      this.resultSet = this.prepStatement.executeQuery();

      if (this.resultSet.next()) {
        return this.resultSet.getString(2);
      } return null;

    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
  }

  public ArrayList<Map<String, String>> getAllWeapons() throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.statement = this.connection.createStatement();
      this.resultSet = this.statement.executeQuery(
        "SELECT * FROM chroniclesOfArtifacts.weapons"
      );
      
      ArrayList<Map<String, String>> listWeapons = new ArrayList<Map<String, String>>();

      while(this.resultSet.next()) {

        TreeMap<String, String> line = new TreeMap<String, String>();

        line.put("weapon", this.resultSet.getString("weapon"));
        line.put("categoryWeapon", this.resultSet.getString("categoryWeapon"));
        line.put("proficiency", this.resultSet.getString("proficiency"));
        line.put("damage", this.resultSet.getString("damage"));
        line.put("rangeWeapon", this.resultSet.getString("rangeWeapon"));
        line.put("numberOfHands", this.resultSet.getString("numberOfHands"));

        listWeapons.add(line);
      }
      return listWeapons;
    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    } finally {
      ConnectionDB.closeConnection();
    }
  }

  public void insertWeapon(
    String weapon,
    String categoryWeapon,
    int proficiency,
    String damage,
    String rangeWeapon,
    int numberOfHands
  ) throws FileNotFoundException, IOException {
    
    this.connection = ConnectionDB.getConnection();

    if (this.getWeaponByName(weapon) != null) {
      throw new DBException("Arma " + weapon + " já existente na base de dados!");
    }

    try {
      this.connection.setAutoCommit(false);

      this.prepStatement = this.connection.prepareStatement(
        "INSERT INTO chroniclesOfArtifacts.weapons "
        + "(weapon, categoryWeapon, proficiency, damage, rangeWeapon, numberOfHands) "
        + "VALUES (?, ?, ?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS
      );

      this.prepStatement.setString(1, weapon);
      this.prepStatement.setString(2, categoryWeapon);
      this.prepStatement.setInt(3, proficiency);
      this.prepStatement.setString(4, damage);
      this.prepStatement.setString(5, rangeWeapon);
      this.prepStatement.setInt(6, numberOfHands);
      int rowsAffected = this.prepStatement.executeUpdate();

      this.resultSet = this.prepStatement.getGeneratedKeys();

      this.resultSet.next();

      System.out.println(
          "\nConcluído! Nova arma "
          + weapon 
          + " adicionada com sucesso!\nId: "
          + this.resultSet.getInt(1)
          + ".\nLinhas afetadas: "
          + rowsAffected
          +".\n"
        );

      this.connection.commit();

    } catch (SQLException e) {
      try {
        this.connection.rollback();
      } catch(SQLException e2) {
        throw new DBException("Não foi possível realizar o Rollback");  
      } throw new DBException(e.getMessage());
    } finally {
      ConnectionDB.closeConnection();
    }
  };

  public void updateWeapon(
    int id,
    String weapon,
    String categoryWeapon,
    int proficiency,
    String damage,
    String rangeWeapon,
    int numberOfHands
    ) throws FileNotFoundException, IOException {

    this.connection = ConnectionDB.getConnection();
    if ( this.getWeaponById(id) == null) {
      throw new DBException("A arma de id" + id + " não foi encontrada na base de dados!");  
    }

    try {
      this.connection.setAutoCommit(false);
      
      this.prepStatement = this.connection.prepareStatement(
        "UPDATE chroniclesOfArtifacts.weapons "
        + "SET weapon = ?, categoryWeapon = ?, proficiency = ?, damage = ?, rangeWeapon = ?, numberOfHands = ?"
        + " WHERE id = ?",
        Statement.RETURN_GENERATED_KEYS
      );

      this.prepStatement.setString(1, weapon);
      this.prepStatement.setString(2, categoryWeapon);
      this.prepStatement.setInt(3, proficiency);
      this.prepStatement.setString(4, damage);
      this.prepStatement.setString(5, rangeWeapon);
      this.prepStatement.setInt(6, numberOfHands);
      this.prepStatement.setInt(7, id);

      this.prepStatement.executeUpdate();

      System.out.println(
        "\nConcluído! Atualização realizada com sucesso!\n"
      );

      this.connection.commit();

    } catch (SQLException e) {
      try {
        this.connection.rollback();
      } catch(SQLException e2) {
        throw new DBException("Não foi possível realizar o Rollback");  
      } throw new DBException(e.getMessage());
    } finally {
      ConnectionDB.closeConnection();
    }
  };

  public void removeWeapon(String weapon) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    if ( this.getWeaponByName(weapon) == null) {
      throw new DBException("A arma " + weapon + " não foi encontrada na base de dados!");  
    }

    try {
      this.connection.setAutoCommit(false);

      this.prepStatement = this.connection.prepareStatement("SET SQL_SAFE_UPDATES = 0");
      this.prepStatement.executeUpdate();
      
      this.prepStatement = this.connection.prepareStatement(
        "DELETE FROM chroniclesOfArtifacts.weapons "
        + "WHERE Weapon = ? ",
        Statement.RETURN_GENERATED_KEYS
      );

      this.prepStatement.setString(1, weapon);
      this.prepStatement.executeUpdate();

      this.resultSet = this.prepStatement.getGeneratedKeys();

      this.resultSet.next();

      System.out.println(
        "\nConcluído! A arma "
        + weapon
        + " foi removida com sucesso:\n"
      );

      this.connection.commit();

    } catch (SQLException e) {
      try {
        this.connection.rollback();
      } catch(SQLException e2) {
        throw new DBException("Não foi possível realizar o Rollback");  
      } throw new DBException(e.getMessage());
    } finally {
      ConnectionDB.closeConnection();
    }
  }
}
