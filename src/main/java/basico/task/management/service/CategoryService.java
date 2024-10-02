package basico.task.management.service;

import basico.task.management.dto.CategoryDTO;
import basico.task.management.model.primary.Category;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface CategoryService {

    Category save(CategoryDTO categoryDTO, String token, Locale locale) throws IOException, NoSuchAlgorithmException, KeyManagementException;

    Category update(Long id, CategoryDTO categoryDTO, String token, Locale locale) throws IOException, NoSuchAlgorithmException, KeyManagementException;

    Category findById(Long id, Locale locale);

    Optional<Category> findByName(String name);

    List<Category> findAll();

    void delete(Long id, Locale locale);

    Category updateStatus(Long id, String statusName, String token, Locale locale);

    List<Category> findAllCategoryBySupplierId(String token);

    List<Category> findAllPendingCategory();

    void downloadFile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Long id, Locale locale) throws NoSuchAlgorithmException, IOException, KeyManagementException;
}
