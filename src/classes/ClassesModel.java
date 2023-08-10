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

  public String getClassByNameAndFunction(String nameClass, String functionClass) throws FileNotFoundException, IOException {
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

  public void getAllClasses() throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.statement = this.connection.createStatement();
      this.resultSet = this.statement.executeQuery(
        "SELECT * FROM chroniclesOfArtifacts.classes"
      );
      while(this.resultSet.next()) {
        System.out.print(this.resultSet.getString("nameClass") + this.resultSet.getString("functionClass"));
      }
    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    } finally {
      ConnectionDB.closeConnection();
    }
  }

  public void insertClass(String nameClass, String functionClass) throws FileNotFoundException, IOException {
    
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

  public void updateClass() throws FileNotFoundException, IOException {
    // this.connection.getConnection();
    // this.connection.closeConnection();
  }

  public void removeClass() throws FileNotFoundException, IOException {
    // this.connection.getConnection();
    // this.connection.closeConnection();
  }
}
