package weapons;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class WeaponsControl {
  WeaponsService weaponsService = null;

  public WeaponsControl(WeaponsService weaponsService) {
    this.weaponsService = weaponsService;
  }

  public String firstLetterUp(String word) {
    return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
  }
  
  private void returnWeapons(ArrayList<Map<String, Object>> listWeapons) {
    int i = 0;
    while(i < listWeapons.size()) {
      ArrayList<?> rawProperties = (ArrayList<?>) listWeapons.get(i).get("properties");
      ArrayList<String> properties = new ArrayList<>();
      for (Object item : rawProperties) {
        if (item instanceof String) {
            properties.add((String) item);
        }
      }
      System.out.println(
        "\n"
        + "ID: "
        + listWeapons.get(i).get("id")
        + "\nArma: "
        + this.firstLetterUp((String) listWeapons.get(i).get("weapon"))
        + "\nCategoria: "
        + this.firstLetterUp((String) listWeapons.get(i).get("categoryWeapon"))
        + "\nProficiência: "
        + listWeapons.get(i).get("proficiency")
        + "\nDano: "
        + this.firstLetterUp((String) listWeapons.get(i).get("damage"))
        + "\nAlcance: "
        + this.firstLetterUp((String) listWeapons.get(i).get("rangeWeapon"))
        + "\nNúmero de mãos utilizadas: "
        + listWeapons.get(i).get("numberOfHands")
        + "\n"
        + "Propriedades: "
        + properties
      );
      i += 1;
    }
  }

  public void getWeapons() throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> allWeapons = this.weaponsService.getWeapons();
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
    int numberOfHands,
    ArrayList<String> properties
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> insertedWeapon = this.weaponsService.insertWeapon(
      weapon,
      categoryWeapon,
      proficiency,
      damage,
      rangeWeapon,
      numberOfHands,
      properties
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
    int numberOfHands,
    ArrayList<String> properties,
    boolean override
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> updatedWeapon = this.weaponsService.updateWeapon(
      id,
      weapon,
      categoryWeapon,
      proficiency,
      damage,
      rangeWeapon,
      numberOfHands,
      properties,
      override
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
