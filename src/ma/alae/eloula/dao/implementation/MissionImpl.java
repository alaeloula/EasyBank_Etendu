package ma.alae.eloula.dao.implementation;

import connection.SingletonConnection;
import ma.alae.eloula.classes.Mission;
import ma.alae.eloula.dao.Interfaces.MissionI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MissionImpl implements MissionI {

    Connection connection = SingletonConnection.getConn();
    public boolean ajouterMission(Mission mission)  {

        String sql = "INSERT INTO Mission (nom,description) VALUES (?,?)";
        PreparedStatement MissionStatement = null;

        try {
            MissionStatement = connection.prepareStatement(sql);
            MissionStatement.setString(1,mission.getNom());
            MissionStatement.setString(2,mission.getDescription());


            int rowsAffected = MissionStatement.executeUpdate();
            if (rowsAffected==1)
                return true;



        } catch (SQLException ex) {
            ex.printStackTrace();
        }


     return false;
    }
    public List<Mission> findAllMissions() {
        List<Mission> missions = new ArrayList<>();

        try {
            String sql = "SELECT * FROM mission";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String description = resultSet.getString("description");

                // Vous devez créer un constructeur de Mission pour ces informations
                Mission mission = new Mission(id, nom, description);

                missions.add(mission);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Gérer les erreurs SQL ici
        }

        return missions;
    }
    public Mission findMissionById(int missionId) {
        try {
            String sql = "SELECT id, nom, description FROM mission WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, missionId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Mission mission = new Mission();
                mission.setId(resultSet.getInt("id"));
                mission.setNom(resultSet.getString("nom"));
                mission.setDescription(resultSet.getString("description"));
                return mission;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Gérer les erreurs SQL ici
        }

        return null; // Retourner null si la mission n'a pas été trouvée
    }
    public boolean supprimerMission(int missionId) {
        boolean success = false;
        try {
            // Supprimer la mission de la table "mission"
            String deleteMissionSql = "DELETE FROM mission WHERE id = ?";
            PreparedStatement deleteMissionStatement = connection.prepareStatement(deleteMissionSql);
            deleteMissionStatement.setInt(1, missionId);
            int missionDeleted = deleteMissionStatement.executeUpdate();

            if (missionDeleted == 1) {
                success = true;
                System.out.println("La mission a été supprimée avec succès.");
            } else {
                System.err.println("Aucune mission n'a été supprimée. Vérifiez l'ID de la mission.");
            }

            // Assurez-vous de libérer les ressources
            deleteMissionStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Erreur SQL lors de la suppression de la mission.");
        }

        return success;
    }
    public Optional<Mission> getMissionById(int missionId) {
        try {
            String sql = "SELECT * FROM mission WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, missionId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Mission mission = new Mission();
                mission.setId(resultSet.getInt("id"));
                mission.setNom(resultSet.getString("nom"));
                mission.setDescription(resultSet.getString("description"));
                // ... Set other mission properties ...

                return Optional.of(mission);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return Optional.empty();
    }

}
