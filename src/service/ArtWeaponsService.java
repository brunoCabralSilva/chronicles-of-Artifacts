package service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;

import connection.ConnectionDB;
import connection.DBException;
import model.ArtWeaponsModel;

public class ArtWeaponsService {
  ArtWeaponsModel artWeaponsModel = null;

  public ArtWeaponsService() {
    this.artWeaponsModel = new ArtWeaponsModel();
  }

  public ArrayList<Map<String, Object>> getArtifact() throws FileNotFoundException, IOException {
    return this.artWeaponsModel.getArtifact("all");
  }

  public ArrayList<Map<String, Object>> insertArtifact(
    String artifact,
    String descWeapon,
    String skill,
    String bonusAtk,
    String bonusDamage,
    double price,
    double weightWeapon,
    String typeWeapon,
    Date registerDate
  ) throws FileNotFoundException, IOException {
    
    ArrayList<Map<String, Object>> item = this.artWeaponsModel.getArtifact(artifact);

    if (item.size() > 0) {
      return new ArrayList<Map<String, Object>>();
    }

    int registerArt = this.artWeaponsModel.insertArtifact(
      artifact,
      descWeapon,
      skill,
      bonusAtk,
      bonusDamage,
      price,
      weightWeapon,
      typeWeapon,
      registerDate
    );

    if (registerArt > 0) {
      ArrayList<Map<String, Object>> registeredArt = this.artWeaponsModel.getArtifact(artifact);
      return registeredArt;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar inserir o Artefato Mágico " + artifact + ". Por favor, tente novamente.");
  };

  public ArrayList<Map<String, Object>> updateArtifact(
    int id,
    String artifact,
    String descWeapon,
    String skill,
    String bonusAtk,
    String bonusDamage,
    double price,
    double weightWeapon,
    String typeWeapon,
    Date registerDate
  ) throws FileNotFoundException, IOException {

    ArrayList<Map<String, Object>> item = this.artWeaponsModel.getArtifact(id);
    
    if ( item == null) {
      return new ArrayList<Map<String, Object>>();
    }

    boolean updateArt = this.artWeaponsModel.updateArtifact(
      id,
      artifact,
      descWeapon,
      skill,
      bonusAtk,
      bonusDamage,
      price,
      weightWeapon,
      typeWeapon,
      registerDate
    );

    if (updateArt) {
      ArrayList<Map<String, Object>> updatedArt = this.artWeaponsModel.getArtifact(artifact);
      return updatedArt;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar atualizar o Artefato Mágico de id " + id + ". Por favor, tente novamente.");
  }

  public boolean removeArtifact(String artifact) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> item = this.artWeaponsModel.getArtifact(artifact);
    
    if (item == null || item.size() == 0) {
      return false;
    }

    if(
        this.artWeaponsModel.removeArtifact(
        artifact,
        (int) item.get(0).get("id"))
      ) {
      ArrayList<Map<String, Object>> itemRemoved = this.artWeaponsModel.getArtifact(artifact);
      if (itemRemoved == null || itemRemoved.size() == 0) {
        return true;
      }
    }
    throw new DBException("Ocorreu um erro ao tentar remover o Artefato Mágico  " + artifact + ". Por favor, tente novamente");
  }
}
