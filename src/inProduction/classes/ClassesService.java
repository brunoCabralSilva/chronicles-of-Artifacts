package inProduction.classes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import connection.ConnectionDB;
import connection.DBException;

public class ClassesService {
  ClassesModel classesModel = null;

  public ClassesService(ClassesModel classesModel) {
    this.classesModel = classesModel;
  }
  public ArrayList<Map<String, Object>> getAllClasses() throws FileNotFoundException, IOException {
    return this.classesModel.getClasses("all");
  }

  public ArrayList<Map<String, Object>> insertClass(
    String nameClass,
    String functionClass,
    ArrayList<String> weapons,
    ArrayList<String> armors
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> item = this.classesModel.getClasses(nameClass);
    if (item.size() != 0) {
      return new ArrayList<Map<String, Object>>();
    } 
    int registerClass = this.classesModel.insertClass(nameClass, functionClass, weapons, armors);
    if (registerClass > 0) {
      ArrayList<Map<String, Object>> registeredClass = this.classesModel.getClasses(nameClass);
      return registeredClass;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar inserir a classe " + nameClass + ". Por favor, tente novamente.");
  };

  public ArrayList<Map<String, Object>> updateClass(
    int id,
    String nameClass,
    String functionClass,
    ArrayList<String> weapons,
    ArrayList<String> armors,
    boolean overrideWeapons,
    boolean overrideArmors
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> item = this.classesModel.getClasses(id);
    if ( item == null) {
      return new ArrayList<Map<String, Object>>();
    }
    boolean updateClasses = this.classesModel.updateClass(
      id,
      nameClass,
      functionClass,
      weapons,
      armors,
      overrideWeapons,
      overrideArmors
    );

    if (updateClasses) {
      ArrayList<Map<String, Object>> updatedClass = this.classesModel.getClasses(nameClass);
      return updatedClass;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar atualizar a classe de id " + id + ". Por favor, tente novamente.");
  }

  // public boolean removeClass(String nameClass) throws FileNotFoundException, IOException {
  //   TreeMap<String, Object> item = this.classesModel.getOneClass(nameClass);
    
  //   if ( item == null) {
  //     return false;
  //   }

  //   if (this.classesModel.removeClass(nameClass)){
  //     TreeMap<String, Object> itemRemoved = this.classesModel.getOneClass(nameClass);
  //     if (itemRemoved == null) {
  //       return true;
  //     }
  //   }
  //   throw new DBException("Ocorreu um erro ao tentar remover a classe  " + nameClass + ". Por favor, tente novamente"); 
  // }
}
