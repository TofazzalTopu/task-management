package basico.task.management.service;

import basico.task.management.model.primary.Group;

import java.util.List;

public interface GroupService {

    Group save(Group group);
    Group update(Long id, Group group);
    Group findById(Long id);
    List<Group> findAll();
    void delete(Long id);
}
