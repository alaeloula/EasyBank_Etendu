package ma.alae.eloula.view;

import ma.alae.eloula.classes.Operation;
import ma.alae.eloula.dao.implementation.CompteImp;
import ma.alae.eloula.dao.implementation.OperationImp;

import java.util.Optional;
import java.util.Scanner;

public class MenuGestionOperations {
    Scanner scanner=new Scanner(System.in);
    //CompteImp cimp= new CompteImp();
    OperationImp o1=new OperationImp();
    public MenuGestionOperations() {
        // Scanner scanner = new Scanner(System.in);
        //GererOperations gererOperations = new GererOperations(operationDAO); // Remplacez par votre DAO d'opérations

        int choix;

        do {
            System.out.println("=== Menu de Gestion des Opérations ===");
            System.out.println("1. Effectuer un versement");
            System.out.println("2. Effectuer un retrait");
            System.out.println("3. Rechercher une opération par ID");

            System.out.println("0. Retour au menu principal");
            System.out.print("Entrez votre choix : ");

            choix = scanner.nextInt();
            scanner.nextLine(); // Pour consommer le retour à la ligne

            switch (choix) {
                case 1:
                    // Effectuer un versement
                    System.out.print("vous etes l employee avec l id? : ");
                    int employe_id = scanner.nextInt();
                    System.out.print("Entrez le numéro de compte : ");
                    int numeroCompteVersement = scanner.nextInt();
                    System.out.print("Entrez le montant à verser : ");
                    double montantVersement = scanner.nextDouble();

                    boolean versementReussi = o1.effectuerVersement(numeroCompteVersement, montantVersement,employe_id);

                    if (versementReussi) {
                        System.out.println("Le versement a été effectué avec succès.");
                    } else {
                        System.out.println("Erreur lors de l'effectuation du versement.");
                    }
                    break;
                case 2:
                    // Effectuer un retrait
                    System.out.print("vous etes l employee avec l id? : ");
                    employe_id = scanner.nextInt();
                    System.out.print("Entrez le numéro de compte : ");
                    int numeroCompteRetrait = scanner.nextInt();
                    System.out.print("Entrez le montant à retirer : ");
                    double montantRetrait = scanner.nextDouble();

                    boolean retraitReussi = o1.effectuerRetrait(numeroCompteRetrait, montantRetrait,employe_id);

                    if (retraitReussi) {
                        System.out.println("Le retrait a été effectué avec succès.");
                    } else {
                        System.out.println("Erreur lors de l'effectuation du retrait.");
                    }
                    break;
                case 3:
                    // Rechercher une opération par ID
                    System.out.print("Entrez l'ID de l'opération à rechercher : ");
                    int operationIdARechercher = scanner.nextInt();

                    Optional<Operation> operationOptional = o1.findOperationById(operationIdARechercher);

                    if (operationOptional.isPresent()) {
                        Operation operationTrouvee = operationOptional.get();
                        System.out.println("Opération trouvée :");
                        System.out.println("ID : " + operationTrouvee.getId());
                        System.out.println("Type : " + operationTrouvee.getType());
                        System.out.println("Date de création : " + operationTrouvee.getDateCreation());
                        System.out.println("Montant : " + operationTrouvee.getMontant());
                    } else {
                        System.out.println("Aucune opération trouvée avec l'ID " + operationIdARechercher);
                    }
                    break;

                case 0:
                    System.out.println("Retour au menu principal.");
                    break;
                default:
                    System.out.println("Choix non valide. Veuillez réessayer.");
            }
        } while (choix != 0);
    }
}
