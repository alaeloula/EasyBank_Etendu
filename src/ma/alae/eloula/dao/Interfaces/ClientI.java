package ma.alae.eloula.dao.Interfaces;

import ma.alae.eloula.classes.Client;

import java.util.List;
import java.util.Optional;

public interface ClientI {
    boolean modifierClient(Client client);
    Client findClientById(int clientId);
    Optional<List<Client>> findAllClients();
    Optional<Client> rechercherClient(int clientId);
    int supprimerClient(int clientId);
    public Optional<Client> ajouterClient(Client client);
}
