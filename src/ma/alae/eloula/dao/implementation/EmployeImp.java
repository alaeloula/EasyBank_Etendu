package ma.alae.eloula.dao.implementation;

import connection.SingletonConnection;
import ma.alae.eloula.classes.Client;
import ma.alae.eloula.classes.Employee;
import ma.alae.eloula.dao.Interfaces.Personel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeImp implements Personel {
    Connection connection = SingletonConnection.getConn();
    @Override
    public Optional<Employee> ajouterEmployee(Employee employee) {
        Optional<Employee> addedEmployee = Optional.empty();
        try {
            // Commencez par insérer les données dans la table "Personnel"
            String personelSql = "INSERT INTO Personel (nom, prenom, dateNaissance, tel) VALUES (?, ?, ?, ?)";
            PreparedStatement personelStatement = connection.prepareStatement(personelSql, Statement.RETURN_GENERATED_KEYS);
            personelStatement.setString(1, employee.getNom());
            personelStatement.setString(2, employee.getPrenom());
            personelStatement.setObject(3, employee.getDateNaissance());
            personelStatement.setString(4, employee.getTel());

            int personelRowsAffected = personelStatement.executeUpdate();

            if (personelRowsAffected == 1) {
                ResultSet generatedPersonelKeys = personelStatement.getGeneratedKeys();
                if (generatedPersonelKeys.next()) {
                    int personelId = generatedPersonelKeys.getInt(1);

                    // Ensuite, insérez les données dans la table "Employee" avec l'ID de "Personel"
                    String employeeSql = "INSERT INTO Employee (id, email, dateRecrutement) VALUES (?, ?, ?)";
                    PreparedStatement employeeStatement = connection.prepareStatement(employeeSql);
                    employeeStatement.setInt(1, personelId); // Utilisez l'ID de Personel
                    employeeStatement.setString(2, employee.getEmail());
                    employeeStatement.setObject(3, employee.getDateRecrutement());

                    int employeeRowsAffected = employeeStatement.executeUpdate();

                    if (employeeRowsAffected == 1) {
                        // Si tout s'est bien passé, retournez l'employé ajouté
                        addedEmployee = Optional.of(employee);
                    }
                }
            }

            // Assurez-vous de libérer les ressources
            personelStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return addedEmployee;
    }


    @Override
    public int SupprimerEmpl(int id) {
        int rowsAffected = 0;
        try {
            // Démarrez une transaction
            connection.setAutoCommit(false);

            // Supprimez l'enregistrement de la table "Employee" en utilisant l'ID
            String employeeSql = "DELETE FROM Employee WHERE id = ?";
            PreparedStatement employeeStatement = connection.prepareStatement(employeeSql);
            employeeStatement.setInt(1, id);
            rowsAffected = employeeStatement.executeUpdate();

            if (rowsAffected == 1) {
                // Si la suppression de l'employé dans la table "Employee" a réussi,
                // supprimez également l'enregistrement correspondant dans la table "Personel"
                String personelSql = "DELETE FROM Personel WHERE id = ?";
                PreparedStatement personelStatement = connection.prepareStatement(personelSql);
                personelStatement.setInt(1, id);
                rowsAffected = personelStatement.executeUpdate();

                if (rowsAffected == 1) {
                    // Si la suppression dans la table "Personel" a réussi, validez la transaction
                    connection.commit();
                } else {
                    // Sinon, annulez la transaction
                    connection.rollback();
                }
            } else {
                // Si la suppression dans la table "Employee" a échoué, annulez la transaction
                connection.rollback();
            }

            // Réactivez le mode de validation automatique
            connection.setAutoCommit(true);

            // Assurez-vous de libérer les ressources
            employeeStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                // En cas d'erreur, annulez la transaction et réactivez le mode de validation automatique
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return rowsAffected;
    }


    @Override
    public Optional<Employee> getEmployeeById(int id) {
        Optional<Employee> employee = Optional.empty();
        try {
            String sql = "SELECT E.id, P.nom, P.prenom, P.dateNaissance, P.tel, E.email, E.dateRecrutement " +
                    "FROM Employee E " +
                    "INNER JOIN Personel P ON E.id = P.id " +
                    "WHERE E.id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Employee foundEmployee = new Employee();
                foundEmployee.setId(resultSet.getInt("id"));
                foundEmployee.setNom(resultSet.getString("nom"));
                foundEmployee.setPrenom(resultSet.getString("prenom"));
                foundEmployee.setDateNaissance(resultSet.getDate("dateNaissance").toLocalDate());
                foundEmployee.setTel(resultSet.getString("tel"));
                foundEmployee.setEmail(resultSet.getString("email"));
                foundEmployee.setDateRecrutement(resultSet.getDate("dateRecrutement").toLocalDate());

                employee = Optional.of(foundEmployee);
            }

            // Assurez-vous de libérer les ressources
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return employee;
    }

    public Employee findEmployeeById(int employeeId) {
        try {
            String sql = "SELECT E.id, P.nom, P.prenom, P.dateNaissance, P.tel, E.email, E.dateRecrutement " +
                    "FROM Employee E " +
                    "INNER JOIN Personel P ON E.id = P.id " +
                    "WHERE E.id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Employee Employee = new Employee();
                Employee.setId(resultSet.getInt("id"));
                Employee.setNom(resultSet.getString("nom"));
                Employee.setPrenom(resultSet.getString("prenom"));
                Employee.setDateNaissance(resultSet.getDate("dateNaissance").toLocalDate());
                Employee.setTel(resultSet.getString("tel"));
                Employee.setEmail(resultSet.getString("email"));
                Employee.setDateRecrutement(resultSet.getDate("dateRecrutement").toLocalDate());
                return Employee;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public Optional<List<Employee>> findAllEmployees() {
        List<Employee> employees = new ArrayList<>();

        try {
            String sql = "SELECT * FROM Employee JOIN Personel ON Employee.id = Personel.id";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getInt("id"));
                employee.setNom(resultSet.getString("nom"));
                employee.setPrenom(resultSet.getString("prenom"));
                employee.setDateRecrutement(resultSet.getDate("dateRecrutement").toLocalDate());
                employee.setEmail(resultSet.getString("email"));

                employees.add(employee);
            }

            return Optional.of(employees);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return Optional.empty(); // En cas d'erreur, retournez Optional.empty()
        }
    }

    public boolean modifierEmployee(Employee employee) {
        boolean success = false;
        Connection connection = null;
        try {
            // Établissez une connexion à la base de données et activez la gestion manuelle des transactions
            connection = SingletonConnection.getConn();
            connection.setAutoCommit(false); // Désactiver la validation automatique

            // Mettez à jour les informations de l'employé dans la table "Employee"
            String employeeSql = "UPDATE Employee SET email = ?, daterecrutement = ? WHERE id = ?";
            PreparedStatement employeeStatement = connection.prepareStatement(employeeSql);
            employeeStatement.setString(1, employee.getEmail());
            employeeStatement.setDate(2, Date.valueOf(employee.getDateRecrutement()));
            employeeStatement.setInt(3, employee.getId());

            int rowsUpdatedInEmployee = employeeStatement.executeUpdate();

            // Mettez à jour les informations correspondantes dans la table "Personel"
            if (rowsUpdatedInEmployee == 1) {
                String personelSql = "UPDATE Personel SET nom = ?, prenom = ?, tel = ? WHERE id = ?";
                PreparedStatement personelStatement = connection.prepareStatement(personelSql);
                personelStatement.setString(1, employee.getNom());
                personelStatement.setString(2, employee.getPrenom());
                personelStatement.setString(3, employee.getTel());
                personelStatement.setInt(4, employee.getId());

                int rowsUpdatedInPersonel = personelStatement.executeUpdate();

                if (rowsUpdatedInPersonel == 1) {
                    // Si les deux mises à jour sont réussies, validez la transaction
                    connection.commit();
                    success = true;
                   // System.out.println("L'employé a été mis à jour avec succès.");
                } else {
                    //System.err.println("Aucune mise à jour n'a été effectuée dans la table Personel.");
                }

                // Assurez-vous de libérer les ressources
                personelStatement.close();
            } else {
                //System.err.println("Aucune mise à jour n'a été effectuée dans la table Employee.");
            }

            // Assurez-vous de libérer les ressources
            employeeStatement.close();
        } catch (SQLException ex) {
            // En cas d'erreur, annulez la transaction
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            ex.printStackTrace();
            //System.err.println("Erreur SQL lors de la mise à jour de l'employé.");
        }

        return success;
    }


    public boolean employeeExists(int employeeId) {
        try {
            String sql = "SELECT COUNT(*) FROM Employee WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int rowCount = resultSet.getInt(1);
                return rowCount > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }




}
