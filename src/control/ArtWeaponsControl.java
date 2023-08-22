package control;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import service.ArtWeaponsService;

public class ArtWeaponsControl {
  ArtWeaponsService artWeaponsService = null;
  WeaponsControl weaponsControl = null;

  public ArtWeaponsControl() {
    this.artWeaponsService = new ArtWeaponsService();
    this.weaponsControl = new WeaponsControl();
  }

  public String firstLetterUp(Object object) {
    return ((String) object).substring(0, 1).toUpperCase() + ((String) object).substring(1).toLowerCase();
  }
  
  private void returnArtifact(ArrayList<Map<String, Object>> listArtifact) throws FileNotFoundException, IOException {
    int i = 0;
    while (i < listArtifact.size()) {
      DecimalFormat df = new DecimalFormat("#.##");
      String formattedNumber = df.format((double) listArtifact.get(i).get("price")/2);
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      String formattedDate = sdf.format(listArtifact.get(i).get("registerDate"));
      System.out.println(
        "\nId: "
        + listArtifact.get(i).get("id")
        +"\nArtefato Mágico: "
        + this.firstLetterUp(listArtifact.get(i).get("artifact"))
        + "\nClasses Relacionadas: "
        + listArtifact.get(i).get("classes")
        + "\n\nDescrição: "
        + listArtifact.get(i).get("descWeapon")
        + "\n\nHabilidade: "
        + this.firstLetterUp(listArtifact.get(i).get("skill"))
        + "\n\nBônus de Ataque: "
        + listArtifact.get(i).get("bonusAtk")
        + "\nBônus de Dano: "
        + listArtifact.get(i).get("bonusDamage")
        );
        this.weaponsControl.getWeaponsByName((String) listArtifact.get(i).get("typeWeapon"));
        System.out.println(
          "\nPreço: "
          + df.format((double) listArtifact.get(i).get("price"))
          + " PO"
          + "\nPreço de Venda: "
          + formattedNumber
          + " PO"
          + "\nPeso: "
          + df.format((double) listArtifact.get(i).get("weightWeapon"))
          + "kg"
          + "\nData da última atualização: "
          + formattedDate
          + "\n\n-------------------------------------\n"
        );
      i += 1;
    }
  }

  public void getArtifact() throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> allArtifacts = this.artWeaponsService.getArtifact();
    if (allArtifacts.size() == 0 ) {
      System.out.println("\nNão foram encontrados Artefatos Mágicos registrados no banco de dados!\n");
    } else {
      this.returnArtifact(allArtifacts);
    }
  }

  public void insertArtifact(
    String artifact,
    String descWeapon,
    String skill,
    String bonusAtk,
    String bonusDamage,
    double price,
    double weightWeapon,
    String typeWeapon,
    Date registerDate
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> insertedArtifact = this.artWeaponsService.insertArtifact(
      artifact,
      descWeapon,
      skill,
      bonusAtk,
      bonusDamage,
      price,
      weightWeapon,
      typeWeapon,
      registerDate
    );
    if (insertedArtifact.size() == 0) {
      System.out.println(
        "\nArtefato Mágico "
        + this.firstLetterUp(artifact)
        + " já existente na base de dados!\n"
      );
    } else {
      System.out.println(
        "\nArtefato Mágico "
        + this.firstLetterUp(artifact)
        + " adicionado com sucesso!"
      );
      this.returnArtifact(insertedArtifact);
    }
  };

  public void updateArtifact(
    int id,
    String artifact,
    String descWeapon,
    String skill,
    String bonusAtk,
    String bonusDamage,
    double price,
    double weightWeapon,
    String typeWeapon,
    Date registerDate
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> updatedArtifact = this.artWeaponsService.updateArtifact(
      id,
      artifact,
      descWeapon,
      skill,
      bonusAtk,
      bonusDamage,
      price,
      weightWeapon,
      typeWeapon,
      registerDate
    );
    if (updatedArtifact.size() == 0) { 
      System.out.println("\nO Artefato Mágico de id " + id + " não foi encontrado na base de dados!\n");
    } else {
      System.out.println(
        "\nArtefato Mágico "
        + this.firstLetterUp(artifact)
        + " atualizado com sucesso!"
      );
      this.returnArtifact(updatedArtifact);
    }
  }

  public void removeArtifact(String artifact) throws FileNotFoundException, IOException {
    boolean removedArtifact = this.artWeaponsService.removeArtifact(artifact);
    if (!removedArtifact) { 
      System.out.println(
        "\nO Artefato Mágico "
        + this.firstLetterUp(artifact)
        + " não foi encontrado na base de dados!\n"
      );
    } else {
      System.out.println(
        "\nArtefato Mágico "
        + this.firstLetterUp(artifact)
        + " removido com sucesso!"
      );
    }
  }
}
