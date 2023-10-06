package ma.alae.eloula.dao.Interfaces;

import ma.alae.eloula.classes.Compte;

import java.util.List;
import java.util.Optional;

public interface Acounte {
    boolean supprimerCompte(int numeroCompte);
    public List<Compte> chercherComptesParClient(int idClient);
    public Compte findCompteById(int compteId);
    boolean changerEtatCompte(int numeroCompte);
    Optional<List<Compte>> findAllComptes();
}
