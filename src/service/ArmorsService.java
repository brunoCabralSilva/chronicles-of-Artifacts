package service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import connection.ConnectionDB;
import connection.DBException;
import model.ArmorsModel;

public class ArmorsService {
  ArmorsModel armorsModel = null;

  public ArmorsService() {
    this.armorsModel = new ArmorsModel();
  }

  public ArrayList<Map<String, Object>> getAllArmors() throws FileNotFoundException, IOException {
    return this.armorsModel.getArmors("all");
  }

  public ArrayList<Map<String, Object>> getArmorsByName(String nameArmor) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> allArmors = this.armorsModel.getArmors(nameArmor);
    if (allArmors.size() == 0 ) {
      throw new DBException("Não foram encontradas Armaduras registradas no banco de dados!");
    } else {
      return allArmors;
    }
  }

  public ArrayList<Map<String, Object>> insertArmor(
    String armor,
    int ca,
    int penalty,
    int displacement,
    String type
  ) throws FileNotFoundException, IOException {
    
    ArrayList<Map<String, Object>> item = this.armorsModel.getArmors(armor);

    if (item.size() != 0) {
      return new ArrayList<Map<String, Object>>();
    } 
    boolean registerArmor = this.armorsModel.insertArmor(
      armor,
      ca,
      penalty,
      displacement,
      type
    );

    if (registerArmor) {
      ArrayList<Map<String, Object>> registeredArmor = this.armorsModel.getArmors(armor);
      return registeredArmor;
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
    String type
  ) throws FileNotFoundException, IOException {

    ArrayList<Map<String, Object>> item = this.armorsModel.getArmors(id);
    
    if (item.size() == 0) {
      return new ArrayList<Map<String, Object>>();
    }

    boolean updateArmor = this.armorsModel.updateArmor(
      id,
      armor,
      ca,
      penalty,
      displacement,
      type
    );

    if (updateArmor) {
      ArrayList<Map<String, Object>> updatedArmor = this.armorsModel.getArmors(armor);
      return updatedArmor;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar atualizar a armadura de id " + id + ". Por favor, tente novamente.");
  }

  public boolean removeArmor(String armor) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> item = this.armorsModel.getArmors(armor);
    
    if (item.size() == 0) {
      return false;
    }

    if (this.armorsModel.removeArmor(armor)){
      ArrayList<Map<String, Object>> itemRemoved = this.armorsModel.getArmors(armor);
      if (itemRemoved.size() == 0) {
        return true;
      }
    }
    throw new DBException("Ocorreu um erro ao tentar remover a armadura  " + armor + ". Por favor, tente novamente"); 
  }
}
