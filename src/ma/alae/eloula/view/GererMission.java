package ma.alae.eloula.view;

import ma.alae.eloula.classes.Mission;
import ma.alae.eloula.classes.MissionEmploye;
import ma.alae.eloula.dao.implementation.EmployeImp;
import ma.alae.eloula.dao.implementation.MissionEmployeImp;
import ma.alae.eloula.dao.implementation.MissionImpl;
import ma.alae.eloula.helpers.Helper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class GererMission {
    Scanner scanner=new Scanner(System.in);
    MissionImpl mimp=new MissionImpl();
    MissionEmployeImp missionEmp=new MissionEmployeImp();
    EmployeImp eimp=new EmployeImp();
    public GererMission() {

        // GererMissions gererMissions = new GererMissions(missionDAO); // Remplacez par votre DAO de missions

        int choix;

        do {
            System.out.println("=== Menu de Gestion des Missions ===");
            System.out.println("1. Ajouter une mission");
            System.out.println("2. Supprimer une mission");
            System.out.println("3. Rechercher une mission par ID");
            System.out.println("4. Afficher toutes les missions");
            System.out.println("5. Affecter une mission");
            System.out.println("0. Retour au menu principal");
            System.out.print("Entrez votre choix : ");

            choix = scanner.nextInt();
            scanner.nextLine(); // Pour consommer le retour à la ligne

            switch (choix) {
                case 1:
                    // Ajouter une mission
                    Mission nouvelleMission = new Mission();
                    System.out.print("Entrez le nom de la mission : ");
                    nouvelleMission.setNom(scanner.nextLine());
                    System.out.print("Entrez la description de la mission : ");
                    nouvelleMission.setDescription(scanner.nextLine());

                    boolean ajoutReussi = mimp.ajouterMission(nouvelleMission);

                    if (ajoutReussi) {
                        System.out.println("La mission a été ajoutée avec succès.");
                    } else {
                        System.out.println("Erreur lors de l'ajout de la mission.");
                    }
                    break;
                case 2:
                    // Supprimer une mission
                    System.out.print("Entrez l'ID de la mission à supprimer : ");
                    int missionIdASupprimer = scanner.nextInt();

                    boolean suppressionReussie = mimp.supprimerMission(missionIdASupprimer);

                    if (suppressionReussie) {
                        System.out.println("La mission a été supprimée avec succès.");
                    } else {
                        System.out.println("Erreur lors de la suppression de la mission.");
                    }
                    break;
                case 3:
                    // Rechercher une mission par ID
                    System.out.print("Entrez l'ID de la mission à rechercher : ");
                    int missionIdARechercher = scanner.nextInt();

                    Optional<Mission> missionOptional = mimp.getMissionById(missionIdARechercher);

                    if (missionOptional.isPresent()) {
                        Mission missionTrouvee = missionOptional.get();
                        System.out.println("Mission trouvée :");
                        System.out.println("ID : " + missionTrouvee.getId());
                        System.out.println("Nom : " + missionTrouvee.getNom());
                        System.out.println("Description : " + missionTrouvee.getDescription());
                    } else {
                        System.out.println("Aucune mission trouvée avec l'ID " + missionIdARechercher);
                    }
                    break;
                case 4:
                    // Afficher toutes les missions
                    List<Mission> missions = mimp.findAllMissions();

                    if (!missions.isEmpty()) {
                        System.out.println("Liste des missions :");
                        for (Mission mission : missions) {
                            System.out.println("ID : " + mission.getId());
                            System.out.println("Nom : " + mission.getNom());
                            System.out.println("Description : " + mission.getDescription());
                            System.out.println();
                        }
                    } else {
                        System.out.println("Aucune mission trouvée.");
                    }
                    break;
                case 5:
                    List<Mission> miss = mimp.findAllMissions(); // Supposons que vous avez une méthode findAllMissions() dans votre DAO

                    // Afficher la liste des missions
                    System.out.println("Liste des missions disponibles :");
                    for (int i = 0; i < miss.size(); i++) {
                        Mission mission = miss.get(i);
                        System.out.println(mission.getId() + ". " + mission.getNom()); // Afficher le nom de la mission (ou d'autres informations)
                    }
                    System.out.print("Veuillez entrer le numéro de la mission choisie : ");
                    int choix_m = scanner.nextInt();
                    if (mimp.findMissionById(choix_m)!= null){

                        //Mission missionChoisie = missions.get(choix );

                        MissionEmploye memp=new MissionEmploye();
                        System.out.println("id employer");
                        memp.setE(eimp.findEmployeeById(Helper.getInputInt(scanner)));
                        memp.setM(mimp.findMissionById(choix_m));
                        memp.setDateFin(LocalDate.of(2024, 3, 10));
                        // Faites quelque chose avec la mission choisie, par exemple, affectez-la à un employé
                        missionEmp.assignMissionToEmployee(memp);
                    } else {
                        System.out.println("Choix invalide. Veuillez sélectionner un numéro de mission valide.");
                    }
                case 0:
                    System.out.println("Retour au menu principal.");
                    break;
                default:
                    System.out.println("Choix non valide. Veuillez réessayer.");
            }
        } while (choix != 0);
    }
}
