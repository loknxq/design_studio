package service;

import exception.BusinessException;
import model.Designer;
import repository.DesignerRepository;

import java.util.List;

public class DesignerService {
    private final DesignerRepository repository = new DesignerRepository();

    public void create(Designer designer) {
        validate(designer);
        repository.save(designer);
    }

    public List<Designer> getAll() {
        return repository.findAll();
    }

    public Designer getById(int id) {
        return repository.findById(id);
    }

    public void update(Designer designer) {
        validate(designer);
        repository.update(designer);
    }

    public void delete(int id) {
        repository.delete(id);
    }

    private void validate(Designer designer) {
        if (designer.getFullName() == null || designer.getFullName().isBlank()) {
            throw new BusinessException("Имя дизайнера не может быть пустым");
        }
        if (designer.getSpecialization() == null || designer.getSpecialization().isBlank()) {
            throw new BusinessException("Специализация дизайнера не может быть пустой");
        }
        if (designer.getEmail() == null || !designer.getEmail().contains("@")) {
            throw new BusinessException("Некорректный email дизайнера");
        }
    }
}