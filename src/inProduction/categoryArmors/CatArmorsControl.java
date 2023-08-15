package inProduction.categoryArmors;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class CatArmorsControl {
  CatArmorsService catArmorssService = null;

  public CatArmorsControl(CatArmorsService catArmorsService) {
    this.catArmorssService = catArmorsService;
  }

  public String firstLetterUp(String word) {
    return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
  }
  
  private void returnCatArmors(ArrayList<Map<String, String>> listWeapons) {
    int i = 0;
    while(i < listWeapons.size()) {
        System.out.println(
          "\n"
          + "ID: "
          + listWeapons.get(i).get("id")
          + "\nTipo: "
          + this.firstLetterUp(listWeapons.get(i).get("typeArmor"))
          + "\nCategoria: "
          + this.firstLetterUp(listWeapons.get(i).get("categoryArmor"))
          + "\n"
        );
      i += 1;
    }
  }

  public void getAllCatArmors() throws FileNotFoundException, IOException {
    ArrayList<Map<String, String>> catArmors = this.catArmorssService.getAllCatArmors();
    if (catArmors.size() == 0 ) {
      System.out.println("\nNão foram encontradas Categirias de Arma registradas no banco de dados!\n");
    } else {
      this.returnCatArmors(catArmors);
    }
  }

  public void insertCatArmor(
    String typeArmor,
    String categoryArmor
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, String>> insertedCatArmor = this.catArmorssService.insertCatArmor(
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
    ArrayList<Map<String, String>> updatedCatArmor = this.catArmorssService.updateCatArmor(
      id,
      typeArmor,
      categoryArmor
    );
    if (updatedCatArmor.size() == 0) { 
      System.out.println("\nA Categoria de Armadura de id " + id + " não foi encontrada na base de dados!\n");
    } else {
      System.out.println(
        "\nArma "
        + this.firstLetterUp(typeArmor)
        + " atualizada com sucesso!"
      );
      this.returnCatArmors(updatedCatArmor);
    }
  }

  public void removeCatArmor(String typeArmor) throws FileNotFoundException, IOException {
    boolean removedWeapon = this.catArmorssService.removeCatArmor(typeArmor);
    if (!removedWeapon) { 
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
