package database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionDB {
  private Connection connect = null;

  public Connection getConnection() throws FileNotFoundException, IOException {
    if (connect == null) {
      try {
        Properties props = loadProperties();
        String url = props.getProperty("dburl");
        connect = DriverManager.getConnection(url, props);
      } catch(SQLException e) {
        throw new DBException(e.getMessage());
      }
    } return connect;
  }

  public void closeConnection() {
    if (connect != null) {
      try {
        connect.close();
      } catch(SQLException e) {
        throw new DBException(e.getMessage());
      }
    }
  }

  private static Properties loadProperties() throws FileNotFoundException, IOException {
    try (FileInputStream fs = new FileInputStream("db.properties")) {
      Properties props = new Properties();
      props.load(fs);
      return props;
    } catch(IOException e) {
      throw new DBException(e.getMessage());
    }
  }
}
