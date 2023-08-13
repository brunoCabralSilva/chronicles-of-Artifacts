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

  public ArrayList<Map<String, String>> getAllArmors() throws FileNotFoundException, IOException {
    return this.armorsModel.getAllArmors();
  }

  public ArrayList<Map<String, String>> insertArmor(
    String armor,
    int ca,
    int penalty,
    int displacement,
    int category
  ) throws FileNotFoundException, IOException {
    
    TreeMap<String, String> item = this.armorsModel.getArmor(armor);

    if (item != null) {
      return new ArrayList<Map<String, String>>();
    } 
    boolean registerArmor = this.armorsModel.insertArmor(
      armor,
      ca,
      penalty,
      displacement,
      category
    );

    if (registerArmor) {
      TreeMap<String, String> registeredArmor = this.armorsModel.getArmor(armor);
      ArrayList<Map<String, String>> listArmors = new ArrayList<Map<String, String>>();
      listArmors.add(registeredArmor);
      return listArmors;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar inserir a armadura " + armor + ". Por favor, tente novamente.");
  };

  public ArrayList<Map<String, String>> updateArmor(
    int id,
    String armor,
    int ca,
    int penalty,
    int displacement,
    int category
  ) throws FileNotFoundException, IOException {

    TreeMap<String, String> item = this.armorsModel.getArmor(id);
    
    if ( item == null) {
      return new ArrayList<Map<String, String>>();
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
      TreeMap<String, String> updatedArmor = this.armorsModel.getArmor(armor);
      ArrayList<Map<String, String>> listArmors = new ArrayList<Map<String, String>>();
      listArmors.add(updatedArmor);
      return listArmors;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar atualizar a armadura de id " + id + ". Por favor, tente novamente.");
  }

  public boolean removeArmor(String armor) throws FileNotFoundException, IOException {
    TreeMap<String, String> item = this.armorsModel.getArmor(armor);
    
    if ( item == null) {
      return false;
    }

    if (this.armorsModel.removeArmor(armor)){
      TreeMap<String, String> itemRemoved = this.armorsModel.getArmor(armor);
      if (itemRemoved == null) {
        return true;
      }
    }
    throw new DBException("Ocorreu um erro ao tentar remover a armadura  " + armor + ". Por favor, tente novamente"); 
  }
}
