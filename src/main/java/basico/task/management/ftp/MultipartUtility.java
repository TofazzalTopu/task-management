
package basico.task.management.ftp;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class MultipartUtility {
	private final String boundary;
	private static final String LINE_FEED = "\r\n";
	private HttpURLConnection httpConn;
	private String charset;
	private OutputStream outputStream;
	private PrintWriter writer;
	private final static String server="basnas.basico.es";
	private final static int portServer=21;
	//private final static String server="195.76.199.234";
	//private final static int portServer=2121;
	private final static String userServer="online";
	private final static String passServer="DjS17\\kt";
	private final static String serverBack="/opt/wildfly-16.0.0.Final";

	/**
	 * This constructor initializes a new HTTP POST request with content type is set
	 * to multipart/form-data
	 * 
	 * @param requestURL
	 * @param charset
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	public MultipartUtility(String requestURL, String charset)
			throws IOException, KeyManagementException, NoSuchAlgorithmException {
		this.charset = charset;

		// creates a unique boundary based on time stamp
		boundary = "===" + System.currentTimeMillis() + "===";
		/*
		 * SSLContext context = SSLContext.getInstance("TLS"); TrustManager tmf;
		 * context.init(null, tmf, null); SSLSocketFactory sf =
		 * context.getSocketFactory();
		 */
		/*
		 * SSLContext sc = SSLContext.getInstance("SSL"); sc.init(null,
		 * getTrustManager() , new java.security.SecureRandom());
		 */
		URL url = new URL(requestURL);
		httpConn = (HttpURLConnection) url.openConnection();
		// httpConn.setSSLSocketFactory(sc.getSocketFactory());
		httpConn.setUseCaches(false);
		httpConn.setDoOutput(true); // indicates POST method
		httpConn.setDoInput(true);
		httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
		if (url.getUserInfo() != null) {
			String userpass = "online:DjS17\\kt";
			String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
			httpConn.setRequestProperty("Authorization", basicAuth);
		}
		httpConn.setRequestProperty("Test", "Bonjour");
		outputStream = httpConn.getOutputStream();
		writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
	}

	/**
	 * Adds a form field to the request
	 * 
	 * @param name  field name
	 * @param value field value
	 */
	public void addFormField(String name, String value) {
		writer.append("--" + boundary).append(LINE_FEED);
		writer.append("Content-Disposition: form-data; name=\"" + name + "\"").append(LINE_FEED);
		writer.append("Content-Type: text/plain; charset=" + charset).append(LINE_FEED);
		writer.append(LINE_FEED);
		writer.append(value).append(LINE_FEED);
		writer.flush();
	}

	/**
	 * Adds a upload file section to the request
	 * 
	 * @param fieldName  name attribute in <input type="file" name="..." />
	 * @param uploadFile a File to be uploaded
	 * @throws IOException
	 */
	public void addFilePart(String fieldName, File uploadFile) throws IOException {
		String fileName = uploadFile.getName();
		writer.append("--" + boundary).append(LINE_FEED);
		writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"")
				.append(LINE_FEED);
		writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
		writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
		writer.append(LINE_FEED);
		writer.flush();

		FileInputStream inputStream = new FileInputStream(uploadFile);
		byte[] buffer = new byte[4096];
		int bytesRead = -1;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}
		outputStream.flush();
		inputStream.close();

		writer.append(LINE_FEED);
		writer.flush();
	}

	/**
	 * Adds a header field to the request.
	 * 
	 * @param name  - name of the header field
	 * @param value - value of the header field
	 */
	public void addHeaderField(String name, String value) {
		writer.append(name + ": " + value).append(LINE_FEED);
		writer.flush();
	}

	/**
	 * Completes the request and receives response from the server.
	 * 
	 * @return a list of Strings as response in case the server returned status OK,
	 *         otherwise an exception is thrown.
	 * @throws IOException
	 */
	public List<String> finish() throws IOException {
		List<String> response = new ArrayList<String>();

		writer.append(LINE_FEED).flush();
		writer.append("--" + boundary + "--").append(LINE_FEED);
		writer.close();

		// checks server's status code first
		int status = httpConn.getResponseCode();
		if (status == HttpURLConnection.HTTP_OK) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				response.add(line);
			}
			reader.close();
			httpConn.disconnect();
		} else {
			throw new IOException("Server returned non-OK status: " + status);
		}

		return response;
	}

	public static String downloadFTPFile(HttpServletRequest req, HttpServletResponse resp, String fileURL,
                                         Boolean createFile) throws IOException, NoSuchAlgorithmException, KeyManagementException {

		String fileName = "";
		FTPClient ftpClient = new FTPClient();
		String path = "";
		try {
			ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
			ftpClient.connect(server, portServer);
			ftpClient.login(userServer, passServer);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			// String remoteFile1="";
			String remoteFile1 = fileURL;
			// ServletContext cntx= req.getServletContext();
			InputStream in = null;
			try {

				fileName = Paths.get(new URI(URLEncoder.encode(fileURL, "UTF-8")).getPath()).getFileName().toString();
				in = ftpClient.retrieveFileStream(new URI(URLEncoder.encode(remoteFile1, "UTF-8")).getPath());
			} catch (Exception ex) {
				//ex.printStackTrace();
			}
			// retrieve mimeType dynamically
			/*
			 * String mime = cntx.getMimeType(fileURL); if (mime == null) {
			 * resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); return resp; }
			 *
			 * resp.setContentType(mime); File file = new File(fileURL);
			 * resp.setContentLength((int)file.length()); //InputStream is =
			 * ftpClient.retrieveFileStream(fileURL); FileInputStream in = new
			 * FileInputStream(file); OutputStream out = resp.getOutputStream(); // Copy the
			 * contents of the file to the output stream byte[] buf = new byte[4096]; int
			 * count = 0; while ((count = in.read(buf)) >= 0) { out.write(buf, 0, count); }
			 *
			 * out.close(); in.close(); return resp;
			 */

			/*
			 * File downloadFile1 = new File(fileName); OutputStream outputStream1 = new
			 * BufferedOutputStream(new FileOutputStream(downloadFile1)); boolean success =
			 * ftpClient.retrieveFile(remoteFile1, outputStream1);
			 */

			// Copy the contents of the file to the output stream
			if(ftpClient.getReplyCode()==550) {
				try {
					File file = new File(remoteFile1);
					in = ftpClient.retrieveFileStream(file.getPath());
					fileName = Paths.get(fileURL).getFileName().toString();
				}catch(Exception ex) {
				}
				
			}

			if(ftpClient.getReplyCode()==550) {
				try {
					File file = new File(new String(remoteFile1.getBytes(), "ISO-8859-15"));
					in = ftpClient.retrieveFileStream(file.getPath());
					fileName = Paths.get(new String(remoteFile1.getBytes(), "ISO-8859-15")).getFileName().toString();
				}catch(Exception ex) {
				}
				
			}
			if(ftpClient.getReplyCode()!=550) {
				if (!createFile) {
					resp.addHeader("Content-Disposition", "inline; filename=\"" + fileName+"\"");

					if (FilenameUtils.getExtension(fileName).equals("pdf")) {
						resp.setContentType("application/pdf");
					} else {
						resp.setContentType("image/" + FilenameUtils.getExtension(fileName));
					}
				}



				byte[] buf = new byte[1024];
				int count = 0;
				if (createFile) {
					//File fileCreatedDirectory = new File(pathDirectory);
					//fileCreatedDirectory.mkdirs();
					//path = "/opt/wildfly-16.0.0.Final/standalone/log/"+pathDirectory+"/" + fileName;
					path = serverBack+"/temp/" + fileName;
					File fileTemp = new File(path);
					OutputStream outStream = new FileOutputStream(fileTemp);
					while ((count = in.read(buf)) >= 0) {
						outStream.write(buf, 0, count);
					}
					outStream.close();
					/*if(resp.getHeaders("path").size()>0) {
						resp.setHeader("path", path);
					}else {*/
						resp.addHeader("path", path);
					//}	
				} else {
					OutputStream out = resp.getOutputStream();
					while ((count = in.read(buf)) >= 0) {
						out.write(buf, 0, count);
					}
					out.close();
				} // outputStream1.close();
				in.close();
				ftpClient.completePendingCommand();
			}

			return path;

		} catch (IOException ex) {
			ex.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return path;
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				// ex.printStackTrace();
				ex.getMessage();
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return path;
			}
		}
	}
	
	public static String lastModified(HttpServletRequest req, HttpServletResponse resp, String fileURL) throws IOException, NoSuchAlgorithmException, KeyManagementException {
		
		String fileName = "";

		FTPClient ftpClient = new FTPClient();
		try {

			ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
			ftpClient.connect(server, portServer);
			ftpClient.login(userServer, passServer);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
			String time =ftpClient.getModificationTime(fileURL);
			String remoteFile1 = fileURL;
			InputStream in = null;
			try {

				

				fileName = Paths.get(new URI(URLEncoder.encode(fileURL, "UTF-8")).getPath()).getFileName().toString();
				in = ftpClient.retrieveFileStream(new URI(URLEncoder.encode(remoteFile1, "UTF-8")).getPath());
			} catch (Exception ex) {
				//ex.printStackTrace();
			}
			// retrieve mimeType dynamically
			/*
			 * String mime = cntx.getMimeType(fileURL); if (mime == null) {
			 * resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); return resp; }
			 * 
			 * resp.setContentType(mime); File file = new File(fileURL);
			 * resp.setContentLength((int)file.length()); //InputStream is =
			 * ftpClient.retrieveFileStream(fileURL); FileInputStream in = new
			 * FileInputStream(file); OutputStream out = resp.getOutputStream(); // Copy the
			 * contents of the file to the output stream byte[] buf = new byte[4096]; int
			 * count = 0; while ((count = in.read(buf)) >= 0) { out.write(buf, 0, count); }
			 * 
			 * out.close(); in.close(); return resp;
			 */
			
			/*
			 * File downloadFile1 = new File(fileName); OutputStream outputStream1 = new
			 * BufferedOutputStream(new FileOutputStream(downloadFile1)); boolean success =
			 * ftpClient.retrieveFile(remoteFile1, outputStream1);
			 */
			File file = new File(remoteFile1);
			// Copy the contents of the file to the output stream
			if(ftpClient.getReplyCode()==550) {
				try {
					in = ftpClient.retrieveFileStream(file.getPath());
					fileName = Paths.get(fileURL).getFileName().toString();
				}catch(Exception ex) {
				}
				
			}

			if(ftpClient.getReplyCode()==550) {
				try {
					file = new File(new String(remoteFile1.getBytes(), "ISO-8859-15"));
					in = ftpClient.retrieveFileStream(file.getPath());
					fileName = Paths.get(new String(remoteFile1.getBytes(), "ISO-8859-15")).getFileName().toString();
				}catch(Exception ex) {
				}
				
			}
			
			if(ftpClient.getReplyCode()!=550) {
				
				Paths.get(fileURL).getParent();
				if(time.length()<3)
					time = ftpClient.getModificationTime(Paths.get(fileURL).getParent()+"/"+fileName);
				String timeResult = time.substring(6,8)+"/"+time.substring(4,6)+"/"+time.substring(0,4);
				resp.addHeader("date", time.substring(6,8)+"/"+time.substring(4,6)+"/"+time.substring(0,4));
				return timeResult;
			}


			return "";

		} catch (IOException ex) {
			ex.printStackTrace();
			return "";
		} finally {
			try {
				if (ftpClient.isConnected()) {
					//ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				// ex.printStackTrace();
				ex.getMessage();
				return "";
			}
		}
	}

	public static HttpServletResponse uploadFTPDocu(HttpServletRequest req, HttpServletResponse resp, String path,
                                                    MultipartFile fileURL) throws IOException, NoSuchAlgorithmException, KeyManagementException {

		FTPClient ftpClient = new FTPClient();
		try {

			ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
			ftpClient.connect(server, portServer);
			ftpClient.login(userServer, passServer);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
			String remoteFile1 = fileURL.getOriginalFilename();
			File fileTemp = new File(serverBack+"/temp/" + System.currentTimeMillis() + remoteFile1);
			fileURL.transferTo(fileTemp);
			fileTemp.setReadable(true, false);
			fileTemp.setExecutable(true, false);
			fileTemp.setWritable(true, false);
			String[] directories = path.split("/");
			String pathDirectory = "";
			for (String directory : directories) {
				if (!directory.equals("")) {
					pathDirectory += "/" + directory;
				}
				if (!pathDirectory.equals("")) {
					try {
						ftpClient.makeDirectory(pathDirectory);
					} catch (IOException e) {
					}
				}
			}
			ftpClient.storeFile(path + remoteFile1, new FileInputStream(fileTemp));
			return resp;

		} catch (IOException ex) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return resp;
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				// ex.printStackTrace();
				ex.getMessage();
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return resp;
			}
		}
	}

	public static HttpServletResponse uploadFTPFile(HttpServletRequest req, HttpServletResponse resp, String path,
                                                    MultipartFile fileURL, String fileName) throws IOException, NoSuchAlgorithmException, KeyManagementException {

		FTPClient ftpClient = new FTPClient();
		try {

			ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
			ftpClient.connect(server, portServer);
			ftpClient.login(userServer, passServer);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
			String remoteFile1 = fileURL.getResource().getFilename();
			File fileTemp = new File(serverBack+"/temp/" + System.currentTimeMillis() + remoteFile1);
			fileURL.transferTo(fileTemp);
			fileTemp.setReadable(true, false);
			fileTemp.setExecutable(true, false);
			fileTemp.setWritable(true, false);
			String[] directories = path.split("/");
			String pathDirectory = "";
			for (String directory : directories) {
				if (!directory.equals("")) {
					pathDirectory += "/" + directory;
				}
				if (!pathDirectory.equals("")) {
					try {
						ftpClient.makeDirectory(pathDirectory);
					} catch (IOException e) {
						// throw new FtpException(e);
					}
				}
			}

			if(!fileName.equals("") && !FilenameUtils.getExtension(fileName).equals(""))
				remoteFile1 = "/"+fileName;
			else if(!fileName.equals(""))
				remoteFile1 = "/"+fileName+"."+FilenameUtils.getExtension(remoteFile1);
			ftpClient.storeFile(path + remoteFile1, new FileInputStream(fileTemp));

			return resp;

		} catch (IOException ex) {
			ex.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return resp;
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				// ex.printStackTrace();
				ex.getMessage();
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return resp;
			}
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
	public static void downloadFile(String fileURL, String saveDir)
			throws IOException, NoSuchAlgorithmException, KeyManagementException {

		URL url = new URL(fileURL);

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, getTrustManager(), new java.security.SecureRandom());
		HttpsURLConnection httpConn = (HttpsURLConnection) url.openConnection();
		if (url.getUserInfo() != null) {
			String userpass = "online:DjS17\\kt";
			String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
			httpConn.setRequestProperty("Authorization", basicAuth);
		}
		httpConn.setHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession sslSession) {
				return true;
			}
		});
		httpConn.setSSLSocketFactory(sc.getSocketFactory());
		int responseCode = httpConn.getResponseCode();

		// always check HTTP response code first
		if (responseCode == HttpsURLConnection.HTTP_OK) {
			String fileName = "";
			String disposition = httpConn.getHeaderField("Content-Disposition");
			String contentType = httpConn.getContentType();
			int contentLength = httpConn.getContentLength();

			if (disposition != null) {
				// extracts file name from header field
				int index = disposition.indexOf("filename=");
				if (index > 0) {
					fileName = disposition.substring(index + 10, disposition.length() - 1);
				}
			} else {
				// extracts file name from URL
				fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
			}



			// opens input stream from the HTTP connection
			InputStream inputStream = httpConn.getInputStream();
			String saveFilePath = saveDir + File.separator + fileName;

			// opens an output stream to save into file
			FileOutputStream outputStream = new FileOutputStream(saveFilePath);

			int bytesRead = -1;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			outputStream.close();
			inputStream.close();

		} else {
		}
		httpConn.disconnect();
	}

	private static TrustManager[] getTrustManager() {
		TrustManagerFactory trustManagerFactory;

		try {
			trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init((KeyStore) null);

			TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

			if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
				return null;
			}
			return trustManagers;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();

			return null;
		} catch (KeyStoreException e) {
			e.printStackTrace();

			return null;
		}
	}
}
