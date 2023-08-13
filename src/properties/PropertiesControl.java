package properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class PropertiesControl {
  PropertiesService propertiesService = null;

  public PropertiesControl(PropertiesService propertiesService) {
    this.propertiesService = propertiesService;
  }

  public String firstLetterUp(Object object) {
    return ((String) object).substring(0, 1).toUpperCase() + ((String) object).substring(1).toLowerCase();
  }
  
  private void returnProperties(ArrayList<Map<String, Object>> listProps) {
    int i = 0;
    while(i < listProps.size()) {
        System.out.println(
          "\n"
          + "ID: "
          + listProps.get(i).get("id")
          + "\nPropriedade: "
          + this.firstLetterUp(listProps.get(i).get("property"))
          + "\n"
        );
      i += 1;
    }
  }

  public void getAllProperties() throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> allProperties = this.propertiesService.getAllProperties();
    if (allProperties.size() == 0 ) {
      System.out.println("\nNão foram encontradas Propriedades registradas no banco de dados!\n");
    } else {
      this.returnProperties(allProperties);
    }
  }

  public void insertProperty(String property) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> insertedProp = this.propertiesService.insertProperty(property);
    if (insertedProp.size() == 0) {
      System.out.println(
        "\nPropriedade "
        + this.firstLetterUp(property)
        + " já existente na base de dados!\n"
      );
    } else {
      System.out.println(
        "\nPropriedade "
        + this.firstLetterUp(property)
        + " adicionada com sucesso!"
      );
      this.returnProperties(insertedProp);
    }
  };

  public void updateProperty(int id, String property) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> updatedProp = this.propertiesService.updateProperty(id, property);
    if (updatedProp.size() == 0) { 
      System.out.println("\nA propriedade de id " + id + " não foi encontrada na base de dados!\n");
    } else {
      System.out.println(
        "\nPropriedade "
        + this.firstLetterUp(property)
        + " atualizada com sucesso!"
      );
      this.returnProperties(updatedProp);
    }
  }

  public void removeProperty(String property) throws FileNotFoundException, IOException {
    boolean removedProperty = this.propertiesService.removeProperty(property);
    if (!removedProperty) { 
      System.out.println(
        "\nA Propriedade "
        + this.firstLetterUp(property)
        + " não foi encontrada na base de dados!\n"
      );
    } else {
      System.out.println(
        "\nPropriedade "
        + this.firstLetterUp(property)
        + " removida com sucesso!"
      );
    }
  }
}
