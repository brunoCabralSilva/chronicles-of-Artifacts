package service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;

import connection.ConnectionDB;
import connection.DBException;
import model.ArtArmorsModel;

public class ArtArmorsService {
  ArtArmorsModel artArmorsModel = null;

  public ArtArmorsService() {
    this.artArmorsModel = new ArtArmorsModel();
  }

  public ArrayList<Map<String, Object>> getArtifact() throws FileNotFoundException, IOException {
    return this.artArmorsModel.getArtifact("all");
  }

  public ArrayList<Map<String, Object>> insertArtifact(
    String artifact,
    String descArmor,
    String skill,
    String bonusDefense,
    double price,
    double weightArmor,
    String typeArmor,
    Date registerDate
  ) throws FileNotFoundException, IOException {
    
    ArrayList<Map<String, Object>> item = this.artArmorsModel.getArtifact(artifact);

    if (item.size() > 0) {
      return new ArrayList<Map<String, Object>>();
    }

    int registerArt = this.artArmorsModel.insertArtifact(
      artifact,
      descArmor,
      skill,
      bonusDefense,
      price,
      weightArmor,
      typeArmor,
      registerDate
    );

    if (registerArt > 0) {
      ArrayList<Map<String, Object>> registeredArt = this.artArmorsModel.getArtifact(artifact);
      return registeredArt;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar inserir o Artefato Mágico " + artifact + ". Por favor, tente novamente.");
  };

  public ArrayList<Map<String, Object>> updateArtifact(
    int id,
    String artifact,
    String descArmor,
    String skill,
    String bonusDefense,
    double price,
    double weightArmor,
    String typeArmor,
    Date registerDate
  ) throws FileNotFoundException, IOException {

    ArrayList<Map<String, Object>> item = this.artArmorsModel.getArtifact(id);
    
    if ( item == null) {
      return new ArrayList<Map<String, Object>>();
    }

    boolean updateArt = this.artArmorsModel.updateArtifact(
      id,
      artifact,
      descArmor,
      skill,
      bonusDefense,
      price,
      weightArmor,
      typeArmor,
      registerDate
    );

    if (updateArt) {
      ArrayList<Map<String, Object>> updatedArt = this.artArmorsModel.getArtifact(artifact);
      return updatedArt;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar atualizar o Artefato Mágico de id " + id + ". Por favor, tente novamente.");
  }

  public boolean removeArtifact(String artifact) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> item = this.artArmorsModel.getArtifact(artifact);
    
    if (item == null || item.size() == 0) {
      return false;
    }

    if(
        this.artArmorsModel.removeArtifact(
        artifact,
        (int) item.get(0).get("id"))
      ) {
      ArrayList<Map<String, Object>> itemRemoved = this.artArmorsModel.getArtifact(artifact);
      if (itemRemoved == null || itemRemoved.size() == 0) {
        return true;
      }
    }
    throw new DBException("Ocorreu um erro ao tentar remover o Artefato Mágico  " + artifact + ". Por favor, tente novamente");
  }
}
