package basico.task.management.service.impl;

import basico.task.management.constant.AppConstants;
import basico.task.management.dto.CategoryDTO;
import basico.task.management.dto.CategoryUploadDTO;
import basico.task.management.dto.SubCategoryDTO;
import basico.task.management.enums.Status;
import basico.task.management.exception.AlreadyExistException;
import basico.task.management.exception.NotFoundException;
import basico.task.management.exception.UnAuthorizedException;
import basico.task.management.model.primary.Category;
import basico.task.management.model.primary.Role;
import basico.task.management.model.primary.SubCategory;
import basico.task.management.repository.primary.SubCategoryRepository;
import basico.task.management.service.*;
import basico.task.management.util.FileUtil;
import basico.task.management.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {

    private final CategoryService categoryService;
    private final SubCategoryRepository subCategoryRepository;
    private final RoleService roleService;
    private final UserService userService;
    private final MessageSource messageSource;
    private final FtpService ftpService;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;

    @Override
    public SubCategory save(SubCategoryDTO dto, String token, Locale locale) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.isNull(role)) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }

        Category category = categoryService.findById(dto.getCategoryId(), locale);
        if (subCategoryRepository.findByName(dto.getName()).isPresent()) {
            throw new AlreadyExistException(messageSource.getMessage("sub.category.name.already.exist", null, locale));
        }
        SubCategory subCategory = new SubCategory();
        subCategory.setName(dto.getName());
        subCategory.setCategory(category);
        subCategory.setCode(dto.getCode());
        subCategory.setCapex(dto.getCapex());
        if (role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_SUPPLIER)) {
            subCategory.setStatus(Status.PENDING.name());
            subCategory.setSupplier(userService.findById(Long.parseLong(userId), locale));
        } else {
            subCategory.setStatus(Status.APPROVED.name());
        }

        if (!dto.getFile().isEmpty() || dto.getFile() != null) {
            UUID uuid = UUID.randomUUID();
            String path = ftpService.uploadFileCategory(httpServletRequest, httpServletResponse, dto.getFile(), dto.getFile().getOriginalFilename(), uuid.toString());
            subCategory.setFilePath(path + dto.getFile().getOriginalFilename());
            subCategory.setFileUUID(uuid.toString());
            subCategory.setFileName(dto.getFile().getOriginalFilename());
            subCategory.setFile(dto.getFile().getBytes());
        }
        return subCategoryRepository.save(subCategory);
    }

    @Override
    public SubCategory update(Long id, SubCategoryDTO dto, String token, Locale locale) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.isNull(role)) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
        Category category = categoryService.findById(dto.getCategoryId(), locale);
        SubCategory subCategory = findById(id, locale);
        Optional<SubCategory> optionalCategory = subCategoryRepository.findByName(dto.getName());
        if (optionalCategory.isPresent() && !subCategory.getId().equals(optionalCategory.get().getId())) {
            throw new AlreadyExistException(messageSource.getMessage("sub.category.name.already.exist", null, locale));
        }

        subCategory.setName(dto.getName());
        subCategory.setCategory(category);
        subCategory.setCode(dto.getCode());
        subCategory.setCapex(dto.getCapex());
        if (role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_SUPPLIER)) {
            if (category.getStatus().equals(Status.APPROVED.name())) {
                throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
            }
        } else {
            subCategory.setStatus(Status.APPROVED.name());
        }

        if (!dto.getFile().isEmpty() || dto.getFile() != null) {
            UUID uuid = UUID.randomUUID();
            String path = ftpService.uploadFileCategory(httpServletRequest, httpServletResponse, dto.getFile(), dto.getFile().getOriginalFilename(), uuid.toString());
            subCategory.setFilePath(path + dto.getFile().getOriginalFilename());
            subCategory.setFileUUID(uuid.toString());
            subCategory.setFileName(dto.getFile().getOriginalFilename());
            subCategory.setFile(dto.getFile().getBytes());
        }
        return subCategoryRepository.save(subCategory);
    }

    @Override
    public SubCategory findById(Long id, Locale locale) {
        return subCategoryRepository.findById(id).orElseThrow(() -> new NotFoundException(messageSource.getMessage("category.not.found", null, locale)));
    }

    @Override
    public List<SubCategory> findAll() {
        return subCategoryRepository.findAll(Sort.by("category.name").ascending().and(Sort.by("name").ascending()));
    }

    @Override
    public List<Category> findAllCategoryAndSubCategories() {
        List<Category> categoryList = new ArrayList<>();
        List<SubCategory> subCategoryList = subCategoryRepository.findAll(Sort.by("id").ascending());
        List<Category> categories = categoryService.findAll();
        categories.forEach(c -> {
            List<SubCategory> subCategories = subCategoryList.stream().filter(s -> s.getCategory().getId().equals(c.getId())).collect(Collectors.toList());
            if (!subCategories.isEmpty()) {
                c.setSubCategoryList(subCategories);
            }
            categoryList.add(c);
        });
        return categoryList;
    }

    @Override
    public List<SubCategory> findAllByCategoryId(Long categoryId) {
        return subCategoryRepository.findAllByCategoryIdOrderByIdAsc(categoryId);
    }

    @Override
    public Optional<SubCategory> findByName(String name) {
        return subCategoryRepository.findByName(name);
    }

    @Override
    public List<SubCategory> uploadFile(MultipartFile file, String token, Locale locale) throws Exception {
        List<CategoryUploadDTO> categoryUploadDTOList = FileUtil.readCategoryUploadDTO(file.getInputStream());
        List<SubCategory> subCategoryList = new ArrayList<>();
        categoryUploadDTOList.forEach(categoryDto -> {
            Optional<Category> optionalCategory = categoryService.findByName(categoryDto.getCategoryName());
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setName(categoryDto.getCategoryName());
            Category category = new Category();
            if (optionalCategory.isPresent()) {
                category = optionalCategory.get();
            } else {
                try {
                    category = categoryService.save(categoryDTO, token, locale);
                } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
                    e.printStackTrace();
                }
            }
            Optional<SubCategory> optionalSubCategory = subCategoryRepository.findByCategoryIdAndName(category.getId(), categoryDto.getSubCategoryName());
            if (optionalSubCategory.isEmpty()) {
                SubCategoryDTO subCategoryDTO = new SubCategoryDTO();
                subCategoryDTO.setName(categoryDto.getSubCategoryName());
                subCategoryDTO.setCategoryId(category.getId());
                SubCategory subCategory = new SubCategory();
                try {
                    subCategory = save(subCategoryDTO, token, locale);
                } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
                    e.printStackTrace();
                }
                subCategoryList.add(subCategory);
            }
        });
        return subCategoryList;
    }

    @Override
    public void delete(Long id, Locale locale) {
        findById(id, locale);
        subCategoryRepository.deleteById(id);
    }

    @Override
    public List<SubCategory> findAllSubCategoryBySupplierId(String token) {
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        return subCategoryRepository.findAllBySupplierIdOrderByIdAsc(Long.parseLong(userId));
    }

    @Override
    public List<SubCategory> findAllPendingSubCategory() {
        return subCategoryRepository.findAllByStatusOrderByIdAsc(Status.PENDING.name());
    }

    @Override
    public SubCategory updateStatus(Long id, String statusName, String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.isNull(role) || !role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_TECHNICAL_USER)) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
        SubCategory subCategory = findById(id, locale);
        subCategory.setStatus(statusName);
        return subCategoryRepository.save(subCategory);
    }

    @Override
    public void downloadFile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Long id, Locale locale) throws NoSuchAlgorithmException, IOException, KeyManagementException {
        SubCategory subCategory = findById(id, locale);
        ftpService.downloadFile(httpServletRequest, httpServletResponse, subCategory.getFilePath());
    }
}
