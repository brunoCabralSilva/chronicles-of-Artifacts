package service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import connection.ConnectionDB;
import connection.DBException;
import model.CatArmorsModel;

public class CatArmorsService {
  CatArmorsModel catArmorsModel = null;

  public CatArmorsService(CatArmorsModel catArmorsModel) {
    this.catArmorsModel = catArmorsModel;
  }

  public ArrayList<Map<String, Object>> getAllCatArmors() throws FileNotFoundException, IOException {
    return this.catArmorsModel.getCatArmors("all");
  }

  public ArrayList<Map<String, Object>> insertCatArmor(
    String typeArmor,
    String categoryArmor
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> item = this.catArmorsModel.getCatArmors(typeArmor);
    if (item.size() != 0) {
      return new ArrayList<Map<String, Object>>();
    } 
    boolean registerCatArmor = this.catArmorsModel.insertCatArmor(
      typeArmor,
      categoryArmor
    );
    if (registerCatArmor) {
      ArrayList<Map<String, Object>> registeredCatArmor = this.catArmorsModel.getCatArmors(typeArmor);
      return registeredCatArmor;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar inserir a Categoria de Armadura " + typeArmor + ". Por favor, tente novamente.");
  };

  public ArrayList<Map<String, Object>> updateCatArmor(
    int id,
    String typeArmor,
    String categoryArmor
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> item = this.catArmorsModel.getCatArmors(id);
    if ( item == null) {
      return new ArrayList<Map<String, Object>>();
    }
    boolean updateCatArmor = this.catArmorsModel.updateCatArmor(
      id,
      typeArmor,
      categoryArmor
    );
    if (updateCatArmor) {
      ArrayList<Map<String, Object>> updatedCatArmor = this.catArmorsModel.getCatArmors(typeArmor);
      return updatedCatArmor;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar atualizar a Categoria de Armadura de id " + id + ". Por favor, tente novamente.");
  }

  public boolean removeCatArmor(String typeArmor) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> item = this.catArmorsModel.getCatArmors(typeArmor);
    if ( item == null || item.size() == 0) {
      return false;
    }
    if (this.catArmorsModel.removeCatArmor(typeArmor)){
      ArrayList<Map<String, Object>> itemRemoved = this.catArmorsModel.getCatArmors(typeArmor);
      if (itemRemoved == null || itemRemoved.size() == 0) {
        return true;
      }
    }
    throw new DBException("Ocorreu um erro ao tentar remover a Categoria de Armadura " + typeArmor + ". Por favor, tente novamente"); 
  }
}
