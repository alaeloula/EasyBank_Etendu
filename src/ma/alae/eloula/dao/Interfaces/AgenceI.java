package ma.alae.eloula.dao.Interfaces;

import ma.alae.eloula.classes.Agence;

import java.util.Optional;

public interface AgenceI {
    Optional<Agence> createAgence(Agence agence);
    boolean deleteAgence(int agenceId);
}
