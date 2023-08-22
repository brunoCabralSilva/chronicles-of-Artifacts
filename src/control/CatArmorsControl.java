package control;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import service.CatArmorsService;

public class CatArmorsControl {
  CatArmorsService catArmorssService = null;

  public CatArmorsControl(CatArmorsService catArmorsService) {
    this.catArmorssService = catArmorsService;
  }

  public String firstLetterUp(String word) {
    return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
  }
  
  private void returnCatArmors(ArrayList<Map<String, Object>> listArmors) {
    int i = 0;
    while(i < listArmors.size()) {
      System.out.println(
        "\n"
        + "ID: "
        + listArmors.get(i).get("id")
        + "\nTipo: "
        + this.firstLetterUp((String) listArmors.get(i).get("typeArmor"))
        + "\nCategoria: "
        + this.firstLetterUp((String) listArmors.get(i).get("categoryArmor"))
        + "\n"
        + "Armaduras relacionadas: "
        + listArmors.get(i).get("armors")
      );
      i += 1;
    }
  }

  public void getAllCatArmors() throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> catArmors = this.catArmorssService.getAllCatArmors();
    if (catArmors.size() == 0 ) {
      System.out.println("\nNão foram encontradas Categorias de Armadura registradas no banco de dados!\n");
    } else {
      this.returnCatArmors(catArmors);
    }
  };

  public void insertCatArmor(
    String typeArmor,
    String categoryArmor
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> insertedCatArmor = this.catArmorssService.insertCatArmor(
      typeArmor,
      categoryArmor
    );

    if (insertedCatArmor.size() == 0) {
      System.out.println(
        "\nCategoria de Armadura "
        + this.firstLetterUp(typeArmor)
        + " já existente na base de dados!\n"
      );
    } else {
      System.out.println(
        "\nCategoria de Armadura "
        + this.firstLetterUp(typeArmor)
        + " adicionada com sucesso!"
      );
      this.returnCatArmors(insertedCatArmor);
    }
  };

  public void updateCatArmor(
    int id,
    String typeArmor,
    String categoryArmor
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> updatedCatArmor = this.catArmorssService.updateCatArmor(
      id,
      typeArmor,
      categoryArmor
    );
    if (updatedCatArmor.size() == 0) { 
      System.out.println("\nA Categoria de Armadura de id " + id + " não foi encontrada na base de dados!\n");
    } else {
      System.out.println(
        "\nArmadura "
        + this.firstLetterUp(typeArmor)
        + " atualizada com sucesso!"
      );
      this.returnCatArmors(updatedCatArmor);
    }
  }

  public void removeCatArmor(String typeArmor) throws FileNotFoundException, IOException {
    boolean removedArmor = this.catArmorssService.removeCatArmor(typeArmor);
    if (!removedArmor) { 
      System.out.println(
        "\nA Categoria de Armadura "
        + this.firstLetterUp(typeArmor)
        + " não foi encontrada na base de dados!\n"
      );
    } else {
      System.out.println(
        "\nCategoria de Armadura "
        + this.firstLetterUp(typeArmor)
        + " removida com sucesso!"
      );
    }
  }
}
