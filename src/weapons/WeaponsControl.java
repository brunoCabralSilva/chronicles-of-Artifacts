package weapons;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class WeaponsControl {
  WeaponsService weaponsService = null;

  public WeaponsControl() {
    this.weaponsService = new WeaponsService();
  }

  public String firstLetterUp(String word) {
  return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
  }
  
  private void returnWeapons(ArrayList<Map<String, String>> listWeapons) {
    int i = 0;
    
    while(i < listWeapons.size()) {
        System.out.println(
          "\n"
          + "ID: "
          + listWeapons.get(i).get("id")
          + "\nArma: "
          + this.firstLetterUp(listWeapons.get(i).get("weapon"))
          + "\nCategoria: "
          + this.firstLetterUp(listWeapons.get(i).get("categoryWeapon"))
          + "\nProficiência: "
          + listWeapons.get(i).get("proficiency")
          + "\nDano: "
          + this.firstLetterUp(listWeapons.get(i).get("damage"))
          + "\nAlcance: "
          + this.firstLetterUp(listWeapons.get(i).get("rangeWeapon"))
          + "\nNúmero de mãos utilizadas: "
          + listWeapons.get(i).get("numberOfHands")
          + "\n"
        );
      i += 1;
    }
  }

  public void getAllWeapons() throws FileNotFoundException, IOException {
    ArrayList<Map<String, String>> allWeapons = this.weaponsService.getAllWeapons();
    if (allWeapons.size() == 0 ) {
      System.out.println("\nNão foram encontradas Armas registradas no banco de dados!\n");
    } else {
      this.returnWeapons(allWeapons);
    }
  }

  public void insertWeapon(
    String weapon,
    String categoryWeapon,
    int proficiency,
    String damage,
    String rangeWeapon,
    int numberOfHands
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, String>> insertedWeapon = this.weaponsService.insertWeapon(
      weapon,
      categoryWeapon,
      proficiency,
      damage,
      rangeWeapon,
      numberOfHands
    );

    if (insertedWeapon.size() == 0) {
      System.out.println(
        "\nArma "
        + this.firstLetterUp(weapon)
        + " já existente na base de dados!\n"
      );
    } else {
      System.out.println(
        "\nArma "
        + this.firstLetterUp(weapon)
        + " adicionada com sucesso!"
      );
      this.returnWeapons(insertedWeapon);
    }
  };

  public void updateWeapon(
    int id,
    String weapon,
    String categoryWeapon,
    int proficiency,
    String damage,
    String rangeWeapon,
    int numberOfHands
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, String>> updatedWeapon = this.weaponsService.updateWeapon(
      id,
      weapon,
      categoryWeapon,
      proficiency,
      damage,
      rangeWeapon,
      numberOfHands
    );
    if (updatedWeapon.size() == 0) { 
      System.out.println("\nA arma de id " + id + " não foi encontrada na base de dados!\n");
    } else {
      System.out.println(
        "\nArma "
        + this.firstLetterUp(weapon)
        + " atualizada com sucesso!"
      );
      this.returnWeapons(updatedWeapon);
    }
  }

  public void removeWeapon(String weapon) throws FileNotFoundException, IOException {
    boolean removedWeapon = this.weaponsService.removeWeapon(weapon);
    if (!removedWeapon) { 
      System.out.println(
        "\nA arma "
        + this.firstLetterUp(weapon)
        + " não foi encontrada na base de dados!\n"
      );
    } else {
      System.out.println(
        "\nArma "
        + this.firstLetterUp(weapon)
        + " removida com sucesso!"
      );
    }
  }
}
