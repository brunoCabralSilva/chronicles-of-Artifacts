// import java.util.ArrayList;

// import control.PropertiesControl;
// import control.WeaponsControl;
// import model.PropertiesModel;
// import model.WeaponsModel;
// import service.PropertiesService;
// import service.WeaponsService;

import java.util.ArrayList;

import control.WeaponsControl;
import inProduction.classes.ClassesControl;
import inProduction.classes.ClassesModel;
import inProduction.classes.ClassesService;
import model.WeaponsModel;
import service.WeaponsService;

public class App {
  public static void main(String[] args) throws Exception {
    ClassesControl classesControl = new ClassesControl(new ClassesService(new ClassesModel()));
  
    ArrayList<String> weapons = new ArrayList<String>();
    weapons.add("corpo a corpo superiores");
    weapons.add("à distância superiores");
    weapons.add("corpo a corpo simples");
  
    ArrayList<String> armors = new ArrayList<String>();
    armors.add("corselete");

    // classesControl.getAllClasses();
    // classesControl.insertClass("vingador", "agressor", weapons, armors);
    classesControl.updateClass(25, "vingador 2", "agressor 2", weapons, armors, true, true);
    // classesControl.removeClass("vingador");

    // WeaponsControl weaponsControl = new WeaponsControl(new WeaponsService(new WeaponsModel()));
    // ArrayList<String> list = new ArrayList<String>();
    // list.add("grande");
    // weaponsControl.getWeapons();
    // weaponsControl.insertWeapon("ArCO GRANDE", "à distância superiores", 3, "1d10", "40/50", 2, list);
    // weaponsControl.updateWeapon(38, "ArCO marromenos", "à distância superiores", 3, "1d10", "40/50", 2, list, false);
    // weaponsControl.removeWeapon("ArCO GRANDE");


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
  }
}
