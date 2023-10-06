package ma.alae.eloula.dao.implementation;

import connection.SingletonConnection;
import ma.alae.eloula.classes.CompteCourant;
import ma.alae.eloula.classes.CompteEpargne;
import ma.alae.eloula.classes.Etat;
import ma.alae.eloula.dao.Interfaces.CompteEpargneI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompteEpargneImp extends CompteImp implements CompteEpargneI {
    Connection connection = SingletonConnection.getConn();
    public int creerCompteEpargne(CompteEpargne compteEpargne, int idClient,int idEmploye,int idAgence) {
        int numeroCompteCourant = -1; // Par défaut, retournez -1 en cas d'échec
        try {
            // Commencez par insérer les informations du compte dans la table "Compte"
            String compteSql = "INSERT INTO Compte (solde, dateCreation, etat, id_cl,id_employee,id_agence) VALUES (?, ?, ?, ?,?,?)";
            PreparedStatement compteStatement = connection.prepareStatement(compteSql, PreparedStatement.RETURN_GENERATED_KEYS);
            compteStatement.setDouble(1, compteEpargne.getSolde());
            compteStatement.setObject(2, compteEpargne.getDateCreation());
            compteStatement.setString(3, "ACTIF"); // État par défaut
            compteStatement.setInt(4, idClient); // ID du client
            compteStatement.setInt(5, idEmploye); // ID du employe
            compteStatement.setInt(6,idAgence);

            int rowsAffected = compteStatement.executeUpdate();

            if (rowsAffected == 1) {
                ResultSet generatedKeys = compteStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int compteId = generatedKeys.getInt(1);

                    // Ensuite, insérez les informations du compte courant dans la table "ComptesCourants"
                    String compteCourantSql = "INSERT INTO ComptesEpargnes (numero,tauxinteret ) VALUES (?, ?)";
                    PreparedStatement compteCourantStatement = connection.prepareStatement(compteCourantSql);
                    compteCourantStatement.setInt(1, compteId); // Utilisez l'ID du compte
                    compteCourantStatement.setDouble(2, compteEpargne.getTauxInteret());

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

    public CompteEpargne findCompteEpargneById(int numeroCompteEpargne) {
        try {
            String sql = "SELECT C.numero, C.solde, C.datecreation, C.etat, E.tauxinteret " +
                    "FROM Compte C " +
                    "INNER JOIN ComptesEpargnes E ON C.numero = E.numero " +
                    "WHERE C.numero = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, numeroCompteEpargne);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                CompteEpargne compteEpargne = new CompteEpargne();
                compteEpargne.setNumero(resultSet.getInt("numero"));
                compteEpargne.setSolde(resultSet.getDouble("solde"));
                compteEpargne.setDateCreation(resultSet.getDate("datecreation").toLocalDate());
                compteEpargne.setEtat(Etat.valueOf(resultSet.getString("etat")));
                compteEpargne.setTauxInteret(resultSet.getDouble("tauxinteret"));

                return compteEpargne;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public boolean updateCompteEpargneEtCompte(CompteEpargne compteEpargne) {
        boolean success = false;
        try {
            // Démarrez une transaction
            connection.setAutoCommit(false);

            // Mettez à jour les informations du compte épargne
            String updateCompteEpargneSql = "UPDATE ComptesEpargnes SET tauxinteret = ? WHERE numero = ?";
            PreparedStatement updateCompteEpargneStatement = connection.prepareStatement(updateCompteEpargneSql);
            updateCompteEpargneStatement.setDouble(1, compteEpargne.getTauxInteret());
            updateCompteEpargneStatement.setInt(2, compteEpargne.getNumero());

            int compteEpargneRowsUpdated = updateCompteEpargneStatement.executeUpdate();

            // Mettez à jour les informations du compte général (Compte)
            String updateCompteSql = "UPDATE Compte SET solde = ? WHERE numero = ?";
            PreparedStatement updateCompteStatement = connection.prepareStatement(updateCompteSql);
            updateCompteStatement.setDouble(1, compteEpargne.getSolde());
            //updateCompteStatement.setString(2, compteEpargne.getEtat());
            updateCompteStatement.setInt(2, compteEpargne.getNumero());

            int compteRowsUpdated = updateCompteStatement.executeUpdate();

            // Si les deux mises à jour ont réussi, validez la transaction
            if (compteEpargneRowsUpdated == 1 && compteRowsUpdated == 1) {
                connection.commit();
                success = true;
                //System.out.println("Les informations du compte épargne et du compte général ont été mises à jour avec succès.");
            } else {
                // En cas d'échec, annulez la transaction
                connection.rollback();
                //System.err.println("Aucune mise à jour n'a été effectuée. Vérifiez le numéro du compte épargne.");
            }

            // Rétablissez le mode de commit automatique
            connection.setAutoCommit(true);

            // Assurez-vous de libérer les ressources
            updateCompteEpargneStatement.close();
            updateCompteStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Erreur SQL lors de la mise à jour du compte épargne et du compte général.");
        }

        return success;
    }


}
