package ma.alae.eloula.dao.Interfaces;

import ma.alae.eloula.classes.Demande;

import java.util.Optional;

public interface DemandeI {
    Optional<Demande> ajouter(Demande demande);
}
