package ma.alae.eloula.services;

import ma.alae.eloula.dao.Interfaces.AgenceI;
import ma.alae.eloula.dao.Interfaces.OperationI;

public class OperationService {
    private OperationI operationI;

    public OperationService(OperationI operationI) {
        this.operationI = operationI;
    }

    public boolean effectuerTransfert(int compteSourceNumero, int compteDestinationNumero, double montant, int employeId) {
        // Effectuer le retrait du compte source
        boolean trasfertReussi = operationI.effectuerTransfertAvecTransaction(compteSourceNumero,compteDestinationNumero, montant, employeId);

        if (trasfertReussi) {
            // Effectuer le versement sur le compte destination
            return true;
        }

        // Si le retrait échoue, le transfert échoue également
        return false;
    }

}
