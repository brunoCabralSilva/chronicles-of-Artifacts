// import java.util.ArrayList;

// import control.PropertiesControl;
// import control.WeaponsControl;
// import model.PropertiesModel;
// import model.WeaponsModel;
// import service.PropertiesService;
// import service.WeaponsService;

import inProduction.classes.ClassesControl;
import inProduction.classes.ClassesModel;
import inProduction.classes.ClassesService;

public class App {
  public static void main(String[] args) throws Exception {
    ClassesControl classesControl = new ClassesControl(new ClassesService(new ClassesModel()));
    classesControl.getAllClasses();
    // classesControl.insertClass("vingador", "agressor");
    // classesControl.updateClass(29, "vingador 2", "agressor 2");
    // classesControl.removeClass("vingador 2");

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
