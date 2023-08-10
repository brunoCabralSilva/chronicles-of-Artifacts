package armors;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import connection.ConnectionDB;
import connection.DBException;

public class ArmorsModel {
  Connection connection;
  Statement statement;
  ResultSet resultSet;
  PreparedStatement prepStatement;

  public ArmorsModel() {
    this.connection = null;
    this.statement = null;
    this.resultSet = null;
    this.prepStatement = null;
  }

  private String getArmorByName(String armor) throws FileNotFoundException, IOException {
    try {
      this.statement = this.connection.createStatement();
      this.prepStatement = this.connection.prepareStatement(
        "SELECT * FROM chroniclesOfArtifacts.armors "
        + "WHERE armor = ?"
      );

      this.prepStatement.setString(1, armor);
      this.resultSet = this.prepStatement.executeQuery();

      if (this.resultSet.next()) {
        return this.resultSet.getString(2);
      } return null;

    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
  }

  private String getArmorById(int id) throws FileNotFoundException, IOException {
    try {
      this.statement = this.connection.createStatement();
      this.prepStatement = this.connection.prepareStatement(
        "SELECT * FROM chroniclesOfArtifacts.armors "
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

  public void getAllArmors() throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.statement = this.connection.createStatement();
      this.resultSet = this.statement.executeQuery(
        "SELECT * FROM chroniclesOfArtifacts.armors"
      );
      while(this.resultSet.next()) {
        System.out.print(
          "\n"
          + this.resultSet.getString("armor")
          +"\n"
        );
        
        //IMPLEMENTAR RETORNO DE UMA LISTA DE MAP (CONJUNTO CHAVE VALOR) DE CADA UM DOS DADOS RETORNADOS
      }
    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    } finally {
      ConnectionDB.closeConnection();
    }
  }

  public void insertArmor(
    String armor,
    int ca,
    int penalty,
    int displacement,
    int category
  ) throws FileNotFoundException, IOException {
    
    this.connection = ConnectionDB.getConnection();

    if (this.getArmorByName(armor) != null) {
      throw new DBException("Armadura " + armor + " já existente na base de dados!");
    }

    try {
      this.connection.setAutoCommit(false);

      this.prepStatement = this.connection.prepareStatement(
        "INSERT INTO chroniclesOfArtifacts.armors "
        + "(armor, ca, penalty, displacement, category) "
        + "VALUES (?, ?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS
      );

      this.prepStatement.setString(1, armor);
      this.prepStatement.setInt(2, ca);
      this.prepStatement.setInt(3, penalty);
      this.prepStatement.setInt(4, displacement);
      this.prepStatement.setInt(5, category);

      int rowsAffected = this.prepStatement.executeUpdate();

      this.resultSet = this.prepStatement.getGeneratedKeys();

      this.resultSet.next();

      System.out.println(
          "\nConcluído! Nova armadura "
          + armor 
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

  public void updateArmor(
    int id,
    String armor,
    int ca,
    int penalty,
    int displacement,
    int category
    ) throws FileNotFoundException, IOException {

    this.connection = ConnectionDB.getConnection();
    if ( this.getArmorById(id) == null) {
      throw new DBException("A arma de id" + id + " não foi encontrada na base de dados!");  
    }

    try {
      this.connection.setAutoCommit(false);
      
      this.prepStatement = this.connection.prepareStatement(
        "UPDATE chroniclesOfArtifacts.armors "
        + "SET armor = ?, ca = ?, penalty = ?, displacement = ?, category = ? "
        + " WHERE id = ?",
        Statement.RETURN_GENERATED_KEYS
      );

      this.prepStatement.setString(1, armor);
      this.prepStatement.setInt(2, ca);
      this.prepStatement.setInt(3, penalty);
      this.prepStatement.setInt(4, displacement);
      this.prepStatement.setInt(5, category);
      this.prepStatement.setInt(6, id);

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

  public void removeArmor(String armor) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    if ( this.getArmorByName(armor) == null) {
      throw new DBException("A arma " + armor + " não foi encontrada na base de dados!");  
    }

    try {
      this.connection.setAutoCommit(false);

      this.prepStatement = this.connection.prepareStatement("SET SQL_SAFE_UPDATES = 0");
      this.prepStatement.executeUpdate();
      
      this.prepStatement = this.connection.prepareStatement(
        "DELETE FROM chroniclesOfArtifacts.armors "
        + "WHERE armor = ? ",
        Statement.RETURN_GENERATED_KEYS
      );

      this.prepStatement.setString(1, armor);
      this.prepStatement.executeUpdate();

      this.resultSet = this.prepStatement.getGeneratedKeys();

      this.resultSet.next();

      System.out.println(
        "\nConcluído! A arma "
        + armor
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
