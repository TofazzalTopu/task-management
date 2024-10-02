package basico.task.management.service.impl;

import basico.task.management.constant.AppConstants;
import basico.task.management.dto.FilterDto;
import basico.task.management.enums.Entity;
import basico.task.management.enums.Status;
import basico.task.management.exception.NotFoundException;
import basico.task.management.model.primary.GlpiTask;
import basico.task.management.model.primary.Role;
import basico.task.management.model.primary.UserProfile;
import basico.task.management.repository.primary.GlpiTaskRepository;
import basico.task.management.service.GlpiTaskService;
import basico.task.management.service.RoleService;
import basico.task.management.service.StatusService;
import basico.task.management.service.UserService;
import basico.task.management.service.glpi.GlpiService;
import basico.task.management.util.Numeric;
import basico.task.management.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GlpiTaskServiceImpl implements GlpiTaskService {

    private final GlpiTaskRepository glpiTaskRepository;
    private final GlpiService glpiService;
    private final MessageSource messageSource;
    private final StatusService statusService;
    private final UserService userService;
    private final RoleService roleService;

    @Scheduled(cron = "0 0 0 * * *")
    private void saveAcdcTask() {
        basico.task.management.model.primary.Status status = statusService.findByNameAndEntity(Status.NEW.name(), Entity.TASK.name());
        List<GlpiTask> glpiTasks = glpiService.taskList(status);
        for (GlpiTask glpiExt : glpiTasks) {
            GlpiTask glpiTaskDb = glpiTaskRepository.findByTicketId(glpiExt.getTicketId());
            if (Objects.isNull(glpiTaskDb)) {
                glpiTaskRepository.save(glpiExt);
            }
        }
    }

    @Override
    public GlpiTask save(GlpiTask glpiTask) throws Exception {
        return glpiTaskRepository.save(glpiTask);
    }

    @Override
    public GlpiTask update(Long id, GlpiTask glpiTask, Locale locale) throws Exception {
        GlpiTask savedGlpiTask = findById(id, locale);
        savedGlpiTask.setCode(glpiTask.getCode());
        savedGlpiTask.setCity(glpiTask.getCity());
        savedGlpiTask.setDirection(glpiTask.getDirection());
        savedGlpiTask.setProvince(glpiTask.getProvince());
        return glpiTaskRepository.save(savedGlpiTask);
    }

    @Override
    public GlpiTask updateStatus(Long id, Locale locale) throws Exception {
        GlpiTask savedGlpiTask = findById(id, locale);
        basico.task.management.model.primary.Status status = statusService.findByNameAndEntity(Status.PROCESSED.name(), Entity.TASK.name());
        savedGlpiTask.setStatus(status);
        return glpiTaskRepository.save(savedGlpiTask);
    }

    @Override
    public Page<GlpiTask> filterGlpi(Pageable pageable, String token, String searchText, Locale locale) {
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        UserProfile userProfile = userService.findById(Long.parseLong(userId), locale);

        String uppcaseFilter = null;
        Long id = (long) 0;
        if (Numeric.isNumeric(searchText)) {
            id = Long.parseLong(searchText);
            uppcaseFilter = searchText;
        } else {
            uppcaseFilter = searchText.toUpperCase();
        }

        if (role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_ADMIN)) {
            return glpiTaskRepository.findAllByIdIsLikeOrTicketIdIsLikeOrLocation_DescriptionContainsOrPromotion_PromotionNameContainsOrSociety_SocietyNameContainsOrGarajes_DescriptionContainsOrStorageRoom_DescriptionContainsOrProvinceContainsOrDirectionContainsOrCityContains(pageable, id, id, searchText, searchText, searchText, searchText, searchText, searchText, searchText, searchText);
        } else {
            List<GlpiTask> glpiTaskList = glpiTaskRepository.findAllByEmailAndIdIsLikeOrTicketIdIsLikeOrLocationDescriptionContainsOrPromotionPromotionNameContainsOrSocietySocietyNameContainsOrGarajesDescriptionContainsOrStorageRoomDescriptionContainsOrProvinceContainsOrDirectionContainsOrCityContains(pageable, userProfile.getEmail(), id, id, searchText, searchText, searchText, searchText, searchText, searchText, searchText, searchText)
                    .stream().filter(g-> g.getEmail().equals(userProfile.getEmail())).collect(Collectors.toList());
            final int start = (int)pageable.getOffset();
            final int end = Math.min((start + pageable.getPageSize()), glpiTaskList.size());
            return new PageImpl<>(glpiTaskList.subList(start, end), pageable, glpiTaskList.size());
        }
    }

    @Override
    public Page<GlpiTask> findAll(Pageable pageable, FilterDto filterDto, String token, Locale locale) {
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        UserProfile userProfile = userService.findById(Long.parseLong(userId), locale);
        if (role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_ADMIN)) {
            if (Objects.isNull(filterDto.getEmail()) || Objects.isNull(filterDto.getUserGroup())) {
                return glpiTaskRepository.findAll(pageable);
            } else {
                return glpiTaskRepository.findByUserGroupContainsOrEmailContains(pageable, filterDto.getUserGroup(), filterDto.getEmail());
            }
        } else {
            if (Objects.isNull(filterDto.getEmail()) || Objects.isNull(filterDto.getUserGroup())) {
                return glpiTaskRepository.findAllByEmail(pageable, userProfile.getEmail());
            } else {
                return glpiTaskRepository.findByUserGroupContainsAndEmail(pageable, filterDto.getUserGroup(), userProfile.getEmail());
            }
        }

    }

    @Override
    public GlpiTask findById(Long id, Locale locale) throws Exception {
        return glpiTaskRepository.findById(id).orElseThrow(() -> new NotFoundException(messageSource.getMessage("glpitask.not.found", null, locale)));
    }

    @Override
    public void delete(Long id, Locale locale) throws Exception {
        findById(id, locale);
        glpiTaskRepository.deleteById(id);
    }
}
