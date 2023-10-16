package ma.alae.eloula.view;

import java.util.Scanner;

public class MainPrincipale {
    Scanner scanner=new Scanner(System.in);
    public MainPrincipale() {
        int ch;

        do {
            System.out.println("=== Menu de Gestion de Comptes Bancaires ===");
            System.out.println("1. Gérer les employés");
            System.out.println("2. Gérer les clients");
            System.out.println("3. Gérer les comptes");
            System.out.println("4. Gérer les missions");
            System.out.println("5. Gérer les operation");
            System.out.println("6. Gérer les Agence");
            System.out.println("7. simulation des credit");

            System.out.println("0. Quitter");
            System.out.print("Entrez votre choix : ");

            ch = scanner.nextInt();
            scanner.nextLine(); // Pour consommer le retour à la ligne

            switch (ch) {
                case 1:
                    // Menu de gestion des employés
                    //gererEmployes(scanner, eimp);
                    new GererEmployes();
                    break;
                case 2:
                    // Menu de gestion des clients
                    //gererClients(scanner, cimp,compteImp);
                    new GererClients();
                    break;
                case 3:
                    // Menu de gestion des comptes
                    //gererComptes(scanner,compteImp, o1, ccimp, ceimp);
                    new GererComptes();
                    break;
                case 4:
                    // Menu de gestion des missions
                    //gererMissions(scanner, mimp, missionEmp, eimp);
                    new GererMission();
                    break;
                case 5:
                    //menuGestionOperations(scanner,compteImp,o1);
                    new MenuGestionOperations();
                    break;
                case 6:
                        new GererAgence();
                        break;
                case 7:
                    new GererSimulation();
                case 0:
                    System.out.println("Merci d'avoir utilisé notre application !");
                    break;

                default:
                    System.out.println("Choix non valide. Veuillez réessayer.");
            }
        } while (ch != 0);

    }
}
