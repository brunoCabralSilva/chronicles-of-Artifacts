package inProduction.properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import connection.ConnectionDB;
import connection.DBException;

public class PropertiesService {
  PropertiesModel propertiesModel = null;

  public PropertiesService(PropertiesModel propertiesModel) {
    this.propertiesModel = propertiesModel;
  }

  public ArrayList<Map<String, Object>> getAllProperties() throws FileNotFoundException, IOException {
    return this.propertiesModel.getAllProperties();
  }

  public ArrayList<Map<String, Object>> insertProperty(String property) throws FileNotFoundException, IOException {
    
    TreeMap<String, Object> item = this.propertiesModel.getProperty(property);

    if (item != null) {
      return new ArrayList<Map<String, Object>>();
    } 
    int registerProp = this.propertiesModel.insertProperty(property);

    if (registerProp > 0) {
      TreeMap<String, Object> registeredProp = this.propertiesModel.getProperty(property);
      ArrayList<Map<String, Object>> listProperties = new ArrayList<Map<String, Object>>();
      listProperties.add(registeredProp);
      return listProperties;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar inserir a propriedade " + property + ". Por favor, tente novamente.");
  };

  public ArrayList<Map<String, Object>> updateProperty(int id, String property) throws FileNotFoundException, IOException {

    TreeMap<String, Object> item = this.propertiesModel.getProperty(id);
    
    if ( item == null) {
      return new ArrayList<Map<String, Object>>();
    }

    boolean updateProp = this.propertiesModel.updateProperty(id, property);

    if (updateProp) {
      TreeMap<String, Object> updatedProp = this.propertiesModel.getProperty(property);
      ArrayList<Map<String, Object>> listProperties = new ArrayList<Map<String, Object>>();
      listProperties.add(updatedProp);
      return listProperties;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar atualizar a propriedade de id " + id + ". Por favor, tente novamente.");
  }

  public boolean removeProperty(String property) throws FileNotFoundException, IOException {
    TreeMap<String, Object> item = this.propertiesModel.getProperty(property);
    
    if ( item == null) {
      return false;
    }

    if (this.propertiesModel.removeProperty(property)){
      TreeMap<String, Object> itemRemoved = this.propertiesModel.getProperty(property);
      if (itemRemoved == null) {
        return true;
      }
    }
    throw new DBException("Ocorreu um erro ao tentar remover a propriedade  " + property + ". Por favor, tente novamente"); 
  }
}
