package ma.alae.eloula.dao.implementation;

import connection.SingletonConnection;
import ma.alae.eloula.classes.Compte;
import ma.alae.eloula.classes.Etat;
import ma.alae.eloula.dao.Interfaces.Acounte;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CompteImp implements Acounte {
    Connection connection = SingletonConnection.getConn();

    public boolean supprimerCompte(int numeroCompte) {
        boolean success = false;
        try {
            // Supprimez d'abord les opérations associées à ce compte (s'il y en a)
            //String deleteOperationsSql = "DELETE FROM Operation WHERE compte_numero = ?";
            //PreparedStatement deleteOperationsStatement = connection.prepareStatement(deleteOperationsSql);
            //deleteOperationsStatement.setInt(1, numeroCompte);
            //int operationsDeleted = deleteOperationsStatement.executeUpdate();

            // Ensuite, supprimez le compte de la table "Compte"
            String deleteCompteSql = "DELETE FROM Compte WHERE numero = ? ";
            PreparedStatement deleteCompteStatement = connection.prepareStatement(deleteCompteSql);
            deleteCompteStatement.setInt(1, numeroCompte);
            int compteDeleted = deleteCompteStatement.executeUpdate();

            if (compteDeleted == 1) {
                // Si la suppression du compte a réussi
                success = true;
            }

            // Assurez-vous de libérer les ressources
            //deleteOperationsStatement.close();
            //deleteCompteStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return success;
    }
    public List<Compte> chercherComptesParClient(int idClient) {
        List<Compte> comptes = new ArrayList<>();

        // Recherche des comptes courants du client
        String sqlCourants = "SELECT c.numero, c.solde, c.datecreation, c.etat " +
                "FROM compte c " +
                "JOIN comptescourants cc ON c.numero = cc.numero " +
                "WHERE c.id_cl = ?";

        // Recherche des comptes épargnes du client
        String sqlEpargnes = "SELECT c.numero, c.solde, c.datecreation, c.etat " +
                "FROM compte c " +
                "JOIN comptesepargnes ce ON c.numero = ce.numero " +
                "WHERE c.id_cl = ?";

        try {
            // Recherche des comptes courants
            PreparedStatement preparedStatementCourants = connection.prepareStatement(sqlCourants);
            preparedStatementCourants.setInt(1, idClient);
            ResultSet resultSetCourants = preparedStatementCourants.executeQuery();
            while (resultSetCourants.next()) {
                Compte compteCourant = new Compte();
                compteCourant.setNumero(resultSetCourants.getInt("numero"));
                compteCourant.setSolde(resultSetCourants.getDouble("solde"));
                compteCourant.setDateCreation(resultSetCourants.getDate("datecreation").toLocalDate());
                compteCourant.setEtat(Etat.valueOf(resultSetCourants.getString("etat")));
                comptes.add(compteCourant);
            }
            preparedStatementCourants.close();

            // Recherche des comptes épargnes
            PreparedStatement preparedStatementEpargnes = connection.prepareStatement(sqlEpargnes);
            preparedStatementEpargnes.setInt(1, idClient);
            ResultSet resultSetEpargnes = preparedStatementEpargnes.executeQuery();
            while (resultSetEpargnes.next()) {
                Compte compteEpargne = new Compte();
                compteEpargne.setNumero(resultSetEpargnes.getInt("numero"));
                compteEpargne.setSolde(resultSetEpargnes.getDouble("solde"));
                compteEpargne.setDateCreation(resultSetEpargnes.getDate("datecreation").toLocalDate());
                compteEpargne.setEtat(Etat.valueOf(resultSetEpargnes.getString("etat")));
                comptes.add(compteEpargne);
            }
            preparedStatementEpargnes.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Gérer les exceptions appropriées ici
        }

        return comptes;
    }
    public Compte findCompteById(int compteId) {
        try {
            String sql = "SELECT * FROM Compte WHERE numero = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, compteId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Compte compte = new Compte();
                compte.setNumero(resultSet.getInt("numero"));
                compte.setSolde(resultSet.getDouble("solde"));
                // Remplissez les autres champs de Compte en fonction de la structure de votre table

                return compte;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public boolean changerEtatCompte(int numeroCompte) {
        boolean success = false;
        try {
            // Récupérez l'état actuel du compte
            String etatActuel = getEtatCompte(numeroCompte);

            // Basculez l'état en fonction de l'état actuel
            String nouvelEtat = "ACTIF".equals(etatActuel) ? "INACTIF" : "ACTIF";

            // Mettez à jour l'état du compte avec le nouvel état calculé
            String sql = "UPDATE Compte SET etat = ? WHERE numero = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, nouvelEtat);
            preparedStatement.setInt(2, numeroCompte);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 1) {
                success = true;
            }
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return success;
    }

    // Méthode pour récupérer l'état actuel du compte
    private String getEtatCompte(int numeroCompte) throws SQLException {
        String etatActuel = "";
        String sql = "SELECT etat FROM Compte WHERE numero = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, numeroCompte);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            etatActuel = resultSet.getString("etat");
        }

        resultSet.close();
        preparedStatement.close();
        return etatActuel;
    }

    public Optional<List<Compte>> findAllComptes() {
        try {
            String sql = "SELECT * FROM Compte";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Compte> comptes = new ArrayList<>();

            while (resultSet.next()) {
                Compte compte = new Compte();
                EmployeImp eimp=new EmployeImp();
                compte.setNumero(resultSet.getInt("numero"));
                compte.setSolde(resultSet.getDouble("solde"));
                compte.setDateCreation(resultSet.getDate("dateCreation").toLocalDate());
                compte.setEtat(Etat.valueOf(resultSet.getString("etat")));
                compte.setEmployee(eimp.findEmployeeById(resultSet.getInt("id_employee")));
                comptes.add(compte);
            }

            if (!comptes.isEmpty()) {
                return Optional.of(comptes);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<List<Compte>> findComptesByEtat(String etat) {
        try {
            String sql = "SELECT * FROM Compte WHERE etat = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, etat);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Compte> comptes = new ArrayList<>();

            while (resultSet.next()) {
                Compte compte = new Compte();
                compte.setNumero(resultSet.getInt("numero"));
                compte.setSolde(resultSet.getDouble("solde"));
                compte.setDateCreation(resultSet.getDate("dateCreation").toLocalDate());
                compte.setEtat(Etat.valueOf(resultSet.getString("etat")));

                comptes.add(compte);
            }

            if (!comptes.isEmpty()) {
                return Optional.of(comptes);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return Optional.empty();
    }

    // ...

    public Optional<List<Compte>> findComptesByDateCreation(Date dateCreation) {
        try {
            String sql = "SELECT * FROM Compte WHERE dateCreation = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1, new java.sql.Date(dateCreation.getTime()));
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Compte> comptes = new ArrayList<>();

            while (resultSet.next()) {
                Compte compte = new Compte();
                compte.setNumero(resultSet.getInt("numero"));
                compte.setSolde(resultSet.getDouble("solde"));
                compte.setDateCreation(resultSet.getDate("dateCreation").toLocalDate());
                compte.setEtat(Etat.valueOf(resultSet.getString("etat")));

                comptes.add(compte);
            }

            if (!comptes.isEmpty()) {
                return Optional.of(comptes);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return Optional.empty();



    }

    public String getTypeDeCompte(int numeroCompte) {
        try {
            // Vérifiez si l'ID du compte existe dans ComptesCourants
            String checkCourantSql = "SELECT numero FROM ComptesCourants WHERE numero = ?";
            PreparedStatement checkCourantStatement = connection.prepareStatement(checkCourantSql);
            checkCourantStatement.setInt(1, numeroCompte);
            ResultSet courantResultSet = checkCourantStatement.executeQuery();

            if (courantResultSet.next()) {
                return "Courant";
            }

            // Vérifiez si l'ID du compte existe dans ComptesEpargnes
            String checkEpargneSql = "SELECT numero FROM ComptesEpargnes WHERE numero = ?";
            PreparedStatement checkEpargneStatement = connection.prepareStatement(checkEpargneSql);
            checkEpargneStatement.setInt(1, numeroCompte);
            ResultSet epargneResultSet = checkEpargneStatement.executeQuery();

            if (epargneResultSet.next()) {
                return "Epargne";
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
