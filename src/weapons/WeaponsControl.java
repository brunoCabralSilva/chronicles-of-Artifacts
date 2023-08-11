package weapons;

import java.io.FileNotFoundException;
import java.io.IOException;

public class WeaponsControl {
  WeaponsService weaponsService = null;

  public WeaponsControl() {
    this.weaponsService = new WeaponsService();
  }

  public void getAllWeapons() throws FileNotFoundException, IOException {
    this.weaponsService.getAllWeapons();
  }

  public void insertWeapon(
    String weapon,
    String categoryWeapon,
    int proficiency,
    String damage,
    String rangeWeapon,
    int numberOfHands
  ) throws FileNotFoundException, IOException {
    this.weaponsService.insertWeapon(
      weapon,
      categoryWeapon,
      proficiency,
      damage,
      rangeWeapon,
      numberOfHands
    );
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
    this.weaponsService.updateWeapon(
      id,
      weapon,
      categoryWeapon,
      proficiency,
      damage,
      rangeWeapon,
      numberOfHands
    );
  }

  public void removeWeapon(String weapon) throws FileNotFoundException, IOException {
    this.weaponsService.removeWeapon(weapon);
  }
}
