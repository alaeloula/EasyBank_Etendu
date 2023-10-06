package ma.alae.eloula.dao.implementation;

import connection.SingletonConnection;
import ma.alae.eloula.classes.MissionEmploye;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import static java.sql.Date.*;

public class MissionEmployeImp {
    Connection connection = SingletonConnection.getConn();
    public boolean assignMissionToEmployee(MissionEmploye missionEmployee) {
        LocalDate currentDate = LocalDate.now();
        try {
            String sql = "INSERT INTO missionemploye (datedebut, datefin, mission_id, employe_id) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setObject(1, java.sql.Date.valueOf(currentDate));

            preparedStatement.setObject(2, java.sql.Date.valueOf(missionEmployee.getDateFin()));
            preparedStatement.setInt(3, missionEmployee.getM().getId());
            preparedStatement.setInt(4, missionEmployee.getE().getId());

            int rowsInserted = preparedStatement.executeUpdate();

            preparedStatement.close();

            return rowsInserted == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Gérer les erreurs SQL ici
            return false;
        }
    }

    public boolean supprimerAffectation(int missionEmployeId) {
        boolean success = false;
        try {
            // Supprimer l'affectation de la table "missionemploye"
            String deleteAffectationSql = "DELETE FROM missionemploye WHERE id = ?";
            PreparedStatement deleteAffectationStatement = connection.prepareStatement(deleteAffectationSql);
            deleteAffectationStatement.setInt(1, missionEmployeId);
            int affectationDeleted = deleteAffectationStatement.executeUpdate();

            if (affectationDeleted == 1) {
                success = true;
                System.out.println("L'affectation a été supprimée avec succès.");
            } else {
                System.err.println("Aucune affectation n'a été supprimée. Vérifiez l'ID de l'affectation.");
            }

            // Assurez-vous de libérer les ressources
            deleteAffectationStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Erreur SQL lors de la suppression de l'affectation.");
        }

        return success;
    }



}
