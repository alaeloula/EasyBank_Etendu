package ma.alae.eloula.dao.Interfaces;

import ma.alae.eloula.classes.Agence;

import java.time.LocalDate;
import java.util.Optional;

public interface AgenceI {
    Optional<Agence> createAgence(Agence agence);
    boolean deleteAgence(int agenceId);
    public Optional<Agence> findAgenceById(int agenceId);
    boolean affecterEmployeeAAgence(int idEmployee, int idAgence, LocalDate dateFin);
    boolean isEmployeeCurrentlyAssigned(int idEmployee);

    boolean updateDateFinEmployeeAssignment(int idEmployee, LocalDate dateFin);
}
