package weapons;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import connection.ConnectionDB;
import connection.DBException;

public class WeaponsService {
  WeaponsModel weaponsModel = null;

  public WeaponsService() {
    this.weaponsModel = new WeaponsModel();
  }

  private void returnWeapons(ArrayList<Map<String, String>> allWeapons) {
    int i = 0;
    while(i < allWeapons.size()) {
        System.out.println(
          "\n"
          + "ID: "
          + allWeapons.get(i).get("id")
          + "\nArma: "
          + allWeapons.get(i).get("weapon")
          + "\nCategoria: "
          + allWeapons.get(i).get("categoryWeapon")
          + "\nProficiência: "
          + allWeapons.get(i).get("proficiency")
          + "\nDano: "
          + allWeapons.get(i).get("damage")
          + "\nAlcance: "
          + allWeapons.get(i).get("rangeWeapon")
          + "\nNúmero de mãos utilizadas: "
          + allWeapons.get(i).get("numberOfHands")
          + "\n"
        );
      i += 1;
    }
  }

  public void getAllWeapons() throws FileNotFoundException, IOException {
    ArrayList<Map<String, String>> allWeapons = this.weaponsModel.getAllWeapons();
    this.returnWeapons(allWeapons);
  }

  public void insertWeapon(
    String weapon,
    String categoryWeapon,
    int proficiency,
    String damage,
    String rangeWeapon,
    int numberOfHands
  ) throws FileNotFoundException, IOException {
    
    TreeMap<String, String> item = this.weaponsModel.getWeapon(weapon, 0);

    if (item != null) {
      throw new DBException("Arma " + weapon + " já existente na base de dados!");
    } 
    boolean registerWeapon = this.weaponsModel.insertWeapon(
      weapon,
      categoryWeapon,
      proficiency,
      damage,
      rangeWeapon,
      numberOfHands
    );

    if (registerWeapon) {
      TreeMap<String, String> registeredWeapon = this.weaponsModel.getWeapon(weapon, 0);
      ArrayList<Map<String, String>> listWeapons = new ArrayList<Map<String, String>>();
      listWeapons.add(registeredWeapon);
      System.out.println("\nArma adicionada com sucesso!");
      this.returnWeapons(listWeapons);
    }
    ConnectionDB.closeConnection();
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

    TreeMap<String, String> item = this.weaponsModel.getWeapon("", id);
    
    if ( item == null) {
      throw new DBException("A arma de id" + id + " não foi encontrada na base de dados!");  
    }

    boolean updateWeapon = this.weaponsModel.updateWeapon(
      id,
      weapon,
      categoryWeapon,
      proficiency,
      damage,
      rangeWeapon,
      numberOfHands
    );

    if (updateWeapon) {
      TreeMap<String, String> updatedWeapon = this.weaponsModel.getWeapon(weapon, 0);
      ArrayList<Map<String, String>> listWeapons = new ArrayList<Map<String, String>>();
      listWeapons.add(updatedWeapon);
      System.out.println("\nArma atualizada com sucesso!");
      this.returnWeapons(listWeapons);
    }
    ConnectionDB.closeConnection();
  }

  public void removeWeapon(String weapon) throws FileNotFoundException, IOException {
    TreeMap<String, String> item = this.weaponsModel.getWeapon(weapon, 0);
    
    if ( item == null) {
      throw new DBException("A arma " + weapon + " não foi encontrada na base de dados!");  
    }

    if (this.weaponsModel.removeWeapon(weapon)){
      TreeMap<String, String> itemRemoved = this.weaponsModel.getWeapon(weapon, 0);

      if (itemRemoved == null) {
        System.out.println("A arma " + weapon + "  foi removida com sucesso");
      } else {
        throw new DBException("Ocorreu um erro ao tentar remover a arma  " + weapon + ". Por favor, tente novamente");  
      }
    }
  }
}
