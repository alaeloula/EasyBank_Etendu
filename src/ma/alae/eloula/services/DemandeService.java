package ma.alae.eloula.services;

import ma.alae.eloula.classes.Demande;
import ma.alae.eloula.dao.Interfaces.DemandeI;
import ma.alae.eloula.dao.implementation.DemandeDao;

import java.util.Optional;

public class DemandeService {
    private DemandeI demandeI;

    public DemandeService(DemandeI demandeI) {
        this.demandeI = demandeI;
    }
    public boolean ajouter(Demande demande) {
        Optional<Demande> optdemande=demandeI.ajouter(demande);
        return optdemande.isPresent();
    }
}
