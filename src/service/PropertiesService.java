package service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import connection.ConnectionDB;
import connection.DBException;
import model.PropertiesModel;

public class PropertiesService {
  PropertiesModel propertiesModel = null;

  public PropertiesService(PropertiesModel propertiesModel) {
    this.propertiesModel = propertiesModel;
  }

  public ArrayList<Map<String, Object>> getProperties() throws FileNotFoundException, IOException {
    return this.propertiesModel.getProperties("all");
  }

  public ArrayList<Map<String, Object>> insertProperty(String property) throws FileNotFoundException, IOException {
    
    ArrayList<Map<String, Object>> item = this.propertiesModel.getProperties(property);

    System.out.println(item);

    if (item.size() > 0) {
      return new ArrayList<Map<String, Object>>();
    }

    int registerProp = this.propertiesModel.insertProperty(property);

    if (registerProp > 0) {
      ArrayList<Map<String, Object>> registeredProp = this.propertiesModel.getProperties(property);
      return registeredProp;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar inserir a propriedade " + property + ". Por favor, tente novamente.");
  };

  public ArrayList<Map<String, Object>> updateProperty(int id, String property) throws FileNotFoundException, IOException {

    ArrayList<Map<String, Object>> item = this.propertiesModel.getProperties(id);
    
    if ( item == null) {
      return new ArrayList<Map<String, Object>>();
    }

    boolean updateProp = this.propertiesModel.updateProperty(id, property);

    if (updateProp) {
      ArrayList<Map<String, Object>> updatedProp = this.propertiesModel.getProperties(property);
      return updatedProp;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar atualizar a propriedade de id " + id + ". Por favor, tente novamente.");
  }

  public boolean removeProperty(String property) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> item = this.propertiesModel.getProperties(property);
    
    if (item == null || item.size() == 0) {
      return false;
    }

    if(
        this.propertiesModel.removeProperty(
        property,
        Integer.parseInt((String) item.get(0).get("id"))
      )
    ) {
      ArrayList<Map<String, Object>> itemRemoved = this.propertiesModel.getProperties(property);
      System.out.println(itemRemoved);
      if (itemRemoved == null || itemRemoved.size() == 0) {
        return true;
      }
    }
    throw new DBException("Ocorreu um erro ao tentar remover a propriedade  " + property + ". Por favor, tente novamente");
  }
}
