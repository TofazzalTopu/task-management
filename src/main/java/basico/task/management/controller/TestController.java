package basico.task.management.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/test")
public class TestController {


    public void face(MultipartFile file) throws IOException {

//        loadLibrary( Core.NATIVE_LIBRARY_NAME );
//        CascadeClassifier faceDetector = new CascadeClassifier();
//        faceDetector.load( "C:\\Users\\Desktop\\ocv\\.idea\\haarcascade_frontalface_alt.xml" );
//
//        // Input image
//
//        com.ocr.ocrexample.FileUploadController.convert(file);
//        Mat image = Imgcodecs.imread(String.valueOf(file));
//// Detecting faces
//        MatOfRect faceDetections = new MatOfRect();
//        faceDetector.detectMultiScale( image, faceDetections );
//
//        // Creating a rectangular box showing faces detected
//        for (Rect rect : faceDetections.toArray()) {
//            rectangle( image, new Point( rect.x, rect.y ), new Point( rect.width + rect.x,
//                    rect.height + rect.y ), new Scalar( 0, 255, 0 ) );
//        }
//
//        // Saving the output image
//        String filename = "Ouput.jpg";
//        System.out.println("Face Detected Successfully ");
//        Imgcodecs.imwrite( "D:\\" + filename, image );


    }
}
