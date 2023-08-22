package control;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import service.ArtItemsService;

public class ArtItemsControl {
  ArtItemsService artItemsService = null;

  public ArtItemsControl() {
    this.artItemsService = new ArtItemsService();
  }

  public String firstLetterUp(Object object) {
    return ((String) object).substring(0, 1).toUpperCase() + ((String) object).substring(1).toLowerCase();
  }
  
  private void returnArtifact(ArrayList<Map<String, Object>> listArtifact) {
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
        + "\n\nDescrição: "
        + listArtifact.get(i).get("descItem")
        + "\n\nHabilidade: "
        + this.firstLetterUp(listArtifact.get(i).get("skill"))
        + "\n\nPreço: "
        + df.format((double) listArtifact.get(i).get("price"))
        + " PO"
        + "\nPreço de Venda: "
        + formattedNumber
        + " PO"
        + "\nPeso: "
        + df.format((double) listArtifact.get(i).get("weightItem"))
        + "kg"
        + "\nEquipado em: "
        + this.firstLetterUp(listArtifact.get(i).get("bodySection"))
        + "\nData da última atualização: "
        + formattedDate
        + "\n\n-------------------------------------\n"
      );
      i += 1;
    }
  }

  public void getArtifact() throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> allArtifacts = this.artItemsService.getArtifact();
    if (allArtifacts.size() == 0 ) {
      System.out.println("\nNão foram encontrados Artefatos Mágicos registrados no banco de dados!\n");
    } else {
      this.returnArtifact(allArtifacts);
    }
  }

  public void insertArtifact(
    String artifact,
    String descItem,
    String skill,
    double price,
    double weightItem,
    String bodySection,
    Date registerDate
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> insertedArtifact = this.artItemsService.insertArtifact(
      artifact,
      descItem,
      skill,
      price,
      weightItem,
      bodySection,
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
    String descItem,
    String skill,
    double price,
    double weightItem,
    String bodySection,
    Date registerDate
  ) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> updatedArtifact = this.artItemsService.updateArtifact(
      id,
      artifact,
      descItem,
      skill,
      price,
      weightItem,
      bodySection,
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
    boolean removedArtifact = this.artItemsService.removeArtifact(artifact);
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
