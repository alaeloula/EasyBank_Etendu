package ma.alae.eloula.dao.implementation;

import connection.SingletonConnection;
import ma.alae.eloula.classes.Client;
import ma.alae.eloula.dao.Interfaces.ClientI;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientImp implements ClientI {
    Connection connection = SingletonConnection.getConn();

    @Override
    public Optional<Client> ajouterClient(Client client) {
        Optional<Client> addedClient = Optional.empty();
        try {
            // Commencez par insérer les données dans la table "Personel"
            String personelSql = "INSERT INTO Personel (nom, prenom, dateNaissance, tel) VALUES (?, ?, ?, ?)";
            PreparedStatement personelStatement = connection.prepareStatement(personelSql, Statement.RETURN_GENERATED_KEYS);
            personelStatement.setString(1, client.getNom());
            personelStatement.setString(2, client.getPrenom());
            personelStatement.setObject(3, client.getDateNaissance());
            personelStatement.setString(4, client.getTel());

            int personelRowsAffected = personelStatement.executeUpdate();

            if (personelRowsAffected == 1) {
                ResultSet generatedPersonelKeys = personelStatement.getGeneratedKeys();
                if (generatedPersonelKeys.next()) {
                    int personelId = generatedPersonelKeys.getInt(1);

                    // Ensuite, insérez les données dans la table "Client" avec l'ID de "Personel"
                    String clientSql = "INSERT INTO Client (id, address) VALUES (?, ?)";
                    PreparedStatement clientStatement = connection.prepareStatement(clientSql);
                    clientStatement.setInt(1, personelId); // Utilisez l'ID de Personel
                    clientStatement.setString(2, client.getAddress());

                    int clientRowsAffected = clientStatement.executeUpdate();

                    if (clientRowsAffected == 1) {
                        // Si tout s'est bien passé, retournez le client ajouté
                        addedClient = Optional.of(client);
                    }
                }
            }

            // Assurez-vous de libérer les ressources
            personelStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return addedClient;
    }


    public int supprimerClient(int clientId) {
        int rowsAffected = 0;
        try {
            // Supprimez d'abord l'enregistrement dans la table "Client"
            String clientSql = "DELETE FROM Client WHERE id = ?";
            PreparedStatement clientStatement = connection.prepareStatement(clientSql);
            clientStatement.setInt(1, clientId);

            rowsAffected += clientStatement.executeUpdate();

            // Ensuite, supprimez l'enregistrement correspondant dans la table "Personel"
            String personelSql = "DELETE FROM Personel WHERE id = ?";
            PreparedStatement personelStatement = connection.prepareStatement(personelSql);
            personelStatement.setInt(1, clientId);

            rowsAffected += personelStatement.executeUpdate();

            // Assurez-vous de libérer les ressources
            clientStatement.close();
            personelStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return rowsAffected;
    }

   @Override
    public Optional<Client> rechercherClient(int clientId) {
        Optional<Client> clientFound = Optional.empty();
        try {
            // Créez une requête SQL pour récupérer les informations du client en fonction de son ID
            String sql = "SELECT c.id, c.address, p.nom, p.prenom, p.dateNaissance, p.tel FROM Client c " +
                    "INNER JOIN Personel p ON c.id = p.id " +
                    "WHERE c.id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, clientId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Si un client correspondant est trouvé, créez un objet Client avec les informations
                int id = resultSet.getInt("id");
                String address = resultSet.getString("address");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                java.sql.Date dateNaissance = resultSet.getDate("dateNaissance");
                String tel = resultSet.getString("tel");

                // Créez un objet Client
                Client client = new Client(id, nom, prenom, dateNaissance.toLocalDate(), tel, address);

                clientFound = Optional.of(client);
            }

            // Assurez-vous de libérer les ressources
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return clientFound;
    }
    @Override
    public Optional<List<Client>> findAllClients() {
        List<Client> clients = new ArrayList<>();

        try {
            // Établissez la connexion à la base de données ici (connexion Singleton recommandée)

            String sql = "SELECT * FROM Client JOIN Personel ON Client.id = Personel.id";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Client client = new Client();
                client.setId(resultSet.getInt("id"));
                client.setNom(resultSet.getString("nom"));
                client.setPrenom(resultSet.getString("prenom"));
                client.setDateNaissance(resultSet.getDate("dateNaissance").toLocalDate());
                client.setTel(resultSet.getString("tel"));
                client.setAddress(resultSet.getString("address"));

                clients.add(client);
            }

            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return Optional.of(clients);
    }
    @Override
    public Client findClientById(int clientId) {
        try {
            String sql = "SELECT C.id, P.nom, P.prenom, P.dateNaissance, P.tel, C.address " +
                    "FROM Client C " +
                    "INNER JOIN Personel P ON C.id = P.id " +
                    "WHERE C.id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Client client = new Client();
                client.setId(resultSet.getInt("id"));
                client.setNom(resultSet.getString("nom"));
                client.setPrenom(resultSet.getString("prenom"));
                client.setDateNaissance(resultSet.getDate("dateNaissance").toLocalDate());
                client.setTel(resultSet.getString("tel"));
                client.setAddress(resultSet.getString("address"));
                return client;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean modifierClient(Client client) {
        boolean success = false;
        Connection connection = null;
        try {
            connection = SingletonConnection.getConn();
            connection.setAutoCommit(false); // Désactiver la validation automatique

            // Étape 1 : Mettre à jour les informations du client dans la table "Personel"
            String updatePersonelSql = "UPDATE Personel SET nom = ?, prenom = ?, datenaissance = ?, tel = ? WHERE id = ?";
            PreparedStatement updatePersonelStatement = connection.prepareStatement(updatePersonelSql);
            updatePersonelStatement.setString(1, client.getNom());
            updatePersonelStatement.setString(2, client.getPrenom());
            updatePersonelStatement.setDate(3, Date.valueOf(client.getDateNaissance()));
            updatePersonelStatement.setString(4, client.getTel());
            updatePersonelStatement.setInt(5, client.getId());

            int rowsUpdatedPersonel = updatePersonelStatement.executeUpdate();

            // Étape 2 : Mettre à jour l'adresse du client dans la table "Client"
            String updateClientSql = "UPDATE Client SET address = ? WHERE id = ?";
            PreparedStatement updateClientStatement = connection.prepareStatement(updateClientSql);
            updateClientStatement.setString(1, client.getAddress());
            updateClientStatement.setInt(2, client.getId());

            int rowsUpdatedClient = updateClientStatement.executeUpdate();

            // Si les deux mises à jour ont réussi, validez la transaction
            if (rowsUpdatedPersonel == 1 && rowsUpdatedClient == 1) {
                connection.commit();
                success = true;
               // System.out.println("Le client a été mis à jour avec succès.");
            } else {
                connection.rollback(); // En cas d'échec, annulez la transaction
                //System.err.println("La mise à jour du client a échoué. Vérifiez l'ID du client.");
            }

            // Réactivez la validation automatique
            connection.setAutoCommit(true);
        } catch (SQLException ex) {
            if (connection != null) {
                try {
                    connection.rollback(); // En cas d'erreur, annulez la transaction
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            ex.printStackTrace();
            System.err.println("Erreur SQL lors de la mise à jour du client.");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return success;
    }


}
