package basico.task.management.service.impl;

import basico.task.management.dto.GroupCheckListDto;
import basico.task.management.exception.NotFoundException;
import basico.task.management.model.primary.GroupCheckList;
import basico.task.management.repository.primary.GroupCheckListRepository;
import basico.task.management.service.GroupCheckListService;
import basico.task.management.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;


@Service
@RequiredArgsConstructor
public class GroupCheckListServiceImpl implements GroupCheckListService {

    private final GroupCheckListRepository groupCheckListRepository;
    private final GroupService groupService;
    private final MessageSource messageSource;

    @Override
    public GroupCheckList save(GroupCheckListDto groupCheckListDto, Locale locale) {
        GroupCheckList groupCheckList = new GroupCheckList();
        groupCheckList.setDescription(groupCheckListDto.getDescription());
        groupCheckList.setNumber(groupCheckListDto.getNumber());
        groupCheckList.setUniDades(groupCheckListDto.getUniDades());
        groupCheckList.setPriceUnit(groupCheckListDto.getPriceUnit());
        groupCheckList.setRef(groupCheckListDto.getRef());
        groupCheckList.setGroup(groupService.findById(groupCheckListDto.getGroupId()));
        return groupCheckListRepository.save(groupCheckList);
    }

    @Override
    public GroupCheckList update(Long id, GroupCheckListDto groupCheckListDto, Locale locale) {
        GroupCheckList groupCheckList = findById(id, locale);
        groupCheckList.setDescription(groupCheckListDto.getDescription());
        groupCheckList.setNumber(groupCheckListDto.getNumber());
        groupCheckList.setUniDades(groupCheckListDto.getUniDades());
        groupCheckList.setPriceUnit(groupCheckListDto.getPriceUnit());
        groupCheckList.setRef(groupCheckListDto.getRef());
        groupCheckList.setGroup(groupService.findById(groupCheckListDto.getGroupId()));
        return groupCheckListRepository.save(groupCheckList);
    }

    @Override
    public GroupCheckList findById(Long id, Locale locale) {
        return groupCheckListRepository.findById(id).orElseThrow(() -> new NotFoundException(messageSource.getMessage("group-checklist.not.found", null, locale)));
    }

    @Override
    public List<GroupCheckList> findAll() {
        return groupCheckListRepository.findAll();
    }

    @Override
    public void delete(Long id, Locale locale) {
        findById(id, locale);
        groupCheckListRepository.deleteById(id);
    }
}
