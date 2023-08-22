package control;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import service.ClassesService;

public class ClassesControl {
  ClassesService classesService = null;

  public ClassesControl(ClassesService classesService) {
    this.classesService = classesService;
  }

  public String firstLetterUp(String word) {
    return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
  }

  private ArrayList<String> getListOfArmors(Object list) {
    ArrayList<String> newList = new ArrayList<String>();
    if (list instanceof ArrayList<?>) {
      ArrayList<?> rawList = (ArrayList<?>) list;
      for (Object item : rawList) {
        if (item instanceof Map<?, ?>) {
          Map<?, ?> armorMap = (Map<?, ?>) item;
          Object typeArmor = armorMap.get("typeArmor");
          Object categoryArmor = armorMap.get("categoryArmor");
          if (typeArmor instanceof String && categoryArmor instanceof String) {
              newList.add(typeArmor + " (" + categoryArmor + ")");
          }
        }
      }
    }
    return newList;
  }

  private void returnClasses(ArrayList<Map<String, Object>> listClasses) {
    int i = 0;
    while(i < listClasses.size()) {
        System.out.println(
          "\n"
          + "ID: "
          + listClasses.get(i).get("id")
          + "\nClasse: "
          + this.firstLetterUp((String) listClasses.get(i).get("nameClass"))
          + "\nFunção: "
          + this.firstLetterUp((String) listClasses.get(i).get("functionClass"))
          + "\n"
          + "Categorias de Armadura: "
          + this.getListOfArmors(listClasses.get(i).get("categoryArmors"))
          + "\nArmaduras proficientes: "
          + listClasses.get(i).get("armors")
          + "\nCategorias de Arma: "
          + listClasses.get(i).get("categoryWeapons")
          + "\nArmas proficientes: "
          + listClasses.get(i).get("weapons")
        );
      i += 1;
    }
  }

  public void getAllClasses() throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> allClasses = this.classesService.getAllClasses();
    if (allClasses.size() == 0 ) {
      System.out.println("\nNão foram encontradas Classes registradas no banco de dados!\n");
    } else {
      this.returnClasses(allClasses);
    }
  }

  public void insertClass(
    String nameClass,
    String functionClass,
    ArrayList<String> weapons,
    ArrayList<String> armors
  ) throws FileNotFoundException, IOException {
    
    ArrayList<Map<String, Object>> insertedClass = this.classesService.insertClass(nameClass, functionClass, weapons, armors);

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
    String functionClass,
    ArrayList<String> weapons,
    ArrayList<String> armors,
    boolean overrideWeapons,
    boolean overrideArmors
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> updatedClass = this.classesService.updateClass(
      id,
      nameClass,
      functionClass,
      weapons,
      armors,
      overrideWeapons,
      overrideArmors
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
