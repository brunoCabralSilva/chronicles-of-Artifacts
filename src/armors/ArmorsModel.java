package armors;

import java.sql.Statement;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import connection.ConnectionDB;
import connection.DBException;

public class ArmorsModel {
  Connection connection;
  Statement statement;
  ResultSet resultSet;

  ArmorsModel() {
    this.connection = null;
    this.statement = null;
    this.resultSet = null;
  }

  public void getAllArmors() throws FileNotFoundException, IOException {

  }

  public void insertArmor() throws FileNotFoundException, IOException {
    // this.connection.getConnection();
    // this.connection.closeConnection();
  };

  public void updateArmor() throws FileNotFoundException, IOException {
    // this.connection.getConnection();
    // this.connection.closeConnection();
  }

  public void removeArmor() throws FileNotFoundException, IOException {
    // this.connection.getConnection();
    // this.connection.closeConnection();
  }
}
