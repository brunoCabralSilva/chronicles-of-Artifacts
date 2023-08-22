package control;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import service.CatWeaponsService;

public class CatWeaponsControl {
  CatWeaponsService catWeaponsService = null;

  public CatWeaponsControl() {
    this.catWeaponsService = new CatWeaponsService();
  }

  public String firstLetterUp(String word) {
    return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
  }
  
  private void returnCatWeapons(ArrayList<Map<String, Object>> listweapons) {
    int i = 0;
    while(i < listweapons.size()) {
      System.out.println(
        "\n"
        + "ID: "
        + listweapons.get(i).get("id")
        + "\nCategoria: "
        + this.firstLetterUp((String) listweapons.get(i).get("categoryWeapon"))
        + "\n"
        + "Armas relacionadas: "
        + listweapons.get(i).get("weapons")
      );
      i += 1;
    }
  }

  public void getAllCatWeapons() throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> catWeapons = this.catWeaponsService.getAllCatWeapons();
    if (catWeapons.size() == 0 ) {
      System.out.println("\nNão foram encontradas Categorias de Arma registradas no banco de dados!\n");
    } else {
      this.returnCatWeapons(catWeapons);
    }
  };

  public void insertCatWeapon(
    String categoryWeapon
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> insertedCatWeapon = this.catWeaponsService.insertCatWeapon(categoryWeapon);

    if (insertedCatWeapon.size() == 0) {
      System.out.println(
        "\nCategoria de Arma "
        + this.firstLetterUp(categoryWeapon)
        + " já existente na base de dados!\n"
      );
    } else {
      System.out.println(
        "\nCategoria de Arma "
        + this.firstLetterUp(categoryWeapon)
        + " adicionada com sucesso!"
      );
      this.returnCatWeapons(insertedCatWeapon);
    }
  };

  public void updateCatWeapon(
    int id,
    String categoryWeapon
    ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> updatedCatWeapon = this.catWeaponsService.updateCatWeapon(
      id,
      categoryWeapon
    );
    if (updatedCatWeapon.size() == 0) { 
      System.out.println("\nA Categoria de Arma de id " + id + " não foi encontrada na base de dados!\n");
    } else {
      System.out.println(
        "\nArma "
        + this.firstLetterUp(categoryWeapon)
        + " atualizada com sucesso!"
      );
      this.returnCatWeapons(updatedCatWeapon);
    }
  }

  public void removeCatWeapon(String categoryWeapon) throws FileNotFoundException, IOException {
    boolean removedWeapon = this.catWeaponsService.removeCatWeapon(categoryWeapon);
    if (!removedWeapon) { 
      System.out.println(
        "\nA Categoria de Arma "
        + this.firstLetterUp(categoryWeapon)
        + " não foi encontrada na base de dados!\n"
      );
    } else {
      System.out.println(
        "\nCategoria de Arma "
        + this.firstLetterUp(categoryWeapon)
        + " removida com sucesso!"
      );
    }
  }
}
