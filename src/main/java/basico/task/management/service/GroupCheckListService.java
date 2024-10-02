package basico.task.management.service;

import basico.task.management.dto.GroupCheckListDto;
import basico.task.management.model.primary.GroupCheckList;

import java.util.List;
import java.util.Locale;

public interface GroupCheckListService {

    GroupCheckList save(GroupCheckListDto groupCheckListDto, Locale locale);
    GroupCheckList update(Long id, GroupCheckListDto groupCheckListDto, Locale locale);
    GroupCheckList findById(Long id, Locale locale);
    List<GroupCheckList> findAll();
    void delete(Long id, Locale locale);

}
