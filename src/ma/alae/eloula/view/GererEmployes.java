package ma.alae.eloula.view;

import ma.alae.eloula.classes.Employee;
import ma.alae.eloula.dao.implementation.EmployeImp;
import ma.alae.eloula.helpers.Helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class GererEmployes {
    Scanner scanner=new Scanner(System.in);
    //Employee e1 =new Employee();
    EmployeImp eimp=new EmployeImp();
    public GererEmployes() {
        int choixEmploye;

        do {
            System.out.println("=== Menu de Gestion des Employés ===");
            System.out.println("1. Ajouter un employé");
            System.out.println("2. Supprimer un employé");
            System.out.println("3. Chercher un employé par ID");
            System.out.println("4. Afficher la liste des employés");
            System.out.println("5. Mettre à jour un employé");
            System.out.println("0. Retour au menu principal");
            System.out.print("Entrez votre choix : ");

            choixEmploye = scanner.nextInt();
            scanner.nextLine(); // Pour consommer le retour à la ligne

            switch (choixEmploye) {
                case 1:
                    // Ajouter un employé
                    // Ajoutez ici votre logique pour ajouter un employé
                   /* Employee e1 =new Employee();
                    e1.setNom("USER");
                    e1.setPrenom("USER");
                    e1.setTel("06000000");
                    e1.setDateNaissance(LocalDate.parse("2020-03-03"));
                    e1.setDateRecrutement(LocalDate.parse("2022-03-03"));
                    e1.setEmail("user@user.com");
                    eimp.ajouterEmployee(e1);*/
                    //Scanner scanner = new Scanner(System.in);
                    //1EmployeImp eimp = new EmployeImp();

                    System.out.println("Création d'un nouvel employé :");

                    System.out.print("Entrez le nom de l'employé : ");
                    String nom = scanner.nextLine();

                    System.out.print("Entrez le prénom de l'employé : ");
                    String prenom = scanner.nextLine();

                    System.out.print("Entrez le numéro de téléphone : ");
                    String tel = scanner.nextLine();

                    System.out.print("Entrez la date de naissance (AAAA-MM-JJ) : ");
                    String dateNaissanceStr = scanner.nextLine();
                    LocalDate dateNaissance = LocalDate.parse(dateNaissanceStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    System.out.print("Entrez la date de recrutement (AAAA-MM-JJ) : ");
                    String dateRecrutementStr = scanner.nextLine();
                    LocalDate dateRecrutement = LocalDate.parse(dateRecrutementStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    System.out.print("Entrez l'email de l'employé : ");
                    String email = scanner.nextLine();

                    Employee nouvelEmployee = new Employee();
                    nouvelEmployee.setNom(nom);
                    nouvelEmployee.setPrenom(prenom);
                    nouvelEmployee.setTel(tel);
                    nouvelEmployee.setDateNaissance(dateNaissance);
                    nouvelEmployee.setDateRecrutement(dateRecrutement);
                    nouvelEmployee.setEmail(email);

                    Optional<Employee> employeeOptional = eimp.ajouterEmployee(nouvelEmployee);
                    if (employeeOptional.isPresent()) {
                        Employee newEmployee = employeeOptional.get();
                        System.out.println("L'employé a été ajouté avec succès !");
                        //System.out.println("ID de l'employé : " + newEmployee.getId());
                    } else {
                        System.err.println("Erreur lors de l'ajout de l'employé.");
                    }
                    break;
                case 2:
                    // Supprimer un employé
                    // Ajoutez ici votre logique pour supprimer un employé
                    System.out.println("entrer l id de l emplyer");
                    if (eimp.SupprimerEmpl(Helper.getInputInt(scanner))==1)
                    {
                        System.out.println("employee bien supprimer");
                    }
                    else
                    {
                        System.out.println("error");
                    }
                    break;
                case 3:
                    // Chercher un employé par ID
                    // Ajoutez ici votre logique pour chercher un employé par ID
                    System.out.println("entreer id");
                    eimp.getEmployeeById(Helper.getInputInt(scanner)).ifPresent(employee -> {
                        // Affichez les détails de l'employé trouvé
                        System.out.println("Employé trouvé :");
                        System.out.println("ID : " + employee.getId());
                        System.out.println("Nom : " + employee.getNom());
                        System.out.println("Prénom : " + employee.getPrenom());
                        System.out.println("Date de naissance : " + employee.getDateNaissance());
                        System.out.println("Téléphone : " + employee.getTel());
                        System.out.println("Email : " + employee.getEmail());
                        System.out.println("Date de recrutement : " + employee.getDateRecrutement());
                    });

                    break;
                case 4:
                    // Afficher la liste des employés
                    // Ajoutez ici votre logique pour afficher la liste des employés
                    Optional<List<Employee>> employeesOptional = eimp.findAllEmployees();

                    if (employeesOptional.isPresent()) {
                        List<Employee> employees = employeesOptional.get();

                        if (!employees.isEmpty()) {
                            System.out.println("Liste des employés :");
                            for (Employee employee : employees) {
                                System.out.println("ID : " + employee.getId());
                                System.out.println("Nom : " + employee.getNom());
                                System.out.println("Prénom : " + employee.getPrenom());
                                System.out.println("Date de recrutement : " + employee.getDateRecrutement());
                                System.out.println("Email : " + employee.getEmail());
                                System.out.println();
                            }
                        } else {
                            System.out.println("Aucun employé trouvé.");
                        }
                    } else {
                        System.out.println("Erreur lors de la récupération des employés.");
                    }
                    break;
                case 5:
                    // Mettre à jour un employé
                    // Ajoutez ici votre logique pour mettre à jour un employé
                   /* System.out.println("entreer id");
                    Employee employeeToUpdate = eimp.findEmployeeById(getInputAsInt(scanner));
                    //employeeToUpdate.setId(1); // Remplacez par l'ID de l'employé que vous souhaitez mettre à jour
                    employeeToUpdate.setNom("NouveauNom");
                    employeeToUpdate.setPrenom("NouveauPrenom");
                    employeeToUpdate.setEmail("nouveau@email.com");
                    employeeToUpdate.setDateRecrutement(LocalDate.now()); // Date actuelle
                    employeeToUpdate.setDateNaissance(LocalDate.of(1990, 5, 15)); // Date de naissance fictive
                    employeeToUpdate.setTel("1234567890"); // Numéro de téléphone fictif

                    boolean updateSuccess = eimp.modifierEmployee(employeeToUpdate);

                    if (updateSuccess) {
                        System.out.println("L'employé a été mis à jour avec succès.");
                    } else {
                        System.out.println("La mise à jour de l'employé a échoué.");
                    }*/
                    System.out.println("Veuillez entrer l'ID de l'employé que vous souhaitez mettre à jour : ");
                    int employeeIdToUpdate = Helper.getInputInt(scanner);

                    // Recherchez l'employé par son ID
                    Employee employeeToUpdate = eimp.findEmployeeById(employeeIdToUpdate);

                    if (employeeToUpdate != null) {
                        // Demandez à l'utilisateur de saisir les nouvelles informations pour l'employé
                        System.out.println("Veuillez entrer les nouvelles informations pour l'employé :");
                        System.out.print("Nouveau nom : ");
                        employeeToUpdate.setNom(scanner.nextLine());
                        System.out.print("Nouveau prénom : ");
                        employeeToUpdate.setPrenom(scanner.nextLine());
                        System.out.print("Nouvel email : ");
                        employeeToUpdate.setEmail(scanner.nextLine());
                        System.out.print("Nouvelle date de naissance (AAAA-MM-JJ) : ");
                        dateNaissanceStr = scanner.nextLine();
                        employeeToUpdate.setDateNaissance(LocalDate.parse(dateNaissanceStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        System.out.print("Nouvelle date de recrutement (AAAA-MM-JJ) : ");
                        dateRecrutementStr = scanner.nextLine();
                        employeeToUpdate.setDateRecrutement(LocalDate.parse(dateRecrutementStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        System.out.print("Nouveau numéro de téléphone : ");
                        employeeToUpdate.setTel(scanner.nextLine());

                        // Appelez la méthode de mise à jour de l'employé
                        boolean updateSuccess = eimp.modifierEmployee(employeeToUpdate);

                        if (updateSuccess) {
                            System.out.println("L'employé a été mis à jour avec succès.");
                        } else {
                            System.out.println("La mise à jour de l'employé a échoué.");
                        }
                    } else {
                        System.out.println("Aucun employé trouvé avec l'ID : " + employeeIdToUpdate);
                    }
                    break;
                case 0:
                    System.out.println("Retour au menu principal.");
                    break;
                default:
                    System.out.println("Choix non valide. Veuillez réessayer.");
            }
        } while (choixEmploye != 0);
    }
}
