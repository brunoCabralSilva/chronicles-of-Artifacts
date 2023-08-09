package classes;

import java.io.FileNotFoundException;
import java.io.IOException;

import connection.ConnectionDB;

public class ClassesModel {

  ConnectionDB connection;

  ClassesModel(ConnectionDB connection) {
    this.connection = connection;
  }

  public void getAllClasses() throws FileNotFoundException, IOException {
    this.connection.getConnection();
    this.connection.closeConnection();
  }

  public void insertClass() throws FileNotFoundException, IOException {
    this.connection.getConnection();
    this.connection.closeConnection();
  };

  public void updateClass() throws FileNotFoundException, IOException {
    this.connection.getConnection();
    this.connection.closeConnection();
  }

  public void removeClass() throws FileNotFoundException, IOException {
    this.connection.getConnection();
    this.connection.closeConnection();
  }
}
