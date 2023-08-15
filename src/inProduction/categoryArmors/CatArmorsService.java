package inProduction.categoryArmors;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import connection.ConnectionDB;
import connection.DBException;

public class CatArmorsService {
  CatArmorsModel catArmorsModel = null;

  public CatArmorsService(CatArmorsModel catArmorsModel) {
    this.catArmorsModel = catArmorsModel;
  }

  public ArrayList<Map<String, String>> getAllCatArmors() throws FileNotFoundException, IOException {
    return this.catArmorsModel.getAllCatArmors();
  }

  public ArrayList<Map<String, String>> insertCatArmor(
    String typeArmor,
    String categoryArmor
  ) throws FileNotFoundException, IOException {
    TreeMap<String, String> item = this.catArmorsModel.getByIdOrType(typeArmor);
    if (item != null) {
      return new ArrayList<Map<String, String>>();
    } 
    boolean registerCatArmor = this.catArmorsModel.insertCatArmor(
      typeArmor,
      categoryArmor
    );
    if (registerCatArmor) {
      TreeMap<String, String> registeredCatArmor = this.catArmorsModel.getByIdOrType(typeArmor);
      ArrayList<Map<String, String>> listCatArmors = new ArrayList<Map<String, String>>();
      listCatArmors.add(registeredCatArmor);
      return listCatArmors;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar inserir a Categoria de Armadura " + typeArmor + ". Por favor, tente novamente.");
  };

  public ArrayList<Map<String, String>> updateCatArmor(
    int id,
    String typeArmor,
    String categoryArmor
  ) throws FileNotFoundException, IOException {
    TreeMap<String, String> item = this.catArmorsModel.getByIdOrType(id);
    if ( item == null) {
      return new ArrayList<Map<String, String>>();
    }
    boolean updateCatArmor = this.catArmorsModel.updateCatArmor(
      id,
      typeArmor,
      categoryArmor
    );
    if (updateCatArmor) {
      TreeMap<String, String> updatedCatArmor = this.catArmorsModel.getByIdOrType(typeArmor);
      ArrayList<Map<String, String>> listCatArmors = new ArrayList<Map<String, String>>();
      listCatArmors.add(updatedCatArmor);
      return listCatArmors;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar atualizar a Categoria de Armadura de id " + id + ". Por favor, tente novamente.");
  }

  public boolean removeCatArmor(String typeArmor) throws FileNotFoundException, IOException {
    TreeMap<String, String> item = this.catArmorsModel.getByIdOrType(typeArmor);
    if ( item == null) {
      return false;
    }
    if (this.catArmorsModel.removeCatArmor(typeArmor)){
      TreeMap<String, String> itemRemoved = this.catArmorsModel.getByIdOrType(typeArmor);
      if (itemRemoved == null) {
        return true;
      }
    }
    throw new DBException("Ocorreu um erro ao tentar remover a Categoria de Armadura " + typeArmor + ". Por favor, tente novamente"); 
  }
}
