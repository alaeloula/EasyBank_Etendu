package ma.alae.eloula.dao.implementation;

import connection.SingletonConnection;
import ma.alae.eloula.classes.Demande;
import ma.alae.eloula.dao.Interfaces.DemandeI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Optional;

public class DemandeDao implements DemandeI {
    Connection connection = SingletonConnection.getConn();
    @Override
    public Optional<Demande> ajouter(Demande demande) {

        try {
            String insertSql = "INSERT INTO demandeCredit (montant, duree, remarques, Client_code) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setDouble(1, demande.getSimulation().getCapitalEmprunte());
            preparedStatement.setDouble(2, demande.getSimulation().getNombreMensualite());
            preparedStatement.setString(3, demande.getRemarques());
            preparedStatement.setInt(4, demande.getClient().getId());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return Optional.ofNullable(demande);
            }
            preparedStatement.close();
        }
        catch (Exception e){
            System.out.print(e.getMessage());
        }
        return Optional.empty();
    }

}
