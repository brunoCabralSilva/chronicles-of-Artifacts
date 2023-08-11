package weapons;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class WeaponsService {
  WeaponsModel weaponsModel = null;

  public WeaponsService() {
    this.weaponsModel = new WeaponsModel();
  }
  public void getAllWeapons() throws FileNotFoundException, IOException {
    ArrayList<Map<String, String>> allWeapons = this.weaponsModel.getAllWeapons();
    System.out.println(allWeapons);
  }

  public void insertWeapon() {

  };

  public void updateWeapon() {

  }

  public void removeWeapon() {
    
  }
}
