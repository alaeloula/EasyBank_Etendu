package ma.alae.eloula.dao.implementation;

import connection.SingletonConnection;
import ma.alae.eloula.classes.Agence;
import ma.alae.eloula.dao.Interfaces.AgenceI;

import java.sql.*;
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


}
