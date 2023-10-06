package ma.alae.eloula.dao.implementation;

import connection.SingletonConnection;
import ma.alae.eloula.classes.Compte;
import ma.alae.eloula.classes.Employee;
import ma.alae.eloula.classes.Operation;
import ma.alae.eloula.classes.Type;
import ma.alae.eloula.dao.Interfaces.OperationI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

public class OperationImp implements OperationI {
    Connection connection = SingletonConnection.getConn();
    public boolean effectuerVersement(int compteNumero, double montant,int employeId) {
        boolean success = false;
        try {
            // Vérifier d'abord si le compte existe et récupérer son solde actuel
            String selectCompteSql = "SELECT solde FROM Compte WHERE numero = ?";
            PreparedStatement selectCompteStatement = connection.prepareStatement(selectCompteSql);
            selectCompteStatement.setInt(1, compteNumero);
            ResultSet compteResultSet = selectCompteStatement.executeQuery();

            if (compteResultSet.next()) {
                double soldeActuel = compteResultSet.getDouble("solde");
                double nouveauSolde = soldeActuel + montant;

                // Mettre à jour le solde du compte
                String updateSoldeSql = "UPDATE Compte SET solde = ? WHERE numero = ?";
                PreparedStatement updateSoldeStatement = connection.prepareStatement(updateSoldeSql);
                updateSoldeStatement.setDouble(1, nouveauSolde);
                updateSoldeStatement.setInt(2, compteNumero);
                int soldeUpdated = updateSoldeStatement.executeUpdate();

                if (soldeUpdated == 1) {
                    // Enregistrer l'opération de versement
                    String sql = "INSERT INTO Operation (type, dateCreation, montant, compte_numero,employe_id) VALUES (?, ?, ?, ?,?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, "VERSEMENT");
                    preparedStatement.setObject(2, new java.sql.Date(new Date().getTime())); // Date actuelle
                    preparedStatement.setDouble(3, montant);
                    preparedStatement.setInt(4, compteNumero);
                    preparedStatement.setInt(5, employeId);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected == 1) {
                        success = true;
                    }

                    preparedStatement.close();
                }
            }

            // Assurez-vous de libérer les ressources
            //selectCompteResultSet.close();
            //selectCompteStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return success;
    }


    public boolean effectuerRetrait(int compteNumero, double montant,int employeId) {
        boolean success = false;
        try {
            // Vérifier d'abord si le compte existe et récupérer son solde actuel
            String selectCompteSql = "SELECT solde FROM Compte WHERE numero = ?";
            PreparedStatement selectCompteStatement = connection.prepareStatement(selectCompteSql);
            selectCompteStatement.setInt(1, compteNumero);
            ResultSet compteResultSet = selectCompteStatement.executeQuery();

            if (compteResultSet.next()) {
                double soldeActuel = compteResultSet.getDouble("solde");

                // Vérifier si le solde est suffisant pour le retrait
                if (soldeActuel >= montant) {
                    double nouveauSolde = soldeActuel - montant;

                    // Mettre à jour le solde du compte
                    String updateSoldeSql = "UPDATE Compte SET solde = ? WHERE numero = ?";
                    PreparedStatement updateSoldeStatement = connection.prepareStatement(updateSoldeSql);
                    updateSoldeStatement.setDouble(1, nouveauSolde);
                    updateSoldeStatement.setInt(2, compteNumero);
                    int soldeUpdated = updateSoldeStatement.executeUpdate();

                    if (soldeUpdated == 1) {
                        // Enregistrer l'opération de retrait
                        String sql = "INSERT INTO Operation (type, dateCreation, montant, compte_numero,employe_id) VALUES (?, ?, ?, ?,?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, "RETRAIT");
                        preparedStatement.setObject(2, new java.sql.Date(new Date().getTime())); // Date actuelle
                        preparedStatement.setDouble(3, montant);
                        preparedStatement.setInt(4, compteNumero);
                        preparedStatement.setInt(5, employeId);

                        int rowsAffected = preparedStatement.executeUpdate();

                        if (rowsAffected == 1) {
                            success = true;
                        }

                        preparedStatement.close();
                    }
                }
            }

            // Assurez-vous de libérer les ressources
            //selectCompteResultSet.close();
            selectCompteStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return success;
    }

    public boolean supprimerOperation(int operationId) {
        boolean success = false;
        try {
            // Supprimez l'opération de la table "Operation" en utilisant l'ID de l'opération
            String deleteOperationSql = "DELETE FROM Operation WHERE id = ?";
            PreparedStatement deleteOperationStatement = connection.prepareStatement(deleteOperationSql);
            deleteOperationStatement.setInt(1, operationId);

            int operationDeleted = deleteOperationStatement.executeUpdate();

            if (operationDeleted == 1) {
                success = true;
            }

            // Assurez-vous de libérer les ressources
            deleteOperationStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return success;
    }

    public Optional<Operation> findOperationById(int operationId) {
        try {
            String sql = "SELECT * FROM Operation WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, operationId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Operation operation = new Operation();
                operation.setId(resultSet.getInt("id"));
                operation.setType(Type.valueOf(resultSet.getString("type")));
                operation.setDateCreation(resultSet.getDate("dateCreation"));
                operation.setMontant(resultSet.getDouble("montant"));

                // Récupérer la référence à Compte en fonction de la clé étrangère
                int compteId = resultSet.getInt("compte_numero");
                CompteImp compte = new CompteImp();
                       Compte c1= compte.findCompteById(compteId); // Écrivez la méthode findCompteById pour obtenir le compte

                // Récupérer la référence à Employee en fonction de la clé étrangère
                int employeeId = resultSet.getInt("employe_id");
                EmployeImp employee = new EmployeImp();
                  Employee e1= employee.findEmployeeById(employeeId); // Écrivez la méthode findEmployeeById pour obtenir l'employé

                operation.setCompte(c1);
                operation.setEmployee(e1);

                return Optional.of(operation);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return Optional.empty();
    }

}
