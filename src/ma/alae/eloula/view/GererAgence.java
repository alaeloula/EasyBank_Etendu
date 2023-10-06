package ma.alae.eloula.view;

import ma.alae.eloula.classes.Agence;
import ma.alae.eloula.classes.Operation;
import ma.alae.eloula.dao.Interfaces.AgenceI;
import ma.alae.eloula.dao.implementation.AgenceImp;
import ma.alae.eloula.dao.implementation.ClientImp;
import ma.alae.eloula.dao.implementation.CompteImp;
import ma.alae.eloula.helpers.Helper;
import ma.alae.eloula.services.AgenceService;

import java.util.Optional;
import java.util.Scanner;

public class GererAgence
{
    Scanner scanner=new Scanner(System.in);
    private AgenceI agenceDao= new AgenceImp();
    AgenceService agenceService=new AgenceService(agenceDao);


    public GererAgence() {
        int choix;

        do {
            System.out.println("=== Menu de Gestion des Agence ===");
            System.out.println("1. Creer une Agence");
            System.out.println("2. supprimer une agence ");
            System.out.println("3. Rechercher une opération par ID");

            System.out.println("0. Retour au menu principal");
            System.out.print("Entrez votre choix : ");

            choix = scanner.nextInt();
            scanner.nextLine(); // Pour consommer le retour à la ligne

            switch (choix) {
                case 1:
                    // Effectuer un versement
                    Agence agence = new Agence();
                    System.out.print("Le nomde l agence : ");
                    agence.setNom(scanner.nextLine());
                    System.out.print("Entrez l'adresse' : ");
                    agence.setAdresse(scanner.nextLine());
                    System.out.print("Entrez le nulero de telephone : ");
                    agence.setTel(scanner.nextLine());
                    Optional<Agence> resultatCreation = agenceService.createAgence(agence);

                    resultatCreation.ifPresent(ag -> {
                        System.out.println("L'agence " + ag.getNom() + " a été créée avec succès.");
                    });
                    break;
                case 2:
                    int agenceId = Helper.getInputInt(scanner);
                    boolean suppressionReussie = agenceService.deleteAgence(agenceId);

                    if (suppressionReussie) {
                        System.out.println("L'agence a été supprimée avec succès.");
                    } else {
                        System.out.println("La suppression de l'agence a échoué.");
                    }
                    break;
                case 3:

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
