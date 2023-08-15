package inProduction.classes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class ClassesControl {
  ClassesService classesService = null;

  public ClassesControl(ClassesService classesService) {
    this.classesService = classesService;
  }

  public String firstLetterUp(String word) {
    return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
  }
  
  private void returnClasses(ArrayList<Map<String, String>> listClasses) {
    int i = 0;
    while(i < listClasses.size()) {
        System.out.println(
          "\n"
          + "ID: "
          + listClasses.get(i).get("id")
          + "\nClasse: "
          + this.firstLetterUp(listClasses.get(i).get("nameClass"))
          + "\nFunção: "
          + this.firstLetterUp(listClasses.get(i).get("functionClass"))
          + "\n"
        );
      i += 1;
    }
  }

  public void getAllClasses() throws FileNotFoundException, IOException {
    ArrayList<Map<String, String>> allClasses = this.classesService.getAllClasses();
    if (allClasses.size() == 0 ) {
      System.out.println("\nNão foram encontradas Classes registradas no banco de dados!\n");
    } else {
      this.returnClasses(allClasses);
    }
  }

  public void insertClass(
    String nameClass,
    String functionClass
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, String>> insertedClass = this.classesService.insertClass(nameClass, functionClass);

    if (insertedClass.size() == 0) {
      System.out.println(
        "\nClasse "
        + this.firstLetterUp(nameClass)
        + " já existente na base de dados!\n"
      );
    } else {
      System.out.println(
        "\nClasse "
        + this.firstLetterUp(nameClass)
        + " adicionada com sucesso!"
      );
      this.returnClasses(insertedClass);
    }
  };

  public void updateClass(
    int id,
    String nameClass,
    String functionClass
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, String>> updatedClass = this.classesService.updateClass(
      id,
      nameClass,
      functionClass
    );
    if (updatedClass.size() == 0) { 
      System.out.println("\nA classe de id " + id + " não foi encontrada na base de dados!\n");
    } else {
      System.out.println(
        "\nClasse "
        + this.firstLetterUp(nameClass)
        + " atualizada com sucesso!"
      );
      this.returnClasses(updatedClass);
    }
  }

  public void removeClass(String nameClass) throws FileNotFoundException, IOException {
    boolean removedClass = this.classesService.removeClass(nameClass);
    if (!removedClass) { 
      System.out.println(
        "\nA classe "
        + this.firstLetterUp(nameClass)
        + " não foi encontrada na base de dados!\n"
      );
    } else {
      System.out.println(
        "\nClasse "
        + this.firstLetterUp(nameClass)
        + " removida com sucesso!"
      );
    }
  }
}
