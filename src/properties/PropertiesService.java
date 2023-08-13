package properties;

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

  public ArrayList<Map<String, String>> getAllProperties() throws FileNotFoundException, IOException {
    return this.propertiesModel.getAllProperties();
  }

  public ArrayList<Map<String, String>> insertProperty(String property) throws FileNotFoundException, IOException {
    
    TreeMap<String, String> item = this.propertiesModel.getProperty(property);

    if (item != null) {
      return new ArrayList<Map<String, String>>();
    } 
    boolean registerProp = this.propertiesModel.insertProperty(property);

    if (registerProp) {
      TreeMap<String, String> registeredProp = this.propertiesModel.getProperty(property);
      ArrayList<Map<String, String>> listProperties = new ArrayList<Map<String, String>>();
      listProperties.add(registeredProp);
      return listProperties;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar inserir a propriedade " + property + ". Por favor, tente novamente.");
  };

  public ArrayList<Map<String, String>> updateProperty(int id, String property) throws FileNotFoundException, IOException {

    TreeMap<String, String> item = this.propertiesModel.getProperty(id);
    
    if ( item == null) {
      return new ArrayList<Map<String, String>>();
    }

    boolean updateProp = this.propertiesModel.updateProperty(id, property);

    if (updateProp) {
      TreeMap<String, String> updatedProp = this.propertiesModel.getProperty(property);
      ArrayList<Map<String, String>> listProperties = new ArrayList<Map<String, String>>();
      listProperties.add(updatedProp);
      return listProperties;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar atualizar a propriedade de id " + id + ". Por favor, tente novamente.");
  }

  public boolean removeProperty(String property) throws FileNotFoundException, IOException {
    TreeMap<String, String> item = this.propertiesModel.getProperty(property);
    
    if ( item == null) {
      return false;
    }

    if (this.propertiesModel.removeProperty(property)){
      TreeMap<String, String> itemRemoved = this.propertiesModel.getProperty(property);
      if (itemRemoved == null) {
        return true;
      }
    }
    throw new DBException("Ocorreu um erro ao tentar remover a propriedade  " + property + ". Por favor, tente novamente"); 
  }
}
