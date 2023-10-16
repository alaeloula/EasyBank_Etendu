package ma.alae.eloula.view;

import ma.alae.eloula.classes.Client;
import ma.alae.eloula.classes.Demande;
import ma.alae.eloula.classes.Simulation;
import ma.alae.eloula.dao.implementation.DemandeDao;
import ma.alae.eloula.helpers.Helper;
import ma.alae.eloula.services.DemandeService;

import java.util.Scanner;

public class GererSimulation {
    Scanner sc= new Scanner(System.in);
    DemandeDao demandeDao= new DemandeDao();
    DemandeService service = new DemandeService(demandeDao);
    Demande demande;

    public GererSimulation() {
        simuler();
    }

    public void simuler(){
        System.out.printf("le Capitale : ");
        Double capitale=sc.nextDouble();
        System.out.printf("le Taux : ");
        Double taux=sc.nextDouble();
        System.out.printf("le Nombre mensualité : ");
        int nombremensualité=sc.nextInt();
        double tauxMensuel = (taux / 12) / 100;
        double mensualite = (capitale * tauxMensuel * Math.pow(1 + tauxMensuel, nombremensualité))
                / (Math.pow(1 + tauxMensuel, nombremensualité) - 1);
        System.out.printf("La mensualité est d'environ %.2f euros par mois.%n", mensualite);
        System.out.printf("1-Confirmer\n2-abondonner");
        int choix = sc.nextInt();
        if(choix==1){
            demande= new Demande();
            Simulation s= new Simulation(capitale,taux,nombremensualité);
            demande.setSimulation(s);
            System.out.printf("entrer le code d'un client : ");
            Client c= new Client();
            c.setId(Helper.getInputInt(sc));
            demande.setClient(c);
            System.out.printf("Remarque : ");
            demande.setRemarques(sc.nextLine());
            boolean optDm=service.ajouter(demande);
            if(optDm)
            {
                System.out.printf("Le demande a ete bien traite et enregistré\n");
            }
        }
    }
}
