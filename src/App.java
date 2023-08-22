import control.ArtArmorsControl;
import control.ArtWeaponsControl;
// import model.ArtArmorsModel;
// import model.ArtItemsModel;
// import model.ArtWeaponsModel;
// import service.ArtArmorsService;
// import service.ArtItemsService;
// import service.ArtWeaponsService;

public class App {
  public static void main(String[] args) throws Exception {
    
    java.util.Date utilDate = new java.util.Date();
    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
    
    ArtWeaponsControl artWeaponsControl = new ArtWeaponsControl();
    artWeaponsControl.getArtifact();
    // artWeaponsControl.insertArtifact("Arco do fogo Sagrado", "Espada de Fogo vinda do quinto dos infernos", "Concede uma ação a mais caso seja ferido por fogo", "1d4 de dano flamejante", "+2", 5000, 10, "Arco curto", sqlDate);
    // artWeaponsControl.updateArtifact(6, "Espada do Fogo", "Espada de Fogo vinda do quinto dos infernos", "Concede uma ação a mais caso seja ferido por fogo", "1d4 de dano flamejante", "+2", 5000, 10, "azagaia", sqlDate);
    // artWeaponsControl.removeArtifact("Arco do fogo Sagrado");
    
    // ArtArmorsControl artArmorsControl = new ArtArmorsControl();

    // artArmorsControl.getArtifact();
    // artArmorsControl.insertArtifact(
    //   "Escudo do Dragão negro",
    //   "Escudo forjado no estômago de um dragão negro",
    //   "+5 contra dragões",
    //   "+2",
    //   8000,
    //   2548,
    //   "escudo leve",
    //   sqlDate
    // );
    // artArmorsControl.updateArtifact(
      // 1,
      // "Escudo do Dragão branco",
      // "Escudo forjado no estômago de um dragão negro",
      // "+5 contra dragões",
      // "+2",
      // 8000,
      // 2548,
      // "escudo leve",
      // sqlDate
    // );
    // artArmorsControl.removeArtifact("Escudo do Dragão branco");
  }
}
