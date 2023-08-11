// import armors.ArmorsModel;
// import weapons.WeaponsModel;

import weapons.WeaponsControl;
import weapons.WeaponsModel;
import weapons.WeaponsService;

public class App {
  public static void main(String[] args) throws Exception {
    WeaponsControl weaponsControl = new WeaponsControl();
    // weaponsControl.getAllWeapons();
    // weaponsControl.insertWeapon("Arco Grande",
    // "à distância superiores", 3, "1d10", "40/50", 2);
    // weaponsControl.updateWeapon(86, "arco Grande",
    // "à distância superiores", 3, "1d10", "40/50", 2);
    weaponsControl.removeWeapon("arco grande");

  }
}
