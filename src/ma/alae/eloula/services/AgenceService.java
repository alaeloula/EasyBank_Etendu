package ma.alae.eloula.services;
import ma.alae.eloula.classes.Agence;
import ma.alae.eloula.dao.Interfaces.AgenceI;
import ma.alae.eloula.dao.implementation.AgenceImp;

import java.util.Optional;

public class AgenceService {
    private AgenceI agenceDao;

    public AgenceService(AgenceI agenceDao) {
        this.agenceDao = agenceDao;
    }

    public Optional<Agence> createAgence(Agence agence) {
        // Vous pouvez ajouter ici une validation des données, des règles métier, etc.


        agence.setNom(agence.getNom());
        agence.setAdresse(agence.getAdresse());
        agence.setTel(agence.getTel());

        // Utilisez le DAO pour tenter de créer l'agence

        return agenceDao.createAgence(agence);
    }
    public boolean deleteAgence(int agenceId) {
        // Ajoutez ici une éventuelle validation ou vérification avant la suppression
        return agenceDao.deleteAgence(agenceId);
    }
    public Optional<Agence> findAgenceById(int agenceId) {
        return agenceDao.findAgenceById(agenceId);
    }

}
