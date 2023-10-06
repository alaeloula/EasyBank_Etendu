package ma.alae.eloula.dao.Interfaces;

import ma.alae.eloula.classes.CompteEpargne;

public interface CompteEpargneI extends Acounte{
    int creerCompteEpargne(CompteEpargne compteEpargne, int idClient,int idEmploye,int idAgence);
    CompteEpargne findCompteEpargneById(int numeroCompteEpargne);
    boolean updateCompteEpargneEtCompte(CompteEpargne compteEpargne);
}
