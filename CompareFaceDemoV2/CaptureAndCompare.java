import com.amazonaws.services.rekognition.model.CompareFacesResult;
import org.bytedeco.javacv.*;
import org.bytedeco.javacpp.opencv_core;

import javax.swing.*;

import java.awt.*;

import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;

/**
 * Created by kienmaingoc on 6/29/17.
 */
public class CaptureAndCompare implements Runnable {

    Identification identification;
    CompareFacesResult result;

    CaptureAndCompare(Identification id) {
        this.identification = id;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);

            cvSaveImage(MultiThreadMain.PHOTO_NAME, Camera.image);

            result = RekognitionProcess.compareFace(MultiThreadMain.PHOTO_NAME, Main.BUCKET_NAME, "KienMN.jpg");

            if (result != null) {
                System.out.println(result.toString());
            }

            identification.changeStatus();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CompareFacesResult getCompareFaceResult() {
        return result;
    }
}
