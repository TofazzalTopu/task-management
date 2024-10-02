package basico.task.management.service.impl;

import basico.task.management.constant.AppConstants;
import basico.task.management.exception.NotFoundException;
import basico.task.management.model.primary.Role;
import basico.task.management.repository.primary.RoleRepository;
import basico.task.management.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final MessageSource messageSource;

    @Override
    public Role save(String name) {
        Role role = new Role();
        role.setName(name);
        if (name.equalsIgnoreCase(AppConstants.LDAP_GROUP_TECNICOS)) {
            role.setLabel(AppConstants.ROLE_LABEL_TECHNICAL_USER);
        } else if (name.equalsIgnoreCase(AppConstants.LDAP_GROUP_PORTFOLIO_MNG)) {
            role.setLabel(AppConstants.ROLE_LABEL_PORTFOLIO_MANAGER);
        } else if (name.equalsIgnoreCase(AppConstants.ROLE_NAME_SUPPLIER)) {
            role.setLabel(AppConstants.ROLE_LABEL_SUPPLIER);
        } else if (name.equalsIgnoreCase(AppConstants.LDAP_GROUP_TECHNOLOGIA)) {
            role.setLabel(AppConstants.ROLE_LABEL_TECHNICAL_MANAGER);
        } else if (name.equalsIgnoreCase(AppConstants.ROLE_NAME_ADMIN)) {
            role.setLabel(AppConstants.ROLE_LABEL_ADMIN);
        }
        return roleRepository.save(role);
    }

    @Override
    public Role update(Long id, String name, Locale locale) {
        Role role = findById(id, locale);
        role.setName(name);
        if (name.equalsIgnoreCase(AppConstants.LDAP_GROUP_TECNICOS)) {
            role.setLabel(AppConstants.ROLE_LABEL_TECHNICAL_USER);
        } else if (name.equalsIgnoreCase(AppConstants.LDAP_GROUP_PORTFOLIO_MNG)) {
            role.setLabel(AppConstants.ROLE_LABEL_PORTFOLIO_MANAGER);
        } else if (name.equalsIgnoreCase(AppConstants.ROLE_NAME_SUPPLIER)) {
            role.setLabel(AppConstants.ROLE_LABEL_SUPPLIER);
        } else if (name.equalsIgnoreCase(AppConstants.LDAP_GROUP_TECHNOLOGIA)) {
            role.setLabel(AppConstants.ROLE_LABEL_TECHNICAL_MANAGER);
        } else if (name.equalsIgnoreCase(AppConstants.ROLE_NAME_ADMIN)) {
            role.setLabel(AppConstants.ROLE_LABEL_ADMIN);
        }
        return roleRepository.save(role);
    }

    @Override
    public Role findById(Long id, Locale locale) {
        return roleRepository.findById(id).orElseThrow(() -> new NotFoundException(messageSource.getMessage("role.not.found", null, locale)));
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll(Sort.by("name").ascending());
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public List<Role> findAllByNames(List<String> names) {
        return roleRepository.findAllByNameIn(names);
    }

    @Override
    public List<Role> findAllByLabels(List<String> labels) {
        return roleRepository.findAllByLabelIn(labels);
    }

    @Override
    public void delete(Long id, Locale locale) {
        findById(id, locale);
        roleRepository.deleteById(id);
    }

}
