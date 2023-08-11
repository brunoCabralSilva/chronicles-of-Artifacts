// import armors.ArmorsModel;
// import weapons.WeaponsModel;

import weapons.WeaponsService;

public class App {
  public static void main(String[] args) throws Exception {
    // ClassesModel classesModel = new ClassesModel();
    // classesModel.getAllClasses();

    // PropertiesModel propertiesModel = new PropertiesModel();
    // propertiesModel.removeProperty("jegue");

    WeaponsService weaponsService = new WeaponsService();
    weaponsService.getAllWeapons();

    // ArmorsModel armorsModel = new ArmorsModel();
    // armorsModel.removeArmor("mopa");
  }
}
