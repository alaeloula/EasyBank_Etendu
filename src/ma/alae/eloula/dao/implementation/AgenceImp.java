package ma.alae.eloula.dao.implementation;

import connection.SingletonConnection;
import ma.alae.eloula.classes.Agence;
import ma.alae.eloula.dao.Interfaces.AgenceI;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class AgenceImp implements AgenceI {
    Connection connection = SingletonConnection.getConn();

    @Override
    public Optional<Agence> createAgence(Agence agence) {

            PreparedStatement preparedStatement = null;
            ResultSet generatedKeys = null;

            try {
                String query = "INSERT INTO agence (nom, adresse, tel) VALUES (?, ?, ?)";
                preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, agence.getNom());
                preparedStatement.setString(2, agence.getAdresse());
                preparedStatement.setString(3, agence.getTel());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    return Optional.empty(); // La création a échoué, retourne un Optional vide
                }

                generatedKeys = preparedStatement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    agence.setId(generatedKeys.getInt(1)); // Mettez à jour l'ID de l'agence avec l'ID généré
                } else {
                    return Optional.empty(); // La création a échoué, aucun ID généré récupéré
                }

                return Optional.of(agence); // Retourne l'agence créée avec l'ID généré
            } catch (SQLException e) {
                e.printStackTrace();
                // Gérer les exceptions liées à la base de données
                return Optional.empty(); // Ou lancez une exception personnalisée si nécessaire
            }



    }
    @Override
    public boolean deleteAgence(int agenceId) {
        try {
            String query = "DELETE FROM agence WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, agenceId);

            int affectedRows = preparedStatement.executeUpdate();

            return affectedRows > 0; // Si au moins une ligne a été supprimée, retourne true
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les exceptions liées à la base de données
            return false;
        }
    }

    @Override
    public Optional<Agence> findAgenceById(int agenceId) {
        try {
            String query = "SELECT * FROM agence WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, agenceId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Agence agence = new Agence();
                agence.setId(resultSet.getInt("id"));
                agence.setNom(resultSet.getString("nom"));
                agence.setAdresse(resultSet.getString("adresse"));
                agence.setTel(resultSet.getString("tel"));

                return Optional.of(agence);
            } else {
                return Optional.empty(); // Aucune agence trouvée avec cet ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les exceptions liées à la base de données
            return Optional.empty();
        }
    }


    @Override
    public boolean affecterEmployeeAAgence(int idEmployee, int idAgence, LocalDate dateFin) {
        try {
            // Obtenir la date de début actuelle
            LocalDate dateDebutActuelle = LocalDate.now();

            String query = "INSERT INTO Employee_Affectation (id_employee, id_agence, date_debut, date_fin) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, idEmployee);
            preparedStatement.setInt(2, idAgence);
            preparedStatement.setDate(3, java.sql.Date.valueOf(dateDebutActuelle)); // Convertir LocalDate en java.sql.Date
            preparedStatement.setDate(4, dateFin != null ? java.sql.Date.valueOf(dateFin) : null); // Convertir LocalDate en java.sql.Date

            int affectedRows = preparedStatement.executeUpdate();

            return affectedRows > 0; // Si au moins une ligne a été insérée, retourne true
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les exceptions liées à la base de données
            return false;
        }
    }

    @Override
    public boolean isEmployeeCurrentlyAssigned(int idEmployee) {
        try {
            String query = "SELECT COUNT(*) FROM Employee_Affectation WHERE id_employee = ? AND date_fin IS NULL";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, idEmployee);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // Si le compte est supérieur à zéro, l'employé est actuellement affecté
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les exceptions liées à la base de données
        }

        return false; // Par défaut, l'employé n'est pas actuellement affecté ou une erreur s'est produite
    }

@Override
    public boolean updateDateFinEmployeeAssignment(int idEmployee, LocalDate dateFin) {
        try {

            String updateQuery = "UPDATE Employee_Affectation SET date_fin = ? WHERE id_employee = ? AND date_fin IS NULL";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

            // Convertissez la date de fin de LocalDate en java.sql.Date
            java.sql.Date sqlDateFin = dateFin != null ? java.sql.Date.valueOf(dateFin) : null;

            preparedStatement.setDate(1, sqlDateFin);
            preparedStatement.setInt(2, idEmployee);

            int rowsAffected = preparedStatement.executeUpdate();

            preparedStatement.close();
            //connection.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }





}
