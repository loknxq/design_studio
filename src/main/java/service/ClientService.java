package service;

import exception.BusinessException;
import model.Client;
import repository.ClientRepository;

import java.util.List;

public class ClientService {
    private final ClientRepository repository = new ClientRepository();

    public void create(Client client) {
        validate(client);
        repository.save(client);
    }

    public List<Client> getAll() {
        return repository.findAll();
    }

    public Client getById(int id) {
        return repository.findById(id);
    }

    public void update(Client client) {
        validate(client);
        repository.update(client);
    }

    public void delete(int id) {
        repository.delete(id);
    }

    private void validate(Client client) {
        if (client.getFullName() == null || client.getFullName().isBlank()) {
            throw new BusinessException("Имя клиента не может быть пустым");
        }
        if (client.getEmail() == null || !client.getEmail().contains("@")) {
            throw new BusinessException("Некорректный email клиента");
        }
        if (client.getPhone() == null || client.getPhone().isBlank()) {
            throw new BusinessException("Телефон клиента не может быть пустым");
        }
    }
}