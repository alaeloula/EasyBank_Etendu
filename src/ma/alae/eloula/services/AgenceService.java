package ma.alae.eloula.services;
import ma.alae.eloula.classes.Agence;
import ma.alae.eloula.dao.Interfaces.AgenceI;
import ma.alae.eloula.dao.implementation.AgenceImp;

import java.time.LocalDate;
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
    public boolean affecterEmployeeAAgence(int idEmployee, int idAgence, LocalDate dateFin) {
        // Ajoutez ici une éventuelle validation ou vérification avant l'affectation
        return agenceDao.affecterEmployeeAAgence(idEmployee, idAgence,  dateFin);
    }
    public boolean isEmployeeCurrentlyAssigned(int idEmployee) {
        return agenceDao.isEmployeeCurrentlyAssigned(idEmployee);
    }

    public boolean muterEmployeeVersNouvelleAgence(int idEmployee, int idAgenceDestination) {
        try {
            boolean isCurrentlyAssigned = isEmployeeCurrentlyAssigned(idEmployee);

            if (isCurrentlyAssigned) {
                // Mettez à jour la date de fin de l'affectation actuelle
                boolean updateSuccess = updateDateFinEmployeeAssignment(idEmployee);

                if (updateSuccess) {
                    // Créez une nouvelle affectation vers la nouvelle agence avec la date de début actuelle
                    boolean newAssignmentSuccess = affecterEmployeeAAgence(idEmployee, idAgenceDestination,null);

                    return newAssignmentSuccess;
                }
            } else {
                // Si l'employé n'est pas actuellement affecté, créez simplement une nouvelle affectation
                return affecterEmployeeAAgence(idEmployee, idAgenceDestination,null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean updateDateFinEmployeeAssignment(int idEmployee) {
        try {
            // Mettez à jour la date de fin de l'affectation actuelle
            // Utilisez la logique appropriée pour définir la date de fin à la date actuelle

            // Exemple : Mettez à jour la date de fin de l'affectation actuelle à la date actuelle
            LocalDate dateFin = LocalDate.now();

            // Appelez votre DAO pour effectuer la mise à jour
            return agenceDao.updateDateFinEmployeeAssignment(idEmployee, dateFin);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
