package basico.task.management.service.impl;

import basico.task.management.constant.AppConstants;
import basico.task.management.dto.CategoryDTO;
import basico.task.management.enums.Status;
import basico.task.management.exception.AlreadyExistException;
import basico.task.management.exception.NotFoundException;
import basico.task.management.exception.UnAuthorizedException;
import basico.task.management.model.primary.Category;
import basico.task.management.model.primary.Role;
import basico.task.management.model.primary.SubCategory;
import basico.task.management.repository.primary.CategoryRepository;
import basico.task.management.service.CategoryService;
import basico.task.management.service.FtpService;
import basico.task.management.service.RoleService;
import basico.task.management.service.UserService;
import basico.task.management.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final MessageSource messageSource;
    private final RoleService roleService;
    private final UserService userService;
    private final FtpService ftpService;
    private final HttpServletResponse httpServletResponse;
    private final HttpServletRequest httpServletRequest;

    @Override
    public Category save(CategoryDTO categoryDTO, String token, Locale locale) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.isNull(role)) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
        if (categoryRepository.findByName(categoryDTO.getName()).isPresent()) {
            throw new AlreadyExistException(messageSource.getMessage("category.name.already.exist", null, locale));
        }
        Category category = Category.builder().name(categoryDTO.getName()).file(categoryDTO.getFile().getBytes()).build();
        category.setFileName(categoryDTO.getFile().getOriginalFilename());
        UUID uuid= UUID.randomUUID();
        String path=  ftpService.uploadFileCategory(httpServletRequest,httpServletResponse,categoryDTO.getFile(),categoryDTO.getFile().getOriginalFilename(),uuid.toString());
        category.setFilePath(path+categoryDTO.getFile().getOriginalFilename());
        category.setFileUUID(uuid.toString());
        if (role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_SUPPLIER)) {
            category.setStatus(Status.PENDING.name());
            category.setSupplier(userService.findById(Long.parseLong(userId), locale));
        } else {
            category.setStatus(Status.APPROVED.name());
        }

        return categoryRepository.save(category);
    }


    @Override
    public Category update(Long id, CategoryDTO categoryDTO, String token, Locale locale) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.isNull(role)) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
        Category category = findById(id, locale);
        Optional<Category> optionalCategory = categoryRepository.findByName(categoryDTO.getName());

        if (optionalCategory.isPresent() && !category.getId().equals(optionalCategory.get().getId())) {
            throw new AlreadyExistException(messageSource.getMessage("category.name.already.exist", null, locale));
        }
        category.setName(categoryDTO.getName());
        if (role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_SUPPLIER)) {
            if (category.getStatus().equals(Status.APPROVED.name())) {
                throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
            }
        } else {
            category.setStatus(Status.APPROVED.name());
        }
        if (Objects.nonNull(categoryDTO.getFile()) && !categoryDTO.getFile().isEmpty()) {
            UUID uuid= UUID.randomUUID();
            String path=  ftpService.uploadFileCategory(httpServletRequest,httpServletResponse,categoryDTO.getFile(),categoryDTO.getFile().getOriginalFilename(),uuid.toString());
            category.setFilePath(path+categoryDTO.getFile().getOriginalFilename());
            category.setFileUUID(uuid.toString());
            category.setFileName(categoryDTO.getFile().getOriginalFilename());
            category.setFile(categoryDTO.getFile().getBytes());
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category findById(Long id, Locale locale) {
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException(messageSource.getMessage("category.not.found", null, locale)));
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAllByStatusOrderByIdAsc(Status.APPROVED.name());
    }

    @Override
    public void delete(Long id, Locale locale) {
        findById(id, locale);
        categoryRepository.deleteById(id);
    }

    @Override
    public Category updateStatus(Long id, String statusName, String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.isNull(roleId) || !role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_TECHNICAL_USER)) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
        Category category = findById(id, locale);
        category.setStatus(statusName);
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findAllCategoryBySupplierId(String token) {
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        return categoryRepository.findAllBySupplierIdOrderByIdAsc(Long.parseLong(userId));
    }

    @Override
    public List<Category> findAllPendingCategory() {
        return categoryRepository.findAllByStatusOrderByIdAsc(Status.PENDING.name());
    }

    @Override
    public void downloadFile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Long id, Locale locale) throws NoSuchAlgorithmException, IOException, KeyManagementException {
        Category category=findById(id,locale);
        ftpService.downloadFile(httpServletRequest,httpServletResponse,category.getFilePath());
    }
}
