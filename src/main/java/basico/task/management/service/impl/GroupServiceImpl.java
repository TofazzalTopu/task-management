package basico.task.management.service.impl;

import basico.task.management.exception.AlreadyExistException;
import basico.task.management.exception.Messages;
import basico.task.management.exception.NotFoundException;
import basico.task.management.model.primary.Group;
import basico.task.management.repository.primary.GroupRepository;
import basico.task.management.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final Messages messages;

    public GroupServiceImpl(GroupRepository groupRepository, Messages messages) {
        this.groupRepository = groupRepository;
        this.messages = messages;
    }

    @Override
    public Group save(Group group) {
        Group savedGroup=findByName(group.getGroupName());
        if(Objects.isNull(savedGroup)){
            throw new AlreadyExistException(messages.get("group.already.exits"));
        }
        return groupRepository.save(group);
    }

    @Override
    public Group update(Long id, Group group) {
        Group savedGroup=findByName(group.getGroupName());
        if (!savedGroup.getGroupName().equalsIgnoreCase(group.getGroupName()))
            throw new NotFoundException(messages.get("group.already.exits"));
        savedGroup.setGroupName(savedGroup.getGroupName());
        return groupRepository.save(savedGroup);
    }

    @Override
    public Group findById(Long id) {
        return groupRepository.findById(id).orElseThrow(() -> new NotFoundException(messages.get("group.not.found")));
    }

    private  Group findByName(String name){
      return   groupRepository.findByGroupName(name);
    }



    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        findById(id);
        groupRepository.deleteById(id);
    }
}
