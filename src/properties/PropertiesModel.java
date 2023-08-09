package properties;

import java.io.FileNotFoundException;
import java.io.IOException;

import connection.ConnectionDB;

public class PropertiesModel {

  ConnectionDB connection;

  PropertiesModel(ConnectionDB connection) {
    this.connection = connection;
  }

  public void getAllProperties() throws FileNotFoundException, IOException {
    this.connection.getConnection();
    this.connection.closeConnection();
  }

  public void insertProperty() throws FileNotFoundException, IOException {
    this.connection.getConnection();
    this.connection.closeConnection();
  };

  public void updateProperty() throws FileNotFoundException, IOException {
    this.connection.getConnection();
    this.connection.closeConnection();
  }

  public void removeProperty() throws FileNotFoundException, IOException {
    this.connection.getConnection();
    this.connection.closeConnection();
  }
}
