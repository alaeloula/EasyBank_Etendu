package ma.alae.eloula.dao.implementation;

import connection.SingletonConnection;
import ma.alae.eloula.classes.CompteCourant;
import ma.alae.eloula.classes.Etat;
import ma.alae.eloula.dao.Interfaces.CompteCourantI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompteCourantimp extends CompteImp implements CompteCourantI {
    Connection connection = SingletonConnection.getConn();
    public int creerCompteCourant(CompteCourant compteCourant, int idClient,int idEmployee,int idAgence) {
        int numeroCompteCourant = -1; // Par défaut, retournez -1 en cas d'échec
        try {
            // Commencez par insérer les informations du compte dans la table "Compte"
            String compteSql = "INSERT INTO Compte (solde, dateCreation, etat, id_cl,id_employee,id_agence) VALUES (?, ?, ?, ?,?,?)";
            PreparedStatement compteStatement = connection.prepareStatement(compteSql, PreparedStatement.RETURN_GENERATED_KEYS);
            compteStatement.setDouble(1, compteCourant.getSolde());
            compteStatement.setObject(2, compteCourant.getDateCreation());
            compteStatement.setString(3, "ACTIF"); // État par défaut
            compteStatement.setInt(4, idClient); // ID du client
            compteStatement.setInt(5, idEmployee); // ID du employe
            compteStatement.setInt(6, idAgence); // ID du agence
            int rowsAffected = compteStatement.executeUpdate();

            if (rowsAffected == 1) {
                ResultSet generatedKeys = compteStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int compteId = generatedKeys.getInt(1);

                    // Ensuite, insérez les informations du compte courant dans la table "ComptesCourants"
                    String compteCourantSql = "INSERT INTO ComptesCourants (numero, decouvert) VALUES (?, ?)";
                    PreparedStatement compteCourantStatement = connection.prepareStatement(compteCourantSql);
                    compteCourantStatement.setInt(1, compteId); // Utilisez l'ID du compte
                    compteCourantStatement.setDouble(2, compteCourant.getDecouvert());

                    int compteCourantRowsAffected = compteCourantStatement.executeUpdate();

                    if (compteCourantRowsAffected == 1) {
                        numeroCompteCourant = compteId; // Retournez l'ID du compte courant généré
                    }
                }
            }

            // Assurez-vous de libérer les ressources
            compteStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return numeroCompteCourant;
    }


    public CompteCourant findCompteCourantById(int numeroCompteCourant) {
        try {
            String sql = "SELECT CC.numero, CC.decouvert, C.solde, C.dateCreation, C.etat " +
                    "FROM ComptesCourants CC " +
                    "INNER JOIN Compte C ON CC.numero = C.numero " +
                    "WHERE CC.numero = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, numeroCompteCourant);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                CompteCourant compteCourant = new CompteCourant();
                compteCourant.setNumero(resultSet.getInt("numero"));
                compteCourant.setDecouvert(resultSet.getDouble("decouvert"));
                compteCourant.setSolde(resultSet.getDouble("solde"));
                compteCourant.setDateCreation(resultSet.getDate("dateCreation").toLocalDate());
                compteCourant.setEtat(Etat.valueOf(resultSet.getString("etat")));

                return compteCourant;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }


    public boolean updateCompteCourantEtCompte(CompteCourant compteCourant) {

        boolean success = false;

        try {

                    // Démarrez une transaction
                    connection.setAutoCommit(false);

            // Écrivez ici le code pour mettre à jour la table ComptesCourants
            String sqlCompteCourant = "UPDATE ComptesCourants SET decouvert = ? WHERE numero = ?";
            PreparedStatement preparedStatementCompteCourant = connection.prepareStatement(sqlCompteCourant);
            preparedStatementCompteCourant.setDouble(1, compteCourant.getDecouvert());
            preparedStatementCompteCourant.setInt(2, compteCourant.getNumero());

            int rowsUpdatedCompteCourant = preparedStatementCompteCourant.executeUpdate();

            // Écrivez ici le code pour mettre à jour la table Compte
            String sqlCompte = "UPDATE Compte SET solde = ? WHERE numero = ?";
            PreparedStatement preparedStatementCompte = connection.prepareStatement(sqlCompte);
            preparedStatementCompte.setDouble(1, compteCourant.getSolde());
            //preparedStatementCompte.setString(2, "ACTIF"); // ou "INACTIF" en fonction de la logique
            preparedStatementCompte.setInt(2, compteCourant.getNumero());

            int rowsUpdatedCompte = preparedStatementCompte.executeUpdate();

            // Si les deux mises à jour sont réussies, validez la transaction
            if (rowsUpdatedCompteCourant == 1 && rowsUpdatedCompte == 1) {
                connection.commit();
                success = true;
            } else {
                // Sinon, annulez la transaction
                connection.rollback();
            }

            // Assurez-vous de libérer les ressources et de restaurer le mode de traitement automatique
            preparedStatementCompteCourant.close();
            preparedStatementCompte.close();
            connection.setAutoCommit(true);
        } catch (SQLException ex) {
            ex.printStackTrace();

            // En cas d'erreur, annulez la transaction et revenez à l'état précédent
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return success;
    }

}
