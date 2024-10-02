package basico.task.management.service;

import basico.task.management.model.primary.Cartera;

import java.util.List;
import java.util.Locale;

public interface CarteraService {
    Cartera save(Cartera cartera, Locale locale);
    Cartera update(Long id, Cartera cartera, Locale locale);
    Cartera findById(Long id, Locale locale);
    Cartera findByName(String name);
    List<Cartera> findByNameContains(String name);
    List<Cartera> findAll();
    void delete(Long id, Locale locale);
}
