package weapons;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import connection.ConnectionDB;
import connection.DBException;

public class WeaponsService {
  WeaponsModel weaponsModel = null;

  public WeaponsService() {
    this.weaponsModel = new WeaponsModel();
  }

  public ArrayList<Map<String, String>> getAllWeapons() throws FileNotFoundException, IOException {
    return this.weaponsModel.getAllWeapons();
  }

  public ArrayList<Map<String, String>> insertWeapon(
    String weapon,
    String categoryWeapon,
    int proficiency,
    String damage,
    String rangeWeapon,
    int numberOfHands
  ) throws FileNotFoundException, IOException {
    
    TreeMap<String, String> item = this.weaponsModel.getWeapon(weapon);

    if (item != null) {
      return new ArrayList<Map<String, String>>();
    } 
    boolean registerWeapon = this.weaponsModel.insertWeapon(
      weapon,
      categoryWeapon,
      proficiency,
      damage,
      rangeWeapon,
      numberOfHands
    );

    if (registerWeapon) {
      TreeMap<String, String> registeredWeapon = this.weaponsModel.getWeapon(weapon);
      ArrayList<Map<String, String>> listWeapons = new ArrayList<Map<String, String>>();
      listWeapons.add(registeredWeapon);
      return listWeapons;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar inserir a arma " + weapon + ". Por favor, tente novamente.");
  };

  public ArrayList<Map<String, String>> updateWeapon(
    int id,
    String weapon,
    String categoryWeapon,
    int proficiency,
    String damage,
    String rangeWeapon,
    int numberOfHands
  ) throws FileNotFoundException, IOException {

    TreeMap<String, String> item = this.weaponsModel.getWeapon(id);
    
    if ( item == null) {
      return new ArrayList<Map<String, String>>();
    }

    boolean updateWeapon = this.weaponsModel.updateWeapon(
      id,
      weapon,
      categoryWeapon,
      proficiency,
      damage,
      rangeWeapon,
      numberOfHands
    );

    if (updateWeapon) {
      TreeMap<String, String> updatedWeapon = this.weaponsModel.getWeapon(weapon);
      ArrayList<Map<String, String>> listWeapons = new ArrayList<Map<String, String>>();
      listWeapons.add(updatedWeapon);
      return listWeapons;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar atualizar a arma de id " + id + ". Por favor, tente novamente.");
  }

  public boolean removeWeapon(String weapon) throws FileNotFoundException, IOException {
    TreeMap<String, String> item = this.weaponsModel.getWeapon(weapon);
    
    if ( item == null) {
      return false;
    }

    if (this.weaponsModel.removeWeapon(weapon)){
      TreeMap<String, String> itemRemoved = this.weaponsModel.getWeapon(weapon);
      if (itemRemoved == null) {
        return true;
      }
    }
    throw new DBException("Ocorreu um erro ao tentar remover a arma  " + weapon + ". Por favor, tente novamente"); 
  }
}
