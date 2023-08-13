import java.util.ArrayList;

import weapons.WeaponsControl;
import weapons.WeaponsModel;
import weapons.WeaponsService;

// import properties.PropertiesControl;
// import properties.PropertiesModel;
// import properties.PropertiesService;

// import classes.ClassesControl;
// import classes.ClassesModel;
// import classes.ClassesService;

// import armors.ArmorsControl;
// import armors.ArmorsModel;
// import armors.ArmorsService;

// import categoryArmors.CatArmorsControl;
// import categoryArmors.CatArmorsModel;
// import categoryArmors.CatArmorsService;

public class App {
  public static void main(String[] args) throws Exception {
    ArrayList<String> properties = new ArrayList<String>();
    properties.add("alcance");
    properties.add("mão inábil");
    properties.add("mão hábil");
    WeaponsControl weaponsControl = new WeaponsControl(new WeaponsService(new WeaponsModel()));
    // weaponsControl.getAllWeapons();
    // weaponsControl.insertWeapon("Arco Grande",
    // "à distância superiores", 3, "1d10", "40/50", 2, properties);
    weaponsControl.removeWeapon("ARCO GRANDE");

    // CatArmorsControl catArmorsControl = new CatArmorsControl(new CatArmorsService(new CatArmorsModel()));
    // catArmorsControl.getAllCatArmors();
    // catArmorsControl.insertCatArmor("Plantas", "leve");
    // catArmorsControl.updateCatArmor(8, "Raízes", "leve");
    // catArmorsControl.removeCatArmor("Raízes");

    // ArmorsControl armorsControl = new ArmorsControl(new ArmorsService(new ArmorsModel()));
    // armorsControl.getAllArmors();
    // armorsControl.insertArmor("Armadura de ossos", 2, 0, 0, 2);
    // armorsControl.updateArmor(24, "Armadura de plantas", 2, 0, 0, 2);
    // armorsControl.removeArmor("Armadura de plantas");

    // weaponsControl.updateWeapon(93, "ARCO GRANDE",
    // "à distância superior", 3, "1d10", "40/50", 2);
    // weaponsControl.removeWeapon("arco grande");

    // PropertiesControl propertiesControl = new PropertiesControl(new PropertiesService(new PropertiesModel()));
    // propertiesControl.getAllProperties();
    // propertiesControl.insertProperty("new");
    // propertiesControl.updateProperty(12, "new2");
    // propertiesControl.removeProperty("new2");

    // ClassesControl classesControl = new ClassesControl(new ClassesService(new ClassesModel()));
    // classesControl.getAllClasses();
    // classesControl.insertClass("vindador", "agressor");
    // classesControl.updateClass(28, "vindador 2", "agressor 2");
    // classesControl.removeClass("vindador 2");
  }
}
