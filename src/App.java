import inProduction.armors.ArmorsControl;
import inProduction.armors.ArmorsModel;
import inProduction.armors.ArmorsService;

public class App {
  public static void main(String[] args) throws Exception {

    ArmorsControl armorsControl = new ArmorsControl(new ArmorsService(new ArmorsModel()));
    armorsControl.getAllArmors();
    // armorsControl.insertArmor("Armadura de ossos", 2, 0, 0, 2);
    // armorsControl.updateArmor(24, "Armadura de plantas", 2, 0, 0, 2);
    // armorsControl.removeArmor("Armadura de plantas");
  }
}
