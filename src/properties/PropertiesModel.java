package properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import connection.ConnectionDB;
import connection.DBException;

public class PropertiesModel {
  Connection connection;
  Statement statement;
  ResultSet resultSet;
  PreparedStatement prepStatement;

  public PropertiesModel() {
    this.connection = null;
    this.statement = null;
    this.resultSet = null;
    this.prepStatement = null;
  }

  private String getPropertyByName(String property) throws FileNotFoundException, IOException {
    try {
      this.statement = this.connection.createStatement();
      this.prepStatement = this.connection.prepareStatement(
        "SELECT * FROM chroniclesOfArtifacts.properties "
        + "WHERE property = ?"
      );

      this.prepStatement.setString(1, property);
      this.resultSet = this.prepStatement.executeQuery();

      if (this.resultSet.next()) {
        return this.resultSet.getString(2);
      } return null;

    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
  }

  private String getPropertyById(int id) throws FileNotFoundException, IOException {
    try {
      this.statement = this.connection.createStatement();
      this.prepStatement = this.connection.prepareStatement(
        "SELECT * FROM chroniclesOfArtifacts.properties "
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

  public void getAllProperties() throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.statement = this.connection.createStatement();
      this.resultSet = this.statement.executeQuery(
        "SELECT * FROM chroniclesOfArtifacts.properties"
      );
      while(this.resultSet.next()) {
        System.out.print(
          "\n"
          + this.resultSet.getString("property")
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

  public void insertProperty(String property) throws FileNotFoundException, IOException {
    
    this.connection = ConnectionDB.getConnection();

    if (this.getPropertyByName(property) != null) {
      throw new DBException("Propriedade " + property + " já existente na base de dados!");  
    }

    try {
      this.connection.setAutoCommit(false);

      this.prepStatement = this.connection.prepareStatement(
        "INSERT INTO chroniclesOfArtifacts.properties "
        + "(property) "
        + "VALUES (?)",
        Statement.RETURN_GENERATED_KEYS
      );

      this.prepStatement.setString(1, property);
      int rowsAffected = this.prepStatement.executeUpdate();

      this.resultSet = this.prepStatement.getGeneratedKeys();

      this.resultSet.next();

      System.out.println(
          "\nConcluído! Nova propriedade "
          + property 
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

  public void updateProperty(int id, String property) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    if ( this.getPropertyById(id) == null) {
      throw new DBException("A propriedade de id" + id + " não foi encontrada na base de dados!");  
    }

    try {
      this.connection.setAutoCommit(false);
      
      this.prepStatement = this.connection.prepareStatement(
        "UPDATE chroniclesOfArtifacts.properties "
        + "SET property = ? "
        + "WHERE id = ?",
        Statement.RETURN_GENERATED_KEYS
      );

      this.prepStatement.setString(1, property);
      this.prepStatement.setInt(2, id);

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

  public void removeProperty(String property) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    if ( this.getPropertyByName(property) == null) {
      throw new DBException("A propriedade " + property + " não foi encontrada na base de dados!");  
    }

    try {
      this.connection.setAutoCommit(false);

      this.prepStatement = this.connection.prepareStatement("SET SQL_SAFE_UPDATES = 0");
      this.prepStatement.executeUpdate();
      
      this.prepStatement = this.connection.prepareStatement(
        "DELETE FROM chroniclesOfArtifacts.properties "
        + "WHERE property = ? ",
        Statement.RETURN_GENERATED_KEYS
      );

      this.prepStatement.setString(1, property);
      this.prepStatement.executeUpdate();

      this.resultSet = this.prepStatement.getGeneratedKeys();

      this.resultSet.next();

      System.out.println(
        "\nConcluído! A propriedade "
        + property
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
