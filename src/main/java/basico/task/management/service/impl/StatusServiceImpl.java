package basico.task.management.service.impl;

import basico.task.management.exception.Messages;
import basico.task.management.exception.NotFoundException;
import basico.task.management.model.primary.Status;
import basico.task.management.repository.primary.StatusRepository;
import basico.task.management.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;
    private final Messages messages;

    @Override
    public Status findByNameAndEntity(String name, String entity) {
        return statusRepository.findByNameAndEntity(name, entity);
    }

    @Override
    public Status findById(Long id) {
        return statusRepository.findById(id).orElseThrow(() -> new NotFoundException(messages.get("status.not.found")));
    }

    @Override
    public List<Status> findAllByNameInAndEntity(List<String> names, String entity) {
        return statusRepository.findAllByNameInAndEntity(names, entity);
    }

}
