package control;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import service.ArmorsService;

public class ArmorsControl {
  ArmorsService armorsService = null;

  public ArmorsControl() {
    this.armorsService = new ArmorsService();
  }

  public String firstLetterUp(String word) {
    return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
  }

  private void returnArmors(ArrayList<Map<String, Object>> listArmors) {
    int i = 0; 
    while(i < listArmors.size()) {
      System.out.println(
        "\n"
        + "ID: "
        + listArmors.get(i).get("id")
        + "\nArmadura: "
        + this.firstLetterUp((String) listArmors.get(i).get("armor"))
        + "\nBônus de CA: "
        + listArmors.get(i).get("ca")
        + "\nPenalidade: "
        + listArmors.get(i).get("penalty")
        + "\nDeslocamento: "
        + listArmors.get(i).get("displacement")
        + "\nTipo: "
        + this.firstLetterUp((String) listArmors.get(i).get("type"))
        + "\nCategoria: "
        + this.firstLetterUp((String) listArmors.get(i).get("category"))
        + "\n"
      );
      i += 1;
    }
  }

  private void returnDataArmors(ArrayList<Map<String, Object>> listArmors) {
    int i = 0; 
    while(i < listArmors.size()) {
      System.out.println(
        "\nArmadura: "
        + this.firstLetterUp((String) listArmors.get(i).get("armor"))
        + "\nBônus de CA: "
        + listArmors.get(i).get("ca")
        + "\nPenalidade da Armadura: "
        + listArmors.get(i).get("penalty")
        + "\nDeslocamento: "
        + listArmors.get(i).get("displacement")
        + "\nTipo: "
        + this.firstLetterUp((String) listArmors.get(i).get("type"))
        + "\nCategoria: "
        + this.firstLetterUp((String) listArmors.get(i).get("category"))
        + "\n"
      );
      i += 1;
    }
  }

  public void getArmorsByName(String nameArmor) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> allArmors = this.armorsService.getArmorsByName(nameArmor);
    if (allArmors.size() == 0 ) {
      System.out.println("\nNão foram encontradas Armaduras registradas no banco de dados!\n");
    } else {
      this.returnDataArmors(allArmors);
    }
  }

  public void getAllArmors() throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> allArmors = this.armorsService.getAllArmors();
    if (allArmors.size() == 0 ) {
      System.out.println("\nNão foram encontradas Armaduras registradas no banco de dados!\n");
    } else {
      this.returnArmors(allArmors);
    }
  }

  public void insertArmor(
    String armor,
    int ca,
    int penalty,
    int displacement,
    String type
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> insertedArmor = this.armorsService.insertArmor(
      armor,
      ca,
      penalty,
      displacement,
      type
    );
    if (insertedArmor.size() == 0) {
      System.out.println(
        "\nArmadura "
        + this.firstLetterUp(armor)
        + " já existente na base de dados!\n"
      );
    } else {
      System.out.println(
        "\nArmadura "
        + this.firstLetterUp(armor)
        + " adicionada com sucesso!"
      );
      this.returnArmors(insertedArmor);
    }
  };

  public void updateArmor(
    int id,
    String armor,
    int ca,
    int penalty,
    int displacement,
    String type
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> updatedArmor = this.armorsService.updateArmor(
      id,
      armor,
      ca,
      penalty,
      displacement,
      type
    );
    if (updatedArmor.size() == 0) { 
      System.out.println("\nA armadura de id " + id + " não foi encontrada na base de dados!\n");
    } else {
      System.out.println(
        "\nArmadura "
        + this.firstLetterUp(armor)
        + " atualizada com sucesso!"
      );
      this.returnArmors(updatedArmor);
    }
  }

  public void removeArmor(String armor) throws FileNotFoundException, IOException {
    boolean removedArmor = this.armorsService.removeArmor(armor);
    if (!removedArmor) { 
      System.out.println(
        "\nA armadura "
        + this.firstLetterUp(armor)
        + " não foi encontrada na base de dados!\n"
      );
    } else {
      System.out.println(
        "\nArmadura "
        + this.firstLetterUp(armor)
        + " removida com sucesso!"
      );
    }
  }
}
