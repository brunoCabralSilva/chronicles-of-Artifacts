package weapons;

import java.io.FileNotFoundException;
import java.io.IOException;

import connection.ConnectionDB;

public class WeaponsModel {

  ConnectionDB connection;

  WeaponsModel(ConnectionDB connection) {
    this.connection = connection;
  }

  public void getAllWeapons() throws FileNotFoundException, IOException {
    this.connection.getConnection();
    this.connection.closeConnection();
  }

  public void insertWeapon() throws FileNotFoundException, IOException {
    this.connection.getConnection();
    this.connection.closeConnection();
  };

  public void updateWeapon() throws FileNotFoundException, IOException {
    this.connection.getConnection();
    this.connection.closeConnection();
  }

  public void removeWeapon() throws FileNotFoundException, IOException {
    this.connection.getConnection();
    this.connection.closeConnection();
  }
}
