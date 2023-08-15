package weaponProperties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.TreeMap;

import connection.ConnectionDB;
import connection.DBException;
import properties.PropertiesModel;

public class WeaponPropertiesModel {
  private Connection connection;
  private ResultSet resultSet;
  private PreparedStatement prepStatement;
  PropertiesModel propertiesModel;

  public WeaponPropertiesModel() {
    this.connection = null;
    this.resultSet = null;
    this.prepStatement = null;
    this.propertiesModel = new PropertiesModel();
  }
  public ArrayList<String> weaponPropertiesByWeapon(String weapon) throws SQLException, FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    this.prepStatement = this.connection.prepareStatement(
      "SELECT p.property FROM chroniclesOfArtifacts.properties p "
      + "INNER JOIN chroniclesOfArtifacts.weaponProperties wp ON p.id = wp.propertyId "
      + "INNER JOIN chroniclesOfArtifacts.weapons w ON wp.weaponId = w.id "
      + "WHERE w.weapon = ?;",
      Statement.RETURN_GENERATED_KEYS
    );
    this.prepStatement.setString(1, weapon);
    ResultSet propertyResultSet = this.prepStatement.executeQuery();
    ArrayList<String> properties = new ArrayList<String>();
    while (propertyResultSet.next()) {
      properties.add(propertyResultSet.getString("property"));
    }
    return properties;
  }

  public void insertWeaponProperties(int weaponId, ArrayList<String> listProperties) throws FileNotFoundException, IOException {
    try {
      this.connection = ConnectionDB.getConnection();
      this.connection.setAutoCommit(false);
      for (String property : listProperties) {
        TreeMap<String, Object> propertyId = this.propertiesModel.getProperty(property);
        if (propertyId == null) {
          propertyId = new TreeMap<String, Object>();
          propertyId.put("id", this.propertiesModel.insertProperty(property));
        }

        this.prepStatement = this.connection.prepareStatement(
          "SELECT * FROM chroniclesOfArtifacts.weaponProperties "
          + "WHERE propertyId = ? AND weaponId = ?"
        );
        this.prepStatement.setInt(1, (Integer) propertyId.get("id"));
        this.prepStatement.setInt(2, weaponId);
        this.resultSet = this.prepStatement.executeQuery();

        if (!this.resultSet.next()) {
          this.prepStatement = this.connection.prepareStatement(
            "INSERT INTO chroniclesOfArtifacts.weaponProperties (propertyId, weaponId) VALUES (?, ?)"
          );

          this.prepStatement.setInt(1, (Integer) propertyId.get("id"));
          this.prepStatement.setInt(2, weaponId);
          this.prepStatement.executeUpdate();
        }
      }
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    }
  }

  public void removeWeaponProperty(int id) throws FileNotFoundException, IOException {
    this.connection = ConnectionDB.getConnection();
    try {
      this.connection.setAutoCommit(false);
      this.prepStatement = this.connection.prepareStatement("SET SQL_SAFE_UPDATES = 0");
      this.prepStatement = this.connection.prepareStatement(
        "DELETE FROM chroniclesOfArtifacts.weaponProperties "
        + "WHERE weaponId = ?"
        );
      this.prepStatement.setInt(1, id);
      this.prepStatement.executeUpdate();
      this.connection.commit();
    } catch (SQLException e) {
      ConnectionDB.rollbackFunction(this.connection);
      throw new DBException(e.getMessage());
    }
  }
}
