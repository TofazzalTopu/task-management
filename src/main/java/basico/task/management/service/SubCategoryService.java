package basico.task.management.service;

import basico.task.management.dto.SubCategoryDTO;
import basico.task.management.model.primary.Category;
import basico.task.management.model.primary.SubCategory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface SubCategoryService {

    SubCategory save(SubCategoryDTO subCategory, String token, Locale locale) throws IOException, NoSuchAlgorithmException, KeyManagementException;

    SubCategory update(Long id, SubCategoryDTO dto, String token, Locale locale) throws IOException, NoSuchAlgorithmException, KeyManagementException;

    SubCategory findById(Long id, Locale locale);

    List<SubCategory> findAll();

    List<Category> findAllCategoryAndSubCategories();

    List<SubCategory> findAllByCategoryId(Long categoryId);

    Optional<SubCategory> findByName(String name);

    List<SubCategory> uploadFile(MultipartFile file, String token, Locale locale) throws Exception;

    void delete(Long id, Locale locale);

    List<SubCategory> findAllSubCategoryBySupplierId(String token);

    List<SubCategory> findAllPendingSubCategory();

    SubCategory updateStatus(Long id, String statusName, String token, Locale locale);

    void downloadFile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Long id, Locale locale) throws NoSuchAlgorithmException, IOException, KeyManagementException;
}
