package service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;

import connection.ConnectionDB;
import connection.DBException;
import model.ArtItemsModel;

public class ArtItemsService {
  ArtItemsModel artItemsModel = null;

  public ArtItemsService() {
    this.artItemsModel = new ArtItemsModel();
  }

  public ArrayList<Map<String, Object>> getArtifact() throws FileNotFoundException, IOException {
    return this.artItemsModel.getArtifact("all");
  }

  public ArrayList<Map<String, Object>> insertArtifact(
    String artifact,
    String descItem,
    String skill,
    double price,
    double weightItem,
    String bodySection,
    Date registerDate
  ) throws FileNotFoundException, IOException {
    
    ArrayList<Map<String, Object>> item = this.artItemsModel.getArtifact(artifact);

    if (item.size() > 0) {
      return new ArrayList<Map<String, Object>>();
    }

    int registerArt = this.artItemsModel.insertArtifact(
      artifact,
      descItem,
      skill,
      price,
      weightItem,
      bodySection,
      registerDate
    );

    if (registerArt > 0) {
      ArrayList<Map<String, Object>> registeredArt = this.artItemsModel.getArtifact(artifact);
      return registeredArt;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar inserir o Artefato Mágico " + artifact + ". Por favor, tente novamente.");
  };

  public ArrayList<Map<String, Object>> updateArtifact(
    int id,
    String artifact,
    String descItem,
    String skill,
    double price,
    double weightItem,
    String bodySection,
    Date registerDate
  ) throws FileNotFoundException, IOException {

    ArrayList<Map<String, Object>> item = this.artItemsModel.getArtifact(id);
    
    if ( item == null) {
      return new ArrayList<Map<String, Object>>();
    }

    boolean updateArt = this.artItemsModel.updateArtifact(
      id,
      artifact,
      descItem,
      skill,
      price,
      weightItem,
      bodySection,
      registerDate
    );

    if (updateArt) {
      ArrayList<Map<String, Object>> updatedArt = this.artItemsModel.getArtifact(artifact);
      return updatedArt;
    }
    ConnectionDB.closeConnection();
    throw new DBException("Ocorreu um erro ao tentar atualizar o Artefato Mágico de id " + id + ". Por favor, tente novamente.");
  }

  public boolean removeArtifact(String artifact) throws FileNotFoundException, IOException {
    ArrayList<Map<String, Object>> item = this.artItemsModel.getArtifact(artifact);
    
    if (item == null || item.size() == 0) {
      return false;
    }

    if(
        this.artItemsModel.removeArtifact(
        artifact,
        (int) item.get(0).get("id"))
      ) {
      ArrayList<Map<String, Object>> itemRemoved = this.artItemsModel.getArtifact(artifact);
      if (itemRemoved == null || itemRemoved.size() == 0) {
        return true;
      }
    }
    throw new DBException("Ocorreu um erro ao tentar remover o Artefato Mágico  " + artifact + ". Por favor, tente novamente");
  }
}
