package basico.task.management.ftp;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class FTP {

	public static HttpServletResponse UploadDocu(HttpServletRequest req, HttpServletResponse resp, String path,
                                                 MultipartFile file) throws IOException, KeyManagementException, NoSuchAlgorithmException {

		try {
			return MultipartUtility.uploadFTPDocu(req, resp, path, file);
		} catch (IOException ex) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return resp;
		}
	}

	public static HttpServletResponse UploadFile(HttpServletRequest req, HttpServletResponse resp, String path,
                                                 MultipartFile file, String fileName) throws IOException, KeyManagementException, NoSuchAlgorithmException {
		try {
			return MultipartUtility.uploadFTPFile(req, resp, path, file, fileName);

		} catch (IOException ex) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return resp;
		}

	}

	private static final int BUFFER_SIZE = 4096;

	/**
	 * Downloads a file from a URL
	 * 
	 * @param fileURL HTTP URL of the file to be downloaded
	 * @param saveDir path of the directory to save the file
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static String DownloadFile(HttpServletRequest req, HttpServletResponse resp, String fileURL,
                                      Boolean createFile) throws IOException, KeyManagementException, NoSuchAlgorithmException {

		try {
			return MultipartUtility.downloadFTPFile(req, resp, fileURL, createFile);

		} catch (IOException ex) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return "";
		}
	}
	
	public static String LastModified(HttpServletRequest req, HttpServletResponse resp, String fileURL) throws IOException, KeyManagementException, NoSuchAlgorithmException {

		try {
			return MultipartUtility.lastModified(req, resp,fileURL);
		} catch (IOException ex) {
			return "";
		}
	}
}
