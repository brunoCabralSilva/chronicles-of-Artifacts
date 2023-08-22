package armors;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import connection.ConnectionDB;
import connection.DBException;

public class ArmorsService {
  ArmorsModel armorsModel = null;

  public ArmorsService(ArmorsModel armorsModel) {
    this.armorsModel = armorsModel;
  }

  public ArrayList<Map<String, Object>> getAllArmors() throws FileNotFoundException, IOException {
    return this.armorsModel.getArmors("all");
  }

  public ArrayList<Map<String, Object>> insertArmor(
    String armor,
    int ca,
    int penalty,
    int displacement,
    int category
  ) throws FileNotFoundException, IOException {
    
    TreeMap<String, Object> item = this.armorsModel.getArmors(armor);

    if (item != null) {
      return new ArrayList<Map<String, Object>>();
    } 
    boolean registerArmor = this.armorsModel.insertArmor(
      armor,
      ca,
      penalty,
      displacement,
      category
    );

    if (registerArmor) {
      TreeMap<String, Object> registeredArmor = this.armorsModel.getArmors(armor);
      ArrayList<Map<String, Object>> listArmors = new ArrayList<Map<String, Object>>();
      listArmors.add(registeredArmor);
      return listArmors;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar inserir a armadura " + armor + ". Por favor, tente novamente.");
  };

  public ArrayList<Map<String, Object>> updateArmor(
    int id,
    String armor,
    int ca,
    int penalty,
    int displacement,
    int category
  ) throws FileNotFoundException, IOException {

    TreeMap<String, Object> item = this.armorsModel.getArmors(id);
    
    if ( item == null) {
      return new ArrayList<Map<String, Object>>();
    }

    boolean updateArmor = this.armorsModel.updateArmor(
      id,
      armor,
      ca,
      penalty,
      displacement,
      category
    );

    if (updateArmor) {
      TreeMap<String, Object> updatedArmor = this.armorsModel.getArmors(armor);
      ArrayList<Map<String, Object>> listArmors = new ArrayList<Map<String, Object>>();
      listArmors.add(updatedArmor);
      return listArmors;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar atualizar a armadura de id " + id + ". Por favor, tente novamente.");
  }

  public boolean removeArmor(String armor) throws FileNotFoundException, IOException {
    TreeMap<String, Object> item = this.armorsModel.getArmors(armor);
    
    if ( item == null) {
      return false;
    }

    if (this.armorsModel.removeArmor(armor)){
      TreeMap<String, Object> itemRemoved = this.armorsModel.getArmors(armor);
      if (itemRemoved == null) {
        return true;
      }
    }
    throw new DBException("Ocorreu um erro ao tentar remover a armadura  " + armor + ". Por favor, tente novamente"); 
  }
}
