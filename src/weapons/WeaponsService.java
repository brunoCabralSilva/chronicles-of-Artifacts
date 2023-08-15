package weapons;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import connection.ConnectionDB;
import connection.DBException;

public class WeaponsService {
  WeaponsModel weaponsModel = null;

  public WeaponsService(WeaponsModel weaponsModel) {
    this.weaponsModel = weaponsModel;
  }

  public ArrayList<Map<String, Object>> getWeapons() throws FileNotFoundException, IOException {
    return this.weaponsModel.getWeapons("all");
  }

  public ArrayList<Map<String, Object>> insertWeapon(
    String weapon,
    String categoryWeapon,
    int proficiency,
    String damage,
    String rangeWeapon,
    int numberOfHands,
    ArrayList<String> properties
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String,Object>> item = this.weaponsModel.getWeapons(weapon);
    if (item.size() != 0) {
      return new ArrayList<Map<String, Object>>();
    } 
    this.weaponsModel.insertWeapon(
      weapon,
      categoryWeapon,
      proficiency,
      damage,
      rangeWeapon,
      numberOfHands,
      properties
    );
    ArrayList<Map<String,Object>> registeredWeapon = this.weaponsModel.getWeapons(weapon);
    ConnectionDB.closeConnection();
    if (registeredWeapon.size() > 0) {
      return registeredWeapon;
    }
    throw new DBException("Ocorreu um erro ao tentar inserir a arma " + weapon + ". Por favor, tente novamente.");
  };

  public ArrayList<Map<String, Object>> updateWeapon(
    int id,
    String weapon,
    String categoryWeapon,
    int proficiency,
    String damage,
    String rangeWeapon,
    int numberOfHands,
    ArrayList<String> properties,
    boolean override
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String,Object>> item = this.weaponsModel.getWeapons(id);
    if ( item == null) {
      return new ArrayList<Map<String, Object>>();
    }
    boolean updateWeapon = this.weaponsModel.updateWeapon(
      id,
      weapon,
      categoryWeapon,
      proficiency,
      damage,
      rangeWeapon,
      numberOfHands,
      properties,
      override
    );
    if (updateWeapon) {
      ArrayList<Map<String,Object>> updatedWeapon = this.weaponsModel.getWeapons(weapon);
      return updatedWeapon;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar atualizar a arma de id " + id + ". Por favor, tente novamente.");
  }

  public boolean removeWeapon(String weapon) throws FileNotFoundException, IOException {
    ArrayList<Map<String,Object>> item = this.weaponsModel.getWeapons(weapon);
    if ( item == null || item.size() == 0) {
      return false;
    }
    System.out.println(item);
    if (this.weaponsModel.removeWeapon(weapon, item)){
      ArrayList<Map<String,Object>> itemRemoved = this.weaponsModel.getWeapons(weapon);
      if (itemRemoved.size() == 0) {
        return true;
      }
    }
    throw new DBException("Ocorreu um erro ao tentar remover a arma  " + weapon + ". Por favor, tente novamente"); 
  }
}
