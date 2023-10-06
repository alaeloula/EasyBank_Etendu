package ma.alae.eloula.dao.Interfaces;

import ma.alae.eloula.classes.Operation;

import java.util.Optional;

public interface OperationI {
    boolean effectuerVersement(int compteNumero, double montant,int employeId);
    boolean effectuerRetrait(int compteNumero, double montant,int employeId);
    boolean supprimerOperation(int operationId);
    Optional<Operation> findOperationById(int operationId);
}
