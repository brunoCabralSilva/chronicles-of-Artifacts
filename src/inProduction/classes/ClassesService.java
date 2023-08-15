package inProduction.classes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import connection.ConnectionDB;
import connection.DBException;

public class ClassesService {
  ClassesModel classesModel = null;

  public ClassesService(ClassesModel classesModel) {
    this.classesModel = classesModel;
  }
  public ArrayList<Map<String, String>> getAllClasses() throws FileNotFoundException, IOException {
    return this.classesModel.getAllClasses();
  }

  public ArrayList<Map<String, String>> insertClass(String nameClass, String functionClass) throws FileNotFoundException, IOException {
    TreeMap<String, String> item = this.classesModel.getOneClass(nameClass);
    if (item != null) {
      return new ArrayList<Map<String, String>>();
    } 
    boolean registerClass = this.classesModel.insertClass(nameClass, functionClass);
    if (registerClass) {
      TreeMap<String, String> registeredClass = this.classesModel.getOneClass(nameClass);
      ArrayList<Map<String, String>> listClasses = new ArrayList<Map<String, String>>();
      listClasses.add(registeredClass);
      return listClasses;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar inserir a classe " + nameClass + ". Por favor, tente novamente.");
  };

  public ArrayList<Map<String, String>> updateClass(
    int id,
    String nameClass,
    String functionClass
  ) throws FileNotFoundException, IOException {
    TreeMap<String, String> item = this.classesModel.getOneClass(id);
    if ( item == null) {
      return new ArrayList<Map<String, String>>();
    }
    boolean updateClasses = this.classesModel.updateClass(id, nameClass, functionClass);

    if (updateClasses) {
      TreeMap<String, String> updatedClass = this.classesModel.getOneClass(nameClass);
      ArrayList<Map<String, String>> listClasses = new ArrayList<Map<String, String>>();
      listClasses.add(updatedClass);
      return listClasses;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar atualizar a classe de id " + id + ". Por favor, tente novamente.");
  }

  public boolean removeClass(String nameClass) throws FileNotFoundException, IOException {
    TreeMap<String, String> item = this.classesModel.getOneClass(nameClass);
    
    if ( item == null) {
      return false;
    }

    if (this.classesModel.removeClass(nameClass)){
      TreeMap<String, String> itemRemoved = this.classesModel.getOneClass(nameClass);
      if (itemRemoved == null) {
        return true;
      }
    }
    throw new DBException("Ocorreu um erro ao tentar remover a classe  " + nameClass + ". Por favor, tente novamente"); 
  }
}
