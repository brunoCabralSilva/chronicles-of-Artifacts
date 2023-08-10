package classes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import connection.ConnectionDB;
import connection.DBException;

public class ClassesModel {
  Connection connection;
  Statement statement;
  ResultSet resultSet;
  PreparedStatement prepStatement;

  public ClassesModel() {
    this.connection = null;
    this.statement = null;
    this.resultSet = null;
    this.prepStatement = null;
  }

  private String getClassByNameAndFunction(String nameClass, String functionClass) throws FileNotFoundException, IOException {
    try {
      this.statement = this.connection.createStatement();
      this.prepStatement = this.connection.prepareStatement(
        "SELECT * FROM chroniclesOfArtifacts.classes "
        + "WHERE nameClass = ? AND functionClass = ?"
      );

      this.prepStatement.setString(1, nameClass);
      this.prepStatement.setString(2, functionClass);
      this.resultSet = this.prepStatement.executeQuery();

      if (this.resultSet.next()) {
        return this.resultSet.getString(2);
      } return null;

    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
  }

  private String getClassById(int id) throws FileNotFoundException, IOException {
    try {
      this.statement = this.connection.createStatement();
      this.prepStatement = this.connection.prepareStatement(
        "SELECT * FROM chroniclesOfArtifacts.classes "
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

  public void getAllClasses() throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.statement = this.connection.createStatement();
      this.resultSet = this.statement.executeQuery(
        "SELECT * FROM chroniclesOfArtifacts.classes"
      );
      while(this.resultSet.next()) {
        System.out.print(
          "\nClasse: "
          + this.resultSet.getString("nameClass")
          + "\nFunção: "
          + this.resultSet.getString("functionClass")
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

  public void insertClass(String nameClass, String functionClass, String[] catArmors, String[] catWeapons) throws FileNotFoundException, IOException {
    
    this.connection = ConnectionDB.getConnection();

    if (this.getClassByNameAndFunction(nameClass, functionClass) != null) {
      throw new DBException("Classe " + nameClass + " já existente na base de dados!");  
    }

    try {
      this.connection.setAutoCommit(false);

      this.prepStatement = this.connection.prepareStatement(
        "INSERT INTO chroniclesOfArtifacts.classes "
        + "(nameClass, functionClass) "
        + "VALUES (?, ?)",
        Statement.RETURN_GENERATED_KEYS
      );

      this.prepStatement.setString(1, nameClass);
      this.prepStatement.setString(2, functionClass);
      int rowsAffected = this.prepStatement.executeUpdate();

      this.resultSet = this.prepStatement.getGeneratedKeys();

      this.resultSet.next();

      System.out.println(
          "\nConcluído! Nova classe "
          + nameClass 
          + " adicionada com sucesso!\nId: "
          + this.resultSet.getInt(1)
          + ".\nLinhas afetadas: "
          + rowsAffected
          +".\n"
        );

      //IMPLEMENTAR O REGISTRO DE ARMAS E ARMADURAS DA CLASSE EM weaponsClasses e armorsClasses

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

  public void updateClass(int id, String nameClass, String functionClass) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    if ( this.getClassById(id) == null) {
      throw new DBException("A Classe de id" + id + " não foi encontrada na base de dados!");  
    }

    try {
      this.connection.setAutoCommit(false);
      
      this.prepStatement = this.connection.prepareStatement(
        "UPDATE chroniclesOfArtifacts.classes "
        + "SET nameClass = ?, functionClass = ? "
        + "WHERE id = ?;",
        Statement.RETURN_GENERATED_KEYS
      );

      this.prepStatement.setString(1, nameClass);
      this.prepStatement.setString(2, functionClass);
      this.prepStatement.setInt(3, id);

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

  public void removeClass(String nameClass, String functionClass) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    if ( this.getClassByNameAndFunction(nameClass, functionClass) == null) {
      throw new DBException("A Classe " + nameClass + " não foi encontrada na base de dados!");  
    }

    try {
      this.connection.setAutoCommit(false);

      this.prepStatement = this.connection.prepareStatement("SET SQL_SAFE_UPDATES = 0");
      this.prepStatement.executeUpdate();
      
      this.prepStatement = this.connection.prepareStatement(
        "DELETE FROM chroniclesOfArtifacts.classes "
        + "WHERE nameClass = ? AND functionClass = ? ",
        Statement.RETURN_GENERATED_KEYS
      );

      this.prepStatement.setString(1, nameClass);
      this.prepStatement.setString(2, functionClass);
      this.prepStatement.executeUpdate();

      this.resultSet = this.prepStatement.getGeneratedKeys();

      this.resultSet.next();

      //IMPLEMENTAR REMOÇÃO DE ARMAS E ARMADURAS DA CLASSE EM weaponsClasses e armorsClasses

      System.out.println(
        "\nConcluído! A classe "
        + nameClass
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
