package basico.task.management.service;

import basico.task.management.model.primary.Task;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public interface FtpService {

    public String uploadFile(HttpServletRequest req, HttpServletResponse resp,
                             MultipartFile file, String fileName, Task task, String s)
            throws IOException, KeyManagementException, NoSuchAlgorithmException;

    void downloadFile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String filePath) throws NoSuchAlgorithmException, IOException, KeyManagementException;

    public String uploadFileCategory(HttpServletRequest req, HttpServletResponse resp,
                             MultipartFile file, String fileName, String uuid)
            throws IOException, KeyManagementException, NoSuchAlgorithmException;

    public String uploadFileSubcategory(HttpServletRequest req, HttpServletResponse resp,
                                     MultipartFile file, String fileName,  String uuid)
            throws IOException, KeyManagementException, NoSuchAlgorithmException;

}
