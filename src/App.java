import control.ArmorsControl;
import model.ArmorsModel;
import service.ArmorsService;

public class App {
  public static void main(String[] args) throws Exception {

    ArmorsControl armorsControl = new ArmorsControl(new ArmorsService(new ArmorsModel()));
    armorsControl.getAllArmors();
    // armorsControl.insertArmor("Armadura de ossos", 2, 0, 0, "traje");
    // armorsControl.updateArmor(21, "Armadura de lombras", 2, 0, 0, "placas");
    // armorsControl.removeArmor("Armadura de lombras");
  }
}
