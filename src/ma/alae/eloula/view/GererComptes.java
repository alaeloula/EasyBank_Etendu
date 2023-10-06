package ma.alae.eloula.view;

import ma.alae.eloula.classes.*;
import ma.alae.eloula.dao.implementation.*;
import ma.alae.eloula.helpers.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class GererComptes {
    Scanner scanner=new Scanner(System.in);
    CompteImp compteImp =new CompteImp();
    OperationImp o1 = new OperationImp();
    CompteCourantimp cc= new CompteCourantimp();
    CompteEpargneImp ce=new CompteEpargneImp();
    public GererComptes() {
        int choixCompte;

        do {
            System.out.println("=== Menu de Gestion des Comptes ===");
            System.out.println("1. Créer un compte");
            System.out.println("2. Supprimer un compte");
            System.out.println("3. Changer l'état d'un compte");
            System.out.println("4. mise a jour un compte");
            System.out.println("5. Afficher la liste des comptes");
            System.out.println("6. find Comptes By DateCreation");
            System.out.println("0. Retour au menu principal");
            System.out.print("Entrez votre choix : ");

            choixCompte = scanner.nextInt();
            scanner.nextLine(); // Pour consommer le retour à la ligne

            switch (choixCompte) {
                case 1:
                    // Créer un compte
                    // Ajoutez ici votre logique pour créer un compte
                    creerCompte(scanner,cc,ce);
                    break;
                case 2:
                    // Supprimer un compte
                    // Ajoutez ici votre logique pour supprimer un compte
                    System.out.println("entrer numero compte");
                    if (compteImp.supprimerCompte(Helper.getInputInt(scanner)))
                        System.out.println("bien supprimmer");
                    else
                        System.out.println("erreur supprimer");
                    break;
                case 3:
                    // Changer l'état d'un compte
                    // Ajoutez ici votre logique pour changer l'état d'un compte
                    System.out.println("entrer numero compte");
                    int numeroCompteAChanger = Helper.getInputInt(scanner);

                    // Supposons que vous ayez une classe CompteImp pour les opérations de compte

                    // Changer l'état du compte
                    boolean success = compteImp.changerEtatCompte(numeroCompteAChanger);

                    if (success) {
                        System.out.println("L'état du compte a été changé avec succès.");
                    } else {
                        System.err.println("Erreur lors du changement de l'état du compte.");
                    }
                    break;
                case 4:
                    // update compte
                    System.out.println("entrer le numero de compte");
                    int choixTypeCompte = scanner.nextInt();
                    String typeDeCompte=compteImp.getTypeDeCompte(choixTypeCompte);

                    if (typeDeCompte != null) {
                        if (typeDeCompte.equals("Courant")) {

                            CompteCourant compte =cc.findCompteCourantById(6);
                            if (compte != null){
                                System.out.println("Entrez le solde du compte courant : ");
                                double soldeCourant = scanner.nextDouble();
                                compte.setDecouvert(soldeCourant);
                                System.out.println("Entrez le découvert autorisé : ");
                                double decouvertCourant = scanner.nextDouble();
                                compte.setSolde(decouvertCourant);
                                //compte.setDateCreation(LocalDate.of(1985, 3, 10));
                                cc.updateCompteCourantEtCompte(compte);
                                System.out.println("bien modifier");
                            }else {
                                System.out.println("eerrrr");
                            }




                        } else if (typeDeCompte.equals("Epargne")) {
                            System.out.println("Epargne");
                            //CompteEpargneImp cptimp= new CompteEpargneImp();
                            CompteEpargne compte =ce.findCompteEpargneById(choixTypeCompte);
                            if (compte != null){
                                System.out.println("Entrez le solde du compte épargne : ");
                                double soldeEpargne = scanner.nextDouble();
                                compte.setTauxInteret(soldeEpargne);
                                System.out.println("Entrez le taux d'intérêt : ");
                                double tauxInteret = scanner.nextDouble();
                                compte.setSolde(tauxInteret);
                                //compte.setDateCreation(LocalDate.of(1985, 3, 10));
                                if (ce.updateCompteEpargneEtCompte(compte))
                                    System.out.println("bien modifier");
                                else
                                    System.out.println("erreur de modification");
                            }else {
                                System.out.println("eerrrr");
                            }

                        }
                    }
                    else
                        System.out.println("le compte n existe pas");
                    break;
                case 5:
                    // Afficher la liste des comptes
                    // Ajoutez ici votre logique pour afficher la liste des comptes
                    Optional<List<Compte>> optionalComptes = compteImp.findAllComptes();

                    if (optionalComptes.isPresent()) {
                        List<Compte> comptess = optionalComptes.get();
                        System.out.println("Liste des comptes :");
                        for (Compte compte : comptess) {
                            System.out.println("Numéro de compte : " + compte.getNumero());
                            System.out.println("Solde : " + compte.getSolde());
                            System.out.println("Date de création : " + compte.getDateCreation());
                            System.out.println("État : " + compte.getEtat());
                            System.out.println("-----------------------");
                            System.out.println("l employee qui a creer le compte");
                            EmployeImp eimp=new EmployeImp();
                            Employee e=compte.getEmployee();
                            System.out.println(e.getNom());
                            System.out.println();
                        }
                    } else {
                        System.out.println("Aucun compte n'a été trouvé.");
                    }
                    break;
                case 6:
                    // findComptesByDateCreation
                    // findComptesByDateCreation
                    System.out.print("Entrez une date de création au format 'yyyy-MM-dd' : ");
                    String dateCreationStr = scanner.nextLine();

                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date dateCreation = dateFormat.parse(dateCreationStr);

                        // Appelez la méthode pour trouver les comptes par date de création
                        Optional<List<Compte>> comptesOptional = compteImp.findComptesByDateCreation(dateCreation);

                        if (comptesOptional.isPresent()) {
                            List<Compte> comptes = comptesOptional.get();
                            System.out.println("Comptes trouvés pour la date de création '" + dateCreationStr + "' :");
                            for (Compte compte : comptes) {
                                System.out.println("Numéro de compte : " + compte.getNumero());
                                System.out.println("Solde : " + compte.getSolde());
                                System.out.println("Date de création : " + compte.getDateCreation());
                                System.out.println("État : " + compte.getEtat());
                                System.out.println();
                            }
                        } else {
                            System.out.println("Aucun compte trouvé pour la date de création '" + dateCreationStr + "'.");
                        }
                    } catch (ParseException e) {
                        System.out.println("Format de date invalide. Utilisez le format 'yyyy-MM-dd'.");
                    }
                    break;
                case 0:
                    System.out.println("Retour au menu principal.");
                    break;
                default:
                    System.out.println("Choix non valide. Veuillez réessayer.");
            }
        } while (choixCompte != 0);
    }
    private static void creerCompte(Scanner scanner,CompteCourantimp cc,CompteEpargneImp ceimp) {
        int choixTypeCompte;

        do {
            System.out.println("=== Création de Compte ===");
            System.out.println("Choisissez le type de compte à créer :");
            System.out.println("1. Compte Courant");
            System.out.println("2. Compte Épargne");
            System.out.println("0. Retour au menu précédent");
            System.out.print("Entrez votre choix : ");

            choixTypeCompte = scanner.nextInt();
            scanner.nextLine(); // Pour consommer le retour à la ligne

            switch (choixTypeCompte) {
                case 1:
                    // Créer un compte courant
                    // Ajoutez ici votre logique pour créer un compte courant
                    System.out.print("Entrez l'ID du client : ");
                    int clientId = scanner.nextInt();
                    System.out.print("Entrez l'ID du employee : ");
                    int employeId = scanner.nextInt();

                    System.out.print("Entrez l'ID du agence : ");
                    int agenceId = Helper.getInputInt(scanner);
                    // Créez une instance de votre classe ClientImp pour rechercher le client
                    ClientImp clientImp = new ClientImp();
                    Optional<Client> clientOptional = clientImp.rechercherClient(clientId);
                    EmployeImp employeImp = new EmployeImp();

                     if (employeImp.employeeExists(employeId)){
                         if (clientOptional.isPresent()) {
                             // Le client existe, continuez à créer le compte courant
                             Client client = clientOptional.get();

                             // Création d'un objet CompteCourant
                             CompteCourant ccourant = new CompteCourant();

                             // Demandez à l'utilisateur d'entrer le solde
                             System.out.print("Entrez le solde du compte courant : ");
                             double solde = scanner.nextDouble();
                             ccourant.setSolde(solde);

                             // Demandez à l'utilisateur d'entrer le découvert
                             System.out.print("Entrez le découvert autorisé : ");
                             double decouvert = scanner.nextDouble();
                             ccourant.setDecouvert(decouvert);

                             // Demandez à l'utilisateur d'entrer la date de création
                             System.out.print("Entrez la date de création (format yyyy-MM-dd) : ");
                             String dateOfBirth = scanner.next();
                             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                             LocalDate dateOfBirthpars = LocalDate.parse(dateOfBirth, formatter);
                             ccourant.setDateCreation(dateOfBirthpars);
                             cc.creerCompteCourant(ccourant, client.getId(),employeId,agenceId);

                             System.out.println("Le compte courant a été créé avec succès pour le client : " + client.getNom() + " " + client.getPrenom());
                         }

                         else {
                             System.out.println("Aucun client trouvé avec l'ID : " + clientId);
                         }
                     }else{
                         System.out.println("aucun employer avec ce id");
                     }


                    break;
                case 2:
                    // Créer un compte épargne
                    // Ajoutez ici votre logique pour créer un compte épargne
                    System.out.print("Entrez l'ID du client : ");
                    int clId = scanner.nextInt();
                    System.out.print("Entrez l'ID du employee : ");
                    employeId = scanner.nextInt();
                    System.out.print("Entrez l'ID du agence : ");
                    agenceId = Helper.getInputInt(scanner);
                    EmployeImp empImp = new EmployeImp();
                    ClientImp clientImpp = new ClientImp();
                    Optional<Client> clientOptionale = clientImpp.rechercherClient(clId);
                    if (empImp.employeeExists(employeId)){
                        if (clientOptionale.isPresent()) {
                            Client client = clientOptionale.get();
                            CompteEpargne ce = new CompteEpargne();
                            System.out.print("Entrez le solde du compte épargne : ");
                            double solde = scanner.nextDouble();
                            ce.setSolde(solde);
                            System.out.print("Entrez le taux d'intérêt : ");
                            double tauxInteret = scanner.nextDouble();
                            ce.setTauxInteret(tauxInteret);
                            System.out.print("Entrez la date de création (format yyyy-MM-dd) : ");
                            String dateOfBirth = scanner.next();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate dateOfBirthpars = LocalDate.parse(dateOfBirth, formatter);
                            ce.setDateCreation(dateOfBirthpars);
                            ceimp.creerCompteEpargne(ce, client.getId(),employeId,agenceId);
                            System.out.println("Le compte épargne a été créé avec succès pour le client : " + client.getNom() + " " + client.getPrenom());
                        }
                        else {
                            System.out.println("Aucun client trouvé avec l'ID : " + clId);
                        }
                    }else {
                        System.out.println("pas de employée avec ce id");
                    }
                    break;
                case 0:
                    System.out.println("Retour au menu précédent.");
                    break;
                default:
                    System.out.println("Choix non valide. Veuillez réessayer.");
            }
        } while (choixTypeCompte != 0);
    }
}
