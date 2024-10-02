package basico.task.management.service;

import basico.task.management.model.primary.Role;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface RoleService {

    Role save(String name);

    Role update(Long id, String name, Locale locale);

    Role findById(Long id, Locale locale);

    List<Role> findAll();

    Optional<Role> findByName(String name);

    List<Role> findAllByNames(List<String> names);

    List<Role> findAllByLabels(List<String> labels);

    void delete(Long id, Locale locale);
}
