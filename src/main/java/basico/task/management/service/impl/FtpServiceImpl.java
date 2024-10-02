package basico.task.management.service.impl;

import basico.task.management.ftp.FTP;
import basico.task.management.model.primary.Task;
import basico.task.management.service.FtpService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Service
public class FtpServiceImpl implements FtpService {


    @Override
    public String uploadFile(HttpServletRequest req, HttpServletResponse resp, MultipartFile file, String fileName, Task task, String uuid) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        String path = "/PROVEEDOR/" +task.getId() + "/"+uuid  ;
        FTP.UploadFile(req, resp, path, file, fileName);
        return path+"/";
    }

    @Override
    public void downloadFile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String filePath) throws NoSuchAlgorithmException, IOException, KeyManagementException {
            FTP.DownloadFile(httpServletRequest,httpServletResponse,filePath,false);
    }

    @Override
    public String uploadFileCategory(HttpServletRequest req, HttpServletResponse resp, MultipartFile file, String fileName, String uuid) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        String path = "/PROVEEDOR/" +"CATEGORY" + "/"+uuid  ;
        FTP.UploadFile(req, resp, path, file, fileName);
        return path+"/";
    }

    @Override
    public String uploadFileSubcategory(HttpServletRequest req, HttpServletResponse resp, MultipartFile file, String fileName, String uuid) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        String path = "/PROVEEDOR/" +"SUBCATEGORY" + "/"+uuid  ;
        FTP.UploadFile(req, resp, path, file, fileName);
        return path+"/";
    }
}
