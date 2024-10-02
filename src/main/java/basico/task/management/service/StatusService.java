package basico.task.management.service;

import basico.task.management.model.primary.Status;

import java.util.List;

public interface StatusService {
    Status findByNameAndEntity(String name, String entity);
    Status findById(Long id);
    List<Status> findAllByNameInAndEntity(List<String> names, String entity);

}
