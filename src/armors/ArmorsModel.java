package armors;

import java.io.FileNotFoundException;
import java.io.IOException;

import connection.ConnectionDB;

public class ArmorsModel {

  ConnectionDB connection;

  ArmorsModel(ConnectionDB connection) {
    this.connection = connection;
  }

  public void getAllArmors() throws FileNotFoundException, IOException {
    this.connection.getConnection();
    this.connection.closeConnection();
  }

  public void insertArmor() throws FileNotFoundException, IOException {
    this.connection.getConnection();
    this.connection.closeConnection();
  };

  public void updateArmor() throws FileNotFoundException, IOException {
    this.connection.getConnection();
    this.connection.closeConnection();
  }

  public void removeArmor() throws FileNotFoundException, IOException {
    this.connection.getConnection();
    this.connection.closeConnection();
  }
}
