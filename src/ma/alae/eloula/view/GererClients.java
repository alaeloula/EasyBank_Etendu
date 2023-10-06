package ma.alae.eloula.view;

import ma.alae.eloula.classes.Client;
import ma.alae.eloula.classes.Compte;
import ma.alae.eloula.dao.implementation.ClientImp;
import ma.alae.eloula.dao.implementation.CompteImp;
import ma.alae.eloula.helpers.Helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class GererClients {
    Scanner scanner=new Scanner(System.in);
    ClientImp cimp = new ClientImp();
    CompteImp compteImp=new CompteImp();
    public GererClients() {
        int choixClient;
        do {
            System.out.println("=== Menu de Gestion des Clients ===");
            System.out.println("1. Ajouter un client");
            System.out.println("2. Supprimer un client");
            System.out.println("3. Chercher un client par code");
            System.out.println("4. Afficher la liste des clients");
            System.out.println("5. Mettre à jour un client");
            System.out.println("6. Chercher les comptes par client");
            System.out.println("0. Retour au menu principal");
            System.out.print("Entrez votre choix : ");

            choixClient = scanner.nextInt();
            scanner.nextLine(); // Pour consommer le retour à la ligne

            switch (choixClient) {
                case 1:

                    System.out.println("Veuillez entrer les informations du nouveau client :");
                    System.out.print("Nom : ");
                    String nom = scanner.nextLine();
                    System.out.print("Prénom : ");
                    String prenom = scanner.nextLine();
                    System.out.print("Téléphone : ");
                    String tel = scanner.nextLine();
                    System.out.print("Date de naissance (AAAA-MM-JJ) : ");
                    String dateNaissanceStr = scanner.nextLine();
                    LocalDate dateNaissance = LocalDate.parse(dateNaissanceStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    System.out.print("Adresse : ");
                    String address = scanner.nextLine();

                    // Créez un nouveau client avec les informations saisies
                    Client nouveauClient = new Client();
                    nouveauClient.setNom(nom);
                    nouveauClient.setPrenom(prenom);
                    nouveauClient.setTel(tel);
                    nouveauClient.setDateNaissance(dateNaissance);
                    nouveauClient.setAddress(address);

                    Optional<Client> clientOptional = cimp.ajouterClient(nouveauClient);
                    if (clientOptional.isPresent()) {

                        System.out.println("le client a été ajouté avec succès !");
                        //System.out.println("ID de l'employé : " + newEmployee.getId());
                    } else {
                        System.err.println("Erreur lors de l'ajout de client.");
                    }
                    break;
                case 2:
                    // Supprimer un client
                    // Ajoutez ici votre logique pour supprimer un client
                    System.out.println("entreer id");

                    int rowsDeleted = cimp.supprimerClient(Helper.getInputInt(scanner));
                    if (rowsDeleted > 0) {
                        System.out.println("Client supprimé avec succès.");
                    } else {
                        System.out.println("Aucun client trouvé avec l'ID " );
                    }
                    break;
                case 3:
                    // Chercher un client par code
                    // Ajoutez ici votre logique pour chercher un client par code
                    System.out.println("entreer id");
                    int clientIdToFind = Helper.getInputInt(scanner); // Remplacez par l'ID du client que vous souhaitez trouver
                    Optional<Client> client = cimp.rechercherClient(clientIdToFind);

                    if (client.isPresent()) {
                        Client foundClient = client.get();
                        // Affichez les détails du client trouvé
                        System.out.println("Client trouvé :");
                        System.out.println("ID : " + foundClient.getId());
                        System.out.println("Nom : " + foundClient.getNom());
                        System.out.println("Prénom : " + foundClient.getPrenom());
                        System.out.println("Date de naissance : " + foundClient.getDateNaissance());
                        System.out.println("Téléphone : " + foundClient.getTel());
                        System.out.println("Adresse : " + foundClient.getAddress());
                    } else {
                        System.out.println("Aucun client trouvé avec l'ID " + clientIdToFind);
                    }
                    break;
                case 4:
                    // Afficher la liste des clients
                    // Ajoutez ici votre logique pour afficher la liste des clients
                    Optional<List<Client>> clientsOptional = cimp.findAllClients();

                    // Vérifiez si la liste des clients est présente dans l'Optional
                    if (clientsOptional.isPresent()) {
                        List<Client> clients = clientsOptional.get();
                        if (!clients.isEmpty()) {
                            System.out.println("Liste des clients :");
                            for (Client cl : clients) {
                                System.out.println("ID : " + cl.getId());
                                System.out.println("Nom : " + cl.getNom());
                                System.out.println("Prénom : " + cl.getPrenom());
                                System.out.println("Date de naissance : " + cl.getDateNaissance());
                                System.out.println("Téléphone : " + cl.getTel());
                                System.out.println("Adresse : " + cl.getAddress());
                                System.out.println("--------------------");
                            }
                        } else {
                            System.out.println("Aucun client trouvé dans la base de données.");
                        }
                    } else {
                        System.out.println("Une erreur s'est produite lors de la récupération des clients.");
                    }
                    break;
                case 5:
                    // Mettre à jour un client
                    // Ajoutez ici votre logique pour mettre à jour un client
                /*    Client clientToUpdate = cimp.findClientById(getInputAsInt(scanner));
                    //clientToUpdate.setId(1); // Remplacez par l'ID du client que vous souhaitez mettre à jour
                    clientToUpdate.setNom("NouveauNom");
                    clientToUpdate.setPrenom("NouveauPrenom");
                    clientToUpdate.setDateNaissance(LocalDate.of(1985, 3, 10)); // Nouvelle date de naissance fictive
                    clientToUpdate.setTel("987654321"); // Nouveau numéro de téléphone fictif
                    clientToUpdate.setAddress("NouvelleAdresse"); // Nouvelle adresse fictive

                    boolean updateClientSuccess = cimp.modifierClient(clientToUpdate);

                    if (updateClientSuccess) {
                        System.out.println("Le client a été mis à jour avec succès.");
                    } else {
                        System.out.println("La mise à jour du client a échoué.");
                    }*/
                    System.out.print("Veuillez entrer l'ID du client à mettre à jour : ");
                    int clientId = Helper.getInputInt(scanner);

                    // Recherchez le client par ID
                    Client clientToUpdate = cimp.findClientById(clientId);

                    if (clientToUpdate != null) {
                        // Affichez les informations actuelles du client
                        System.out.println("Informations actuelles du client :");
                        System.out.println("Nom : " + clientToUpdate.getNom());
                        System.out.println("Prénom : " + clientToUpdate.getPrenom());
                        System.out.println("Date de naissance : " + clientToUpdate.getDateNaissance());
                        System.out.println("Téléphone : " + clientToUpdate.getTel());
                        System.out.println("Adresse : " + clientToUpdate.getAddress());

                        // Demandez à l'utilisateur de saisir les nouvelles informations
                        System.out.println("Veuillez entrer les nouvelles informations :");
                        System.out.print("Nouveau nom : ");
                        String nouveauNom = scanner.nextLine();
                        System.out.print("Nouveau prénom : ");
                        String nouveauPrenom = scanner.nextLine();
                        System.out.print("Nouvelle date de naissance (AAAA-MM-JJ) : ");
                        String nouvelleDateNaissanceStr = scanner.nextLine();
                        LocalDate nouvelleDateNaissance = LocalDate.parse(nouvelleDateNaissanceStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        System.out.print("Nouveau téléphone : ");
                        String nouveauTel = scanner.nextLine();
                        System.out.print("Nouvelle adresse : ");
                        String nouvelleAdresse = scanner.nextLine();

                        // Mettez à jour les informations du client
                        clientToUpdate.setNom(nouveauNom);
                        clientToUpdate.setPrenom(nouveauPrenom);
                        clientToUpdate.setDateNaissance(nouvelleDateNaissance);
                        clientToUpdate.setTel(nouveauTel);
                        clientToUpdate.setAddress(nouvelleAdresse);

                        // Appelez la méthode de mise à jour du client
                        boolean updateClientSuccess = cimp.modifierClient(clientToUpdate);

                        if (updateClientSuccess) {
                            System.out.println("Le client a été mis à jour avec succès.");
                        } else {
                            System.out.println("La mise à jour du client a échoué.");
                        }
                    } else {
                        System.out.println("Aucun client trouvé avec l'ID " + clientId);
                    }
                    break;
                case 6:
                    // Chercher les comptes par client
                    // Ajoutez ici votre logique pour chercher les comptes d'un client
                    //compteImp.chercherComptesParClient(getInputAsInt(scanner));
                    List<Compte> comptes = compteImp.chercherComptesParClient(Helper.getInputInt(scanner));
                    if (comptes.isEmpty()){
                        System.out.println("liste vides");
                    }else{
                        for (Compte compte : comptes) {
                            System.out.println("Numéro de compte : " + compte.getNumero());
                            System.out.println("Solde : " + compte.getSolde());
                            System.out.println("Date de création : " + compte.getDateCreation());
                            System.out.println("État : " + compte.getEtat());
                            System.out.println();
                        }
                    }

                    break;
                case 0:
                    System.out.println("Retour au menu principal.");
                    break;
                default:
                    System.out.println("Choix non valide. Veuillez réessayer.");
            }
        } while (choixClient != 0);
    }
}
