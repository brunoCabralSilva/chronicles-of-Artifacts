package service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import connection.ConnectionDB;
import connection.DBException;
import model.CatWeaponsModel;

public class CatWeaponsService {
  CatWeaponsModel catWeaponsModel = null;

  public CatWeaponsService(CatWeaponsModel catWeaponsModel) {
    this.catWeaponsModel = catWeaponsModel;
  }

  public ArrayList<Map<String, Object>> getAllCatWeapons() throws FileNotFoundException, IOException {
    return this.catWeaponsModel.getCatWeapons("all");
  }

  public ArrayList<Map<String, Object>> insertCatWeapon(String categoryWeapon) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> item = this.catWeaponsModel.getCatWeapons(categoryWeapon);
    if (item.size() != 0) {
      return new ArrayList<Map<String, Object>>();
    } 
    boolean registerCatWeapon = this.catWeaponsModel.insertCatWeapon(categoryWeapon);
    if (registerCatWeapon) {
      ArrayList<Map<String, Object>> registeredCatWeapon = this.catWeaponsModel.getCatWeapons(categoryWeapon);
      return registeredCatWeapon;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar inserir a Categoria de Arma " + categoryWeapon + ". Por favor, tente novamente.");
  };

  public ArrayList<Map<String, Object>> updateCatWeapon(
    int id,
    String categoryWeapon
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> item = this.catWeaponsModel.getCatWeapons(id);
    if ( item == null) {
      return new ArrayList<Map<String, Object>>();
    }
    boolean updateCatWeapon = this.catWeaponsModel.updateCatWeapon(
      id,
      categoryWeapon
    );
    if (updateCatWeapon) {
      ArrayList<Map<String, Object>> updatedCatWeapon = this.catWeaponsModel.getCatWeapons(categoryWeapon);
      return updatedCatWeapon;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar atualizar a Categoria de Arma de id " + id + ". Por favor, tente novamente.");
  }

  public boolean removeCatWeapon(String categoryWeapon) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> item = this.catWeaponsModel.getCatWeapons(categoryWeapon);
    if ( item == null || item.size() == 0) {
      return false;
    }
    if (this.catWeaponsModel.removeCatWeapon(categoryWeapon)){
      ArrayList<Map<String, Object>> itemRemoved = this.catWeaponsModel.getCatWeapons(categoryWeapon);
      if (itemRemoved == null || itemRemoved.size() == 0) {
        return true;
      }
    }
    throw new DBException("Ocorreu um erro ao tentar remover a Categoria de Arma " + categoryWeapon + ". Por favor, tente novamente"); 
  }
}
